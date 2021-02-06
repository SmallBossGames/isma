package ru.nstu.isma.app.ui.windows.simulation;

import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import ru.nstu.isma.app.env.IsmaEnvironment;
import ru.nstu.isma.app.ui.common.rsyntaxtextarea.TitledBorderPanel;
import ru.nstu.isma.app.util.Consts;

import java.awt.*;

/**
 * Created by Bessonov Alex
 * on 30.07.2016.
 */
public class SimulationConfigurationPanel extends TitledBorderPanel {

    private IsmaEnvironment env;

    private SimulationWindowController controller;

    public SimulationConfigurationPanel(IsmaEnvironment env, SimulationWindowController controller) {
//        super(new BorderLayout());

        super("Configurations", Consts.PANELS_ROUND);

        setPreferredSize(new Dimension(225, 250));

        setLayout(new BorderLayout());

        this.env = env;
        this.controller = controller;

        add(confManagerToolbarPanel(), BorderLayout.NORTH);

        WebList editableList = new WebList(createSampleData());
        editableList.setVisibleRowCount(4);
        editableList.setSelectedIndex(0);
        editableList.setEditable(true);

        add(new WebScrollPane(editableList), BorderLayout.CENTER);
    }


    private static String[] createSampleData() {
        return new String[]{"Editable element 1", "Editable element 2", "Editable element 3", "Editable element 4", "Editable element 5",
                "Editable element 6"};
    }


    private WebPanel confManagerToolbarPanel() {
        WebPanel buttonBar = new WebPanel(new HorizontalFlowLayout(10));

        buttonBar.setMargin(0, 0, 5, 0);

        buttonBar.add(new WebButton("add", env.loadIcon("new.png")));

        buttonBar.add(new WebButton("remove", env.loadIcon("minus.png")));

        return buttonBar;
    }


}
