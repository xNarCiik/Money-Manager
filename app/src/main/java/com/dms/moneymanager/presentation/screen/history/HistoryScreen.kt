package com.dms.moneymanager.presentation.screen.history

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.util.toAmountString

@Composable
fun HistoryScreen(viewState: HistoryUiModel) {
    val scaffoldState = rememberScaffoldState()
    val listTransaction = viewState.listTransaction

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        bottomBar = {
          /*  BottomBar(
                defaultSelectedItem = MenuRoute.HOME,
                onHomeClick = { },
                onSettingClick = { }
            ) */ // TODO LATER
        }
    ) {
        HistoryContent(
            modifier = Modifier.padding(paddingValues = it),
            listTransaction = listTransaction
        )
    }
}

@Composable
private fun HistoryContent(
    modifier: Modifier = Modifier,
    listTransaction: List<Transaction>
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(listTransaction) { index, transaction ->
            Text(
                modifier = Modifier.padding(all = 18.dp),
                text = "$index. ${transaction.name}(${transaction.id}) : ${transaction.amount.toAmountString()} isApplied:${transaction.isApplied}",
                color = Color.White
            )
        }
    }
}