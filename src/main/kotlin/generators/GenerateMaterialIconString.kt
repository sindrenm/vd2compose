package generators

import androidx.compose.ui.text.decapitalize
import androidx.compose.ui.text.intl.Locale
import models.VectorDrawable

fun VectorDrawable.toImageVectorString(name: String, packageName: String, receiverName: String): String {
    val packageAndImports = """
        |package $packageName
        |
        |import androidx.compose.material.Icon
        |import androidx.compose.material.icons.materialIcon
        |import androidx.compose.runtime.Composable
        |import androidx.compose.ui.graphics.Color
        |import androidx.compose.ui.graphics.PathFillType
        |import androidx.compose.ui.graphics.SolidColor
        |import androidx.compose.ui.graphics.vector.ImageVector
        |import androidx.compose.ui.graphics.vector.path
        |import androidx.compose.ui.tooling.preview.Preview
        |import $receiverName
    """.trimMargin()

    val paths = paths.joinToString("\n\n") {
        it.toMaterialIconPathString()
    }

    val simpleReceiverName = receiverName.split(".").last()
    val backingPropertyName = "_${name.decapitalize(Locale.current)}"

    val property = """
        |@Suppress("UnusedReceiverParameter") // for convenient access
        |val $simpleReceiverName.$name: ImageVector
        |    get() {
        |        if ($backingPropertyName != null) {
        |            return $backingPropertyName!!
        |        }
        |
        |        $backingPropertyName = materialIcon(name = "$name") {
        |            $paths
        |        }
        |
        |        return $backingPropertyName!!
        |    }
    """.trimMargin()

    val backingProperty = """
        |@Suppress("ObjectPropertyName")
        |private var $backingPropertyName: ImageVector? = null
    """.trimMargin()

    val preview = """
        |@Preview
        |@Composable
        |private fun SatsIcon${name}Preview() {
        |    Icon(SatsIcons.$name, contentDescription = null)
        |}
    """.trimMargin()

    return listOf(
        packageAndImports,
        property,
        backingProperty,
        preview,
    ).joinToString("\n\n")
}
