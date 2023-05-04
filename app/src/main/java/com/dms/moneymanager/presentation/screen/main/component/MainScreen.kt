package com.dms.moneymanager.presentation.screen.main.component

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Menu
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.BottomSheetCreateAccount
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.BottomSheetCreateTransaction
import com.dms.moneymanager.presentation.screen.main.component.mainlist.MainList
import com.dms.moneymanager.presentation.screen.main.model.MainBottomSheetType
import com.dms.moneymanager.presentation.screen.main.model.MainUiModel
import com.dms.moneymanager.presentation.util.toAmountString
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewState: MainUiModel,
    onEvent: (MainEvent) -> Unit,
    navController: NavHostController
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    viewState.toastMessage?.let { error ->
        Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        onEvent(MainEvent.RemoveToast)
    }

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

                is MainBottomSheetType.BottomSheetCreateTransaction -> {
                    BottomSheetCreateTransaction(onEvent = onEvent)
                }
            }
        }
    }

    MainContent(
        viewState = viewState,
        onEvent = onEvent,
        navController = navController
    )
}

@Composable
private fun MainContent(
    viewState: MainUiModel,
    onEvent: (MainEvent) -> Unit,
    navController: NavController
) {
    Column(horizontalAlignment = Alignment.End) {
        HeaderContent(
            onMenuClick = { navController.navigate("history") },
            onInfoClick = { /* TODO BOTTOM SHEET */ }
        )

        InfoBalance(
            modifier = Modifier.padding(vertical = 12.dp),
            currentBalance = viewState.currentBalance,
            futureBalance = viewState.futureBalance
        )

        MainList(
            mainUiState = viewState.mainUiState,
            listAccount = viewState.listAccount,
            listTransaction = viewState.listTransaction,
            onEvent = onEvent
        )

        Spacer(modifier = Modifier.weight(weight = 1f))

        AddFloatingButton(
            modifier = Modifier.padding(bottom = 10.dp, end = 10.dp),
            addAccountAction = { onEvent(MainEvent.OpenBottomSheet(mainBottomSheetType = MainBottomSheetType.BottomSheetCreateAccount)) },
            addTransactionAction = { onEvent(MainEvent.OpenBottomSheet(mainBottomSheetType = MainBottomSheetType.BottomSheetCreateTransaction)) }
        )
    }
}

@Composable
private fun HeaderContent(onMenuClick: () -> Unit, onInfoClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(26.dp)
                .clickable { onMenuClick() },
            imageVector = Icons.Rounded.Menu,
            contentDescription = "Menu icon"
        )

        Text(
            modifier = Modifier.weight(weight = 1f),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Icon(
            modifier = Modifier
                .size(26.dp)
                .clickable { onInfoClick() },
            imageVector = Icons.Rounded.Info,
            contentDescription = "Info icon"
        )
    }
}

@Composable
private fun InfoBalance(
    modifier: Modifier = Modifier,
    currentBalance: Float,
    futureBalance: Float
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.current_balance))
            Text(text = currentBalance.toAmountString())
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.future_balance))
            Text(text = futureBalance.toAmountString())
        }
    }
}

@Composable
private fun AddFloatingButton(
    modifier: Modifier,
    addAccountAction: () -> Unit,
    addTransactionAction: () -> Unit
) {
    var expandedDropDownMenu by remember { mutableStateOf(false) }

    FloatingActionButton(
        modifier = modifier,
        onClick = { expandedDropDownMenu = true },
        containerColor = Color.Blue,
        shape = RoundedCornerShape(22.dp),
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