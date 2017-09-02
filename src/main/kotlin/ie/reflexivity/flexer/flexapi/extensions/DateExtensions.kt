package ie.reflexivity.flexer.flexapi.extensions

import java.time.ZoneId
import java.util.*


fun Date.toLocalDate() = toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

fun Date.toLocalDateTime() = toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
