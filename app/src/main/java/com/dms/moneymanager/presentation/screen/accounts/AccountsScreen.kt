package com.dms.moneymanager.presentation.screen.accounts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.screen.accounts.component.accountslist.AccountsList
import com.dms.moneymanager.presentation.screen.transactions.component.InfoBalance
import com.dms.moneymanager.presentation.util.NavigationRoute
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun AccountsScreen(
    viewState: AccountsUiModel,
    onEvent: (BaseEvent) -> Unit
) {
    BackHandler { } // Do nothing

    LaunchedEffect(key1 = true, block = {
        onEvent(AccountsEvent.RefreshData)
    })

    Scaffold(
        floatingActionButton = {
            AddFloatingButton(
                onClick = { onEvent(BaseEvent.NavigateToScreen(route = NavigationRoute.CREATE_OR_EDIT_ACCOUNT.route)) }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        AccountsContent(
            modifier = Modifier.padding(paddingValues = it),
            viewState = viewState,
            onEvent = onEvent
        )
    }
}

@Composable
private fun AccountsContent(
    modifier: Modifier = Modifier,
    viewState: AccountsUiModel,
    onEvent: (BaseEvent) -> Unit
) {
    Column(modifier = modifier) {
        var isExpended by remember { mutableStateOf(true) }
        InfoBalance(
            modifier = Modifier.padding(all = 12.dp),
            currentBalance = viewState.currentBalance,
            futureBalance = viewState.futureBalance,
            isExpended = isExpended,
            onClick = { }, // TODO DETAIL SCREEN
            onExpendedClick = { isExpended = !isExpended }
        )

        AccountsList(
            modifier = Modifier.padding(top = 12.dp),
            listAccount = viewState.accounts,
            onEvent = onEvent
        )
    }
}

@Composable
private fun AddFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add FAB",
            tint = Color.White,
        )
    }
}

@Preview
@Composable
private fun AccountsPreview() {
    MoneyManagerTheme {
        AccountsScreen(
            viewState = AccountsUiModel(
                accounts = arrayListOf(Account(name = "account 1", currentBalance = 2000.0f))
            ),
            onEvent = { }
        )
    }
}