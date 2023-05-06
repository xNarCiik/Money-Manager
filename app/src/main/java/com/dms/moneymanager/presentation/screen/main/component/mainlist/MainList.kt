package com.dms.moneymanager.presentation.screen.main.component.mainlist

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.presentation.screen.main.model.MainBottomSheetType
import com.dms.moneymanager.presentation.screen.main.model.MainUiState

@Composable
fun MainList(
    modifier: Modifier = Modifier,
    mainUiState: MainUiState,
    listAccount: List<Account>,
    listTransaction: List<Transaction>,
    onEvent: (MainEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        AccountList(
            mainUiState = mainUiState,
            listAccount = listAccount,
            onEvent = onEvent
        )

        TransactionList(
            modifier = Modifier.padding(top = 32.dp),
            listTransaction = listTransaction,
            onEvent = onEvent
        )
    }
}

@Composable
private fun AccountList(
    modifier: Modifier = Modifier,
    mainUiState: MainUiState,
    listAccount: List<Account>,
    onEvent: (MainEvent) -> Unit
) {
    Column(modifier = modifier) {
        TitleListText(stringId = R.string.accounts)

        TitleListDivider(modifier = Modifier.padding(vertical = 12.dp))

        if (listAccount.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 2),
                contentPadding = PaddingValues(all = 4.dp)
            ) {
                itemsIndexed(listAccount) { _, account ->
                    AccountItem(
                        mainUiState = mainUiState,
                        account = account,
                        appliedTransaction = { onEvent(MainEvent.AppliedTransaction(toAccount = account)) },
                        editAction = { onEvent(MainEvent.OpenBottomSheet(MainBottomSheetType.BottomSheetEditAccount(account = account))) },
                        removeAction = { onEvent(MainEvent.RemoveAccountEvent(account = account)) }
                    )
                }
            }
        } else {
            EmptyText(
                modifier = Modifier.padding(top = 12.dp),
                stringId = R.string.empty_account,
                onClick = { onEvent(MainEvent.OpenBottomSheet(MainBottomSheetType.BottomSheetCreateAccount)) }
            )
        }
    }
}

@Composable
private fun TransactionList(
    modifier: Modifier = Modifier,
    listTransaction: List<Transaction>,
    onEvent: (MainEvent) -> Unit
) {
    Column(modifier = modifier) {
        TitleListText(stringId = R.string.incoming_transactions)

        TitleListDivider(modifier = Modifier.padding(vertical = 12.dp))

        if (listTransaction.isNotEmpty()) {
            LazyColumn {
                itemsIndexed(listTransaction) { _, transaction ->
                    TransactionItem(
                        transaction = transaction,
                        appliedAction = {
                            onEvent(MainEvent.OnClickAppliedTransaction(transaction = transaction))
                        },
                        removeAction = { onEvent(MainEvent.RemoveTransactionEvent(transaction = transaction)) }
                    )
                }
            }
        } else {
            EmptyText(
                modifier = Modifier.padding(top = 12.dp),
                stringId = R.string.empty_transaction,
                onClick = { onEvent(MainEvent.OpenBottomSheet(MainBottomSheetType.BottomSheetCreateTransaction)) }
            )
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
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TitleListDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        thickness = 0.3.dp,
        color = Color.White
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
