package com.dms.moneymanager.presentation.screen.settings

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.usecase.AccountUseCase
import com.dms.moneymanager.domain.usecase.TransactionUseCase
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

sealed interface SettingsEvent: BaseEvent {
    object OnClickSaveState : SettingsEvent
    object OnClickRestoreState : SettingsEvent
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val accountUseCase: AccountUseCase,
    private val transactionUseCase: TransactionUseCase
) : BaseViewModel() {

    private val gson = Gson()
    private var fileTransactions: File
    private var fileAccounts: File

    init {
        val filesDir = application.filesDir
        fileAccounts = File(filesDir, "accounts.json")
        fileTransactions = File(filesDir, "transactions.json")
    }

    override fun onEvent(event: BaseEvent) {
        when (event) {
            SettingsEvent.OnClickSaveState -> {
                saveCurrentState()
            }

            SettingsEvent.OnClickRestoreState -> {
                restoreState()
            }
        }
    }

    private fun saveCurrentState() {
        viewModelScope.launch {
            try {
                val accounts = accountUseCase.getAccounts()
                val jsonAccounts = gson.toJson(accounts)
                fileAccounts.writeText(text = jsonAccounts)

                val transactions = transactionUseCase.getAllTransactions()
                val jsonTransactions = gson.toJson(transactions)
                fileTransactions.writeText(text = jsonTransactions)

                _toastMessage.value = R.string.save_state_success
            } catch (exception: Exception) {
                Timber.e(exception)
                _toastMessage.value = R.string.error_save_state
            }
        }
    }

    private fun restoreState() {
    }
}