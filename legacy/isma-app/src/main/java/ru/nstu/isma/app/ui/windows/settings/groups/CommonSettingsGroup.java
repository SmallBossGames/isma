package ru.nstu.isma.app.ui.windows.settings.groups;

import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.text.WebTextField;
import com.alee.utils.FileUtils;
import com.alee.utils.swing.DialogOptions;
import ru.nstu.isma.app.env.setting.IsmaSettings;
import ru.nstu.isma.app.ui.windows.settings.SettingsWindow;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Bessonov Alex
 * on 23.08.2016.
 */
public class CommonSettingsGroup extends WebPanel implements SettingGroup {

    private String name;

    private IsmaSettings settings;

    private SettingsWindow parent;

    private File choosedDir = new File(System.getProperty("user.dir") + "/model/");


    public CommonSettingsGroup(SettingsWindow parent, IsmaSettings settings) {
        this.name = "Common";
        this.parent = parent;
        this.settings = settings;

        build();
    }

    @Override
    public String getGroupName() {
        return name;
    }

    @Override
    public void saveGroup() {
    }

    @Override
    public Component getPanel() {
        return this;
    }


    private void build() {
        WebPanel contentPanel = new WebPanel(new VerticalFlowLayout(10, 10));

        contentPanel.add(buildWorkingDirPanel());
//        contentPanel.add(new WebSeparator(false, true));


        add(contentPanel);
        setMargin(10);
    }


    private WebPanel buildWorkingDirPanel() {
        WebPanel bar = new WebPanel(new VerticalFlowLayout(10));
        bar.setMargin(5, 10, 10, 10);

        GroupPanel gp1 = new GroupPanel(10, true);
        gp1.add(new WebLabel("Chose working directory:"));
        gp1.setMargin(5, 0, 5, 0);

        final WebButton directoryChooserButton = new WebButton("Choose any directory...");
        directoryChooserButton.setIcon(FileUtils.getFileIcon(choosedDir));
        directoryChooserButton.setText(FileUtils.getDisplayFileName(choosedDir));

        directoryChooserButton.addActionListener(new ActionListener() {
            private WebDirectoryChooser directoryChooser = null;

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (directoryChooser == null) {
                    directoryChooser = new WebDirectoryChooser(parent);
                    directoryChooser.setSelectedDirectory(choosedDir);
                }
                directoryChooser.setVisible(true);

                if (directoryChooser.getResult() == DialogOptions.OK_OPTION) {
                    choosedDir = directoryChooser.getSelectedDirectory();
                    directoryChooserButton.setIcon(FileUtils.getFileIcon(choosedDir));
                    directoryChooserButton.setText(FileUtils.getDisplayFileName(choosedDir));
                }
            }
        });
        gp1.add(directoryChooserButton);

        bar.add(gp1);

        return bar;
    }


}
