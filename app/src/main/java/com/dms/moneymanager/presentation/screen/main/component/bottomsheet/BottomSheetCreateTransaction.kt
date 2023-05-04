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
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun BottomSheetCreateTransaction(
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
            text = stringResource(R.string.add_transaction),
            style = MaterialTheme.typography.titleLarge
        )

        var name by remember { mutableStateOf(TextFieldValue("")) }
        var amount by remember { mutableStateOf(TextFieldValue("")) }

        val onValidateAction = {
            onEvent(
                MainEvent.AddTransactionEvent(
                    name = name.text,
                    amount = amount.text
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { onValidateAction() }
            )
        )

        Button(
            modifier = Modifier.padding(vertical = 18.dp),
            onClick = onValidateAction
        ) {
            Text(text = stringResource(R.string.add_the_transaction))
        }
    }
}

@Preview
@Composable
private fun BottomSheetCreateTransactionPreview() {
    MoneyManagerTheme {
        BottomSheetCreateTransaction(
            onEvent = { }
        )
    }
}