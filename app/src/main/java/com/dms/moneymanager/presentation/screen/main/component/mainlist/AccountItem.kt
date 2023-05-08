package com.dms.moneymanager.presentation.screen.main.component.mainlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.presentation.screen.main.model.MainUiState
import com.dms.moneymanager.presentation.util.getTextColor
import com.dms.moneymanager.presentation.util.toAmountString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountItem(
    modifier: Modifier = Modifier,
    mainUiState: MainUiState,
    account: Account,
    appliedTransaction: () -> Unit,
    transferAction: () -> Unit,
    editAction: () -> Unit,
    removeAction: () -> Unit
) {
    var expandedDropDownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.padding(all = 4.dp),
        shape = RoundedCornerShape(size = 8.dp),
        border = BorderStroke(width = 1.dp, color = Color.Black),
        onClick = {
            when (mainUiState) {
                MainUiState.APPLIED_TRANSACTION -> {
                    appliedTransaction()
                }

                else -> expandedDropDownMenu = true
            }
        }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = account.currentBalance.toAmountString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                color = account.currentBalance.getTextColor()
            )

            DropDownMenuAccount(
                expanded = expandedDropDownMenu,
                closeDropDownMenuAction = { expandedDropDownMenu = false },
                transferAction = transferAction,
                editAction = editAction,
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
            text = { Text(text = stringResource(R.string.remove)) },
            onClick = {
                removeAction()
                closeDropDownMenuAction()
            }
        )
    }
}