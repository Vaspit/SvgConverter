package utils

import data.SvgLine
import data.SvgLineType
import data.SvgPictureParams
import data.SvgPictureParamsTypes

fun SvgLine.toSvgPictureParams(): SvgPictureParams {
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