package com.dms.moneymanager.presentation.screen.accounts.component.accountslist

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
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.screen.accounts.AccountsBottomSheetType
import com.dms.moneymanager.presentation.screen.transactions.TransactionsEvent

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
            Text(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                text = stringResource(id = R.string.my_accounts),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        if (listAccount.isNotEmpty()) {
            itemsIndexed(listAccount) { _, account ->
                AccountItem(
                    modifier = Modifier.padding(bottom = 1.dp),
                    account = account,
                    appliedTransaction = { onEvent(TransactionsEvent.AppliedTransaction(toAccount = account)) },
                    transferAction = {
                        onEvent(
                            BaseEvent.OpenBottomSheet(
                                bottomSheetType = AccountsBottomSheetType.BottomSheetTransfer(
                                    account = account
                                )
                            )
                        )
                    },
                    editAction = {
                        onEvent(
                            BaseEvent.OpenBottomSheet(
                                AccountsBottomSheetType.BottomSheetEditAccount(
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
                                bottomSheetType = AccountsBottomSheetType.BottomSheetConfirmRemoveAccount(
                                    account = account
                                )
                            )
                        )
                    }
                )
            }
        } else {
            item {
                Text(
                    text = stringResource(id = R.string.empty_account),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEvent(BaseEvent.OpenBottomSheet(AccountsBottomSheetType.BottomSheetCreateAccount)) },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}