package com.dms.moneymanager.presentation.screen.accounts

import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.presentation.BottomSheetType

data class AccountsUiModel(
    val accounts: List<Account> = emptyList(),
    val currentBalance: Float = 0.0f,
    val futureBalance: Float = 0.0f,
)


sealed interface AccountsBottomSheetType : BottomSheetType {
    class BottomSheetTransfer(val account: Account) : AccountsBottomSheetType
    class BottomSheetConfirmRemoveAccount(val account: Account) : AccountsBottomSheetType
}
