package com.swing.editor;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.mxgraph.util.mxResources;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ErrorsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable table1;

    private static List<Integer> rowSize = new ArrayList<>();

    private Object[][] data = {
            {mxResources.get("noOutPortsInMacrosErrorTitle"), mxResources.get("noOutPortsInMacrosErrorDescr")},
            {mxResources.get("errorNoGeneratorTitle"), mxResources.get("errorNoGeneratorDescr")},
            {mxResources.get("errorElementNotConnectedTitle"), mxResources.get("errorElementNotConnectedDescr")},
            {mxResources.get("errorDirectGeneratorsConnectionTitle"), mxResources.get("errorDirectGeneratorsConnectionDescr")},
            {mxResources.get("errorInvalidCellValuesTitle"), mxResources.get("errorInvalidCellValuesDescr")},
            {mxResources.get("errorInvalidGeneratorValuesTitle"), mxResources.get("errorInvalidGeneratorValuesDescr")},
            {mxResources.get("errorInfiniteLoopTitle"), mxResources.get("errorInfiniteLoopDescr")},
            {mxResources.get("errorSchemeNotUnifiedTitle"), mxResources.get("errorSchemeNotUnifiedDescr")}
    };

    public ErrorsDialog() {
        $$$setupUI$$$();
        for (int i = 0; i < 10; i++)
            rowSize.add(i, 0);
        setContentPane(contentPane);
        setModal(true);

        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        dispose();
    }

    private String[] columnNames = {mxResources.get("error"),
            mxResources.get("description")};

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(500, 310));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 10, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setMargin(new Insets(3, 20, 3, 20));
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1.setAutoCreateColumnsFromModel(true);
        table1.setFont(new Font(table1.getFont().getName(), table1.getFont().getStyle(), table1.getFont().getSize()));
        table1.setOpaque(true);
        table1.setShowHorizontalLines(true);
        table1.setShowVerticalLines(true);
        scrollPane1.setViewportView(table1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    public class LineWrapCellRenderer extends JTextArea implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            this.setText((String) value);
            this.setWrapStyleWord(true);
            this.setLineWrap(true);

            int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
            int textLength = this.getText().length();
            int lines = textLength / 25;
            if (lines == 0) {
                lines = 1;
            }
            if (textLength % 25 != 0) {
                lines++;
            }

            int height = fontHeight * lines;
            if (rowSize.get(row) < height) {
                rowSize.set(row, height);
                table.setRowHeight(row, height);
                pack();
            }

            return this;
        }
    }

    private void createUIComponents() {
        table1 = new JTable(data, columnNames);
        table1.setEnabled(false);
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };
        table1.setModel(model);
        table1.setDefaultRenderer(String.class, new LineWrapCellRenderer());
    }

}