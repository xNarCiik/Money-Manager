package com.dms.moneymanager.presentation.screen.main.model

import androidx.annotation.StringRes
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction

data class MainUiModel(
    val mainUiState: MainUiState = MainUiState.NORMAL,
    val currentBalance: Float = 0.0f,
    val futureBalance: Float = 0.0f,
    val listAccount: List<Account> = emptyList(),
    val listTransaction: List<Transaction> = emptyList(),
    val selectedTransaction: Transaction? = null,
    val mainBottomSheetType: MainBottomSheetType? = null,
    @StringRes val toastMessage: Int? = null
)

enum class MainUiState {
    NORMAL, APPLIED_TRANSACTION
}

sealed interface MainBottomSheetType {
    object BottomSheetCreateAccount : MainBottomSheetType
    object BottomSheetCreateTransaction : MainBottomSheetType
}
