package models

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.assertThrows

class CommandSpecTest {
    @Test
    fun `from(A) returns the correct spec`() {
        val actual = CommandSpec.from('A')

        val expected = CommandSpec(
            svgPathCommand = 'A',
            materialIconFunctionName = "arcTo",
            numberOfArguments = 7,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(C) returns the correct spec`() {
        val actual = CommandSpec.from('C')

        val expected = CommandSpec(
            svgPathCommand = 'C',
            materialIconFunctionName = "curveTo",
            numberOfArguments = 6,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(H) returns the correct spec`() {
        val actual = CommandSpec.from('H')

        val expected = CommandSpec(
            svgPathCommand = 'H',
            materialIconFunctionName = "horizontalLineTo",
            numberOfArguments = 1,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(L) returns the correct spec`() {
        val actual = CommandSpec.from('L')

        val expected = CommandSpec(
            svgPathCommand = 'L',
            materialIconFunctionName = "lineTo",
            numberOfArguments = 2,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(M) returns the correct spec`() {
        val actual = CommandSpec.from('M')

        val expected = CommandSpec(
            svgPathCommand = 'M',
            materialIconFunctionName = "moveTo",
            numberOfArguments = 2,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(Q) returns the correct spec`() {
        val actual = CommandSpec.from('Q')

        val expected = CommandSpec(
            svgPathCommand = 'Q',
            materialIconFunctionName = "quadTo",
            numberOfArguments = 4,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(S) returns the correct spec`() {
        val actual = CommandSpec.from('S')

        val expected = CommandSpec(
            svgPathCommand = 'S',
            materialIconFunctionName = "reflectiveCurveTo",
            numberOfArguments = 4,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(T) returns the correct spec`() {
        val actual = CommandSpec.from('T')

        val expected = CommandSpec(
            svgPathCommand = 'T',
            materialIconFunctionName = "reflectiveQuadTo",
            numberOfArguments = 2,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(V) returns the correct spec`() {
        val actual = CommandSpec.from('V')

        val expected = CommandSpec(
            svgPathCommand = 'V',
            materialIconFunctionName = "verticalLineTo",
            numberOfArguments = 1,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(Z) returns the correct spec`() {
        val actual = CommandSpec.from('Z')

        val expected = CommandSpec(
            svgPathCommand = 'Z',
            materialIconFunctionName = "close",
            numberOfArguments = 0,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(a) returns the correct spec`() {
        val actual = CommandSpec.from('a')

        val expected = CommandSpec(
            svgPathCommand = 'a',
            materialIconFunctionName = "arcToRelative",
            numberOfArguments = 7,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(c) returns the correct spec`() {
        val actual = CommandSpec.from('c')

        val expected = CommandSpec(
            svgPathCommand = 'c',
            materialIconFunctionName = "curveToRelative",
            numberOfArguments = 6,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(h) returns the correct spec`() {
        val actual = CommandSpec.from('h')

        val expected = CommandSpec(
            svgPathCommand = 'h',
            materialIconFunctionName = "horizontalLineToRelative",
            numberOfArguments = 1,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(l) returns the correct spec`() {
        val actual = CommandSpec.from('l')

        val expected = CommandSpec(
            svgPathCommand = 'l',
            materialIconFunctionName = "lineToRelative",
            numberOfArguments = 2,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(m) returns the correct spec`() {
        val actual = CommandSpec.from('m')

        val expected = CommandSpec(
            svgPathCommand = 'm',
            materialIconFunctionName = "moveToRelative",
            numberOfArguments = 2,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(q) returns the correct spec`() {
        val actual = CommandSpec.from('q')

        val expected = CommandSpec(
            svgPathCommand = 'q',
            materialIconFunctionName = "quadToRelative",
            numberOfArguments = 4,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(s) returns the correct spec`() {
        val actual = CommandSpec.from('s')

        val expected = CommandSpec(
            svgPathCommand = 's',
            materialIconFunctionName = "reflectiveCurveToRelative",
            numberOfArguments = 4,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(t) returns the correct spec`() {
        val actual = CommandSpec.from('t')

        val expected = CommandSpec(
            svgPathCommand = 't',
            materialIconFunctionName = "reflectiveQuadToRelative",
            numberOfArguments = 2,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(v) returns the correct spec`() {
        val actual = CommandSpec.from('v')

        val expected = CommandSpec(
            svgPathCommand = 'v',
            materialIconFunctionName = "verticalLineToRelative",
            numberOfArguments = 1,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from(z) returns the correct spec`() {
        val actual = CommandSpec.from('z')

        val expected = CommandSpec(
            svgPathCommand = 'z',
            materialIconFunctionName = "close",
            numberOfArguments = 0,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `from({unknown-command}) throws an error`() {
        assertThrows<IllegalArgumentException> { CommandSpec.from('P') }
    }
}
