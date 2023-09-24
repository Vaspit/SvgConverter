package data

import utils.SvgLineType
import utils.SvgPictureParamsTypes
import utils.getCommandType
import java.util.regex.Pattern

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
            val coordinates = getCommandsFromPath(path)

            SvgPathParams(
                path = path,
                commands = coordinates,
                fill = matcher.group(2),
            )
        } else {
            throw Exception("Matcher could not fine a subsequence for \"path\" parameter")
        }
    }

    /**
     * @return general picture parameters, such as width, height, background color and so on
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

    private fun getCommandsFromPath(path: String): MutableList<Command> {
        val regex = Regex("\\s+")
        val resultList = path.split(regex)
        val commands: MutableList<Command> = mutableListOf()

        resultList.forEach { command ->
            val type = command.getCommandType()

            commands.add(
                Command(
                    type = type,
                    command = command
                )
            )
        }

        return commands
    }
}