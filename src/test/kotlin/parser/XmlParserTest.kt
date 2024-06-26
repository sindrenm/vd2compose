package parser

import kotlin.test.Test
import kotlin.test.assertEquals
import models.VectorDrawable
import models.VectorDrawablePath
import org.intellij.lang.annotations.Language

class XmlParserTest {
    @Test
    fun `parse(String) returns the correct VectorDrawable`() {
        @Language("xml")
        val xmlString = """
            <?xml version="1.0" encoding="utf-8"?>
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                android:width="24dp"
                android:height="24dp"
                android:viewportWidth="24"
                android:viewportHeight="24"
                android:tint="#FF000000">
                <path
                    android:fillColor="#FF14233C"
                    android:fillType="evenOdd"
                    android:strokeColor="#FF123456"
                    android:strokeWidth="1.5"
                    android:pathData="M8.07 13.22l2-4.3 3.6 10.15c0.1 0.28 0.42 0.44 0.8 0.43 0.38 0 0.72-0.18 0.82-0.48l1.87-5.15h3.53c0.21-0.3 0.3-0.51 0.31-0.72 0-0.21-0.08-0.45-0.31-0.78H16.7c-0.3 0-0.5 0.06-0.65 0.16-0.14 0.1-0.26 0.25-0.35 0.48l-1.24 2.94-3.6-9.51C10.75 6.14 10.44 5.99 10.1 6c-0.35 0-0.7 0.18-0.85 0.5l-2.77 5.87H3.33C3.08 12.68 2.99 12.91 3 13.11c0 0.21 0.1 0.44 0.33 0.76h3.71c0.3 0 0.5-0.06 0.64-0.16 0.15-0.1 0.28-0.26 0.39-0.49Z"/>
            </vector>
        """.trimIndent()

        val expected = VectorDrawable(
            paths = listOf(
                VectorDrawablePath(
                    fillColor = "#FF14233C",
                    fillType = "evenOdd",
                    pathData = "M8.07,13.22l2,-4.3,3.6,10.15c0.1,0.28,0.42,0.44,0.8,0.43,0.38,0,0.72,-0.18,0.82,-0.48l1.87,-5.15h3.53c0.21,-0.3,0.3,-0.51,0.31,-0.72,0,-0.21,-0.08,-0.45,-0.31,-0.78H16.7c-0.3,0,-0.5,0.06,-0.65,0.16,-0.14,0.1,-0.26,0.25,-0.35,0.48l-1.24,2.94,-3.6,-9.51C10.75,6.14,10.44,5.99,10.1,6c-0.35,0,-0.7,0.18,-0.85,0.5l-2.77,5.87H3.33C3.08,12.68,2.99,12.91,3,13.11c0,0.21,0.1,0.44,0.33,0.76h3.71c0.3,0,0.5,-0.06,0.64,-0.16,0.15,-0.1,0.28,-0.26,0.39,-0.49Z",
                    strokeColor = "#FF123456",
                    strokeWidth = "1.5",
                ),
            ),
        )

        val actual = XmlParser.parse(xmlString)

        assertEquals(expected, actual)
    }
}
