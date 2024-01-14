package com.dms.moneymanager.presentation.screen.transactions.component.mainlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.presentation.screen.transactions.model.TransactionsUiState
import com.dms.moneymanager.presentation.util.getTextColor
import com.dms.moneymanager.presentation.util.toAmountString

@Composable
fun AccountItem(
    modifier: Modifier = Modifier,
    transactionsUiState: TransactionsUiState,
    account: Account,
    appliedTransaction: () -> Unit,
    transferAction: () -> Unit,
    editAction: () -> Unit,
    enableOrDisableAction: () -> Unit,
    removeAction: () -> Unit
) {
    var expandedDropDownMenu by remember { mutableStateOf(false) }
    val isEnable = account.isEnable

    Card(
        modifier = modifier
            .padding(all = 4.dp),
        shape = RoundedCornerShape(size = 8.dp),
        border = BorderStroke(width = 1.dp, color = Color.Black),
        colors = cardColors(
            containerColor = if (isEnable) MaterialTheme.colorScheme.surfaceVariant else Color.Gray
        ),
        onClick = {
            when (transactionsUiState) {
                TransactionsUiState.APPLIED_TRANSACTION -> {
                    appliedTransaction()
                }

                else -> expandedDropDownMenu = true
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = account.currentBalance.toAmountString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = account.currentBalance.getTextColor()
                )

                Text(
                    text = "(${account.futureBalance.toAmountString()})",
                    style = MaterialTheme.typography.bodySmall,
                    color = account.futureBalance.getTextColor()
                )
            }

            DropDownMenuAccount(
                expanded = expandedDropDownMenu,
                closeDropDownMenuAction = { expandedDropDownMenu = false },
                transferAction = transferAction,
                editAction = editAction,
                isEnable = isEnable,
                enableOrDisableAction = enableOrDisableAction,
                removeAction = removeAction
            )
        }

    }
}

@Composable
private fun DropDownMenuAccount(
    expanded: Boolean,
    closeDropDownMenuAction: () -> Unit,
    transferAction: () -> Unit,
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
            text = { Text(text = stringResource(R.string.transfer)) },
            onClick = {
                transferAction()
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
            text = { Text(text = stringResource(R.string.remove)) },
            onClick = {
                removeAction()
                closeDropDownMenuAction()
            }
        )
    }
}