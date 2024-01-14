package com.dms.moneymanager.presentation.screen.transactions

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.BottomSheetType
import com.dms.moneymanager.presentation.screen.transactions.component.InfoBalance
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetConfirmRemoveAccount
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetConfirmRemoveTransaction
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetCreateAccount
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetCreateTransaction
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetEditAccount
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetEditTransaction
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetTransfer
import com.dms.moneymanager.presentation.screen.transactions.component.mainlist.MainList
import com.dms.moneymanager.presentation.screen.transactions.model.TransactionsBottomSheetType
import com.dms.moneymanager.presentation.screen.transactions.model.TransactionsUiModel
import com.dms.moneymanager.presentation.screen.transactions.model.TransactionsUiState
import com.dms.moneymanager.presentation.util.NavigationRoute
import com.dms.moneymanager.ui.theme.MoneyManagerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    viewState: TransactionsUiModel,
    onEvent: (BaseEvent) -> Unit,
    currentBottomSheet: BottomSheetType? = null,
    toastMessage: Int? = null,
    navController: NavHostController
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Sheet content
    if (currentBottomSheet != null) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(BaseEvent.CloseBottomSheet) },
            sheetState = bottomSheetState
        ) {
            when (currentBottomSheet) {
                is TransactionsBottomSheetType.BottomSheetCreateAccount -> {
                    BottomSheetCreateAccount(onEvent = onEvent)
                }

                is TransactionsBottomSheetType.BottomSheetTransfer -> {
                    BottomSheetTransfer(
                        listAccount = viewState.accounts,
                        account = currentBottomSheet.account,
                        onEvent = onEvent
                    )
                }

                is TransactionsBottomSheetType.BottomSheetEditAccount -> {
                    BottomSheetEditAccount(
                        account = currentBottomSheet.account,
                        onEvent = onEvent
                    )
                }

                is TransactionsBottomSheetType.BottomSheetConfirmRemoveAccount -> {
                    BottomSheetConfirmRemoveAccount(
                        account = currentBottomSheet.account,
                        onEvent = onEvent
                    )
                }

                is TransactionsBottomSheetType.BottomSheetCreateTransaction -> {
                    BottomSheetCreateTransaction(
                        onEvent = onEvent,
                        accounts = viewState.accounts
                    )
                }

                is TransactionsBottomSheetType.BottomSheetEditTransaction -> {
                    BottomSheetEditTransaction(
                        transaction = currentBottomSheet.transaction,
                        accounts = viewState.accounts,
                        onEvent = onEvent
                    )
                }

                is TransactionsBottomSheetType.BottomSheetConfirmRemoveTransaction -> {
                    BottomSheetConfirmRemoveTransaction(
                        transaction = currentBottomSheet.transaction,
                        onEvent = onEvent
                    )
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(key1 = true, block = {
        onEvent(TransactionsEvent.RefreshData)
    })

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            AddFloatingButton(
                addAccountAction = { onEvent(BaseEvent.OpenBottomSheet(bottomSheetType = TransactionsBottomSheetType.BottomSheetCreateAccount)) },
                addTransactionAction = { onEvent(BaseEvent.OpenBottomSheet(bottomSheetType = TransactionsBottomSheetType.BottomSheetCreateTransaction)) }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        TransactionsContent(
            modifier = Modifier.padding(paddingValues = it),
            viewState = viewState,
            onEvent = onEvent,
            navController = navController
        )
    }

    toastMessage?.let { error ->
        Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        onEvent(BaseEvent.RemoveToast)
    }

    when (viewState.transactionsUiState) {
        TransactionsUiState.APPLIED_TRANSACTION -> {
            LaunchedEffect(key1 = "snackbar_key", block = {
                coroutineScope.launch {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = "Cliquez sur le compte vers lequel appliquer la transaction ${viewState.selectedTransaction?.name}.",
                        actionLabel = "Annuler",
                        duration = SnackbarDuration.Indefinite
                    )
                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> onEvent(BaseEvent.ActionPerformedSnackbar)
                        SnackbarResult.Dismissed -> {}
                    }
                }
            })
        }

        else -> {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}

@Composable
private fun TransactionsContent(
    modifier: Modifier = Modifier,
    viewState: TransactionsUiModel,
    onEvent: (BaseEvent) -> Unit,
    navController: NavHostController
) {
    Column(modifier = modifier.padding(horizontal = 12.dp)) {
        var isExpended by remember { mutableStateOf(true) }
        InfoBalance(
            modifier = Modifier.padding(top = 12.dp),
            currentBalance = viewState.currentBalance,
            futureBalance = viewState.futureBalance,
            isExpended = isExpended,
            onClick = { navController.navigate(NavigationRoute.HISTORY.route) }, // TODO DETAIL SCREEN
            onExpendedClick = { isExpended = !isExpended }
        )

        MainList(
            modifier = Modifier.padding(top = 12.dp),
            transactionsUiState = viewState.transactionsUiState,
            listAccount = viewState.accounts,
            listTransaction = viewState.transactions,
            onEvent = onEvent
        )
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
        shape = CircleShape,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
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
            HorizontalDivider()
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
private fun TransactionsScreenPreview() {
    MoneyManagerTheme {
        TransactionsScreen(
            viewState = TransactionsUiModel(
                accounts = arrayListOf(Account(name = "account 1", currentBalance = 2000.0f)),
                transactions = arrayListOf(
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