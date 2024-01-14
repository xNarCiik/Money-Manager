package com.dms.moneymanager.presentation.screen.transactions.component.transactionslist

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
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
import com.dms.moneymanager.presentation.screen.transactions.TransactionsEvent
import com.dms.moneymanager.presentation.screen.transactions.TransactionsBottomSheetType

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
            TitleListText(
                modifier = Modifier.padding(bottom = 8.dp),
                stringId = R.string.incoming_transactions
            )
        }

        if (listTransaction.isNotEmpty()) {
            itemsIndexed(listTransaction) { _, transaction ->
                TransactionItem(
                    transaction = transaction,
                    appliedAction = {
                        onEvent(TransactionsEvent.OnClickAppliedTransaction(transaction = transaction))
                    },
                    editAction = {
                        onEvent(
                            BaseEvent.OpenBottomSheet(
                                bottomSheetType = TransactionsBottomSheetType.BottomSheetEditTransaction(
                                    transaction = transaction
                                )
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
                EmptyText(
                    stringId = R.string.empty_transaction,
                    onClick = { onEvent(BaseEvent.OpenBottomSheet(TransactionsBottomSheetType.BottomSheetCreateTransaction)) }
                )
            }
        }
    }
}

@Composable
private fun TitleListText(
    modifier: Modifier = Modifier,
    @StringRes stringId: Int
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = stringResource(id = stringId),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun EmptyText(
    modifier: Modifier = Modifier,
    @StringRes stringId: Int, onClick: () -> Unit
) {
    Text(
        text = stringResource(id = stringId),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        textAlign = TextAlign.Center
    )
}