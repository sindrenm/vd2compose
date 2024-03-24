package models

data class VectorDrawable(
    val paths: List<VectorDrawablePath>,
)

data class VectorDrawablePath(
    val pathData: String,
    val fillColor: String?,
    val fillType: String?,
    val strokeColor: String?,
    val strokeWidth: String?,
)
