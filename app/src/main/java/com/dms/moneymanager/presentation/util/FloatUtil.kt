package com.dms.moneymanager.presentation.util

import com.dms.moneymanager.ui.theme.Gray
import com.dms.moneymanager.ui.theme.Green
import com.dms.moneymanager.ui.theme.Red
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val dec = DecimalFormat("###,###,###,###,###.00", DecimalFormatSymbols(Locale.FRENCH))

fun Float.toAmountString(): String {
    return dec.format(this)
        .replace(oldValue = "-", newValue = "")
        .replace(oldChar = ',', newChar = '.') // TODO Handle currency
}

fun Float.getTextColor() = if (this > 0) Green else if (this < 0) Red else Gray
