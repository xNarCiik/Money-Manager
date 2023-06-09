package com.dms.moneymanager.presentation.screen.main.model

import androidx.annotation.StringRes
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction

data class MainUiModel(
    val mainUiState: MainUiState = MainUiState.NORMAL,
    val currentBalance: Float = 0.0f,
    val futureBalance: Float = 0.0f,
    val accounts: List<Account> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val selectedAccount: Account? = null,
    val selectedTransaction: Transaction? = null,
    val mainBottomSheetType: MainBottomSheetType? = null,
    @StringRes val toastMessage: Int? = null
)

enum class MainUiState {
    NORMAL, APPLIED_TRANSACTION
}

sealed interface MainBottomSheetType {
    object BottomSheetCreateAccount : MainBottomSheetType
    class BottomSheetTransfer(val account: Account) : MainBottomSheetType
    class BottomSheetEditAccount(val account: Account) : MainBottomSheetType
    class BottomSheetConfirmRemoveAccount(val account: Account) : MainBottomSheetType
    object BottomSheetCreateTransaction : MainBottomSheetType
    class BottomSheetEditTransaction(val transaction: Transaction) : MainBottomSheetType
    class BottomSheetConfirmRemoveTransaction(val transaction: Transaction) : MainBottomSheetType
}
