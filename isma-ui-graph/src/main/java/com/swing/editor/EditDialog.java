package com.swing.editor;

import com.editor.utils.PaletteElementsSchemeData;
import com.editor.utils.parser.ParseToValues;
import com.editor.utils.parser.ValuesForParametersAndConditions;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

//Класс диалога редактирования для всех элементов. Если кто-то хочет добавить/удалить/переместить
//элементы интерфейса - желательно делать это в .form файле.
public class EditDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<ComboBoxObj> comboBox1;
    private JTable table1;
    private JTable table2;
    private JLabel imageLabel;
    private JPanel paramPanel;
    private JPanel condPanel;
    private JTextPane textPane1;

    private String currentStyle;

    public EditDialog() {
        setTitle("Редактирование элемента");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        textPane1.setVisible(false);
        textPane1.setEditable(false);
        textPane1.setText("Комменты комменты и еще раз комменты...");
        textPane1.setOpaque(false);
        textPane1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        table1.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        table2.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    private void onOK() {
        //Запись значения в граф cell.setParams

        EditorActions.EditAction.writeInGraph(table1, table2, comboBox1.getSelectedItem().toString());
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public void setComboBox(JComboBox<ComboBoxObj> newComboBox) {
        comboBox1.setModel(newComboBox.getModel());
        comboBox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                if (arg0.getStateChange() == ItemEvent.SELECTED) {
                    String selectedScheme = comboBox1.getSelectedItem().toString();
                    EditorActions.EditAction.getDefaultTableValues(selectedScheme, currentStyle, EditDialog.this);
                }
            }
        });
    }

    private void setParamsTable(ParamsAndCondsTableModel newTable) {
        table1.setModel(newTable);
    }

    private void setCondTable(ParamsAndCondsTableModel newTable) {
        table2.setModel(newTable);
    }

    public void setData(String style, String newScheme) {
        //Сохраняем какую схему выбрал пользователь в списке чтобы использовать её позже
        PaletteElementsSchemeData.current = newScheme;
        currentStyle = style;

        //Почему-то ставит картинку только через setIcon
        JLabel label1 = PaletteElementsSchemeData.getImage(style);
        imageLabel.setIcon(label1.getIcon());

        ParseToValues valueData = new ParseToValues(EditorActions.EditAction.getData());
        comboBox1.setSelectedItem(new ComboBoxObj(newScheme));

        //Если таблица не пуста добавляем её в диалог
        HashMap<Integer, ValuesForParametersAndConditions> paramMap = valueData.getHashMapForValuesParameters();
        if (paramMap.size() != 0) {
            paramPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                    "Параметры",
                    TitledBorder.CENTER,
                    TitledBorder.TOP));
            setParamsTable(new ParamsAndCondsTableModel(paramMap));
        } else
            this.remove(paramPanel);

        //Если таблица не пуста добавляем её в диалог
        HashMap<Integer, ValuesForParametersAndConditions> condMap = valueData.getHashMapForValuesConditions();
        if (condMap.size() != 0) {
            condPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                    "Начальные условия",
                    TitledBorder.CENTER,
                    TitledBorder.TOP));

            setCondTable(new ParamsAndCondsTableModel(condMap));
        } else
            this.remove(condPanel);
        pack();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1);
        imageLabel = new JLabel();
        panel1.add(imageLabel, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paramPanel = new JPanel();
        paramPanel.setLayout(new GridBagLayout());
        panel1.add(paramPanel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        table1 = new JTable();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        paramPanel.add(table1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        paramPanel.add(spacer1, gbc);
        condPanel = new JPanel();
        condPanel.setLayout(new GridBagLayout());
        panel1.add(condPanel, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        table2 = new JTable();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        condPanel.add(table2, gbc);
        textPane1 = new JTextPane();
        textPane1.setMargin(new Insets(3, 3, 3, 3));
        panel1.add(textPane1, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        comboBox1 = new JComboBox();
        panel1.add(comboBox1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 5, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }


    class ParamsAndCondsTableModel implements TableModel {

        //Здесь хранятся данные модели
        private HashMap<Integer, ValuesForParametersAndConditions> data = null;

        //Список слушателей событий
        private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();

        public ParamsAndCondsTableModel(HashMap<Integer, ValuesForParametersAndConditions> newData) {
            this.data = newData;
        }


        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return PaletteElementsSchemeData.COLUMN_NAMES[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0)
                return String.class;
            else
                return Double.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ValuesForParametersAndConditions cell = data.get(rowIndex);
            if (columnIndex == 0)
                return cell.getName();
            else
                return cell.getValue();
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            ValuesForParametersAndConditions newCell = new ValuesForParametersAndConditions(data.get(rowIndex).getName(), (Double) aValue);
            data.put(rowIndex, newCell);
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            if (listeners.contains(l))
                return;
            listeners.add(l);
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            listeners.remove(l);
        }
    }

}






