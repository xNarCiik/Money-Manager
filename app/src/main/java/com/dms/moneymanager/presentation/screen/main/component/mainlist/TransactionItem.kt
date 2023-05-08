package com.dms.moneymanager.presentation.screen.main.component.mainlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.util.toAmountString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionItem(
    transaction: Transaction,
    appliedAction: () -> Unit,
    editAction: () -> Unit,
    removeAction: () -> Unit
) {
    var expandedDropDownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(all = 4.dp),
        shape = RoundedCornerShape(size = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(width = 0.3.dp, Color.White),
        onClick = { expandedDropDownMenu = true }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(all = 15.dp)) {
                Text(
                    text = transaction.name,
                    modifier = Modifier.weight(weight = 1f),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = transaction.amount.toAmountString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            DropDownMenuTransaction(
                expanded = expandedDropDownMenu,
                closeDropDownMenuAction = { expandedDropDownMenu = false },
                appliedAction = appliedAction,
                editAction = editAction,
                removeAction = removeAction
            )
        }
    }
}

@Composable
private fun DropDownMenuTransaction(
    expanded: Boolean,
    closeDropDownMenuAction: () -> Unit,
    appliedAction: () -> Unit,
    editAction: () -> Unit,
    removeAction: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = closeDropDownMenuAction
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.applied)) },
            onClick = {
                appliedAction()
                closeDropDownMenuAction()
            }
        )
        Divider()
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.edit)) },
            onClick = {
                editAction()
                closeDropDownMenuAction()
            }
        )
        Divider()
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.remove)) },
            onClick = {
                removeAction()
                closeDropDownMenuAction()
            }
        )
    }
}