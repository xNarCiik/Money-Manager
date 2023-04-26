package com.dms.moneymanager.presentation.screen.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.screen.main.MainEvent
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.BottomSheetAppliedTransaction
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.BottomSheetCreateAccount
import com.dms.moneymanager.presentation.screen.main.component.bottomsheet.BottomSheetCreateTransaction
import com.dms.moneymanager.presentation.screen.main.component.mainlist.MainList
import com.dms.moneymanager.presentation.screen.main.model.BottomSheetAppliedTransaction
import com.dms.moneymanager.presentation.screen.main.model.BottomSheetCreateAccount
import com.dms.moneymanager.presentation.screen.main.model.BottomSheetCreateTransaction
import com.dms.moneymanager.presentation.screen.main.model.HeaderBackgroundColor
import com.dms.moneymanager.presentation.screen.main.model.MainUiModel
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewState: MainUiModel,
    onEvent: (MainEvent) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Sheet content
    if (viewState.mainBottomSheetType != null) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(MainEvent.CloseBottomSheet) },
            sheetState = bottomSheetState
        ) {
            when (viewState.mainBottomSheetType) {
                is BottomSheetCreateAccount -> {
                    BottomSheetCreateAccount(
                        onEvent = onEvent,
                        closeBottomSheetAction = { onEvent(MainEvent.CloseBottomSheet) }
                    )
                }

                is BottomSheetCreateTransaction -> {
                    BottomSheetCreateTransaction(
                        onEvent = onEvent,
                        closeBottomSheetAction = { onEvent(MainEvent.CloseBottomSheet) }
                    )
                }

                is BottomSheetAppliedTransaction -> {
                    BottomSheetAppliedTransaction(
                        accounts = viewState.listAccount,
                        transaction = viewState.mainBottomSheetType.transaction,
                        onEvent = onEvent,
                        closeBottomSheetAction = { onEvent(MainEvent.CloseBottomSheet) }
                    )
                }
            }
        }
    }

    MainContent(
        viewState = viewState,
        onEvent = onEvent
    )
}

@Composable
private fun MainContent(
    viewState: MainUiModel,
    onEvent: (MainEvent) -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {
        HeaderContent(
            headerBackgroundColor = viewState.headerBackgroundColor,
            currentBalance = viewState.currentBalance,
            futureBalance = viewState.futureBalance
        )
        MainList(
            listAccount = viewState.listAccount,
            listTransaction = viewState.listTransaction,
            onEvent = onEvent
        )
        Spacer(modifier = Modifier.weight(1f))
        AddFloatingButton(
            modifier = Modifier.padding(bottom = 10.dp, end = 10.dp),
            addAccountAction = { onEvent(MainEvent.OpenBottomSheet(mainBottomSheetType = BottomSheetCreateAccount)) },
            addTransactionAction = { onEvent(MainEvent.OpenBottomSheet(mainBottomSheetType = BottomSheetCreateTransaction)) }
        )
    }
}

@Composable
private fun HeaderContent(
    headerBackgroundColor: HeaderBackgroundColor,
    currentBalance: Float,
    futureBalance: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = headerBackgroundColor.value)
            .padding(all = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Budjet actuel")
            Text(text = "${currentBalance}€")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Budjet prévisionnel futur")
            Text(text = "${futureBalance}€")
        }
    }
}

@Composable
private fun AddFloatingButton(
    modifier: Modifier,
    addAccountAction: () -> Unit,
    addTransactionAction: () -> Unit
) {
    var expandedDropDownMenu by remember { mutableStateOf(false) }

    FloatingActionButton(
        modifier = modifier,
        onClick = { expandedDropDownMenu = true },
        containerColor = Color.Blue,
        shape = RoundedCornerShape(22.dp),
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add FAB",
            tint = Color.White,
        )

        DropdownMenu(
            expanded = expandedDropDownMenu,
            onDismissRequest = { expandedDropDownMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Add account") },
                onClick = {
                    expandedDropDownMenu = false
                    addAccountAction()
                }
            )
            Divider()
            DropdownMenuItem(
                text = { Text("Add transaction") },
                onClick = {
                    expandedDropDownMenu = false
                    addTransactionAction()
                }
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MoneyManagerTheme {
        MainScreen(
            viewState = MainUiModel(
                listAccount = arrayListOf(Account(name = "account 1", currentBalance = 2000.0f)),
                listTransaction = arrayListOf(Transaction(name = "transaction 1", amount = -10.5f))
            ),
            onEvent = { }
        )
    }
}