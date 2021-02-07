package ru.nstu.isma.app.ui.windows.projectmanager;

import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.painter.TitledBorderPainter;
import com.alee.extended.panel.BorderPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.text.WebTextField;
import com.alee.utils.FileUtils;
import com.alee.utils.swing.DialogOptions;
import com.alee.utils.swing.UnselectableButtonGroup;
import org.springframework.util.Assert;
import ru.nstu.isma.app.env.project.ProjectManager;
import ru.nstu.isma.app.env.project.IsmaProject;
import ru.nstu.isma.app.env.project.IsmaProjectType;
import ru.nstu.isma.app.ui.common.i18n.I18nUtils;
import ru.nstu.isma.app.util.Consts;
import ru.nstu.isma.app.util.UiUtils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Bessonov Alex on 22.07.2016.
 */
public class NewProjectWindow extends WebDialog {

    private IsmaProject rawProject;

    private ProjectManager projectManager;

    private File choosedDir = new File(System.getProperty("user.dir") + "/model/");

    private String fileName = "unnamedModel.im";

    public NewProjectWindow(ProjectManager manager) {

        setProjectManager(manager);

        choosedDir.mkdirs();

        rawProject = IsmaProject.newProject();


        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(buildContentPanel(), BorderLayout.CENTER);

        getContentPane().add(buildButtonsPanel(), BorderLayout.SOUTH);

        setTitle(I18nUtils.getMessage("newModelForm.title"));
        pack();
        setSize(550, 370);
        setResizable(false);
        UiUtils.centerWindowLocation(this);
        setModal(true);
    }

    private WebPanel buildContentPanel() {
        WebPanel contentPanel = new WebPanel(new VerticalFlowLayout(10, 10));

        contentPanel.add(buildProjectNamePanel());
        contentPanel.add(new WebSeparator(false, true));
        contentPanel.add(buildLismaTypePanel());
        contentPanel.add(new WebSeparator(false, true));
        contentPanel.add(buildFilePathPanel());

        BorderPanel mainPanel = new BorderPanel(contentPanel);

        final TitledBorderPainter titledBorderPainter = new TitledBorderPainter(I18nUtils.getMessage("newModelForm.title2"));
        titledBorderPainter.setTitleOffset(20);
        titledBorderPainter.setRound(Consts.PANELS_ROUND);
        mainPanel.setPainter(titledBorderPainter);

        WebPanel panel = new WebPanel();
        panel.add(mainPanel);
        panel.setMargin(10);

        return panel;
    }

    private WebPanel buildButtonsPanel() {

        WebPanel buttonBar = new WebPanel(new HorizontalFlowLayout(10));

        WebButton ok = new WebButton(I18nUtils.getMessage("newModelForm.ok"));
        ok.addActionListener(e1 -> {
            try {

                projectManager.createNew(rawProject, new File(choosedDir.getPath() + File.separator + fileName));
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
            setVisible(false);
        });
        WebButton cancel = new WebButton(I18nUtils.getMessage("newModelForm.cancel"));
        cancel.addActionListener(e -> setVisible(false));
        buttonBar.add(ok);

        buttonBar.add(cancel);
        buttonBar.setMargin(0, 10, 10, 10);

        WebPanel buttonBarMain = new WebPanel(new BorderLayout());
        buttonBarMain.add(buttonBar, BorderLayout.EAST);

        return buttonBarMain;
    }

    private WebPanel buildProjectNamePanel() {
        WebPanel bar = new WebPanel(new HorizontalFlowLayout(10));
        bar.setMargin(10, 10, 5, 10);
        bar.add(new WebLabel(I18nUtils.getMessage("newModelForm.modelName")));

        WebTextField nameField = new WebTextField(I18nUtils.getMessage("newModelForm.unnamed1"));
        nameField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                rawProject.setName(nameField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                rawProject.setName(nameField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                rawProject.setName(nameField.getText());
            }
        });

        nameField.setPreferredSize(new Dimension(300, 28));
        bar.add(nameField);
        return bar;
    }

    private WebPanel buildLismaTypePanel() {
        WebPanel contentPanel = new WebPanel(new HorizontalFlowLayout(10));

        contentPanel.add(new WebLabel(I18nUtils.getMessage("newModelForm.type")));

        contentPanel.setMargin(5, 10, 5, 10);

        final WebRadioButton LISMA_PDE = new WebRadioButton("LISMA_PDE");

        LISMA_PDE.addItemListener(e -> {
            if (LISMA_PDE.isSelected())
                rawProject.setType(IsmaProjectType.LISMA_PDE);
        });

        LISMA_PDE.setSelected(true);

        final WebRadioButton LISMA_EPS = new WebRadioButton("LISMA_EPS");

        LISMA_EPS.setEnabled(false);
        LISMA_EPS.addItemListener(e -> {
            if (LISMA_EPS.isSelected())
                rawProject.setType(IsmaProjectType.LISMA_EPS);
        });

        UnselectableButtonGroup.group(LISMA_PDE, LISMA_EPS);

        contentPanel.add(new GroupPanel(4, false, LISMA_PDE, LISMA_EPS));

        return contentPanel;
    }

    private WebPanel buildFilePathPanel() {
        WebPanel bar = new WebPanel(new VerticalFlowLayout(10));
        bar.setMargin(5, 10, 10, 10);


        GroupPanel gp1 = new GroupPanel(10, true);
        gp1.add(new WebLabel(I18nUtils.getMessage("newModelForm.modelDir")));
        gp1.setMargin(5, 0, 5, 0);

        final WebButton directoryChooserButton = new WebButton("Choose any directory...");
        directoryChooserButton.setIcon(FileUtils.getFileIcon(choosedDir));
        directoryChooserButton.setText(FileUtils.getDisplayFileName(choosedDir));

        directoryChooserButton.addActionListener(new ActionListener() {
            private WebDirectoryChooser directoryChooser = null;

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (directoryChooser == null) {
                    directoryChooser = new WebDirectoryChooser(NewProjectWindow.this);
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

        GroupPanel gp2 = new GroupPanel(10, true);
        gp2.setMargin(5, 0, 5, 0);

        gp2.add(new WebLabel(I18nUtils.getMessage("newModelForm.fileName")));
        WebTextField fileNameField = new WebTextField(fileName);

        fileNameField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                fileName = fileNameField.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fileName = fileNameField.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fileName = fileNameField.getText();
            }
        });

        fileNameField.setPreferredSize(new Dimension(280, 28));
        gp2.add(fileNameField);

        bar.add(gp1);
        bar.add(gp2);

        return bar;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setProjectManager(ProjectManager projectManager) {
        Assert.notNull(projectManager);
        this.projectManager = projectManager;
    }
}
