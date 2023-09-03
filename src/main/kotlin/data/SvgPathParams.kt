package data

data class SvgPathParams(
    val path: String = "",
    val coordinates: List<Pair<Double, Double>>,
    val fill: String = ""
)