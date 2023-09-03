package data

import java.util.regex.Pattern

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
) {

    fun toPath(): SvgPathParams {
        if (this.lineType != SvgLineType.PATH)
            throw Exception("SVG line type is ${this.lineType.name} but ${SvgLineType.PATH.name} expected")

        val pathPattern = Pattern.compile("""<path d="([^"]+)" fill="([^"]+)"/>""")
        val matcher = pathPattern.matcher(data)

        return if (matcher.find()) {
            SvgPathParams(
                path = matcher.group(1),
                fill = matcher.group(2),
            )
        } else {
            throw Exception("Matcher could not fine a subsequence for \"path\" parameter")
        }
    }

    fun toSvgPictureParams(): SvgPictureParams {
        if (this.lineType != SvgLineType.HEADER)
            throw Exception("SVG line type is ${this.lineType.name} but ${SvgLineType.HEADER.name} expected")

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
}