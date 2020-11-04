package ru.nstu.isma.ui.minor.beditor.editor;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;
import ru.nstu.isma.ui.common.AppData;
import ru.nstu.isma.ui.common.OutputAware;
import ru.nstu.isma.ui.i18n.I18ChangedListener;
import ru.nstu.isma.ui.i18n.I18nUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Bessonov Alex
 * Date: 04.12.13 Time: 2:35
 */
public class MainWindow extends JFrame implements OutputAware {

    private final MainWindowController controller;

    private JPanel codePanel;
    private RSyntaxTextArea codeArea;
    private RSyntaxTextArea outputArea;
    private JPanel outputPanel;
    private JPanel messagesPanel;
    private JSplitPane contentSplit;
    private JSplitPane horSplit;
    private JToolBar toolBar;
    private JTable errorTable;
    private AppData data;
    private Set<I18ChangedListener> i18ChangedListeners = new HashSet<>();
    private InfoPanel infoPanel;
    private MethodLibraryPanel methodLibraryPanel;

    public MainWindow(AppData appData) {
        this.controller = new MainWindowController(appData, this);

        data = appData;
        setTitle(I18nUtils.getMessage("isma"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        buildMainPanels();
        buildErrorPanel();
        toolBar = new Toolbar(this);

        MenuBuilder menuBuilder = new MenuBuilder(data, controller);
        this.addI18NChangedListener(menuBuilder);

        setJMenuBar(menuBuilder.build());
        add(toolBar, BorderLayout.PAGE_START);
        getContentPane().add(horSplit);
        pack();
        setSize(920, 600);
        centerWindowLocation();

        try {
            codeArea.setText(loadLast());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addI18NChangedListener(I18ChangedListener listener) {
        i18ChangedListeners.add(listener);
    }

    public void openAboutDialog() {
        if (infoPanel == null) {
            infoPanel = new InfoPanel();
            this.addI18NChangedListener(infoPanel);
        }
        infoPanel.setVisible(true);
    }

    public void openMethodLibraryDialog() {
        if (methodLibraryPanel == null) {
            methodLibraryPanel = new MethodLibraryPanel();
            this.addI18NChangedListener(methodLibraryPanel);
        } else {
            methodLibraryPanel.refresh();
        }
        methodLibraryPanel.setVisible(true);
    }

    private String loadLast() throws IOException {
        File file = new File("lastModel.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), "UTF-8"
                )
        );
        String line = null;
        String res = "";
        while ((line = br.readLine()) != null) {
            res += line + "\n";
        }
        br.close();

        return res;
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

    private void buildMainPanels() {
        codePanel = buildCodePanel();
        outputPanel = buildOutputPanel();
        contentSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, codePanel, outputPanel);
        contentSplit.setResizeWeight(0.5);
    }

    private void buildErrorPanel() {

        TableColumnModel tcm = new DefaultTableColumnModel();
        tcm.addColumn(new TableColumn(0));

        errorTable = new JTable(data.getErrorTableModel(), tcm);
        errorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);


        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(errorTable);
        horSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, contentSplit, tablePanel);
        horSplit.setResizeWeight(0.8);
    }

    private JPanel buildCodePanel() {
        codePanel = new JPanel(new BorderLayout());
        codeArea = buildRSyntaxTextArea(codePanel);
        codeArea.setFont( new Font("Consolas", Font.TRUETYPE_FONT, 16));

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/myLanguage", "ru.nstu.isma.ui.common.rsyntaxtextarea.LismaTokenMaker");
        codeArea.setSyntaxEditingStyle("text/myLanguage");

        codeArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                data.setText(codeArea.getText());
            }
        });
        return codePanel;
    }

    private JPanel buildOutputPanel() {
        outputPanel = new JPanel(new BorderLayout());
        outputArea = buildRSyntaxTextArea(outputPanel);

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/dumpLang", "ru.nstu.isma.ui.common.rsyntaxtextarea.DumpTokenMaker");
        outputArea.setSyntaxEditingStyle("text/dumpLang");


        return outputPanel;
    }

    // GETTERS

    public RSyntaxTextArea getCodeArea() {
        return codeArea;
    }

    public RSyntaxTextArea getOutputArea() {
        return outputArea;
    }

    public AppData getData() {
        return data;
    }

    public JTable getErrorTable() {
        return errorTable;
    }



    protected RSyntaxTextArea buildRSyntaxTextArea(JPanel panel) {
        RSyntaxTextArea textArea = new RSyntaxTextArea(6, 6);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        sp.setFoldIndicatorEnabled(true);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        data.applyEditorTheme(textArea);
        panel.add(sp);
        return textArea;
    }

    public void changeI18N(Locale newLocale) {
        I18nUtils.setLocale(newLocale);

        setTitle(I18nUtils.getMessage("isma"));

        i18ChangedListeners.forEach(l -> l.i18Changed(newLocale));
    }
}
