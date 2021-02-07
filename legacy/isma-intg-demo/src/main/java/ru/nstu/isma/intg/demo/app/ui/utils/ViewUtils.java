package ru.nstu.isma.intg.demo.app.ui.utils;

import org.apache.commons.lang.SystemUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mariya Nasyrova
 * @since 13.10.14
 */
public class ViewUtils {

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static Point getCenterLocation(JFrame jFrame) {
        Dimension screenSize = getScreenSize();
        Point point = new Point(screenSize.width / 2 - jFrame.getSize().width / 2, screenSize.height / 2 - jFrame.getSize().height / 2);
        return point;
    }

    public static void setWindowsLookAndFeel(JFrame frame) {
        try {
            String lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
            if (SystemUtils.IS_OS_WINDOWS) {
                lookAndFeelClassName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            }
            UIManager.setLookAndFeel(lookAndFeelClassName);

            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
