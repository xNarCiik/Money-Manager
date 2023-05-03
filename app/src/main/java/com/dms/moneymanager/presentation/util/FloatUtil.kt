package com.dms.moneymanager.presentation.util

fun Float.toAmountString() =
    String.format("%.2f€", this).replace(oldChar = ',', newChar = '.') // TODO Handle currency