package ie.reflexivity.flexer.flexapi.extensions

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Date.toLocalDate() = toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

fun Date.toLocalDateTime() = toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

fun LocalDateTime.toDate():Date{
    val zone = atZone(ZoneId.systemDefault())
    return Date.from(zone.toInstant())
}
