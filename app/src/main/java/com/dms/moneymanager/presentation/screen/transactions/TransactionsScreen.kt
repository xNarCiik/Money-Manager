package com.dms.moneymanager.presentation.screen.transactions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.screen.transactions.component.transactionslist.TransactionsList
import com.dms.moneymanager.presentation.util.NavigationRoute
import com.dms.moneymanager.presentation.util.monthlyAndYearString
import com.dms.moneymanager.ui.theme.Green
import com.dms.moneymanager.ui.theme.MoneyManagerTheme
import com.dms.moneymanager.ui.theme.Red

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
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        DateSelector(
            modifier = Modifier.padding(top = 8.dp),
            onClickArrowLeft = { onEvent(TransactionsEvent.ClickLeftArrowDate) },
            onClickArrowRight = { onEvent(TransactionsEvent.ClickRightArrowDate) },
            currentDate = viewState.currentDate.monthlyAndYearString()
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Revenus", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "10 480.00 €",
                    style = MaterialTheme.typography.bodySmall,
                    color = Green
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Dépense", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "1 202.00 €",
                    style = MaterialTheme.typography.bodySmall,
                    color = Red
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Solde actuel",
                    style = MaterialTheme.typography.bodySmall
                ) //Solde actuel/estimé
                Text(
                    text = "1 200.00 €",
                    style = MaterialTheme.typography.bodySmall,
                    color = Green
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Solde final", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = " 8 451.00 €",
                    style = MaterialTheme.typography.bodySmall,
                    color = Green
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        TransactionsList(
            modifier = Modifier.padding(top = 12.dp),
            listTransaction = viewState.transactions,
            onEvent = onEvent
        )
    }
}

@Composable
private fun DateSelector(
    modifier: Modifier = Modifier,
    onClickArrowLeft: () -> Unit,
    onClickArrowRight: () -> Unit,
    currentDate: String
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onClickArrowLeft) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = "icone date left"
            )
        }

        Text(text = currentDate)

        IconButton(onClick = onClickArrowRight) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = "icone date left"
            )
        }
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