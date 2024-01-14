package com.dms.moneymanager.presentation.screen.history

import com.dms.moneymanager.domain.model.main.Transaction

data class HistoryUiModel(
    val listTransaction: List<Transaction> = emptyList()
)