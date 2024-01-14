package com.dms.moneymanager.presentation.screen.settings

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.usecase.AccountUseCase
import com.dms.moneymanager.domain.usecase.TransactionUseCase
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.BaseViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

sealed interface SettingsEvent : BaseEvent {
    object OnClickSaveState : SettingsEvent
    object OnClickRestoreState : SettingsEvent
    object OnClickRemoveData : SettingsEvent
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
        super.onEvent(event)

        when (event) {
            SettingsEvent.OnClickSaveState -> {
                saveCurrentState()
            }

            SettingsEvent.OnClickRestoreState -> {
                restoreState()
            }

            SettingsEvent.OnClickRemoveData -> {
                removeData()
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
        viewModelScope.launch {
            try {
                val jsonAccounts = fileAccounts.readText()
                val accountsListType = object : TypeToken<ArrayList<Account>>() {}.type

                val accounts = gson.fromJson<List<Account>>(jsonAccounts, accountsListType)
                accounts.forEach { account ->
                    accountUseCase.createAccount(account = account)
                }

                val jsonTransactions = fileTransactions.readText()
                val transactionsListType = object : TypeToken<ArrayList<Transaction>>() {}.type

                val transactions =
                    gson.fromJson<List<Transaction>>(jsonTransactions, transactionsListType)
                transactions.forEach { transaction ->
                    transactionUseCase.createTransaction(transaction = transaction)
                }

                _toastMessage.value = R.string.restore_state_success
            } catch (exception: Exception) {
                Timber.e(exception)
                _toastMessage.value = R.string.error_restore_state
            }
        }
    }

    private fun removeData() {
        viewModelScope.launch {
            accountUseCase.removeAll()
            transactionUseCase.removeAll()
            _toastMessage.value = R.string.data_removed
        }
    }
}