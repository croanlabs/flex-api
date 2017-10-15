package ie.reflexivity.flexer.flexapi.extensions

const val DATBASE_DEFAULT_STRING_SIZE = 255

fun String.shortenToDBMaxAllowedSize(): String {
    if (length < DATBASE_DEFAULT_STRING_SIZE) return this
    else return substring(0, DATBASE_DEFAULT_STRING_SIZE)
}
