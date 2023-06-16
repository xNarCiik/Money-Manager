package com.dms.moneymanager.presentation.screen.main.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
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
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.commun.AccountItem
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun BottomSheetEditTransaction(
    transaction: Transaction,
    accounts: List<Account>,
    onEvent: (MainEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.edit_transaction),
            style = MaterialTheme.typography.titleLarge
        )

        var name by remember { mutableStateOf(TextFieldValue(transaction.name)) }
        var amount by remember { mutableStateOf(TextFieldValue(transaction.amount.toString())) }
        var selectAccount by remember { mutableStateOf(value = false) }
        var selectedAccount by remember { mutableStateOf<Account?>(null) }

        val onValidateAction = {
            onEvent(
                MainEvent.EditTransactionEvent(
                    id = transaction.id,
                    name = name.text,
                    amount = amount.text,
                    destinationAccount = selectedAccount
                )
            )
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

        Row(verticalAlignment = Alignment.CenterVertically) {
            var appliedDateIsChecked by remember { mutableStateOf(false) }
            Checkbox(
                checked = appliedDateIsChecked,
                onCheckedChange = { appliedDateIsChecked = it }
            )
            Text(text = "Date d'application")

            if (appliedDateIsChecked) {
                // TODO
            }
        }

        Spacer(modifier = Modifier.padding(top = 14.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Sélectionner un compte de destination",
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Checkbox(
                checked = selectAccount,
                onCheckedChange = { selected ->
                    if(!selected) {
                        selectedAccount = null
                    }
                    selectAccount = selected
                }
            )
        }

        if (selectAccount) {
            Spacer(modifier = Modifier.padding(top = 8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
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
            }
        }
        Button(
            modifier = Modifier.padding(vertical = 18.dp),
            onClick = onValidateAction
        ) {
            Text(text = stringResource(R.string.edit_transaction))
        }
    }
}

@Preview
@Composable
private fun BottomSheetEditAccountPreview() {
    MoneyManagerTheme {
        BottomSheetEditTransaction(
            transaction = Transaction(id = 0, name = "Transaction name", amount = 100f),
            accounts = listOf(Account(name = "test", currentBalance = 0f)),
            onEvent = { }
        )
    }
}