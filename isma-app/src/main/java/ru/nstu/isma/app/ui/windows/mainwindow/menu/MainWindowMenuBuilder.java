package ru.nstu.isma.app.ui.windows.mainwindow.menu;


import com.alee.laf.menu.WebMenuBar;
import ru.nstu.isma.app.env.IsmaEnvironment;
import ru.nstu.isma.app.ui.common.i18n.I18ChangedListener;
import ru.nstu.isma.app.ui.common.i18n.I18nUtils;
import ru.nstu.isma.app.ui.windows.mainwindow.MainWindowController;
import ru.nstu.isma.app.ui.windows.mainwindow.workbench.WorkbenchController;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

/**
 * Created by Bessonov Alex
 * Date: 05.01.14
 * Time: 2:15
 */

public class MainWindowMenuBuilder implements I18ChangedListener {

    @Resource
    private MainWindowController controller;

    @Resource
    private WorkbenchController workbenchController;

    @Resource
    private IsmaEnvironment env;


    private JMenu fileMenu;
    private JMenuItem fileNewMenuItem;
    private JMenuItem fileOpenMenuItem;
    private JMenuItem fileSaveMenuItem;
    private JMenuItem fileSaveAsMenuItem;
    private JMenuItem fileCloseMenuItem;
    private JMenuItem fileCloseAllMenuItem;
    private JMenuItem fileSettingsMenuItem;
    private JMenuItem fileExitMenuItem;

    private JMenu editMenu;
    private JMenuItem cutMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;

    private JMenu simMenu;
    private JMenuItem validateMenuItem;
    private JMenuItem runMenuItem;

    private JMenu settingsMenu;
    private JMenuItem appSettings;
    private JMenuItem langSettings;
//    private JMenuItem settingsAppThemeMenuItem;
//    private JMenuItem settingsEditorThemeMenuItem;

    private JMenu helpMenu;
    private JMenuItem helpMenuItem;
    private JMenuItem aboutMenuItem;

    public MainWindowMenuBuilder() {
    }

    public WebMenuBar build() {
        WebMenuBar menuBar;

        //Create the menu bar.
        menuBar = new WebMenuBar();
        menuBar.setUndecorated(true);

        // 1. Файл
        fileMenu = new JMenu(I18nUtils.getMessage("menu.fileMenu"));
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        // 1.1 Новая модель
        fileNewMenuItem = getMenuItem("menu.fileNewMenuItem", KeyEvent.VK_N, menuShortcutKeyMask, e -> workbenchController.newProject());
//        fileNewMenuItem.setIcon(env.loadIcon("new.png"));
        fileNewMenuItem.setIcon(env.loadIcon("menu/plus-black-symbol.png"));
        fileMenu.add(fileNewMenuItem);

        // 1.2 Открыть модель
        fileOpenMenuItem = getMenuItem("menu.fileOpenMenuItem", KeyEvent.VK_O, menuShortcutKeyMask, e -> workbenchController.openProject());
        fileOpenMenuItem.setIcon(env.loadIcon("menu/open-folder.png"));
        fileMenu.add(fileOpenMenuItem);

        // 1.3 Сохранить модель
        fileSaveMenuItem = getMenuItem("menu.fileSaveMenuItem", KeyEvent.VK_S, menuShortcutKeyMask, e -> workbenchController.saveProject());
        fileSaveMenuItem.setIcon(env.loadIcon("menu/save-file-option.png"));
//        fileSaveMenuItem.setIcon(env.loadIcon("save.png"));
        fileMenu.add(fileSaveMenuItem);

        // 1.4 Сохранить как
        fileSaveAsMenuItem = getMenuItem("menu.fileSaveAsMenuItem", null, menuShortcutKeyMask, null /*e -> controller.saveModel()*/);
        fileSaveAsMenuItem.setEnabled(false);
        fileMenu.add(fileSaveAsMenuItem);

        // разделитель
        fileMenu.addSeparator();

        // 1.5 Параметры модели
        fileSettingsMenuItem = getMenuItem("menu.fileSettingsMenuItem", null, menuShortcutKeyMask, null /*e -> controller.saveModel()*/);
        fileSettingsMenuItem.setIcon(env.loadIcon("menu/cog-wheel-silhouette.png"));
        fileSettingsMenuItem.setEnabled(false);
//        fileSettingsMenuItem.setIcon(env.loadIcon("settings2.png"));
        fileMenu.add(fileSettingsMenuItem);

        // разделитель
        fileMenu.addSeparator();

        // 1.6 Закрыть
        fileCloseMenuItem = getMenuItem("menu.fileCloseMenuItem", null, menuShortcutKeyMask, null /*e -> controller.saveModel()*/);
        fileCloseMenuItem.setIcon(env.loadIcon("menu/minus-symbol.png"));
        fileCloseMenuItem.setEnabled(false);
        fileMenu.add(fileCloseMenuItem);

        // 1.7 Закрыть все
        fileCloseAllMenuItem = getMenuItem("menu.fileCloseAllMenuItem", null, menuShortcutKeyMask, null /*e -> controller.saveModel()*/);
        fileCloseAllMenuItem.setEnabled(false);
        fileMenu.add(fileCloseAllMenuItem);

        // разделитель
        fileMenu.addSeparator();

        // 1.8 Выход
        fileExitMenuItem = getMenuItem("menu.fileExitMenuItem", KeyEvent.VK_W, menuShortcutKeyMask, e -> controller.closeApplication());
        fileExitMenuItem.setIcon(env.loadIcon("menu/sign-out-option.png"));
//        fileExitMenuItem.setIcon(env.loadIcon("exit.png"));
        fileMenu.add(fileExitMenuItem);


        // 2.Редактирование
        editMenu = new JMenu(I18nUtils.getMessage("menu.editMenu"));
        editMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(editMenu);

        cutMenuItem = getMenuItem("menu.cutMenuItem", KeyEvent.VK_X, menuShortcutKeyMask, e -> workbenchController.cut());
        cutMenuItem.setIcon(env.loadIcon("scissors.png"));
        editMenu.add(cutMenuItem);

        copyMenuItem = getMenuItem("menu.copyMenuItem", KeyEvent.VK_C, menuShortcutKeyMask, e -> workbenchController.copy());
        copyMenuItem.setIcon(env.loadIcon("copy-document.png"));
        editMenu.add(copyMenuItem);

        pasteMenuItem = getMenuItem("menu.pasteMenuItem", KeyEvent.VK_V, menuShortcutKeyMask, e -> workbenchController.paste());
        pasteMenuItem.setIcon(env.loadIcon("paste.png"));
        editMenu.add(pasteMenuItem);

        // 3. Моделирование
        simMenu = new JMenu(I18nUtils.getMessage("menu.simMenu"));
        simMenu.setMnemonic(KeyEvent.VK_R);
        menuBar.add(simMenu);

        validateMenuItem = getMenuItem("menu.validateMenuItem", KeyEvent.VK_F4, menuShortcutKeyMask, e -> workbenchController.validateCurrentModel());
        validateMenuItem.setIcon(env.loadIcon("menu/correct-symbol.png"));
        simMenu.add(validateMenuItem);

        runMenuItem = getMenuItem("menu.runMenuItem", KeyEvent.VK_F5, menuShortcutKeyMask, e -> workbenchController.runSimulation());
        runMenuItem.setIcon(env.loadIcon("menu/arrowhead-pointing-to-the-right.png"));
        simMenu.add(runMenuItem);

        // 4. Настройки
        settingsMenu = new JMenu(I18nUtils.getMessage("menu.settingsMenu"));
        settingsMenu.setMnemonic(KeyEvent.VK_P);
        menuBar.add(settingsMenu);

        appSettings = getMenuItem("menu.appSettings", KeyEvent.VK_P, menuShortcutKeyMask, e -> workbenchController.projectSettings());
        appSettings.setIcon(env.loadIcon("menu/cog-wheel-silhouette.png"));
        appSettings.setEnabled(false);
        settingsMenu.add(appSettings);

        langSettings = getMenuItem("menu.langSettings", KeyEvent.VK_L, menuShortcutKeyMask, null /*e -> controller.saveModel()*/);
        langSettings.setIcon(env.loadIcon("menu/cog-wheel-silhouette.png"));
        langSettings.setEnabled(false);
        settingsMenu.add(langSettings);

// 2.1 настройки темы
//        ButtonGroup group = new ButtonGroup();
//        String currentSkinName = (String) appData.getProperties().get(UIStatic.SKIN_THEME_KEY);
//        for (String skinName : UIStatic.SKIN_THEME_OPTIONS) {
//            rbMenuItem = new JRadioButtonMenuItem(skinName);
//            rbMenuItem.setSelected(skinName.equals(currentSkinName));
//            rbMenuItem.addActionListener(e -> controller.setAppProperty(UIStatic.SKIN_THEME_KEY, skinName));
//            group.add(rbMenuItem);
//            settingsAppThemeMenuItem.add(rbMenuItem);
//        }
//        settingsMenu.add(settingsAppThemeMenuItem);
//        settingsAppThemeMenuItem = getMenuItem("menu.settingsAppThemeMenuItem", null, menuShortcutKeyMask, null);
//        settingsAppThemeMenuItem.setIcon(env.loadIcon("settings2.png"));
//        settingsMenu.add(settingsAppThemeMenuItem);
        // 2.2 настройки редактора
//        group = new ButtonGroup();
//        String currentEditorTheme = (String) appData.getProperties().get(UIStatic.EDITOR_THEME_KEY);
//        for (String themeName : UIStatic.EDITOR_THEME_OPTIONS) {
//            rbMenuItem = new JRadioButtonMenuItem(themeName);
//            rbMenuItem.setSelected(themeName.equals(currentEditorTheme));
//            rbMenuItem.addActionListener(e -> controller.setAppProperty(UIStatic.EDITOR_THEME_KEY, themeName));
//            group.add(rbMenuItem);
//            settingsEditorThemeMenuItem.add(rbMenuItem);
//        }1

//        settingsEditorThemeMenuItem = getMenuItem("menu.settingsEditorThemeMenuItem", null, menuShortcutKeyMask, null);
//        settingsEditorThemeMenuItem.setIcon(env.loadIcon("settings2.png"));
//        settingsMenu.add(settingsEditorThemeMenuItem);

        // 3. справка
        helpMenu = new JMenu(I18nUtils.getMessage("menu.helpMenu"));
        helpMenu.setMnemonic(KeyEvent.VK_N);

        helpMenuItem = getMenuItem("menu.helpMenuItem", KeyEvent.VK_F1, menuShortcutKeyMask, null);
        helpMenuItem.setIcon(env.loadIcon("menu/question-sign.png"));
        helpMenuItem.setEnabled(false); // TODO
        helpMenu.add(helpMenuItem);

        // 3.1 о программе
        aboutMenuItem = getMenuItem("menu.aboutMenuItem", KeyEvent.VK_A, menuShortcutKeyMask, e -> controller.openAboutDialog());
        aboutMenuItem.setIcon(env.loadIcon("menu/information-symbol.png"));
        helpMenu.add(aboutMenuItem);


        menuBar.add(helpMenu);

        env.addI18NChangedListener(this);
        return menuBar;
    }

    private JMenuItem getMenuItem(String text, Integer event, Integer menuShortcutKeyMask, ActionListener l) {
        JMenuItem item = new JMenuItem(I18nUtils.getMessage(text));

        if (event != null)
            item.setAccelerator(KeyStroke.getKeyStroke(event, menuShortcutKeyMask));

        if (l != null)
            item.addActionListener(l);

        return item;
    }


    @Override
    public void i18Changed(Locale newLocale) {
        fileMenu.setText(I18nUtils.getMessage("menu.fileMenu"));
        fileNewMenuItem.setText(I18nUtils.getMessage("menu.fileNewMenuItem"));
        fileOpenMenuItem.setText(I18nUtils.getMessage("menu.fileOpenMenuItem"));
        fileSaveMenuItem.setText(I18nUtils.getMessage("menu.fileSaveMenuItem"));
        fileSaveAsMenuItem.setText(I18nUtils.getMessage("menu.fileSaveAsMenuItem"));
        fileCloseMenuItem.setText(I18nUtils.getMessage("menu.fileCloseMenuItem"));
        fileCloseAllMenuItem.setText(I18nUtils.getMessage("menu.fileCloseAllMenuItem"));
        fileSettingsMenuItem.setText(I18nUtils.getMessage("menu.fileSettingsMenuItem"));
        fileExitMenuItem.setText(I18nUtils.getMessage("menu.fileExitMenuItem"));

        simMenu.setText(I18nUtils.getMessage("menu.simMenu"));
        validateMenuItem.setText(I18nUtils.getMessage("menu.validateMenuItem"));
        runMenuItem.setText(I18nUtils.getMessage("menu.runMenuItem"));


        settingsMenu.setText(I18nUtils.getMessage("menu.settingsMenu"));
        appSettings.setText(I18nUtils.getMessage("menu.appSettings"));
        langSettings.setText(I18nUtils.getMessage("menu.langSettings"));
//        settingsAppThemeMenuItem.setText(I18nUtils.getMessage("menu.settingsAppThemeMenuItem"));
//        settingsEditorThemeMenuItem.setText(I18nUtils.getMessage("menu.settingsEditorThemeMenuItem"));

        editMenu.setText(I18nUtils.getMessage("menu.editMenu"));
        cutMenuItem.setText(I18nUtils.getMessage("menu.cutMenuItem"));
        copyMenuItem.setText(I18nUtils.getMessage("menu.copyMenuItem"));
        pasteMenuItem.setText(I18nUtils.getMessage("menu.pasteMenuItem"));

        helpMenu.setText(I18nUtils.getMessage("menu.helpMenu"));
        helpMenuItem.setText(I18nUtils.getMessage("menu.helpMenuItem"));
        aboutMenuItem.setText(I18nUtils.getMessage("menu.aboutMenuItem"));
    }

    public MainWindowController getController() {
        return controller;
    }

    public void setController(MainWindowController controller) {
        this.controller = controller;
    }

    public IsmaEnvironment getEnv() {
        return env;
    }

    public void setEnv(IsmaEnvironment env) {
        this.env = env;
    }
}
