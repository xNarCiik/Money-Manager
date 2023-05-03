package com.dms.moneymanager.presentation.screen.history.model

import com.dms.moneymanager.domain.model.main.Transaction

data class HistoryUiModel(
    val listTransaction: List<Transaction> = emptyList()
)