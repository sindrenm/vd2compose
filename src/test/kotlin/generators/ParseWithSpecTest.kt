package generators

import kotlin.test.Test
import models.CommandSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class ParseWithSpecTest {
    @Test
    fun `parseWithSpec() returns the correct string when given a spec with 0 arguments`() {
        val line = "Z"
        val spec = CommandSpec.from(line.first())
        val actual = line.parseWithSpec(spec)

        val expected = "close()"

        assertEquals(expected, actual)
    }

    @Test
    fun `parseWithSpec() returns the correct string when given the exact number of arguments`() {
        val line = "l2,-4.3"
        val spec = CommandSpec.from(line.first())
        val actual = line.parseWithSpec(spec)

        val expected = "lineToRelative(2f, -4.3f)"

        assertEquals(expected, actual)
    }

    @Test
    fun `parseWithSpec() returns the correct string when given twice as many arguments`() {
        val line = "l2,-4.3,3.6,10.15"
        val spec = CommandSpec.from(line.first())
        val actual = line.parseWithSpec(spec)

        val expected = listOf(
            "lineToRelative(2f, -4.3f)",
            "lineToRelative(3.6f, 10.15f)",
        ).joinToString("\n")

        assertEquals(expected, actual)
    }

    @Test
    fun `parseWithSpec() fails when given the wrong number of arguments`() {
        val line = "l2,-4.3,3.6,10.15,5"
        val spec = CommandSpec.from(line.first())

        assertThrows<IllegalArgumentException> { line.parseWithSpec(spec) }
    }
}
