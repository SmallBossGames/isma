package ru.isma.next.editor.blueprint.models

data class CodeRegion(
    val name: String,
    val startLine: Int,
    val endLine: Int,
)

data class LismaTextModel(
    val fullText: String,
    val regions: List<CodeRegion> = emptyList(),
) {
    fun fragmentNameByIndex(index: Int) = regions
        .firstOrNull { index > it.startLine && index <= it.endLine } ?: DefaultFragment

    companion object {
        val DefaultFragment = CodeRegion(
            name = "Main",
            startLine = 0,
            endLine = 0,
        )
    }
}
