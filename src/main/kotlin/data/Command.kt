package data

/**
 * @param type type of coordinate in SVG picture coordinates, such as M, C, H, V and so on
 */
data class Command(
    val type: String = "",
    val command: String,
    val isEnd: Boolean
)
