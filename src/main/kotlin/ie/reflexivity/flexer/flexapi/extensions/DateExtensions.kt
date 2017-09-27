package ie.reflexivity.flexer.flexapi.extensions

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Date.toLocalDate() = toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

fun Date.toLocalDateTime() = toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

fun LocalDateTime.toDate(): Date {
    val zone = atZone(ZoneId.systemDefault())
    return Date.from(zone.toInstant())
}

fun LocalDate.isOlderThanADay() = isBefore((LocalDate.now()))

fun LocalDateTime.isOlderThanADay(): Boolean {
    val duration = Duration.between(this, LocalDateTime.now())
    if (duration.toDays() >= 1) return true
    return false
}
