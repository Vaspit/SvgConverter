package data

/**
 * @param x x coordinate
 * @param y y coordinate
 * @param type type of coordinate in SVG picture coordinates, such as M, C, H, V and so on
 */
data class Coordinate(
    val type: String = "",
    val x: Double,
    val y: Double,
    val isEnd: Boolean
)
