package data

import java.util.regex.Pattern

// TODO: find out how to add appropriate label (M,H,V,Z...) for vector coordinates

enum class SvgLineType {
    SVG_START, RECT, PATH, SVG_END
}

/**
 * @param lineType an SVG tag
 * @param data a string within SVG tag, include this tag itself
 */
data class SvgLine(
    val lineType: SvgLineType,
    val data: String
) {

    /**
     * @return [SvgPathParams], that contains vectors coordinates and their color
     */
    fun toPath(): SvgPathParams {
        if (this.lineType != SvgLineType.PATH)
            throw Exception("SVG line type is ${this.lineType.name} but ${SvgLineType.PATH.name} expected")

        val pathPattern = Pattern.compile("""<path d="([^"]+)" fill="([^"]+)"/>""")
        val matcher = pathPattern.matcher(data)

        return if (matcher.find()) {
            val path = matcher.group(1)
            val coordinates = getCoordinatesFromPath(path)

            SvgPathParams(
                path = path,
                coordinates = coordinates,
                fill = matcher.group(2),
            )
        } else {
            throw Exception("Matcher could not fine a subsequence for \"path\" parameter")
        }
    }

    /**
     * This method return general picture parameters, such as width, height, background color and so on
     */
    fun toSvgPictureParams(): SvgPictureParams {
        if (this.lineType != SvgLineType.SVG_START)
            throw Exception("SVG line type is ${this.lineType.name} but ${SvgLineType.SVG_START.name} expected")

        val pattern = """([a-zA-Z-]+)="([^"]+)"""".toRegex()
        val matches = pattern.findAll(this.data)

        val parametersMap = mutableMapOf<String, String>()

        for (match in matches) {
            val (key, value) = match.destructured
            parametersMap[key] = value
        }

        return SvgPictureParams(
            width = parametersMap[SvgPictureParamsTypes.WIDTH.paramName] ?: "",
            height = parametersMap[SvgPictureParamsTypes.HEIGHT.paramName] ?: "",
            viewBox = parametersMap[SvgPictureParamsTypes.VIEW_BOX.paramName] ?: "",
            fill = parametersMap[SvgPictureParamsTypes.FILL.paramName] ?: "",
            xmlns = parametersMap[SvgPictureParamsTypes.XMLNS.paramName] ?: ""
        )
    }

    private fun getCoordinatesFromPath(path: String): MutableList<Coordinate> {
        // TODO: coordinates are incorrect
        val regex = """([MLHCV]?)([-+]?\d+\.\d+)\s+([-+]?\d+\.\d+)(Z)?""".toRegex()
        val matches = regex.findAll(path)

        val coordinates = mutableListOf<Coordinate>()

        for (match in matches) {
            val type = match.groupValues[1].ifEmpty { "" }
            val x = match.groupValues[2].toDouble()
            val y = match.groupValues[3].toDouble()
            val isEnd = match.groupValues[4] == "Z"

            coordinates.add(Coordinate(type, x, y, isEnd))
        }

        return coordinates
    }
}