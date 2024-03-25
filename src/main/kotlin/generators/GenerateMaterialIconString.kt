package generators

import models.VectorDrawable

fun VectorDrawable.toImageVectorString(name: String): String {
    val imports = """
        |import androidx.compose.material.Icon
        |import androidx.compose.material.icons.materialIcon
        |import androidx.compose.runtime.Composable
        |import androidx.compose.ui.graphics.Color
        |import androidx.compose.ui.graphics.PathFillType
        |import androidx.compose.ui.graphics.SolidColor
        |import androidx.compose.ui.graphics.vector.ImageVector
        |import androidx.compose.ui.graphics.vector.path
        |import androidx.compose.ui.tooling.preview.Preview
        |import com.sats.dna.icons.SatsIcons
    """.trimMargin()

    val paths = paths.joinToString("\n\n") {
        it.toMaterialIconPathString()
    }

    val property = """
        |@Suppress("UnusedReceiverParameter") // for convenient access
        |val SatsIcons.$name: ImageVector
        |    get() {
        |        if (_$name != null) {
        |            return _$name!!
        |        }
        |
        |        _$name = materialIcon(name = "$name") {
        |            $paths
        |        }
        |
        |        return _$name!!
        |    }
    """.trimMargin()

    val backingProperty = """
        |@Suppress("ObjectPropertyName")
        |private var _$name: ImageVector? = null
    """.trimMargin()

    val preview = """
        |@Preview
        |@Composable
        |private fun SatsIcon${name}Preview() {
        |    Icon(SatsIcons.$name, contentDescription = null)
        |}
    """.trimMargin()

    return listOf(
        imports,
        property,
        backingProperty,
        preview,
    ).joinToString("\n\n")
}
