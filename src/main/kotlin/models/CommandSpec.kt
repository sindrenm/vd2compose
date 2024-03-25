package models

data class CommandSpec(
    val svgPathCommand: Char,
    val materialIconFunctionName: String,
    val numberOfArguments: Int,
) {
    companion object {
        private val specs = listOf(
            CommandSpec('C', "curveTo", numberOfArguments = 6),
            CommandSpec('H', "horizontalLineTo", numberOfArguments = 1),
            CommandSpec('L', "lineTo", numberOfArguments = 2),
            CommandSpec('M', "moveTo", numberOfArguments = 2),
            CommandSpec('Q', "quadTo", numberOfArguments = 4),
            CommandSpec('S', "reflectiveCurveTo", numberOfArguments = 4),
            CommandSpec('T', "reflectiveQuadTo", numberOfArguments = 2),
            CommandSpec('V', "verticalLineTo", numberOfArguments = 1),
            CommandSpec('Z', "close", numberOfArguments = 0),
            CommandSpec('c', "curveToRelative", numberOfArguments = 6),
            CommandSpec('h', "horizontalLineToRelative", numberOfArguments = 1),
            CommandSpec('l', "lineToRelative", numberOfArguments = 2),
            CommandSpec('m', "moveToRelative", numberOfArguments = 2),
            CommandSpec('q', "quadToRelative", numberOfArguments = 4),
            CommandSpec('s', "reflectiveCurveToRelative", numberOfArguments = 4),
            CommandSpec('t', "reflectiveQuadToRelative", numberOfArguments = 2),
            CommandSpec('v', "verticalLineToRelative", numberOfArguments = 1),
            CommandSpec('z', "close", numberOfArguments = 0),
        )

        fun from(svgPathCommand: Char): CommandSpec {
            return requireNotNull(specs.find { it.svgPathCommand == svgPathCommand }) {
                "Unknown path command: $svgPathCommand"
            }
        }
    }
}
