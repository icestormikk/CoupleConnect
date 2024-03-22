package com.icestormikk.coupleconnect.database.entities

import com.icestormikk.coupleconnect.R

enum class StatisticsType(val labelId: Int) {
    WEEK(R.string.statistics_type_last_week),
    MONTH(R.string.statistics_type_last_month),
    QUARTER(R.string.statistics_type_last_quarter),
    YEAR(R.string.statistics_type_last_year),
    CUSTOM(R.string.statistics_type_custom)
}

data class Statistics(
    val propertyName: String,
    val oldValue: Long,
    val newValue: Long
)
