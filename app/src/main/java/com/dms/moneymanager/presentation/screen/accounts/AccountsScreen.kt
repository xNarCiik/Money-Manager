package com.dms.moneymanager.presentation.screen.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.screen.accounts.component.AccountsList
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun AccountsScreen(
    viewState: AccountsUiModel,
    onEvent: (BaseEvent) -> Unit,
    navController: NavHostController
) {
    LaunchedEffect(key1 = true, block = {
        onEvent(AccountsEvent.RefreshData)
    })

    AccountsContent(
        viewState = viewState,
        onEvent = onEvent
    )
}

@Composable
private fun AccountsContent(
    modifier: Modifier = Modifier,
    viewState: AccountsUiModel,
    onEvent: (BaseEvent) -> Unit
) {
    Column(modifier = modifier.padding(horizontal = 12.dp)) {
        AccountsList(
            modifier = Modifier.padding(top = 12.dp),
            listAccount = viewState.accounts,
            onEvent = onEvent
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
            onEvent = { },
            navController = rememberNavController()
        )
    }
}