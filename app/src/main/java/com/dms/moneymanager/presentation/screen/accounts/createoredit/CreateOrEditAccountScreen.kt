package com.dms.moneymanager.presentation.screen.accounts.createoredit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.dms.moneymanager.presentation.screen.accounts.AccountsEvent
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun CreateOrEditAccountScreen(
    account: Account? = null,
    onEvent: (AccountsEvent) -> Unit
) {
    Scaffold {
        CreateOrEditAccountContent(
            modifier = Modifier.padding(it),
            onEvent = onEvent,
            account = account
        )
    }
}

@Composable
private fun CreateOrEditAccountContent(
    modifier: Modifier = Modifier,
    onEvent: (AccountsEvent) -> Unit,
    account: Account? = null
) {
    // if transaction != null is a edit screen
    val isCreateScreen = account == null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(if (isCreateScreen) R.string.add_account else R.string.edit_account),
            style = MaterialTheme.typography.titleLarge
        )

        var name by remember { mutableStateOf(TextFieldValue(account?.name ?: "")) }
        var balance by remember {
            mutableStateOf(
                TextFieldValue(
                    account?.currentBalance?.toString() ?: ""
                )
            )
        }

        val onValidateAction: () -> Unit = {
            // TODO unifier Add & edit
            if (isCreateScreen) {
                onEvent(
                    AccountsEvent.ClickAddAccount(
                        name = name.text,
                        balance = balance.text
                    )
                )
            } else {
                account?.id?.let { id ->
                    onEvent(
                        AccountsEvent.ClickEditAccount(
                            id = account.id,
                            name = name.text,
                            balance = balance.text
                        )
                    )
                }
            }
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onValidateAction() }
            )
        )

        Button(
            modifier = Modifier.padding(vertical = 18.dp),
            onClick = onValidateAction
        ) {
            Text(text = stringResource(if (isCreateScreen) R.string.add_the_account else R.string.edit_account))
        }
    }
}

@Preview
@Composable
private fun CreateOrEditAccountScreenPreview() {
    MoneyManagerTheme {
        CreateOrEditAccountScreen(
            onEvent = { },
        )
    }
}