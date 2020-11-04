package ru.nstu.isma.ui.minor.beditor.editor;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.lib.IntgMethodLibrary;
import ru.nstu.isma.intg.lib.IntgMethodLibraryLoader;
import ru.nstu.isma.ui.i18n.I18ChangedListener;
import ru.nstu.isma.ui.i18n.I18nUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Maria
 * @since 18.07.2016
 */
public class MethodLibraryPanel extends JFrame implements I18ChangedListener {
    private static final Logger logger = LoggerFactory.getLogger(MethodLibraryPanel.class);

    private JList<String> methodList;
    private JButton addMethodButton;
    //private JButton removeMethodButton;

    private final FileChooser fileChooser;

    public MethodLibraryPanel() throws HeadlessException {
        setSize(450, 300);
        setTitle(I18nUtils.getMessage("menu.settingsMethodLibraryMenuItem"));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        init();
        centerWindowLocation();
        fileChooser = new FileChooser();
    }

    public void refresh() {
        DefaultListModel<String> methodListModel = (DefaultListModel<String>) methodList.getModel();
        methodListModel.removeAllElements();
        java.util.List<String> methods = IntgMethodLibrary.getIntgMethodNames(); // todo: disable system methods for selecting and removing
        methods.forEach(methodListModel::addElement);
    }

    private void init() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(mainPanel, BorderLayout.CENTER);

        methodList = new JList<>(new DefaultListModel<>());
        refresh();
//        methodList.addListSelectionListener(e -> {
//            boolean selected = methodList.getSelectedIndex() > -1;
//            boolean registered = IntgMethodLibrary.containsIntgMethod(methodList.getSelectedValue());
//            boolean notSystem = !IntgMethodLibrary.isSystemIntgMethod(methodList.getSelectedValue());
//            removeMethodButton.setEnabled(selected && registered && notSystem);
//        });
        methodList.setCellRenderer(new MethodCellRenderer());
        mainPanel.add(methodList, BorderLayout.CENTER);

        JToolBar editMethodToolbar = new JToolBar(JToolBar.VERTICAL);
        editMethodToolbar.setFloatable(false);
        editMethodToolbar.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        Icon addIcon = new ImageIcon(this.getClass().getResource("/icons/add.png"));
        addMethodButton = new JButton(addIcon);
        addMethodButton.setEnabled(true);
        addMethodButton.addActionListener(l -> addMethod());
        editMethodToolbar.add(addMethodButton);
//        Icon removeIcon = new ImageIcon(this.getClass().getResource("/icons/remove.png"));
//        removeMethodButton = new JButton(removeIcon);
//        removeMethodButton.setEnabled(false);
//        removeMethodButton.addActionListener(l -> {
//            removeMethod();
//        });
//        editMethodToolbar.add(removeMethodButton);
        mainPanel.add(editMethodToolbar, BorderLayout.LINE_END);
    }

    private void addMethod() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                I18nUtils.getMessage("menu.file.choose.dialog.jarFiles"), "jar");
        File chosenFile = fileChooser.choose(filter);
        if (chosenFile != null) {
            copyFileToMethodLibrary(chosenFile);

            String msg = I18nUtils.getMessage("methodLibraryPanel.restart.dialog.msg");
            String title = I18nUtils.getMessage("methodLibraryPanel.add.title");
            JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /*private void removeMethod() {
        DefaultListModel<String> model = (DefaultListModel<String>) methodList.getModel();
        int selectedIndex = methodList.getSelectedIndex();
        while (selectedIndex > -1) {
            model.remove(selectedIndex);
            selectedIndex = methodList.getSelectedIndex();
        }
        removeMethodButton.setEnabled(false);

        String msg = I18nUtils.getMessage("methodLibraryPanel.restart.dialog.msg");
        String title = I18nUtils.getMessage("methodLibraryPanel.remove.title");
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }*/

    private void copyFileToMethodLibrary(File srcFile) {
        File destDir = new File(IntgMethodLibraryLoader.DFLT_DIR);

        if (!destDir.exists()) {
            boolean created = destDir.mkdir();
            if (!created) {
                logger.error("Unable to create directory for method library");
            }
        }

        File destFile = new File(destDir, srcFile.getName());
        if (destFile.exists()) {
            logger.warn("File \"" + destDir.getAbsolutePath() + srcFile.getName() + "\" is already exists and will be replaced");
        }

        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            logger.error("Failed to copy file from \"" + srcFile.getAbsolutePath() + "\" to" + destFile.getAbsolutePath() + "\"");
        }
    }

    private void centerWindowLocation() {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // Move the window
        this.setLocation(x, y);
    }

    @Override
    public void i18Changed(Locale newLocale) {
        setTitle(I18nUtils.getMessage("menu.settingsMethodLibraryMenuItem"));
    }

    private static class MethodCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String methodName = (String) value;
            boolean system = IntgMethodLibrary.containsIntgMethod(methodName) && IntgMethodLibrary.isSystemIntgMethod(methodName);
            if (system) {
                Component systemMethodCellRendererComponent = super.getListCellRendererComponent(list, value, index, false, cellHasFocus);
                systemMethodCellRendererComponent.setEnabled(false);
                return systemMethodCellRendererComponent;
            }

            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

}
