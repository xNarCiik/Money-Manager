package com.dms.moneymanager.presentation.screen.transactions.component.transactionslist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.screen.transactions.TransactionsBottomSheetType
import com.dms.moneymanager.presentation.screen.transactions.TransactionsEvent
import com.dms.moneymanager.presentation.util.NavigationRoute

@Composable
fun TransactionsList(
    modifier: Modifier = Modifier,
    listTransaction: List<Transaction>,
    onEvent: (BaseEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                text = stringResource(id = R.string.incoming_transactions),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        if (listTransaction.isNotEmpty()) {
            itemsIndexed(listTransaction) { _, transaction ->
                TransactionItem(
                    modifier = Modifier
                        .padding(bottom = 1.dp),
                    transaction = transaction,
                    appliedAction = {
                        onEvent(TransactionsEvent.OnClickAppliedTransaction(transaction = transaction))
                    },
                    editAction = {
                        onEvent(
                            BaseEvent.NavigateToScreen(
                                route = NavigationRoute.CREATE_OR_EDIT_TRANSACTION.route + "/transaction" // TODO parameter
                            )
                        )
                    },
                    enableOrDisableAction = {
                        onEvent(TransactionsEvent.EnableOrDisableTransactionEvent(transaction = transaction))
                    },
                    removeAction = {
                        onEvent(
                            BaseEvent.OpenBottomSheet(
                                bottomSheetType = TransactionsBottomSheetType.BottomSheetConfirmRemoveTransaction(
                                    transaction = transaction
                                )
                            )
                        )
                    }
                )
            }
        } else {
            item {
                Text(
                    text = stringResource(id = R.string.empty_transaction),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}