package ru.nstu.isma.app.ui.windows.mainwindow.workbench;

import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import ru.nstu.isma.app.env.IsmaEnvironment;
import ru.nstu.isma.app.ui.windows.mainwindow.IsmaMainWindow;
import ru.nstu.isma.ui.i18n.I18nUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Bessonov Alex on 23.07.2016.
 */
public class Toolbar extends WebToolBar {
    @Resource
    private IsmaEnvironment env;

    @Resource
    private WorkbenchController workbenchController;

    @Resource
    private IsmaMainWindow mainWindow;

    public Toolbar() {
        super(WebToolBar.HORIZONTAL);

        setFloatable(false);

        setToolbarStyle(ToolbarStyle.attached);
    }

    @PostConstruct
    public void setupToolBar() {

        add(button("new.png", e -> workbenchController.newProject(), I18nUtils.getMessage("toolbar.new")));
        add(button("open.png", e -> workbenchController.openProject(), I18nUtils.getMessage("toolbar.open")));
        add(button("toolbar/save.png", e -> workbenchController.saveProject(), I18nUtils.getMessage("toolbar.save")));
        add(button("toolbar/saveall.png", e -> workbenchController.saveAllProject(), I18nUtils.getMessage("toolbar.saveAll")));

        addSeparator();

        add(button("toolbar/cut.png", e -> workbenchController.cut(), I18nUtils.getMessage("toolbar.cut")));
        add(button("toolbar/copy.png", e -> workbenchController.copy(), I18nUtils.getMessage("toolbar.copy")));
        add(button("toolbar/paste.png", e -> workbenchController.paste(), I18nUtils.getMessage("toolbar.paste")));


        addSeparator();

        add(button("toolbar/checked.png", e -> workbenchController.validateCurrentModel(), I18nUtils.getMessage("toolbar.verify")));
        add(button("toolbar/play.png", e -> workbenchController.runSimulation(), I18nUtils.getMessage("toolbar.simulation")));

        addSeparator();

        add(buttonDisabled("toolbar/settings.png", e -> workbenchController.projectSettings(), I18nUtils.getMessage("toolbar.settings")));
        add(createLocaleButton());

    }

    private WebButton buttonDisabled(String iconName, ActionListener actionListener, String tooltip) {
        WebButton b = button(iconName, actionListener, tooltip);
        b.setEnabled(false);
        return b;
    }

    private WebButton button(String iconName, ActionListener actionListener, String tooltip) {
        WebButton b = WebButton.createIconWebButton(env.loadIcon(iconName), StyleConstants.smallRound, true);

        if (actionListener != null)
            b.addActionListener(actionListener);

//        b.setToolTipText(tooltip);

        TooltipManager.setTooltip(b, tooltip, TooltipWay.down);


        return b;
    }

    private JButton createLocaleButton() {
        Map<String, ImageIcon> localeIcons = new HashMap<>();
        ImageIcon ruIcon = env.loadIcon("ru.png");
        localeIcons.put(I18nUtils.RU.getLanguage(), ruIcon);
        ImageIcon enIcon = env.loadIcon("en.png");
        localeIcons.put(I18nUtils.EN.getLanguage(), enIcon);

        JButton button = new JButton();
        button.setActionCommand(I18nUtils.DEFAULT.getLanguage());
        button.setIcon(localeIcons.get(I18nUtils.DEFAULT.getLanguage()));
        env.changeI18N(I18nUtils.DEFAULT);
        button.addActionListener(l -> {
            String currentCommand = button.getActionCommand();
            Locale newLocale = currentCommand.equals(I18nUtils.RU.getLanguage()) ? I18nUtils.EN : I18nUtils.RU;

            env.changeI18N(newLocale);

            button.setIcon(localeIcons.get(newLocale.getLanguage()));
            button.setActionCommand(newLocale.getLanguage());
        });

        button.setToolTipText(I18nUtils.getMessage("toolbar.language"));
        return button;
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

    public WorkbenchController getWorkbenchController() {
        return workbenchController;
    }

    public void setWorkbenchController(WorkbenchController workbenchController) {
        this.workbenchController = workbenchController;
    }

    public IsmaMainWindow getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(IsmaMainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }


}
