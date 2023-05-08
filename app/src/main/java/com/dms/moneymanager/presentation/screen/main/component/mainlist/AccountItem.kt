package com.dms.moneymanager.presentation.screen.main.component.mainlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.presentation.screen.main.model.MainUiState
import com.dms.moneymanager.presentation.util.toAmountString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountItem(
    mainUiState: MainUiState,
    account: Account,
    appliedTransaction: () -> Unit,
    editAction: () -> Unit,
    removeAction: () -> Unit
) {
    var expandedDropDownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(all = 4.dp),
        shape = RoundedCornerShape(size = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(width = 0.3.dp, Color.White),
        onClick = {
            when (mainUiState) {
                MainUiState.APPLIED_TRANSACTION -> {
                    appliedTransaction()
                }

                else -> expandedDropDownMenu = true
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(all = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = account.currentBalance.toAmountString(),
                style = MaterialTheme.typography.bodyMedium
            )

            DropDownMenuAccount(
                expanded = expandedDropDownMenu,
                closeDropDownMenuAction = { expandedDropDownMenu = false },
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
    editAction: () -> Unit,
    removeAction: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = closeDropDownMenuAction
    ) {
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