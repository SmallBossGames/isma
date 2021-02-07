package ru.nstu.isma.app.ui.windows.mainwindow.workbench;

import com.alee.extended.tab.DocumentData;
import com.alee.extended.tab.DocumentListener;
import com.alee.extended.tab.PaneData;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.panel.WebPanel;
import ru.nstu.isma.app.env.IsmaEnvironment;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import java.awt.*;

/**
 * Created by Bessonov Alex on 17.07.2016.
 */
public class Workbench extends WebPanel {

    @Resource
    private IsmaEnvironment env;

    @Resource
    private Toolbar toolbar;

    @Resource
    private WorkbenchController workbenchController;

    private WebDocumentPane documentPane;


    @PostConstruct
    private void init() {

        setLayout(new BorderLayout());

        buildProjectPane();

        // панель с редакторами
        add(documentPane, BorderLayout.CENTER);

        // панель инструментов
        add(toolbar, BorderLayout.NORTH);

    }


    private void buildProjectPane() {
        documentPane = new WebDocumentPane();

        documentPane.setUndecorated(true);

        env.getProjectManager().addOnCreateProjectListener(workbenchController::newTab);

        env.getProjectManager().addOnCreateProjectListener(project -> {
            env.getSettingsManager().getIsmaSettings().getLastSessionProjects().add(project.getFilePath());
            try {
                env.getSettingsManager().saveSettings();
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        });

        env.getProjectManager().addOnCloseProjectListener(project -> {
            env.getSettingsManager().getIsmaSettings().getLastSessionProjects().remove(project.getFilePath());
            try {
                env.getSettingsManager().saveSettings();
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        });

        documentPane.addDocumentListener(new DocumentListener() {
            @Override
            public void opened(DocumentData documentData, PaneData paneData, int i) {

            }

            @Override
            public boolean closing(DocumentData documentData, PaneData paneData, int i) {
                return true;
            }

            @Override
            public void closed(DocumentData documentData, PaneData paneData, int i) {
                try {
                    workbenchController.closeTab(documentData);
                } catch (JAXBException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    /**
     * ====== GET / SET ======
     */

    public IsmaEnvironment getEnv() {
        return env;
    }

    public void setEnv(IsmaEnvironment env) {
        this.env = env;
    }

    public WorkbenchController getController() {
        return workbenchController;
    }

    public void setController(WorkbenchController controller) {
        this.workbenchController = controller;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public WebDocumentPane getDocumentPane() {
        return documentPane;
    }
}
