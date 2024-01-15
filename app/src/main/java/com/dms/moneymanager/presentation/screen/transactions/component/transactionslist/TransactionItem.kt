package com.dms.moneymanager.presentation.screen.transactions.component.transactionslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.util.getTextColor
import com.dms.moneymanager.presentation.util.toAmountString

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    appliedAction: () -> Unit,
    editAction: () -> Unit,
    enableOrDisableAction: () -> Unit,
    removeAction: () -> Unit
) {
    var expandedDropDownMenu by remember { mutableStateOf(false) }
    val isEnable = transaction.isEnable

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                expandedDropDownMenu = true
            }
            .background(color = if (isEnable) MaterialTheme.colorScheme.surfaceVariant else Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .padding(all = 8.dp)
        ) {
            Text(
                text = transaction.name,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = transaction.destinationAccount?.name ?: "", // TODO FIND ACCOUNT NAME BY ID
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp),
            text = transaction.amount.toAmountString(),
            style = MaterialTheme.typography.bodyLarge,
            color = transaction.amount.getTextColor()
        )

        /* // TODO AFFICHER DATE
        transaction.dueDate?.let { dueDate ->
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .border(
                        width = 1.dp,
                        color = Green,
                        shape = CircleShape
                    )
                    .background(color = Green, shape = CircleShape)
                    .padding(all = 4.dp),
                text = "${getDayOfTheMonth(dueDate)} du mois", // TODO PHRASE
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                color = Color.White
            )
        }
         */

        DropDownMenuTransaction(
            expanded = expandedDropDownMenu,
            closeDropDownMenuAction = { expandedDropDownMenu = false },
            appliedAction = appliedAction,
            editAction = editAction,
            isEnable = isEnable,
            enableOrDisableAction = enableOrDisableAction,
            removeAction = removeAction
        )

    }
}

@Composable
private fun DropDownMenuTransaction(
    expanded: Boolean,
    closeDropDownMenuAction: () -> Unit,
    appliedAction: () -> Unit,
    editAction: () -> Unit,
    isEnable: Boolean,
    enableOrDisableAction: () -> Unit,
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
        HorizontalDivider()
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.edit)) },
            onClick = {
                editAction()
                closeDropDownMenuAction()
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = {
                Text(
                    text =
                    stringResource(id = if (isEnable) R.string.disable else R.string.enable)
                )
            },
            onClick = {
                enableOrDisableAction()
                closeDropDownMenuAction()
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.remove)) },
            onClick = {
                removeAction()
                closeDropDownMenuAction()
            }
        )
    }
}