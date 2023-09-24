package data

data class SvgPathParams(
    val path: String = "",
    val commands: List<Command>,
    val fill: String = ""
)