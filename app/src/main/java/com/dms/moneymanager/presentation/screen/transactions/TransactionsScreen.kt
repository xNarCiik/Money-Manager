package com.dms.moneymanager.presentation.screen.transactions

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.screen.transactions.component.transactionslist.TransactionsList
import com.dms.moneymanager.presentation.util.NavigationRoute
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun TransactionsScreen(
    viewState: TransactionsUiModel,
    onEvent: (BaseEvent) -> Unit
) {
    BackHandler { } // Do nothing

    LaunchedEffect(key1 = true, block = {
        onEvent(TransactionsEvent.RefreshData)
    })

    Scaffold(
        floatingActionButton = {
            AddFloatingButton(
                onClick = {
                    onEvent(BaseEvent.NavigateToScreen(route = NavigationRoute.CREATE_OR_EDIT_TRANSACTION.route))
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        TransactionsContent(
            modifier = Modifier.padding(paddingValues = it),
            viewState = viewState,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun TransactionsContent(
    modifier: Modifier = Modifier,
    viewState: TransactionsUiModel,
    onEvent: (BaseEvent) -> Unit,
) {
    Column(modifier = modifier) {
        TransactionsList(
            modifier = Modifier.padding(top = 12.dp),
            listTransaction = viewState.transactions,
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
private fun TransactionsScreenPreview() {
    MoneyManagerTheme {
        TransactionsScreen(
            viewState = TransactionsUiModel(
                transactions = arrayListOf(
                    Transaction(
                        name = "transaction 1",
                        amount = -10.5f
                    )
                )
            ),
            onEvent = { }
        )
    }
}