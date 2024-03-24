package models

data class CommandSpec(
    val svgPathCommand: Char,
    val materialIconFunctionName: String,
    val numberOfArguments: Int,
) {
    companion object {
        private val specs = listOf(
            CommandSpec(
                svgPathCommand = 'Z',
                materialIconFunctionName = "close",
                numberOfArguments = 0,
            ),
            CommandSpec(
                svgPathCommand = 'C',
                materialIconFunctionName = "curveTo",
                numberOfArguments = 6,
            ),
            CommandSpec(
                svgPathCommand = 'c',
                materialIconFunctionName = "curveToRelative",
                numberOfArguments = 6,
            ),
            CommandSpec(
                svgPathCommand = 'H',
                materialIconFunctionName = "horizontalLineTo",
                numberOfArguments = 1,
            ),
            CommandSpec(
                svgPathCommand = 'h',
                materialIconFunctionName = "horizontalLineToRelative",
                numberOfArguments = 1,
            ),
            CommandSpec(
                svgPathCommand = 'L',
                materialIconFunctionName = "lineTo",
                numberOfArguments = 2,
            ),
            CommandSpec(
                svgPathCommand = 'l',
                materialIconFunctionName = "lineToRelative",
                numberOfArguments = 2,
            ),
            CommandSpec(
                svgPathCommand = 'M',
                materialIconFunctionName = "moveTo",
                numberOfArguments = 2,
            ),
            CommandSpec(
                svgPathCommand = 'v',
                materialIconFunctionName = "verticalLineToRelative",
                numberOfArguments = 1,
            ),
            CommandSpec(
                svgPathCommand = 'z',
                materialIconFunctionName = "close",
                numberOfArguments = 0,
            ),
        )

        fun from(svgPathCommand: Char): CommandSpec {
            return requireNotNull(specs.find { it.svgPathCommand == svgPathCommand }) {
                "Unknown path command: $svgPathCommand"
            }
        }
    }
}
