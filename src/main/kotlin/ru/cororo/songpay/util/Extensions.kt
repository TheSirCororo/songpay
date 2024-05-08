package ru.cororo.songpay.util

import java.time.Instant
import java.util.Date
import kotlin.time.Duration

operator fun Date.compareTo(instant: Instant) = time.compareTo(instant.toEpochMilli())

operator fun Instant.plus(duration: Duration): Instant = plusMillis(duration.inWholeMilliseconds)
