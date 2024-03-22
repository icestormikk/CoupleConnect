package com.icestormikk.coupleconnect.database.entities.moments

import com.icestormikk.coupleconnect.R

enum class MomentType(val iconId: Int, val labelId: Int) {
    ROMANTIC_DATE(
        R.drawable.romantic_date,
        R.string.moment_type_romantic_date
    ),
    CINEMA(R.drawable.cinema, R.string.moment_type_cinema),
    THEATER(R.drawable.theater, R.string.moment_type_theater),
    TRAVEL(R.drawable.travel, R.string.moment_type_travel),
    SURPRISE(R.drawable.surprise_gift, R.string.moment_type_surprise_gift),
    FLOWERS( R.drawable.flowers, R.string.moment_type_flowers),
    ENGAGEMENT( R.drawable.engagement, R.string.moment_type_engagement),
    WEDDING(R.drawable.wedding, R.string.moment_type_wedding),
    BIRTHDAY(R.drawable.birthday, R.string.moment_type_birthday),
    ANNIVERSARY(R.drawable.gift, R.string.moment_type_anniversary),
    CONCERT(R.drawable.concert, R.string.moment_type_concert)
}