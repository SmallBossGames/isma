package env.project;

import ru.nstu.isma.app.util.Consts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bessonov Alex on 20.07.2016.
 */

@XmlRootElement
public class IsmaProject {
    private String name;

    private String description;

    private Set<RunConfiguration> configurations = new HashSet<>();

    private IsmaProjectType type;

    private String modelData;

    private String filePath;

    private boolean changed;

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
        changed = true;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
        changed = true;
    }

    public Set<RunConfiguration> getConfigurations() {
        return configurations;
    }

    @XmlElement(name = "configuration")
    @XmlElementWrapper(name = "simulation")
    public void setConfigurations(Set<RunConfiguration> configurations) {
        this.configurations = configurations;
        changed = true;
    }

    public IsmaProjectType getType() {
        return type;
    }

    @XmlElement
    public void setType(IsmaProjectType type) {
        this.type = type;
        changed = true;
    }

    public String getModelData() {
        return modelData;
    }

    @XmlElement
    public void setModelData(String modelData) {
        this.modelData = modelData;
        changed = true;
    }

    public boolean isChanged() {
        return changed;
    }

    @XmlTransient
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static IsmaProject newProject() {
        IsmaProject project = new IsmaProject();
        project.setName(Consts.DEF_PROJECT_NAME);
        project.setType(IsmaProjectType.LISMA_PDE);
        return project;
    }
}
