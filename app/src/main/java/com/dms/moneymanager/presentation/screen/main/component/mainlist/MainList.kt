package com.dms.moneymanager.presentation.screen.main.component.mainlist

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ) {
        AccountList(listAccount = listAccount, onEvent = onEvent)

        Spacer(modifier = Modifier.height(height = 32.dp))

        TransactionList(listTransaction = listTransaction, onEvent = onEvent)
    }
}

@Composable
private fun AccountList(listAccount: List<Account>, onEvent: (MainEvent) -> Unit) {
    TitleListText(stringId = R.string.accounts)

    TitleListDivider()

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
        EmptyText(stringId = R.string.empty_account)
    }
}

@Composable
private fun TransactionList(listTransaction: List<Transaction>, onEvent: (MainEvent) -> Unit) {
    TitleListText(stringId = R.string.incoming_transactions)

    TitleListDivider()

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
        EmptyText(stringId = R.string.empty_transaction)
    }
}

@Composable
private fun TitleListText(@StringRes stringId: Int) {
    Text(
        text = stringResource(id = stringId),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun TitleListDivider() {
    Divider(modifier = Modifier.padding(top = 12.dp))
}

@Composable
private fun EmptyText(@StringRes stringId: Int) {
    Text(
        text = stringResource(id = stringId),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 22.dp),
        textAlign = TextAlign.Center
    )
}
