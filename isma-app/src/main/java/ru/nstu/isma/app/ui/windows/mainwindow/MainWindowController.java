package ru.nstu.isma.app.ui.windows.mainwindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.app.env.IsmaEnvironment;
import ru.nstu.isma.app.env.setting.IsmaSettings;
import ru.nstu.isma.app.ui.common.i18n.I18nUtils;

import javax.annotation.Resource;
import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class MainWindowController implements WindowStateListener, ComponentListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IsmaMainWindow window;

    @Resource
    private IsmaEnvironment env;


    public void closeApplication() {
        window.dispose();
        System.exit(0);
    }

    public void openAboutDialog() {
        window.openAboutDialog();
    }

    /**
     * Action listener
     */

    @Override
    public void windowStateChanged(WindowEvent e) {
        IsmaSettings settings = env.getSettingsManager().getIsmaSettings();

        if (e.getNewState() ==0 )
            settings.setMainWindowMaximazed(false);  // minimized
        else if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH)
            settings.setMainWindowMaximazed(true);  // maximized

        saveIsmaSettings();
    }

    @Override
    public void componentResized(ComponentEvent evt) {
        IsmaSettings settings = env.getSettingsManager().getIsmaSettings();

        settings.setMainWindowHeight(window.getHeight());
        settings.setMainWindowWidth(window.getWidth());

        saveIsmaSettings();
    }

    private void saveIsmaSettings() {
        try {
            env.getSettingsManager().saveSettings();
        } catch (JAXBException e) {
            String msg = I18nUtils.getMessage("settings.save.failed");
            String title = I18nUtils.getMessage("settings.save.title");
            JOptionPane.showMessageDialog(window, msg, title, JOptionPane.ERROR_MESSAGE);
            logger.error(msg, e);
        }
    }


    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    /**
     * GET / SET
     */

    public IsmaMainWindow getWindow() {
        return window;
    }

    public void setWindow(IsmaMainWindow window) {
        this.window = window;
    }

}
