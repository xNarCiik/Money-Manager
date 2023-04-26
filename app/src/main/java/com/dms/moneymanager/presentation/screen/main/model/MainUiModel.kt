package com.dms.moneymanager.presentation.screen.main.model

import androidx.compose.ui.graphics.Color
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction

data class MainUiModel(
    val headerBackgroundColor: HeaderBackgroundColor = HeaderBackgroundColor.STABLE_BALANCE,
    val currentBalance: Float = 0.0f,
    val futureBalance: Float = 0.0f,
    val listAccount: List<Account> = emptyList(),
    val listTransaction: List<Transaction> = emptyList(),
    val mainBottomSheetType: MainBottomSheetType? = null
)

enum class HeaderBackgroundColor(val value: Color) {
    NEGATIVE_BALANCE(Color.Red),
    STABLE_BALANCE(Color.DarkGray),
    POSITIVE_BALANCE(Color.Green)
}

enum class MainBottomSheetType {
    CREATE_ACCOUNT,
    CREATE_TRANSACTION
}
