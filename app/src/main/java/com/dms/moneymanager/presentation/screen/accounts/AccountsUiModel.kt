package com.dms.moneymanager.presentation.screen.accounts

import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.presentation.BottomSheetType

data class AccountsUiModel(
    val accounts: List<Account> = emptyList(),
)


sealed interface AccountsBottomSheetType : BottomSheetType {
}
