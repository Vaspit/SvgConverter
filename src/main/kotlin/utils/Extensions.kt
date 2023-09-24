package utils

enum class SvgCommandType {
    COMMAND, COORDINATE
}

enum class SvgLineType {
    SVG_START, RECT, PATH, SVG_END
}

enum class SvgPictureParamsTypes(val paramName: String) {
    WIDTH("width"),
    HEIGHT("height"),
    VIEW_BOX("viewBox"),
    FILL("fill"),
    XMLNS("xmlns")
}

/**
 * @return the result of checking whether this command line is just part of a coordinate or a command
 */
fun String.getCommandType(): SvgCommandType {
    val command = this
    val charPattern = Regex("[a-z]|[A-Z]")

    return if (command.contains(charPattern)) {
        SvgCommandType.COMMAND
    } else {
        SvgCommandType.COORDINATE
    }
}