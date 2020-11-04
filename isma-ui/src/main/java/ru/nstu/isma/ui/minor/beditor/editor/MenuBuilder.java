package ru.nstu.isma.ui.minor.beditor.editor;

import ru.nstu.isma.ui.common.AppData;
import ru.nstu.isma.ui.i18n.I18ChangedListener;
import ru.nstu.isma.ui.i18n.I18nUtils;
import ru.nstu.isma.ui.minor.beditor.UIStatic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Locale;

/**
 * Created by Bessonov Alex
 * Date: 05.01.14
 * Time: 2:15
 */
public class MenuBuilder implements I18ChangedListener {

    private final MainWindowController controller;
    private AppData appData;

    private JMenu fileMenu;
    private JMenuItem fileNewMenuItem;
    private JMenuItem fileOpenMenuItem;
    private JMenuItem fileSaveMenuItem;
    private JMenuItem fileExitMenuItem;
    private JMenu settingsMenu;
    private JMenuItem settingsAppThemeMenuItem;
    private JMenuItem settingsEditorThemeMenuItem;
    private JMenuItem settingsMethodLibraryMenuItem;
    private JMenu helpMenu;
    private JMenuItem helpAboutMenuItem;

    public MenuBuilder(AppData data, MainWindowController controller) {
        this.controller = controller;
        this.appData = data;
    }

    public JMenuBar build() {
        //Where the GUI is created:
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        fileMenu = new JMenu(I18nUtils.getMessage("menu.fileMenu"));
        fileMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(fileMenu);

        int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        fileNewMenuItem = new JMenuItem(I18nUtils.getMessage("menu.fileNewMenuItem"));
        fileNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, menuShortcutKeyMask));
        fileNewMenuItem.addActionListener(e -> controller.createModel());
        fileMenu.add(fileNewMenuItem);

        fileOpenMenuItem = new JMenuItem(I18nUtils.getMessage("menu.fileOpenMenuItem"));
        fileOpenMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, menuShortcutKeyMask));
        fileOpenMenuItem.addActionListener(e -> controller.openModel());
        fileMenu.add(fileOpenMenuItem);

        fileSaveMenuItem = new JMenuItem(I18nUtils.getMessage("menu.fileSaveMenuItem"));
        fileSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, menuShortcutKeyMask));
        fileSaveMenuItem.addActionListener(e -> controller.saveModel());
        fileMenu.add(fileSaveMenuItem);

        fileMenu.addSeparator();

        fileExitMenuItem = new JMenuItem(I18nUtils.getMessage("menu.fileExitMenuItem"));
        fileExitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, menuShortcutKeyMask));
        fileExitMenuItem.addActionListener(e -> controller.closeApplication());
        fileMenu.add(fileExitMenuItem);

        settingsMenu = new JMenu(I18nUtils.getMessage("menu.settingsMenu"));
        settingsMenu.setMnemonic(KeyEvent.VK_P);
        menuBar.add(settingsMenu);

        settingsAppThemeMenuItem = new JMenu(I18nUtils.getMessage("menu.settingsAppThemeMenuItem"));
        ButtonGroup group = new ButtonGroup();

        String currentSkinName = (String) appData.getProperties().get(UIStatic.SKIN_THEME_KEY);
        for (String skinName : UIStatic.SKIN_THEME_OPTIONS) {
            rbMenuItem = new JRadioButtonMenuItem(skinName);
            rbMenuItem.setSelected(skinName.equals(currentSkinName));
            rbMenuItem.addActionListener(e -> controller.setAppProperty(UIStatic.SKIN_THEME_KEY, skinName));
            group.add(rbMenuItem);
            settingsAppThemeMenuItem.add(rbMenuItem);
        }

        settingsMenu.add(settingsAppThemeMenuItem);

        settingsEditorThemeMenuItem = new JMenu(I18nUtils.getMessage("menu.settingsEditorThemeMenuItem"));
        group = new ButtonGroup();

        String currentEditorTheme = (String) appData.getProperties().get(UIStatic.EDITOR_THEME_KEY);
        for (String themeName : UIStatic.EDITOR_THEME_OPTIONS) {
            rbMenuItem = new JRadioButtonMenuItem(themeName);
            rbMenuItem.setSelected(themeName.equals(currentEditorTheme));
            rbMenuItem.addActionListener(e -> controller.setAppProperty(UIStatic.EDITOR_THEME_KEY, themeName));
            group.add(rbMenuItem);
            settingsEditorThemeMenuItem.add(rbMenuItem);
        }

        settingsMenu.add(settingsEditorThemeMenuItem);

        settingsMethodLibraryMenuItem = new JMenuItem(I18nUtils.getMessage("menu.settingsMethodLibraryMenuItem"));
        settingsMethodLibraryMenuItem.addActionListener(e -> controller.openMethodLibraryDialog());
        settingsMenu.add(settingsMethodLibraryMenuItem);


        helpMenu = new JMenu(I18nUtils.getMessage("menu.helpMenu"));
        helpMenu.setMnemonic(KeyEvent.VK_N);

        helpAboutMenuItem = new JMenuItem(I18nUtils.getMessage("menu.helpAboutMenuItem"));
        helpAboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, menuShortcutKeyMask));
        helpAboutMenuItem.addActionListener(e -> controller.openAboutDialog());
        helpMenu.add(helpAboutMenuItem);

        menuBar.add(helpMenu);
        return menuBar;
    }

    @Override
    public void i18Changed(Locale newLocale) {
        fileMenu.setText(I18nUtils.getMessage("menu.fileMenu"));
        fileNewMenuItem.setText(I18nUtils.getMessage("menu.fileNewMenuItem"));
        fileOpenMenuItem.setText(I18nUtils.getMessage("menu.fileOpenMenuItem"));
        fileSaveMenuItem.setText(I18nUtils.getMessage("menu.fileSaveMenuItem"));
        fileExitMenuItem.setText(I18nUtils.getMessage("menu.fileExitMenuItem"));
        settingsMenu.setText(I18nUtils.getMessage("menu.settingsMenu"));
        settingsAppThemeMenuItem.setText(I18nUtils.getMessage("menu.settingsAppThemeMenuItem"));
        settingsEditorThemeMenuItem.setText(I18nUtils.getMessage("menu.settingsEditorThemeMenuItem"));
        helpMenu.setText(I18nUtils.getMessage("menu.helpMenu"));
        helpAboutMenuItem.setText(I18nUtils.getMessage("menu.helpAboutMenuItem"));
    }

}
