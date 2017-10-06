package ie.reflexivity.flexer.flexapi.extensions

import ie.reflexivity.flexer.flexapi.client.github.GIT_HUB_DATE_TIME_FORMAT
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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

fun LocalDateTime.toGitHubFormat(): String {
    val formatter = DateTimeFormatter.ofPattern(GIT_HUB_DATE_TIME_FORMAT)
    return this.format(formatter)
}

fun Long.toDateTime() = LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())

