package com.dms.moneymanager.presentation.screen.main.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.screen.main.MainEvent

@Composable
fun BottomSheetCreateTransaction(
    onEvent: (MainEvent) -> Unit,
    closeBottomSheetAction: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var name by remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = name,
            onValueChange = { newText ->
                name = newText
            },
            label = { Text(text = "Name") },
            placeholder = { Text(text = "test") }
        )

        var value by remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = value,
            onValueChange = { newText ->
                value = newText
            },
            label = { Text(text = "Value") },
            placeholder = { Text(text = "test") }
        )

        Button(
            onClick = {
                closeBottomSheetAction()
                onEvent(
                    MainEvent.AddTransactionEvent(
                        transaction = Transaction(
                            name = name.text,
                            amount = value.text.toFloat(), // TODO
                            isApplied = false
                        )
                    )
                )
            }
        ) {
            Text("Create transaction")
        }
    }
}