package ru.nstu.isma.ui.minor.beditor.editor;

import ru.nstu.isma.ui.i18n.I18nUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * @author Maria
 * @since 31.07.2016
 */
class FileChooser {
    private final JFileChooser chooser;
    private File lastUsedDirectory;

    FileChooser() {
        this.chooser = new JFileChooser();
    }

    File choose(FileNameExtensionFilter filter) {
        chooser.setCurrentDirectory(lastUsedDirectory);
        chooser.setFileFilter(filter);
        int action = chooser.showDialog(null, I18nUtils.getMessage("menu.file.choose.dialog.title"));
        lastUsedDirectory = chooser.getCurrentDirectory();
        if (action == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }
}
