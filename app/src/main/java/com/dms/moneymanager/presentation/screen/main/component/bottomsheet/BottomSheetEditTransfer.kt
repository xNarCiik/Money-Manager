package com.dms.moneymanager.presentation.screen.main.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetTransfer(
    listAccount: List<Account>,
    account: Account,
    onEvent: (MainEvent) -> Unit
) {
    val listAccountFiltered = listAccount.filter { it.id != account.id }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.transfer),
            style = MaterialTheme.typography.titleLarge
        )

        var amount by remember { mutableStateOf(TextFieldValue("0.0")) }
        var expanded by remember { mutableStateOf(false) }
        var selectedAccount by remember { mutableStateOf(listAccountFiltered[0]) }

        val onValidateAction = {
            onEvent(
                MainEvent.OnClickTransfer(
                    transmitterAccount = account,
                    receiverAccount = selectedAccount,
                    amount = amount.text
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextField(
                modifier = Modifier
                    .width(100.dp),
                value = amount,
                onValueChange = { amount = it },
                singleLine = true,
                label = { Text(text = stringResource(R.string.amount)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { onValidateAction() }
                )
            )

            Icon(
                modifier = Modifier
                    .size(size = 35.dp),
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = "Arrow Right icon",
                tint = Color.White
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = selectedAccount.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listAccountFiltered.forEachIndexed { index, account ->
                        DropdownMenuItem(
                            text = { Text(text = account.name) },
                            onClick = {
                                selectedAccount = listAccount[index]
                                expanded = false
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
            Text(text = stringResource(R.string.transfer))
        }
    }
}

@Preview
@Composable
private fun BottomSheetEditAccountPreview() {
    MoneyManagerTheme {
        val account = Account(id = 0, name = "name", currentBalance = 100f)
        BottomSheetTransfer(
            listOf(account),
            account = account,
            onEvent = { }
        )
    }
}