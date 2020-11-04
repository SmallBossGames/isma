package ru.nstu.isma.app.ui.windows.settings;

import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.tabbedpane.WebTabbedPane;
import ru.nstu.isma.app.env.setting.ApplicationSettingsManager;
import ru.nstu.isma.app.ui.windows.settings.groups.CommonSettingsGroup;
import ru.nstu.isma.app.ui.windows.settings.groups.SettingGroup;
import ru.nstu.isma.app.util.UiUtils;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Bessonov Alex on 22.07.2016.
 */
public class SettingsWindow extends WebDialog {

    private ApplicationSettingsManager settingsManager;

    private SettingsController controller;

    private WebButton ok;

    private WebButton close;

    private List<SettingGroup> groups = new LinkedList<>();


    public SettingsWindow(ApplicationSettingsManager manager) {

        this.settingsManager = manager;

        controller = new SettingsController(this);

        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(buildContentPanel(), BorderLayout.CENTER);

        getContentPane().add(buildButtonsPanel(), BorderLayout.SOUTH);


        setTitle("Settings");
        pack();
        setSize(900, 650);
        setResizable(false);
        UiUtils.centerWindowLocation(this);
        setModal(true);
    }

    private void populateGroups() {
        groups.add(new CommonSettingsGroup(this, settingsManager.getIsmaSettings()));
    }

    private WebPanel buildContentPanel() {
        WebPanel contentPanel = new WebPanel(new BorderLayout());

        populateGroups();

        contentPanel.add(groupContent(), BorderLayout.CENTER);

        WebPanel panel = new WebPanel();
        panel.add(contentPanel);
        panel.setMargin(10);

        return panel;
    }


    private WebPanel getGroup() {
        WebPanel contentPanel = new WebPanel(new BorderLayout());
        return contentPanel;
    }

    private WebPanel groupContent() {
        WebPanel contentPanel = new WebPanel(new BorderLayout());

        WebTabbedPane tabbedPane3 = new WebTabbedPane();
        tabbedPane3.setPreferredSize(new Dimension(150, 120));
        tabbedPane3.setTabPlacement(WebTabbedPane.TOP);

        groups.stream().forEach(e ->
                tabbedPane3.addTab(e.getGroupName(), e.getPanel()));

        contentPanel.add(tabbedPane3);

        return contentPanel;
    }

    private WebPanel buildButtonsPanel() {
        WebPanel buttonBar = new WebPanel(new HorizontalFlowLayout(10));

        ok = new WebButton("Apply");
        ok.addActionListener(e -> {
            controller.saveSettings();
            setVisible(false);
        });

        close = new WebButton("Cancel");
        close.addActionListener(e -> setVisible(false));

        buttonBar.add(ok);
        buttonBar.add(close);

        buttonBar.setMargin(0, 10, 10, 10);

        WebPanel buttonBarMain = new WebPanel(new BorderLayout());
        buttonBarMain.add(buttonBar, BorderLayout.EAST);

        return buttonBarMain;
    }

}
