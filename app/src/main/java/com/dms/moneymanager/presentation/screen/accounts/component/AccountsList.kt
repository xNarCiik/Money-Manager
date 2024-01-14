package com.dms.moneymanager.presentation.screen.accounts.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.screen.transactions.TransactionsEvent
import com.dms.moneymanager.presentation.screen.transactions.component.mainlist.AccountItem
import com.dms.moneymanager.presentation.screen.transactions.TransactionsBottomSheetType
import com.dms.moneymanager.presentation.util.gridItems

@Composable
fun AccountsList(
    modifier: Modifier = Modifier,
    listAccount: List<Account>,
    onEvent: (BaseEvent) -> Unit
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
                    account = account,
                    appliedTransaction = { onEvent(TransactionsEvent.AppliedTransaction(toAccount = account)) },
                    transferAction = {
                        onEvent(
                            BaseEvent.OpenBottomSheet(
                                bottomSheetType = TransactionsBottomSheetType.BottomSheetTransfer(
                                    account = account
                                )
                            )
                        )
                    },
                    editAction = {
                        onEvent(
                            BaseEvent.OpenBottomSheet(
                                TransactionsBottomSheetType.BottomSheetEditAccount(
                                    account = account
                                )
                            )
                        )
                    },
                    enableOrDisableAction = {
                        onEvent(TransactionsEvent.EnableOrDisableAccountEvent(account = account))
                    },
                    removeAction = {
                        onEvent(
                            BaseEvent.OpenBottomSheet(
                                bottomSheetType = TransactionsBottomSheetType.BottomSheetConfirmRemoveAccount(
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
                    onClick = { onEvent(BaseEvent.OpenBottomSheet(TransactionsBottomSheetType.BottomSheetCreateAccount)) }
                )
            }
        }
    }
}

// TODO MOVE TO COMMON
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
