package ru.nstu.isma.ui.minor.beditor;

import ru.nstu.isma.ui.common.AppData;
import ru.nstu.isma.ui.minor.beditor.editor.MainWindow;

import javax.swing.*;

public class BIsmaEditor {

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Start all Swing applications on the EDT.
        SwingUtilities.invokeLater(() -> {
            AppData data = new AppData();
            data.setLookAndFeel();

            String jdkPath = System.getProperty("java.home");
            if (jdkPath == null) {
                throw new RuntimeException("java.home property is not defined. ISMA will exit");
            }

            new MainWindow(data).setVisible(true);
        });
    }
}
