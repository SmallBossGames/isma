package ru.nstu.isma.app.util;

import com.alee.laf.rootpane.WebDialog;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Bessonov Alex on 17.07.2016.
 */
public class UiUtils {
    public static void centerWindowLocation(JFrame frame) {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // Move the window
        frame.setLocation(x, y);
    }

    public static void centerWindowLocation(WebDialog frame) {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // Move the window
        frame.setLocation(x, y);
    }
}
