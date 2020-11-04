package ru.nstu.isma.app.env;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;
import ru.nstu.isma.app.env.project.ProjectManager;
import ru.nstu.isma.app.env.setting.ApplicationSettingsManager;
import ru.nstu.isma.app.ui.common.i18n.I18ChangedListener;
import ru.nstu.isma.app.ui.common.i18n.I18nUtils;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.*;

/**
 * Created by Bessonov Alex on 17.07.2016.
 */

@Service
public class IsmaEnvironment {

    @Resource
    private LismaPdeTranslator lismaPdeTranslator;

    @Resource
    private ProjectManager projectManager;

    @Resource
    private ApplicationSettingsManager settingsManager;

    private static final Map<String, ImageIcon> iconsCache = new HashMap<>();

    private Set<I18ChangedListener> i18ChangedListeners = new HashSet<>();


    public ImageIcon loadIcon(final String path) {
        return loadIcon(getClass(), path);
    }

    public ImageIcon loadIcon(final Class nearClass, final String path) {
        final String key = nearClass.getCanonicalName() + ":" + path;
        if (!iconsCache.containsKey(key)) {
            iconsCache.put(key, new ImageIcon(nearClass.getResource("/icons/" + path)));
        }
        return iconsCache.get(key);
    }

    public void changeI18N(Locale newLocale) {
        I18nUtils.setLocale(newLocale);

        i18ChangedListeners.forEach(l -> l.i18Changed(newLocale));
    }

    public void addI18NChangedListener(@NotNull I18ChangedListener listener) {
        i18ChangedListeners.add(listener);
    }

    /**
     * GET / SET
     */

    public LismaPdeTranslator getLismaPdeTranslator() {
        return lismaPdeTranslator;
    }

    public void setLismaPdeTranslator(LismaPdeTranslator lismaPdeTranslator) {
        this.lismaPdeTranslator = lismaPdeTranslator;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public ApplicationSettingsManager getSettingsManager() {
        return settingsManager;
    }

    public void setSettingsManager(ApplicationSettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }
}
