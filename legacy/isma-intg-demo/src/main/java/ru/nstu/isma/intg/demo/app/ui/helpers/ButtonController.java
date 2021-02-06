package ru.nstu.isma.intg.demo.app.ui.helpers;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Mariya Nasyrova
 * @since 16.10.2014
 */
public abstract class ButtonController {

    private JButton button;

    public ButtonController(JButton okButton) {
        this.button = okButton;
        okButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonClicked();
            }
        });
    }

    public abstract void buttonClicked();

}
