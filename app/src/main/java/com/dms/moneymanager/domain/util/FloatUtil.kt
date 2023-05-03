package com.dms.moneymanager.domain.util

fun Float.toAmountString() =
    String.format("%.2f€", this).replace(oldChar = ',', newChar = '.') // TODO Handle currency