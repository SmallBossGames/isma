package ru.nstu.isma.app.ui.windows.mainwindow.workbench;

import com.alee.extended.tab.DocumentData;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.utils.TextUtils;
import com.swing.GraphEditor;
import org.springframework.util.Assert;
import ru.nstu.isma.app.env.IsmaEnvironment;
import ru.nstu.isma.app.env.project.ProjectManager;
import ru.nstu.isma.app.env.project.IsmaProject;
import ru.nstu.isma.app.ui.editors.IsmaEditor;
import ru.nstu.isma.app.ui.editors.lismapde.LismaPdeEditor;
import ru.nstu.isma.app.ui.windows.projectmanager.NewProjectWindow;
import ru.nstu.isma.app.ui.windows.settings.SettingsWindow;
import ru.nstu.isma.app.ui.windows.simulation.SimulationWindow;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.ui.i18n.I18nUtils;

import javax.annotation.Resource;
import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bessonov Alex on 23.07.2016.
 */

public class WorkbenchController {

    @Resource
    private Workbench workbenchPanel;

    @Resource
    private ProjectManager projectManager;

    @Resource
    private IsmaEnvironment env;

    private Map<DocumentData, IsmaProject> documents = new HashMap<>();
    private Map<DocumentData, IsmaEditor> editors = new HashMap<>();


    public void newProject() {

        NewProjectWindow newProjectWindow = new NewProjectWindow(projectManager);

        newProjectWindow.setVisible(true);

    }

    public void openProject() {
        WebFileChooser fileChooser = new WebFileChooser(new File(System.getProperty("user.dir") + "/model/"));
        fileChooser.setMultiSelectionEnabled(false);

        if (fileChooser.showOpenDialog(workbenchPanel) == WebFileChooser.APPROVE_OPTION) {
            try {
                projectManager.loadProject(fileChooser.getSelectedFile());
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void saveProject() {
        try {
            projectManager.saveProject(currentProject());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAllProject() {
        try {
            projectManager.saveAll();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void runSimulation() {

        HSM model = getCurrentModel();

        if (model == null) {

            WebOptionPane.showMessageDialog(workbenchPanel, I18nUtils.getMessage("simulationForm.modelHasError"), I18nUtils.getMessage("simulationForm.error"), WebOptionPane.ERROR_MESSAGE);

        } else {

            SimulationWindow simulationWindow = new SimulationWindow(env, getCurrentModel());

            simulationWindow.setVisible(true);
        }
    }

    public void projectSettings() {

        SettingsWindow settingsWindow = new SettingsWindow(env.getSettingsManager());

        settingsWindow.setVisible(true);

    }

    public IsmaProject currentProject() {
        return documents.get(workbenchPanel.getDocumentPane().getSelectedDocument());
    }

    public IsmaEditor currentEditor() {
        return editors.get(workbenchPanel.getDocumentPane().getSelectedDocument());
    }

    public void validateCurrentModel() {
        Assert.notNull(currentEditor());
        currentEditor().validateModel();
    }

    public void cut() {
        Assert.notNull(currentEditor());
        currentEditor().cut();
    }

    public void paste() {
        Assert.notNull(currentEditor());
        currentEditor().paste();
    }

    public void copy() {
        Assert.notNull(currentEditor());
        currentEditor().copy();
    }

    public HSM getCurrentModel() {
        return currentEditor().getModel();
    }

    public void newTab(IsmaProject project) {

        Assert.notNull(project.getType());

        ImageIcon icon;

        Component editor = null;

        switch (project.getType()) {

            case LISMA_EPS:
                icon = env.loadIcon("sitemap.png");
                editor = new GraphEditor();
                break;

            case LISMA_PDE:
                icon = env.loadIcon("font-selection-editor.png");
                editor = new LismaPdeEditor(env.getLismaPdeTranslator(), project, this);
                break;

            default:
                throw new RuntimeException("Not supported.");
        }


        DocumentData data = getNewDocument(project, icon, editor);
        data.setDraggable(false);
        workbenchPanel.getDocumentPane().openDocument(data);
        workbenchPanel.getDocumentPane().setSelected(data);

    }

    public void closeTab(DocumentData documentData) throws JAXBException {
        IsmaProject p = documents.get(documentData);
        Assert.notNull(p);

        env.getProjectManager().closeProject(p);

    }

    private DocumentData getNewDocument(IsmaProject project, ImageIcon icon, Component editor) {
        DocumentData dd = new DocumentData(
                TextUtils.generateId(),
                icon,
                project.getName(),
                null,
                editor);

        Assert.isTrue(editor instanceof IsmaEditor);

        documents.put(dd, project);
        editors.put(dd, (IsmaEditor) editor);

        return dd;
    }

    /**
     * ====== GET / SET ======
     */

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public IsmaEnvironment getEnv() {
        return env;
    }

    public void setEnv(IsmaEnvironment env) {
        this.env = env;
    }
}
