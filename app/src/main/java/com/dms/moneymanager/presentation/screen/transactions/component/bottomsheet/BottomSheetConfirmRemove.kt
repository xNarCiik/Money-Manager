package com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.screen.accounts.AccountsEvent
import com.dms.moneymanager.presentation.screen.transactions.TransactionsEvent
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun BottomSheetConfirmRemoveAccount(onEvent: (AccountsEvent) -> Unit, account: Account) {
    BottomSheetConfirmRemove(
        onValidateAction = {
            onEvent(
                AccountsEvent.RemoveAccountEvent(account = account)
            )
        },
        name = account.name
    )
}

@Composable
fun BottomSheetConfirmRemoveTransaction(
    onEvent: (TransactionsEvent) -> Unit,
    transaction: Transaction
) {
    BottomSheetConfirmRemove(
        onValidateAction = {
            onEvent(
                TransactionsEvent.RemoveTransactionEvent(transaction = transaction)
            )
        },
        name = transaction.name
    )
}

@Composable
private fun BottomSheetConfirmRemove(onValidateAction: () -> Unit, name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Confirmer la suppression",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(height = 18.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Êtes-vous sûr de vouloir supprimer $name ?",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )


        Button(
            modifier = Modifier.padding(vertical = 18.dp),
            onClick = onValidateAction
        ) {
            Text(text = stringResource(R.string.remove))
        }
    }
}

@Preview
@Composable
private fun BottomSheetConfirmRemoveAccountPreview() {
    MoneyManagerTheme {
        BottomSheetConfirmRemoveAccount(
            onEvent = { },
            Account(id = 0, name = "name", currentBalance = 10f)
        )
    }
}

@Preview
@Composable
private fun BottomSheetConfirmRemoveTransactionPreview() {
    MoneyManagerTheme {
        BottomSheetConfirmRemoveTransaction(
            onEvent = { },
            Transaction(id = 0, name = "transaction", amount = 10f)
        )
    }
}