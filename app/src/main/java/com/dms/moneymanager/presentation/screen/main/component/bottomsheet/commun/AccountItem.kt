package com.dms.moneymanager.presentation.screen.main.component.bottomsheet.commun

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.presentation.util.getTextColor
import com.dms.moneymanager.presentation.util.toAmountString
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountItem(
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
private fun AccountItemUnselectedPreview() {
    MoneyManagerTheme {
        AccountItem(
            account = Account(id = 0, name = "account 1", currentBalance = 50f),
            isSelected = false,
            onClick = { },
        )
    }
}

@Preview
@Composable
private fun AccountItemSelectedPreview() {
    MoneyManagerTheme {
        AccountItem(
            account = Account(id = 0, name = "account 1", currentBalance = 50f),
            isSelected = true,
            onClick = { },
        )
    }
}
