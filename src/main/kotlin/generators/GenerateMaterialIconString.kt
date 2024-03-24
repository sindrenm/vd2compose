package generators

import models.VectorDrawable

fun VectorDrawable.toImageVectorString(name: String): String {
    return """
        materialIcon(name = "$name") {
            ${paths.joinToString("\n\n") { it.toMaterialIconPathString() }}
        }
    """.trimIndent()
}
