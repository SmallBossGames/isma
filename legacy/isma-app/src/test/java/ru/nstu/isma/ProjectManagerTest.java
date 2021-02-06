package ru.nstu.isma;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.nstu.isma.app.env.project.ProjectManager;
import ru.nstu.isma.app.env.project.IsmaProject;
import ru.nstu.isma.app.env.project.IsmaProjectType;
import ru.nstu.isma.app.env.project.RunConfiguration;

import javax.xml.bind.JAXBException;
import java.io.File;

/**
 * Unit test for simple IsmaApplication.
 */
public class ProjectManagerTest extends TestCase {

    public ProjectManagerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(ProjectManagerTest.class);
    }


    public void testSaveProject() throws JAXBException {
        IsmaProject project = new IsmaProject();
        project.setName("Ozon problem");
        project.setDescription("Test model for lisma_pde in ozon problem");
        project.setType(IsmaProjectType.LISMA_EPS);

        RunConfiguration configuration = new RunConfiguration();
        configuration.setName("testConf1");
        configuration.setStartTime(0.);
        configuration.setEndTime(10.);
        configuration.setStep(0.1);

        RunConfiguration configuration2 = new RunConfiguration();
        configuration2.setName("testConf2");
        configuration2.setStartTime(0.);
        configuration2.setEndTime(10.);
        configuration2.setStep(0.1);

        project.getConfigurations().add(configuration);
        project.getConfigurations().add(configuration2);

        ProjectManager manager = new ProjectManager();
        manager.saveProject(project, new File("ozon.xml"));
    }

    public void testLoadProject() throws JAXBException {
        ProjectManager manager = new ProjectManager();
        IsmaProject project = manager.loadProject(new File("ozon.xml"));
        System.out.println(project.getModelData());
    }
}
