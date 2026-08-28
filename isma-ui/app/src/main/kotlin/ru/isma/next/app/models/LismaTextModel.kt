package ru.isma.next.app.models

data class CodeRegion(
    val name: String,
    val startLine: Int,
    val endLine: Int,
)

data class LismaTextModel(
    val fullText: String,
    val regions: List<CodeRegion> = emptyList(),
) {
    fun fragmentNameByLine(line: Int): String = regions
        .firstOrNull { line >= it.startLine && line <= it.endLine }
        ?.name
        ?: DefaultFragment.name

    companion object {
        val DefaultFragment = CodeRegion(
            name = "Main",
            startLine = 0,
            endLine = 0,
        )
    }
}
