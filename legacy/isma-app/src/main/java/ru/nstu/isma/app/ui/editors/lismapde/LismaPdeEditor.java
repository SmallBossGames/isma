package ru.nstu.isma.app.ui.editors.lismapde;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.table.renderers.WebTableCellRenderer;
import com.alee.utils.swing.WebDefaultCellEditor;
import error.IsmaError;
import error.IsmaErrorList;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.springframework.util.Assert;
import ru.nstu.isma.app.env.LismaPdeTranslator;
import ru.nstu.isma.app.env.project.IsmaProject;
import ru.nstu.isma.app.ui.common.i18n.I18nUtils;
import ru.nstu.isma.app.ui.editors.IsmaEditor;
import ru.nstu.isma.app.ui.windows.mainwindow.workbench.WorkbenchController;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.sim.fdm.FDMNewConverter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;

import static com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT;

/**
 * Created by Bessonov Alex on 17.07.2016.
 */
public class LismaPdeEditor extends WebPanel implements IsmaEditor {
    private LismaPdeTranslator translator;

    private RSyntaxTextArea codeArea;

    private WebTable errorTable;

    private LismaTableModel lismaTableModel = new LismaTableModel();

    private WorkbenchController workbenchController;

    private IsmaProject project;

    private IsmaErrorList errors = new IsmaErrorList();

    public LismaPdeEditor(LismaPdeTranslator translator, IsmaProject project, WorkbenchController workbenchController) {
        this.translator = translator;
        this.workbenchController = workbenchController;
        this.project = project;
        init();
    }

    @Override
    public HSM getModel() {
        Assert.notNull(translator);

        errors.clear();

        String code = codeArea.getText();

        HSM model = translator.tanslateLisma(code);

        if (model.isPDE())
            model = new FDMNewConverter(model).convert();


        errors.addAll(translator.getLastTranslatedModelErrors());


        lismaTableModel.fireTableDataChanged();

        if (errors.size() > 0)
            return null;

        return model;
    }

    @Override
    public void validateModel() {
        Assert.notNull(translator);

        errors.clear();

        String code = codeArea.getText();

        translator.tanslateLisma(code);

        errors.addAll(translator.getLastTranslatedModelErrors());

        lismaTableModel.fireTableDataChanged();
    }

    @Override
    public void copy() {
        codeArea.copy();
    }

    @Override
    public void cut() {
        codeArea.cut();
    }

    @Override
    public void paste() {
        codeArea.paste();
    }

    public void init() {
        JSplitPane splitPane = new JSplitPane(VERTICAL_SPLIT, buildCodePanel(), bottomPanel());
        splitPane.setOneTouchExpandable(true);
        splitPane.setPreferredSize(new Dimension(250, 200));
        splitPane.setResizeWeight(0.8);
        splitPane.setContinuousLayout(false);
        add(splitPane);

        codeArea.setText(project.getModelData());
        validateModel();
    }


    private Component bottomPanel() {
        WebPanel panel = new WebPanel();

        WebTabbedPane tabbedPane1 = new WebTabbedPane();
        tabbedPane1.setTabPlacement(WebTabbedPane.TOP);
        tabbedPane1.setTabbedPaneStyle(TabbedPaneStyle.attached);
        setupTabbedPane(tabbedPane1);

        panel.add(tabbedPane1);

        return panel;

    }

    private void setupTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.addTab(I18nUtils.getMessage("editors.lismapde.errors"), getErrorTable());
        tabbedPane.addTab(I18nUtils.getMessage("editors.lismapde.tools"), new WebLabel());
//        tabbedPane.setEnabledAt(1, false);
    }

    private JPanel buildCodePanel() {
        JPanel codePanel = new WebPanel();
        codeArea = buildRSyntaxTextArea(codePanel);
        codeArea.setFont(new Font("Consolas", Font.TRUETYPE_FONT, 14));

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/myLanguage", "ru.nstu.isma.app.ui.common.rsyntaxtextarea.LismaTokenMaker");
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
                workbenchController.currentProject().setModelData(codeArea.getText());
            }
        });
        return codePanel;
    }

    protected RSyntaxTextArea buildRSyntaxTextArea(JPanel panel) {
        RSyntaxTextArea textArea = new RSyntaxTextArea(6, 6);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

        RTextScrollPane sp = new RTextScrollPane(textArea);
        sp.setFoldIndicatorEnabled(true);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        applyEditorTheme(textArea);

        panel.add(sp);

        return textArea;
    }

    public void applyEditorTheme(RSyntaxTextArea textArea) {
        try {
            String fileName = "idea.xml";

            InputStream is = this.getClass().getResourceAsStream("/editor_color_schema/" + fileName);

            Theme theme = Theme.load(is);
            theme.apply(textArea);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLismaPdeModel() {
        return codeArea.getText();
    }


    private Component getErrorTable() {
        // Table
        errorTable = new WebTable(lismaTableModel);
        WebScrollPane scrollPane = new WebScrollPane(errorTable);

        // Better column sizes
        initColumnSizes(errorTable);

        return scrollPane;
    }


    private void initColumnSizes(JTable table) {
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(100);

        column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(1000);

    }


    public class LismaTableModel extends AbstractTableModel {

        private String[] columnNames = {I18nUtils.getMessage("editors.lismapde.position"), I18nUtils.getMessage("editors.lismapde.message")};
        private String[][] emptyData = {{"", "The model is correct. There is no errors in the model."}};


        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return errors.size() != 0 ? errors.size() : 1;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {

            if (errors.size() == 0)
                return emptyData[row][col];

            IsmaError e = errors.get(row);

            if (e == null)
                return null;

            switch (col) {
                case 0:
                    return e.getRow() + ":" + e.getCol();
                case 1:
                    return e.getMsg();
                default:
                    return null;
            }
        }

        @Override
        public Class getColumnClass(int c) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col >= 1;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            throw new RuntimeException();
        }
    }

}
