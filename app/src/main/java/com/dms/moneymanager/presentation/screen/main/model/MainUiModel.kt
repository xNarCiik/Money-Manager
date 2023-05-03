package com.dms.moneymanager.presentation.screen.main.model

import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction

data class MainUiModel(
    val currentBalance: Float = 0.0f,
    val futureBalance: Float = 0.0f,
    val listAccount: List<Account> = emptyList(),
    val listTransaction: List<Transaction> = emptyList(),
    val mainBottomSheetType: MainBottomSheetType? = null
)

abstract class MainBottomSheetType
object BottomSheetCreateAccount : MainBottomSheetType()
object BottomSheetCreateTransaction : MainBottomSheetType()
class BottomSheetAppliedTransaction(val transaction: Transaction) : MainBottomSheetType()
