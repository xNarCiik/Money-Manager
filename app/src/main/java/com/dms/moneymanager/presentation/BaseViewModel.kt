package com.dms.moneymanager.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface BottomSheetType

interface BaseEvent {
    class OpenBottomSheet(val bottomSheetType: BottomSheetType) : BaseEvent
    object CloseBottomSheet : BaseEvent
    object RemoveToast : BaseEvent
    object ActionPerformedSnackbar : BaseEvent
}

abstract class BaseViewModel : ViewModel() {

    protected var _currentBottomSheet = MutableStateFlow<BottomSheetType?>(value = null)
    val currentBottomSheet: StateFlow<BottomSheetType?> = _currentBottomSheet

    protected var _toastMessage = MutableStateFlow<Int?>(value = null)
    val toastMessage: StateFlow<Int?> = _toastMessage

    open fun onEvent(event: BaseEvent) {
        when (event) {
            is BaseEvent.OpenBottomSheet -> {
                _currentBottomSheet.value = event.bottomSheetType
            }

            is BaseEvent.CloseBottomSheet -> {
                _currentBottomSheet.value = null
            }

            is BaseEvent.RemoveToast -> {
                _toastMessage.value = null
            }
        }
    }
}