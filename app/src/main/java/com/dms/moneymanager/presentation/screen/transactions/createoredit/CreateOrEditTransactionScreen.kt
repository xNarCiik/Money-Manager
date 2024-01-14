package com.dms.moneymanager.presentation.screen.transactions.createoredit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.screen.transactions.TransactionsEvent
import com.dms.moneymanager.presentation.screen.transactions.TransactionsUiModel
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.commun.AccountItem
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun CreateOrEditTransactionScreen(
    viewState: TransactionsUiModel,
    onEvent: (TransactionsEvent) -> Unit
) {
    Scaffold {
        CreateOrEditTransactionContent(
            modifier = Modifier.padding(it),
            onEvent = onEvent,
            transaction = viewState.selectedTransaction,
            accounts = viewState.accounts
        )
    }
}

@Composable
private fun CreateOrEditTransactionContent(
    modifier: Modifier = Modifier,
    onEvent: (TransactionsEvent) -> Unit,
    transaction: Transaction? = null,
    accounts: List<Account>
) {
    // if transaction != null is a edit screen
    val isCreateScreen = transaction == null

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(if (isCreateScreen) R.string.add_transaction else R.string.edit_transaction),
            style = MaterialTheme.typography.titleLarge
        )

        var name by remember { mutableStateOf(TextFieldValue(text = transaction?.name ?: "")) }
        var amount by remember { mutableStateOf(TextFieldValue(text = transaction?.amount.toString())) }
        var selectedAccount by remember { mutableStateOf(transaction?.destinationAccount) }

        val onValidateAction = {
            onEvent(
                TransactionsEvent.AddTransactionEvent(
                    name = name.text,
                    amount = amount.text,
                    destinationAccount = selectedAccount
                )
            )

            // TODO
//            onEvent(
//                TransactionsEvent.EditTransactionEvent(
//                    id = transaction.id,
//                    name = name.text,
//                    amount = amount.text,
//                    destinationAccount = selectedAccount
//                )
//            )
        }

        TextField(
            modifier = Modifier.padding(top = 18.dp),
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            label = { Text(text = stringResource(R.string.name)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        TextField(
            modifier = Modifier.padding(top = 14.dp),
            value = amount,
            onValueChange = { amount = it },
            singleLine = true,
            label = { Text(text = stringResource(R.string.amount)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onValidateAction() }
            )
        )

        Spacer(modifier = Modifier.padding(top = 14.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            itemsIndexed(accounts) { _, account ->
                AccountItem(
                    account = account,
                    isSelected = selectedAccount == account,
                    onClick = {
                        selectedAccount = account
                    }
                )
            }
        }

        Button(
            modifier = Modifier.padding(vertical = 18.dp),
            onClick = onValidateAction
        ) {
            Text(text = stringResource(if (isCreateScreen) R.string.add_the_transaction else R.string.edit_transaction))
        }
    }
}

@Preview
@Composable
private fun CreateOrEditTransactionScreenPreview() {
    MoneyManagerTheme {
        val account1 = Account(id = 0, name = "account 1", currentBalance = 50f)
        val account2 = Account(id = 1, name = "account 2", currentBalance = 100f)

        CreateOrEditTransactionScreen(
            viewState = TransactionsUiModel(
                accounts = listOf(
                    account1,
                    account2
                )
            ),
            onEvent = { },
        )
    }
}