package com.dms.moneymanager.presentation.screen.main.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun BottomSheetEditAccount(
    account: Account,
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
            text = stringResource(R.string.edit_account),
            style = MaterialTheme.typography.titleLarge
        )

        var name by remember { mutableStateOf(TextFieldValue(account.name)) }
        var balance by remember { mutableStateOf(TextFieldValue(account.currentBalance.toString())) }

        val onValidateAction = {
            onEvent(
                MainEvent.EditAccountEvent(
                    id = account.id,
                    name = name.text,
                    balance = balance.text
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
            value = balance,
            onValueChange = { balance = it },
            singleLine = true,
            label = { Text(text = stringResource(R.string.balance)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { onValidateAction() }
            )
        )

        Button(
            modifier = Modifier.padding(vertical = 18.dp),
            onClick = onValidateAction
        ) {
            Text(text = stringResource(R.string.edit_account))
        }
    }
}

@Preview
@Composable
private fun BottomSheetEditAccountPreview() {
    MoneyManagerTheme {
        BottomSheetEditAccount(
            account = Account(id = 0, name = "name", currentBalance = 100f),
            onEvent = { }
        )
    }
}