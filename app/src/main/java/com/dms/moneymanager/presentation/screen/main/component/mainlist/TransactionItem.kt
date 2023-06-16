package com.dms.moneymanager.presentation.screen.main.component.mainlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.util.getDayOfTheMonth
import com.dms.moneymanager.presentation.util.getTextColor
import com.dms.moneymanager.presentation.util.toAmountString
import com.dms.moneymanager.ui.theme.Blue
import com.dms.moneymanager.ui.theme.Green

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
        modifier = Modifier.padding(all = 4.dp),
        shape = RoundedCornerShape(size = 8.dp),
        border = BorderStroke(width = 1.dp, Color.Black),
        onClick = { expandedDropDownMenu = true }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Text(
                text = transaction.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = transaction.amount.toAmountString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = transaction.amount.getTextColor()
                )

                Spacer(modifier = Modifier.weight(weight = 1f))

                transaction.destinationAccount?.let { destinationAccount ->
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .border(
                                width = 1.dp,
                                color = Blue,
                                shape = CircleShape
                            )
                            .background(color = Blue, shape = CircleShape)
                            .padding(all = 4.dp),
                        text = destinationAccount.name, // TODO FIND ACCOUNT NAME BY ID
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        color = Color.White
                    )
                }

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