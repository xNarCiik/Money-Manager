package com.dms.moneymanager.presentation.screen.main.component.mainlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.presentation.screen.main.model.BottomSheetAppliedTransaction

@Composable
fun MainList(
    listAccount: List<Account>,
    listTransaction: List<Transaction>,
    onEvent: (MainEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Blue)
            .padding(all = 10.dp)
    ) {
        Text(
            text = "Comptes"
        )
    }

    if (listAccount.isNotEmpty()) {
        LazyColumn {
            itemsIndexed(listAccount) { index, account ->
                AccountItem(
                    account = account,
                    removeAction = { onEvent(MainEvent.RemoveAccountEvent(account)) },
                    showDivider = index != listAccount.size - 1
                )
            }
        }
    } else {
        Text(
            text = "Aucun compte",
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            textAlign = TextAlign.Center
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Blue)
            .padding(all = 10.dp)
    ) {
        Text(
            text = "Transactions"
        )
    }

    if (listTransaction.isNotEmpty()) {
        LazyColumn {
            itemsIndexed(listTransaction) { index, transaction ->
                TransactionItem(
                    transaction = transaction,
                    appliedAction = {
                        onEvent(
                            MainEvent.OpenBottomSheet(
                                mainBottomSheetType = BottomSheetAppliedTransaction(
                                    transaction = transaction
                                )
                            )
                        )
                    },
                    removeAction = { onEvent(MainEvent.RemoveTransactionEvent(transaction = transaction)) },
                    showDivider = index != listTransaction.size - 1
                )
            }
        }
    } else {
        Text(
            text = "Aucune transaction",
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            textAlign = TextAlign.Center
        )
    }
}