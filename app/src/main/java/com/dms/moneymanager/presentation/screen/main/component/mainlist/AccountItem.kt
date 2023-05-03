package com.dms.moneymanager.presentation.screen.main.component.mainlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.util.toAmountString

@Composable
fun AccountItem(
    account: Account,
    removeAction: () -> Unit,
    showDivider: Boolean
) {
    var expandedDropDownMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expandedDropDownMenu = true }
    ) {
        Row(modifier = Modifier.padding(all = 15.dp)) {
            Text(
                text = account.name,
                modifier = Modifier.weight(weight = 1f)
            )
            Text(
                text = account.currentBalance.toAmountString()
            )
        }

        if (showDivider) {
            Divider()
        }

        DropDownMenuAccount(
            expanded = expandedDropDownMenu,
            closeDropDownMenuAction = { expandedDropDownMenu = false },
            removeAction = removeAction
        )
    }
}

@Composable
private fun DropDownMenuAccount(
    expanded: Boolean,
    closeDropDownMenuAction: () -> Unit,
    removeAction: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = closeDropDownMenuAction
    ) {
        DropdownMenuItem(
            text = { Text("Supprimer") },
            onClick = {
                removeAction()
                closeDropDownMenuAction()
            }
        )
    }
}