package com.dms.moneymanager.data.database.converter

import androidx.room.TypeConverter
import com.dms.moneymanager.domain.model.main.RecurrenceType
import java.util.Date

class TypeConverter {
    @TypeConverter
    fun toRecurrenceType(value: String?) = if(value != null) RecurrenceType.valueOf(value) else null

    @TypeConverter
    fun fromRecurrenceType(recurrenceType: RecurrenceType?) = recurrenceType?.name

    @TypeConverter
    fun toDate(value: Long?) = if(value != null) Date(value) else null

    @TypeConverter
    fun fromDate(date: Date?) = date?.time
}