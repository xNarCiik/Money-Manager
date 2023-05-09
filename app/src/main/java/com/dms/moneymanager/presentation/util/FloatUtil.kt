package com.dms.moneymanager.presentation.util

import com.dms.moneymanager.ui.theme.Gray
import com.dms.moneymanager.ui.theme.Green
import com.dms.moneymanager.ui.theme.Red

fun Float.toAmountString(): String {
    val stringBuilder = StringBuilder()
    if (this > 0) {
        stringBuilder.append("+ ")
    }
    stringBuilder.append(
        String.format("%.2f€", this)
            .replace(oldValue = "-", newValue = "- ")
            .replace(oldChar = ',', newChar = '.')
    )
    return stringBuilder.toString() // TODO Handle currency
}

fun Float.getTextColor() = if (this > 0) Green else if (this < 0) Red else Gray
