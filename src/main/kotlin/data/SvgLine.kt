package data

enum class SvgLineType {
    HEADER, RECT, PATH, SVG_FINISH
}

/**
 * @param lineType an SVG tag
 * @param data a string within SVG tag, include this tag itself
 */
data class SvgLine(
    val lineType: SvgLineType,
    val data: String
)