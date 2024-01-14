package com.dms.moneymanager.presentation.screen.transactions

import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.BottomSheetType

data class TransactionsUiModel(
    val transactionsUiState: TransactionsUiState = TransactionsUiState.NORMAL,
    val accounts: List<Account> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val selectedAccount: Account? = null,
    val selectedTransaction: Transaction? = null
)

enum class TransactionsUiState {
    NORMAL, APPLIED_TRANSACTION
}

sealed interface TransactionsBottomSheetType : BottomSheetType {
    class BottomSheetConfirmRemoveTransaction(val transaction: Transaction) :
        TransactionsBottomSheetType
}
