package generators

import models.CommandSpec
import models.VectorDrawablePath

fun VectorDrawablePath.toMaterialIconPathString(): String {
    val lines = pathData
        .replace(Regex("([a-zA-Z])")) { matchResult -> "\n${matchResult.groupValues[1]}" }
        .lines()
        .filter { it.isNotBlank() }

    val functionCallStrings: List<String> = lines.map { line ->
        val spec = CommandSpec.from(line.first())

        line.parseWithSpec(spec)
    }

    val fillColorArg = fillColor?.let { "fill = SolidColor(Color.Black)," }
    val fillTypeArg = fillType?.let {
        val pathFillType = when (it) {
            "evenOdd" -> "PathFillType.EvenOdd"
            "nonZero" -> "PathFillType.NonZero"
            else -> return@let null
        }
        """pathFillType = $pathFillType,"""
    }

    val strokeColorArg = strokeColor?.let { "stroke = SolidColor(Color.Black)," }
    val strokeWidthArg = strokeWidth?.let { "strokeLineWidth = ${it}f" }

    val args = listOfNotNull(
        fillColorArg,
        fillTypeArg,
        strokeColorArg,
        strokeWidthArg,
    ).joinToString("\n")

    val pathFunctionCall = if (args.isEmpty()) {
        "path"
    } else {
        """path(
               $args
           )
        """.trimIndent()
    }

    return """
        $pathFunctionCall {
            ${functionCallStrings.joinToString("\n")}
        }
    """.trimIndent()
}

fun String.parseWithSpec(spec: CommandSpec): String {
    val numberOfArguments = spec.numberOfArguments
    val materialIconFunctionName = spec.materialIconFunctionName
    val svgPathCommand = spec.svgPathCommand.toString()

    if (numberOfArguments == 0) return "$materialIconFunctionName()"

    // Arcs are special cases, deal with them elsewhere
    if (svgPathCommand.lowercase() == "a") return parseArcCommand(spec)

    val allArguments = this
        .removePrefix(svgPathCommand)
        .split(",")
        .map { "${it}f" }

    require(allArguments.count() % numberOfArguments == 0) {
        "Illegal number of arguments for command $svgPathCommand. " +
            "Expected $numberOfArguments, got ${allArguments.count()}"
    }

    val chunks = allArguments.chunked(numberOfArguments)
    return chunks.joinToString("\n") { chunk ->
        "$materialIconFunctionName(${chunk.joinToString(", ")})"
    }
}

fun String.parseArcCommand(spec: CommandSpec): String {
    val svgPathCommand = spec.svgPathCommand.toString()
    val numberOfArguments = spec.numberOfArguments
    val materialIconFunctionName = spec.materialIconFunctionName

    val allArguments = this
        .removePrefix(svgPathCommand)
        .split(",")
        .map { "${it}f" }

    require(allArguments.count() % numberOfArguments == 0) {
        "Illegal number of arguments for command $svgPathCommand. " +
            "Expected $numberOfArguments, got ${allArguments.count()}"
    }

    val chunks = allArguments.chunked(numberOfArguments)
    return chunks.joinToString("\n") { chunk ->
        val rx: String = chunk[0]
        val ry: String = chunk[1]
        val xAxisRotation: String = chunk[2]
        val largeArcFlag: Boolean = chunk[3] == "1f"
        val sweepFlag: Boolean = chunk[4] == "1f"
        val x: String = chunk[5]
        val y: String = chunk[6]

        val args = listOf(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y).joinToString(", ")

        "$materialIconFunctionName($args)"
    }
}
