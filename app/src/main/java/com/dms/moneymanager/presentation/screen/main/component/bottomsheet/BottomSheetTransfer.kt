@file:OptIn(ExperimentalMaterial3Api::class)

package com.dms.moneymanager.presentation.screen.main.component.bottomsheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.presentation.util.getTextColor
import com.dms.moneymanager.presentation.util.toAmountString
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun BottomSheetTransfer(
    listAccount: List<Account>,
    account: Account,
    onEvent: (MainEvent) -> Unit
) {
    val listAccountFiltered = listAccount.filter { it.id != account.id }

    var amount by remember { mutableStateOf(TextFieldValue("")) }
    var selectedAccount by remember { mutableStateOf<Account?>(null) }

    val onValidateAction = {
        onEvent(
            MainEvent.OnClickTransfer(
                transmitterAccount = account,
                receiverAccount = selectedAccount,
                amount = amount.text
            )
        )
    }

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

        Spacer(modifier = Modifier.height(height = 18.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Entrer le montant du transfert à effectuer depuis ${account.name} et sélectionner le compte qui recevra ce montant.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(height = 18.dp))

        TextField(
            modifier = Modifier
                .width(250.dp),
            value = amount,
            onValueChange = { amount = it },
            textStyle = TextStyle.Default.copy(textAlign = TextAlign.Center),
            singleLine = true,
            label = { Text(text = "Montant (disponible : ${account.availableBalance})") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onValidateAction() }
            )
        )

        Spacer(modifier = Modifier.height(height = 18.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            itemsIndexed(listAccountFiltered) { _, account ->
                AccountTransferItem(
                    account = account,
                    isSelected = selectedAccount == account,
                    onClick = {
                        selectedAccount = account
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 18.dp))

        Button(onClick = onValidateAction) {
            Text(text = stringResource(R.string.transfer))
        }

        Spacer(modifier = Modifier.height(height = 18.dp))
    }
}

@Composable
private fun AccountTransferItem(
    modifier: Modifier = Modifier,
    account: Account,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.padding(all = 2.dp),
        shape = RoundedCornerShape(size = 8.dp),
        border = BorderStroke(width = 1.dp, color = Color.Black),
        onClick = onClick

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )

            Text(
                text = account.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = account.currentBalance.toAmountString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                color = account.currentBalance.getTextColor()
            )
        }
    }
}

@Preview
@Composable
private fun BottomSheetEditAccountPreview() {
    MoneyManagerTheme {
        val account1 = Account(id = 0, name = "account 1", currentBalance = 50f)
        val account2 = Account(id = 1, name = "account 2", currentBalance = 100f)
        val account3 = Account(id = 2, name = "account 3", currentBalance = -5f)
        val account4 = Account(id = 3, name = "account 4", currentBalance = 125.3f)

        BottomSheetTransfer(
            listOf(account1, account2, account3, account4),
            account = account1,
            onEvent = { }
        )
    }
}