package ru.nstu.isma.app.env.project;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.nstu.isma.app.ui.windows.mainwindow.workbench.WorkbenchController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by Bessonov Alex on 17.07.2016.
 */

@Service
public class ProjectManager {

    private WorkbenchController workbenchController;

    private Set<IsmaProject> projects = new HashSet<>();

    private List<Consumer<IsmaProject>> onCreateProjectListeners = new ArrayList<>();

    private List<Consumer<IsmaProject>> onCloseProjectListeners = new ArrayList<>();


    public boolean createNew(IsmaProject project, File file) throws JAXBException {

        saveProject(project, file);

        projects.add(project);

        onCreateProjectListeners.forEach(l -> l.accept(project));

        return true;
    }

    public boolean closeProject(IsmaProject project) throws JAXBException {

        projects.remove(project);

        onCloseProjectListeners.forEach(l -> l.accept(project));

        return true;
    }


    public boolean saveProject(IsmaProject project) throws JAXBException {
        Assert.notNull(project.getFilePath());

        File f = new File(project.getFilePath());

        Assert.isTrue(f.exists());

        return saveProject(project, f);
    }

    public boolean saveProject(IsmaProject project, File file) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(IsmaProject.class);

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(project, file);

        project.setFilePath(file.getAbsolutePath());

        project.setChanged(false);

        return true;
    }

    public IsmaProject currentProject() {
        return workbenchController.currentProject();
    }

    public void saveAll() throws JAXBException {
        for (IsmaProject p : projects)
            saveProject(p);
    }

    public IsmaProject loadProject(File file) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(IsmaProject.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        IsmaProject project = (IsmaProject) jaxbUnmarshaller.unmarshal(file);

        project.setChanged(false);

        project.setFilePath(file.getAbsolutePath());

        projects.add(project);

        onCreateProjectListeners.forEach(l -> l.accept(project));

        return project;
    }

    public void addOnCreateProjectListener(Consumer<IsmaProject> ismaProjectConsumer) {
        onCreateProjectListeners.add(ismaProjectConsumer);
    }

    public void addOnCloseProjectListener(Consumer<IsmaProject> ismaProjectConsumer) {
        onCloseProjectListeners.add(ismaProjectConsumer);
    }

    public WorkbenchController getWorkbenchController() {
        return workbenchController;
    }

    public void setWorkbenchController(WorkbenchController workbenchController) {
        this.workbenchController = workbenchController;
    }
}
