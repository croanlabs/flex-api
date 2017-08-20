package ie.reflexivity.flexer.flexapi.web.exceptions


open class FlexException(
        message: String
) : RuntimeException(message)

class NotFoundException(
        message: String
) : FlexException(message)
