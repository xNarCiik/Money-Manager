package com.dms.moneymanager.presentation.screen.main.component.main.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.screen.main.MainEvent

@Composable
fun BottomSheetAppliedTransaction(
    accounts: List<Account>,
    transaction: Transaction,
    onEvent: (MainEvent) -> Unit,
    closeBottomSheetAction: () -> Unit
) {
    LazyColumn {
        itemsIndexed(accounts) { _, account ->
            Column(
                modifier = Modifier.clickable {
                    onEvent(
                        MainEvent.AppliedTransaction(
                            account = account,
                            transaction = transaction
                        )
                    )
                    closeBottomSheetAction()
                }
            ) {
                Text(text = account.name)
                Divider()
            }
        }
    }
}