package ru.isma.next.app.services.project

import org.slf4j.LoggerFactory
import ru.isma.next.app.constants.OLD_ISMA_PROJECT_FILE
import ru.isma.next.app.constants.STATE_CHART_ISMA_PROJECT_FILE
import ru.isma.next.app.constants.TEXT_ISMA_PROJECT_FILE
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel
import ru.isma.next.app.models.projects.ProjectContent
import ru.isma.next.app.services.blueprint.BlueprintModelSerializer
import java.io.File

class ProjectFileService(
    private val projectService: IProjectService,
    private val editorPort: ProjectEditorPort,
) {
    private val logger = LoggerFactory.getLogger(ProjectFileService::class.java)

    fun listAllFilesPaths(): List<String> {
        return projectService.projects.mapNotNull { it.file?.path }
    }

    fun open(file: File) {
        when {
            OLD_ISMA_PROJECT_FILE.contains(file.extension) -> {
                throw UnsupportedOperationException("Legacy .im files are no longer supported. Please convert to .isma format first.")
            }
            TEXT_ISMA_PROJECT_FILE.contains(file.extension) -> {
                LismaProjectModel().apply {
                    this.name = file.name
                    this.file = file
                    this.lismaText = file.readText()
                    projectService.addText(this)
                }
            }
            STATE_CHART_ISMA_PROJECT_FILE.contains(file.extension) -> {
                BlueprintProjectModel().apply {
                    this.name = file.name
                    this.file = file
                    this.blueprint = BlueprintModelSerializer.fromJson(file.readText())
                    projectService.addBlueprint(this)
                }
            }
            else -> {
                throw IllegalArgumentException("Unsupported file extension: ${file.extension}")
            }
        }
    }

    fun open(vararg paths: String) {
        paths.forEach { path ->
            val file = File(path)

            if (!file.exists()) {
                return@forEach
            }

            try {
                open(file)
            } catch (e: Exception) {
                logger.error("Failed to open project file: $path", e)
            }
        }
    }

    fun save(project: IProjectModel) {
        val file = project.file ?: return
        file.writeText(fileContentOf(project))
    }

    fun saveAs(project: IProjectModel, file: File) {
        project.apply {
            this.name = file.name
            this.file = file
        }
        file.writeText(fileContentOf(project))
    }

    private fun fileContentOf(project: IProjectModel): String = when (val content = editorPort.content(project)) {
        is ProjectContent.Text -> content.fullText
        is ProjectContent.Blueprint -> BlueprintModelSerializer.toJson(content.model)
    }
}
