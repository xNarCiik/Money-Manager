package com.dms.moneymanager.data.database.converter

import androidx.room.TypeConverter
import com.dms.moneymanager.domain.model.main.RecurrenceType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.Date

class TypeConverter {

    private val mapper = jacksonObjectMapper()

    @TypeConverter
    fun toRecurrenceType(json: String?) =
        if (json != null) mapper.readValue<RecurrenceType>(content = json) else null

    @TypeConverter
    fun fromRecurrenceType(recurrenceType: RecurrenceType?): String? =
        mapper.writeValueAsString(recurrenceType)

    @TypeConverter
    fun toDate(value: Long?) = if (value != null) Date(value) else null

    @TypeConverter
    fun fromDate(date: Date?) = date?.time
}