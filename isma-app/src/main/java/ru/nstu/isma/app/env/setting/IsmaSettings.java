package ru.nstu.isma.app.env.setting;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Bessonov Alex
 * on 23.07.2016.
 */

@XmlRootElement
public class IsmaSettings {

    private String language;

    // Paths
    private String workingDir;

    private String lastProjectDir;

    private String methodsDir;


    // Recent projects
    private Set<String> lastSessionProjects = new LinkedHashSet<>();

    private Set<String> recentProjects = new LinkedHashSet<>();

    private Integer recentProjectsMaxCount;


    // Main window
    private Integer mainWindowWidth;

    private Integer mainWindowHeight;

    private Boolean mainWindowMaximazed;


    /**
     * GET / SET
     */
    public Integer getMainWindowWidth() {
        return mainWindowWidth;
    }

    @XmlElement
    public void setMainWindowWidth(Integer mainWindowWidth) {
        this.mainWindowWidth = mainWindowWidth;
    }

    public Integer getMainWindowHeight() {
        return mainWindowHeight;
    }

    @XmlElement
    public void setMainWindowHeight(Integer mainWindowHeight) {
        this.mainWindowHeight = mainWindowHeight;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    @XmlElement
    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public Boolean getMainWindowMaximazed() {
        return mainWindowMaximazed;
    }

    @XmlElement
    public void setMainWindowMaximazed(Boolean mainWindowMaximazed) {
        this.mainWindowMaximazed = mainWindowMaximazed;
    }

    public String getLanguage() {
        return language;
    }

    @XmlElement
    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMethodsDir() {
        return methodsDir;
    }

    @XmlElement
    public void setMethodsDir(String methodsDir) {
        this.methodsDir = methodsDir;
    }

    public Set<String> getLastSessionProjects() {
        return lastSessionProjects;
    }

    @XmlElement(name = "lastSessionProject")
    @XmlElementWrapper(name = "lastSession")
    public void setLastSessionProjects(Set<String> lastSessionProjects) {
        this.lastSessionProjects = lastSessionProjects;
    }

    public Set<String> getRecentProjects() {
        return recentProjects;
    }

    @XmlElement(name = "recentProject")
    @XmlElementWrapper(name = "recent")
    public void setRecentProjects(Set<String> recentProjects) {
        this.recentProjects = recentProjects;
    }

    public Integer getRecentProjectsMaxCount() {
        return recentProjectsMaxCount;
    }

    @XmlElement
    public void setRecentProjectsMaxCount(Integer recentProjectsMaxCount) {
        this.recentProjectsMaxCount = recentProjectsMaxCount;
    }

    public String getLastProjectDir() {
        return lastProjectDir;
    }

    @XmlElement
    public void setLastProjectDir(String lastProjectDir) {
        this.lastProjectDir = lastProjectDir;
    }
}
