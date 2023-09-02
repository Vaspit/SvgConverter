import data.SvgLine
import data.SvgLineType
import utils.toSvgPictureParams
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter

object Converter {

    fun processFile(inputFilePath: String, outputFilePath: String) {
        TODO("Is not implemented yet")
    }

    fun readFile() {
        println("Enter input file name with an extension:")
        val inputFilePath = readln()
        val currentDirectoryPath = System.getProperty("user.dir")

        val currentDirectory = File("$currentDirectoryPath\\input_file")
        val filePath = inputFilePath.ifEmpty { currentDirectory.listFiles()?.get(0)?.path }

        if (filePath == null) {
            println("Error: file path is null or file doesn't exist!")
            return
        }

        val inputFile = File(filePath)
        val reader = BufferedReader(inputFile.reader())

        //read file line by line
        var lineData: String?
        val lineList = mutableListOf<SvgLine>()

        do {
            lineData = reader.readLine()
            if (lineData != null) {
                when {
                    lineData.startsWith("<svg") -> {
                        lineList.add(SvgLine(lineType = SvgLineType.HEADER, data = lineData))
                    }
                    lineData.startsWith("<rect") -> {
                        lineList.add(SvgLine(lineType = SvgLineType.RECT, data = lineData))
                    }
                    lineData.startsWith("<path") -> {
                        lineList.add(SvgLine(lineType = SvgLineType.PATH, data = lineData))
                    }
                    lineData.startsWith("</svg>") -> {}
                }
            }
        } while (lineData != null)

        reader.close()

        val svgHeader = lineList.find { it.lineType == SvgLineType.HEADER }
        val svgPictureParams = svgHeader?.toSvgPictureParams()

        println(svgPictureParams)
    }

    fun createEmptyVectorXml(height: Int, width: Int, viewPortHeight: Int, viewPortWidth: Int) {
        println("Enter output file path:")
        val outputFilePath = readln()

        try {
            val inputData = getEmptyVector(
                height = height,
                width = width,
                viewPortHeight = viewPortHeight,
                viewPortWidth = viewPortWidth
            )
            val outPutFile = File(outputFilePath)
            val writer = FileWriter(outPutFile)

            writer.write(inputData)
            writer.close()

            println("File processing is finished. Result saved at $outputFilePath")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun getEmptyVector(height: Int, width: Int, viewPortHeight: Int, viewPortWidth: Int): String {
        val emptyVector = StringBuilder()

        emptyVector
            .append("<vector xmlns:android=\"http://schemas.android.com/apk/res/android\"\n")
            .append("\tandroid:width=\"${width}dp\"\n")
            .append("\tandroid:height=\"${height}dp\"\n")
            .append("\tandroid:viewportWidth=\"${viewPortWidth}\"\n")
            .append("\tandroid:viewPortHeight=\"${viewPortHeight}\">\n")
            .append("</vector>")

        return emptyVector.toString()
    }
}