package com.dms.moneymanager.presentation.screen.main.component

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.BottomSheetCreateAccount
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.BottomSheetCreateTransaction
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.BottomSheetEditAccount
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.BottomSheetEditTransaction
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.BottomSheetTransfer
import com.dms.moneymanager.presentation.screen.main.component.mainlist.MainList
import com.dms.moneymanager.presentation.screen.main.model.MainBottomSheetType
import com.dms.moneymanager.presentation.screen.main.model.MainUiModel
import com.dms.moneymanager.presentation.screen.main.model.MainUiState
import com.dms.moneymanager.presentation.util.getTextColor
import com.dms.moneymanager.presentation.util.toAmountString
import com.dms.moneymanager.ui.theme.MoneyManagerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewState: MainUiModel,
    onEvent: (MainEvent) -> Unit,
    navController: NavHostController
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // Sheet content
    if (viewState.mainBottomSheetType != null) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(MainEvent.CloseBottomSheet) },
            sheetState = bottomSheetState
        ) {
            when (viewState.mainBottomSheetType) {
                is MainBottomSheetType.BottomSheetCreateAccount -> {
                    BottomSheetCreateAccount(onEvent = onEvent)
                }

                is MainBottomSheetType.BottomSheetEditAccount -> {
                    BottomSheetEditAccount(
                        account = viewState.mainBottomSheetType.account,
                        onEvent = onEvent
                    )
                }

                is MainBottomSheetType.BottomSheetTransfer -> {
                    BottomSheetTransfer(
                        listAccount = viewState.listAccount,
                        account = viewState.mainBottomSheetType.account,
                        onEvent = onEvent
                    )
                }

                is MainBottomSheetType.BottomSheetCreateTransaction -> {
                    BottomSheetCreateTransaction(onEvent = onEvent)
                }

                is MainBottomSheetType.BottomSheetEditTransaction -> {
                    BottomSheetEditTransaction(
                        transaction = viewState.mainBottomSheetType.transaction,
                        onEvent = onEvent
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            AddFloatingButton(
                addAccountAction = { onEvent(MainEvent.OpenBottomSheet(mainBottomSheetType = MainBottomSheetType.BottomSheetCreateAccount)) },
                addTransactionAction = { onEvent(MainEvent.OpenBottomSheet(mainBottomSheetType = MainBottomSheetType.BottomSheetCreateTransaction)) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        MainContent(
            modifier = Modifier.padding(paddingValues = it),
            viewState = viewState,
            onEvent = onEvent
        )
    }

    viewState.toastMessage?.let { error ->
        Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        onEvent(MainEvent.RemoveToast)
    }

    when (viewState.mainUiState) {
        MainUiState.APPLIED_TRANSACTION -> {
            LaunchedEffect(key1 = "snackbar_key", block = {
                coroutineScope.launch {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = "Cliquez sur le compte vers lequel appliquer la transaction ${viewState.selectedTransaction?.name}.",
                        actionLabel = "Annuler",
                        duration = SnackbarDuration.Indefinite
                    )
                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> onEvent(MainEvent.CancelSnackbar)
                        SnackbarResult.Dismissed -> {}
                    }
                }
            })
        }

        else -> {
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    viewState: MainUiModel,
    onEvent: (MainEvent) -> Unit
) {
    Column(modifier = modifier.padding(all = 12.dp)) {
        InfoBalance(
            currentBalance = viewState.currentBalance,
            futureBalance = viewState.futureBalance
        )

        MainList(
            modifier = Modifier.padding(top = 12.dp),
            mainUiState = viewState.mainUiState,
            listAccount = viewState.listAccount,
            listTransaction = viewState.listTransaction,
            onEvent = onEvent
        )
    }
}

@Composable
private fun InfoBalance(
    modifier: Modifier = Modifier,
    currentBalance: Float,
    futureBalance: Float
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = 8.dp),
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.current_balance),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = "(1 janvier 2023)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = currentBalance.toAmountString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                color = currentBalance.getTextColor()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.future_balance),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = "(31 janvier 2023)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = futureBalance.toAmountString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                color = futureBalance.getTextColor()
            )
        }
    }
}

@Composable
private fun AddFloatingButton(
    addAccountAction: () -> Unit,
    addTransactionAction: () -> Unit
) {
    var expandedDropDownMenu by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { expandedDropDownMenu = true },
        containerColor = Color.DarkGray,
        shape = RoundedCornerShape(25.dp),
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add FAB",
            tint = Color.White,
        )

        DropdownMenu(
            expanded = expandedDropDownMenu,
            onDismissRequest = { expandedDropDownMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.add_account)) },
                onClick = {
                    expandedDropDownMenu = false
                    addAccountAction()
                }
            )
            Divider()
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.add_transaction)) },
                onClick = {
                    expandedDropDownMenu = false
                    addTransactionAction()
                }
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MoneyManagerTheme {
        MainScreen(
            viewState = MainUiModel(
                listAccount = arrayListOf(Account(name = "account 1", currentBalance = 2000.0f)),
                listTransaction = arrayListOf(
                    Transaction(
                        name = "transaction 1",
                        amount = -10.5f
                    )
                )
            ),
            onEvent = { },
            navController = rememberNavController()
        )
    }
}