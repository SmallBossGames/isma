package ru.nstu.isma.ui.minor.beditor.editor;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.ui.common.AppData;
import ru.nstu.isma.ui.i18n.I18nUtils;
import ru.nstu.isma.ui.minor.beditor.UIStatic;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class MainWindowController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MainWindow window;
    private final AppData appData;
    private final FileChooser fileChooser;


    public MainWindowController(AppData appData, MainWindow window) {
        this.appData = appData;
        this.window = window;
        this.fileChooser = new FileChooser();
    }

    public void closeApplication() {
        window.dispose();
        System.exit(0);
    }

    public void openAboutDialog() {
        window.openAboutDialog();
    }

    public void openMethodLibraryDialog() {
        window.openMethodLibraryDialog();
    }

    public void createModel() {
        // Если модель уже пустая, то ничего делать не нужно.
        String model = window.getCodeArea().getText().trim();
        if (model.equals("")) {
            return;
        }

        // Если есть несохраненная модель, предлагаем ее сохранить.
        String msg = I18nUtils.getMessage("menu.file.save.dialog.saveChanges");
        String title = I18nUtils.getMessage("menu.file.save.dialog.title");
        int option = JOptionPane.showConfirmDialog(window, msg, title, JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            saveModel();
        }

        // Очищаем окно редактора.
        window.getCodeArea().setText("");
    }

    public void saveModel() {
        // Если файл не выбран, то выходим.
        File file = chooseFile();
        if (file == null) {
            return;
        }

        String model = window.getCodeArea().getText();

        try {
            Files.write(model, file, Charsets.UTF_8);
            String msg = I18nUtils.getMessage("menu.file.save.dialog.saved");
            String title = I18nUtils.getMessage("menu.file.save.dialog.title");
            JOptionPane.showMessageDialog(window, msg, title, JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            String msg = String.format(I18nUtils.getMessage("menu.file.save.dialog.failed"), file.getName());
            String title = I18nUtils.getMessage("menu.file.save.dialog.title");
            JOptionPane.showMessageDialog(window, msg, title, JOptionPane.ERROR_MESSAGE);
            logger.error(msg, e);
        }
    }

    public void openModel() {
        // Если есть несохраненная модель, сначала спрашиваем, не нужно ли ее сохранить.
        String model = window.getCodeArea().getText().trim();
        if (!model.equals("")) {
            String msg = I18nUtils.getMessage("menu.file.save.dialog.saveChanges");
            String title = I18nUtils.getMessage("menu.file.save.dialog.title");
            int option = JOptionPane.showConfirmDialog(window, msg, title, JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                saveModel();
            }
        }

        // Если файл не выбран, то выходим.
        File file = chooseFile();
        if (file == null) {
            return;
        }

        model = null;

        try {
            model = Files.toString(file, Charsets.UTF_8);
        } catch (IOException e) {
            String msg = String.format(I18nUtils.getMessage("menu.file.open.dialog.failed"), file.getName());
            String title = I18nUtils.getMessage("menu.file.open.dialog.title");
            JOptionPane.showMessageDialog(window, msg, title, JOptionPane.ERROR_MESSAGE);
            logger.error(msg, e);
        }

        if (model != null) {
            window.getCodeArea().setText(model);
        }
    }

    public void setAppProperty(String key, String value) {
        appData.getProperties().setProperty(key, value);
        try {
            appData.saveProperties();
            if (key.equals(UIStatic.SKIN_THEME_KEY)) {
                appData.setLookAndFeel();
            } else if (key.equals(UIStatic.EDITOR_THEME_KEY)) {
                appData.applyEditorTheme(window.getCodeArea());
                appData.applyEditorTheme(window.getOutputArea());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1);
        }
    }

    private File chooseFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                I18nUtils.getMessage("menu.file.choose.dialog.textFiles"), "txt");
        return fileChooser.choose(filter);
    }

}
