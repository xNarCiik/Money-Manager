package com.dms.moneymanager.presentation.screen.main.component.mainlist

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
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.presentation.screen.main.model.MainBottomSheetType
import com.dms.moneymanager.presentation.screen.main.model.MainUiState
import com.dms.moneymanager.presentation.util.gridItems

@Composable
fun MainList(
    modifier: Modifier = Modifier,
    mainUiState: MainUiState,
    listAccount: List<Account>,
    listTransaction: List<Transaction>,
    onEvent: (MainEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        item {
            TitleListText(
                modifier = Modifier.padding(bottom = 8.dp),
                stringId = R.string.my_accounts
            )
        }

        if (listAccount.isNotEmpty()) {
            gridItems(data = listAccount, columnCount = 2) { account ->
                AccountItem(
                    mainUiState = mainUiState,
                    account = account,
                    appliedTransaction = { onEvent(MainEvent.AppliedTransaction(toAccount = account)) },
                    transferAction = {
                        onEvent(
                            MainEvent.OpenBottomSheet(
                                mainBottomSheetType = MainBottomSheetType.BottomSheetTransfer(
                                    account = account
                                )
                            )
                        )
                    },
                    editAction = {
                        onEvent(
                            MainEvent.OpenBottomSheet(
                                MainBottomSheetType.BottomSheetEditAccount(
                                    account = account
                                )
                            )
                        )
                    },
                    removeAction = {
                        onEvent(
                            MainEvent.OpenBottomSheet(
                                mainBottomSheetType = MainBottomSheetType.BottomSheetConfirmRemoveAccount(
                                    account = account
                                )
                            )
                        )
                    }
                )
            }
        } else {
            item {
                EmptyText(
                    stringId = R.string.empty_account,
                    onClick = { onEvent(MainEvent.OpenBottomSheet(MainBottomSheetType.BottomSheetCreateAccount)) }
                )
            }
        }

        item {
            TitleListText(
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                stringId = R.string.incoming_transactions
            )
        }

        if (listTransaction.isNotEmpty()) {
            itemsIndexed(listTransaction) { _, transaction ->
                TransactionItem(
                    transaction = transaction,
                    appliedAction = {
                        onEvent(MainEvent.OnClickAppliedTransaction(transaction = transaction))
                    },
                    editAction = {
                        onEvent(
                            MainEvent.OpenBottomSheet(
                                mainBottomSheetType = MainBottomSheetType.BottomSheetEditTransaction(
                                    transaction = transaction
                                )
                            )
                        )
                    },
                    removeAction = {
                        onEvent(
                            MainEvent.OpenBottomSheet(
                                mainBottomSheetType = MainBottomSheetType.BottomSheetConfirmRemoveTransaction(
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
                    onClick = { onEvent(MainEvent.OpenBottomSheet(MainBottomSheetType.BottomSheetCreateTransaction)) }
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