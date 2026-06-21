package ru.isma.next.app.services.project

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel
import ru.isma.next.app.services.project.ProjectService

class ProjectServiceTest {

    private lateinit var service: ProjectService

    @BeforeEach
    fun setUp() {
        service = ProjectService()
    }

    @AfterEach
    fun tearDown() {
        service.closeAll()
    }

    @Test
    fun `createNew creates LismaProjectModel and adds to list`() {
        service.createNew("TestLismaProject")

        val projects = service.getAllProjects()
        assert(projects.size == 1) { "Expected 1 project, got ${projects.size}" }
        assert(projects[0] is LismaProjectModel) { "Expected LismaProjectModel, got ${projects[0]?.javaClass}" }
        assert(projects[0]?.name == "TestLismaProject") { "Expected name 'TestLismaProject', got ${projects[0]?.name}" }
    }

    @Test
    fun `createNewBlueprint creates BlueprintProjectModel and adds to list`() {
        service.createNewBlueprint("TestBlueprint")

        val projects = service.getAllProjects()
        assert(projects.size == 1) { "Expected 1 project, got ${projects.size}" }
        assert(projects[0] is BlueprintProjectModel) { "Expected BlueprintProjectModel, got ${projects[0]?.javaClass}" }
        assert(projects[0]?.name == "TestBlueprint") { "Expected name 'TestBlueprint', got ${projects[0]?.name}" }
    }

    @Test
    fun `addText adds LismaProjectModel to list`() {
        val project = LismaProjectModel()
        project.name = "AddedTextProject"
        service.addText(project)

        val projects = service.getAllProjects()
        assert(projects.size == 1) { "Expected 1 project, got ${projects.size}" }
        assert(projects[0] == project) { "Expected added project in list" }
    }

    @Test
    fun `addBlueprint adds BlueprintProjectModel to list`() {
        val project = BlueprintProjectModel()
        project.name = "AddedBlueprintProject"
        service.addBlueprint(project)

        val projects = service.getAllProjects()
        assert(projects.size == 1) { "Expected 1 project, got ${projects.size}" }
        assert(projects[0] == project) { "Expected added project in list" }
    }

    @Test
    fun `close removes project from list and disposes it`() {
        val project = LismaProjectModel()
        project.name = "CloseTestProject"
        service.addText(project)

        assert(service.getAllProjects().size == 1) { "Expected 1 project before close" }

        service.close(project)

        assert(service.getAllProjects().size == 0) { "Expected 0 projects after close, got ${service.getAllProjects().size}" }
    }

    @Test
    fun `closeAll removes all projects and disposes them`() {
        val project1 = LismaProjectModel()
        project1.name = "CloseAllProject1"
        service.addText(project1)

        val project2 = LismaProjectModel()
        project2.name = "CloseAllProject2"
        service.addText(project2)

        val project3 = BlueprintProjectModel()
        project3.name = "CloseAllProject3"
        service.addBlueprint(project3)

        assert(service.getAllProjects().size == 3) { "Expected 3 projects before closeAll" }

        service.closeAll()

        assert(service.getAllProjects().size == 0) { "Expected 0 projects after closeAll, got ${service.getAllProjects().size}" }
    }

    @Test
    fun `getAllProjects returns all projects as array`() {
        val project1 = LismaProjectModel()
        project1.name = "GetAllProject1"
        service.addText(project1)

        val project2 = LismaProjectModel()
        project2.name = "GetAllProject2"
        service.addText(project2)

        val projects = service.getAllProjects()

        assert(projects.size == 2) { "Expected 2 projects, got ${projects.size}" }
        assert(projects.contains(project1)) { "Expected project1 in array" }
        assert(projects.contains(project2)) { "Expected project2 in array" }
    }

    @Test
    fun `activeProject can be set and read`() {
        val project = LismaProjectModel()
        project.name = "ActiveProject"
        service.activeProject = project

        assert(service.activeProject == project) { "Expected activeProject to be set" }
        assert(service.activeProject?.name == "ActiveProject") { "Expected name 'ActiveProject', got ${service.activeProject?.name}" }
    }
}
