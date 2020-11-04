package ru.nstu.isma.app.ui.windows.mainwindow;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WebFrame;
import ru.nstu.isma.app.env.IsmaEnvironment;
import ru.nstu.isma.app.env.setting.IsmaSettings;
import ru.nstu.isma.app.ui.windows.about.AboutWindow;
import ru.nstu.isma.app.ui.common.i18n.I18ChangedListener;
import ru.nstu.isma.app.ui.common.i18n.I18nUtils;
import ru.nstu.isma.app.ui.windows.mainwindow.menu.MainWindowMenuBuilder;
import ru.nstu.isma.app.ui.windows.mainwindow.statusbar.StatusPanel;
import ru.nstu.isma.app.ui.windows.mainwindow.workbench.Workbench;
import ru.nstu.isma.app.util.UiUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by Bessonov Alex on 17.07.2016.
 */

public class IsmaMainWindow extends WebFrame implements I18ChangedListener {

    @Resource
    private IsmaEnvironment ismaEnvironment;

    @Resource
    private MainWindowController windowController;

    @Resource
    private MainWindowMenuBuilder mainWindowMenuBuilder;

    @Resource
    private StatusPanel statusPanel;

    private AboutWindow aboutWindow;

    @Resource
    private Workbench mainContentPanel;


    @PostConstruct
    private void initView() throws JAXBException {
        initWindow();

        // меню
        setJMenuBar(mainWindowMenuBuilder.build());

        // панель с основным экраном ИСМА
        getContentPane().add(mainContentPanel, BorderLayout.CENTER);

        // панель статуса
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        // listeners
        addComponentListener(windowController);
        addWindowStateListener(windowController);

        env().addI18NChangedListener(this);

        prepareWindowSize();

        prepareLastOpenedProjects();
    }

    public void openAboutDialog() {
        if (aboutWindow == null) {

            aboutWindow = new AboutWindow();
            env().addI18NChangedListener(aboutWindow);
        }
        aboutWindow.setVisible(true);
    }

    @Override
    public void i18Changed(Locale newLocale) {
        setTitle(I18nUtils.getMessage("isma"));
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);

        if (env().getSettingsManager().getIsmaSettings().getMainWindowMaximazed())
            setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }


    private void initWindow() {
        setTitle(I18nUtils.getMessage("isma"));
        setIconImage(env().loadIcon("title.png").getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
    }

    private void prepareWindowSize() {
        IsmaSettings s = env().getSettingsManager().getIsmaSettings();

        setSize(s.getMainWindowWidth(), s.getMainWindowHeight());
        pack();
        setSize(s.getMainWindowWidth(), s.getMainWindowHeight());

        UiUtils.centerWindowLocation(this);
    }

    private void prepareLastOpenedProjects() throws JAXBException {
        IsmaSettings s = env().getSettingsManager().getIsmaSettings();

        for (String path : s.getLastSessionProjects().stream().collect(Collectors.toList())) {
            File f = new File(path);

            if (f.exists())
                env().getProjectManager().loadProject(f);
            else
                s.getLastSessionProjects().remove(path);

        }

    }


    /**
     * GET / SET
     */

    public IsmaEnvironment getIsmaEnvironment() {
        return ismaEnvironment;
    }

    public IsmaEnvironment env() {
        return ismaEnvironment;
    }

    public void setIsmaEnvironment(IsmaEnvironment ismaEnvironment) {
        this.ismaEnvironment = ismaEnvironment;
    }

    public MainWindowController getWindowController() {
        return windowController;
    }

    public void setWindowController(MainWindowController windowController) {
        this.windowController = windowController;
    }

    public AboutWindow getAboutWindow() {
        return aboutWindow;
    }

    public void setAboutWindow(AboutWindow aboutWindow) {
        this.aboutWindow = aboutWindow;
    }

}
