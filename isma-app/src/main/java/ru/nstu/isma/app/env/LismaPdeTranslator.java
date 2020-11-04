package ru.nstu.isma.app.env;

import error.IsmaErrorList;
import org.springframework.stereotype.Service;
import ru.nstu.isma.app.env.project.IsmaProject;
import ru.nstu.isma.app.env.project.ProjectManager;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.service.HSM2TextDumpTranslator;
import ru.nstu.isma.in.InputTranslator;
import ru.nstu.isma.in.lisma.LismaTranslator;

import javax.annotation.Resource;

/**
 * Created by Bessonov Alex on 17.07.2016.
 */

@Service
public class LismaPdeTranslator {

    @Resource
    private ProjectManager projectManager;

    private IsmaErrorList errors = new IsmaErrorList();

    public HSM getCurrentModel() {
        IsmaProject project = projectManager.currentProject();

        errors.clear();
        InputTranslator translator = new LismaTranslator(project.getModelData(), errors);
        HSM model = translator.translate();

        return model;
    }

    public HSM tanslateLisma(String code) {
        errors.clear();
        InputTranslator translator = new LismaTranslator(code, errors);
        HSM model = translator.translate();

        return model;
    }


    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public IsmaErrorList getLastTranslatedModelErrors() {
        return errors;
    }
}
