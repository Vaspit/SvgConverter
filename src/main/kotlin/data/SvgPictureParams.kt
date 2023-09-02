package data

enum class SvgPictureParamsTypes(val paramName: String) {
    WIDTH("width"), HEIGHT("height"), VIEW_BOX("viewBox"), FILL("fill"), XMLNS("xmlns")
}

data class SvgPictureParams(
    val width: String,
    val height: String,
    val viewBox: String,
    val fill: String,
    val xmlns: String
)