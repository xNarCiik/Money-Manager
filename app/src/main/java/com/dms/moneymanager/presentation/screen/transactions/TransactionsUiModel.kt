package com.dms.moneymanager.presentation.screen.transactions

import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.presentation.BottomSheetType

data class TransactionsUiModel(
    val transactionsUiState: TransactionsUiState = TransactionsUiState.NORMAL,
    val currentBalance: Float = 0.0f,
    val futureBalance: Float = 0.0f,
    val accounts: List<Account> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val selectedAccount: Account? = null,
    val selectedTransaction: Transaction? = null
)

enum class TransactionsUiState {
    NORMAL, APPLIED_TRANSACTION
}

sealed interface TransactionsBottomSheetType : BottomSheetType {
    object BottomSheetCreateAccount : TransactionsBottomSheetType
    class BottomSheetTransfer(val account: Account) : TransactionsBottomSheetType
    class BottomSheetEditAccount(val account: Account) : TransactionsBottomSheetType
    class BottomSheetConfirmRemoveAccount(val account: Account) : TransactionsBottomSheetType
    object BottomSheetCreateTransaction : TransactionsBottomSheetType
    class BottomSheetEditTransaction(val transaction: Transaction) : TransactionsBottomSheetType
    class BottomSheetConfirmRemoveTransaction(val transaction: Transaction) :
        TransactionsBottomSheetType
}
