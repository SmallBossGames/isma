package ru.nstu.isma.app.ui.windows.simulation;

import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import ru.nstu.isma.app.env.IsmaEnvironment;
import ru.nstu.isma.app.ui.common.i18n.I18ChangedListener;
import ru.nstu.isma.app.ui.common.i18n.I18nUtils;
import ru.nstu.isma.app.util.UiUtils;
import ru.nstu.isma.core.hsm.HSM;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by Bessonov Alex on 22.07.2016.
 */

public class ResultViewWindow extends WebDialog implements I18ChangedListener {
    private HSM model;

    private SimulationTask task;

    private boolean simplify;

    private boolean highQuality;

    private double tolerance;

    private LinkedList<String> toShow = new LinkedList<>();

    public ResultViewWindow(HSM model, SimulationTask task, boolean simplify, boolean highQuality, double tolerance) {
        this.model = model;
        this.task = task;

        this.simplify = simplify;
        this.highQuality = highQuality;
        this.tolerance = tolerance;

        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(mainPanel(), BorderLayout.CENTER);


        setTitle(I18nUtils.getMessage("resultForm.title"));
        pack();
//        setSize(900, 650);
        setSize(300, 650);
        setResizable(false);
        UiUtils.centerWindowLocation(this);
        setModal(true);
//        env.addI18NChangedListener(this);
    }

    private WebPanel mainPanel() {
        WebPanel panel = new WebPanel(new BorderLayout(10, 10));

        panel.add(contentConfPanel(), BorderLayout.CENTER);
        panel.add(buildButtonsPanel(), BorderLayout.SOUTH);
        return panel;
    }


    private WebPanel contentConfPanel() {
        WebPanel lists = new WebPanel(new BorderLayout(10, 10));

        WebPanel confPanel = new WebPanel(new BorderLayout());

        confPanel.setMargin(5, 10, 10, 10);
        confPanel.add(buildToShow(), BorderLayout.CENTER);
        return confPanel;
    }

    private JComponent buildToShow() {
        JScrollPane panel = new JScrollPane();
        panel.setAutoscrolls(true);
        panel.getVerticalScrollBar().setUnitIncrement(16);
        panel.setLayout(new ScrollPaneLayout());
        panel.setBorder(BorderFactory.createTitledBorder(ru.nstu.isma.ui.i18n.I18nUtils.getMessage("simulationForm.variablesToShow")));
        panel.setPreferredSize(new Dimension(250, 500));

        JPanel pp = new JPanel();
        pp.setLayout(new BoxLayout(pp, BoxLayout.PAGE_AXIS));
        panel.getViewport().add(pp);

        pp.add(new Label("=[ODE]="));
        model.getVariableTable().getOdes().stream()
                .sorted((a, b) -> a.getCode().compareTo(b.getCode()))
                .forEach(ode -> pp.add(makeCheckBox(ode.getCode())));

        pp.add(new Label("=[ALG]="));
        model.getVariableTable().getAlgs().stream()
                .sorted((a, b) -> a.getCode().compareTo(b.getCode()))
                .forEach(alg -> pp.add(makeCheckBox(alg.getCode())));

        pp.add(new Label("=[LS]="));
        model.getLinearSystem().getVars().values().stream()
                .sorted((a, b) -> a.getCode().compareTo(b.getCode()))
                .forEach(v -> pp.add(makeCheckBox(v.getCode())));

        return panel;
    }

    private JCheckBox makeCheckBox(String code) {
        JCheckBox jCheckBox = new JCheckBox(code);
        jCheckBox.addActionListener(e -> {
            if (jCheckBox.isSelected()) {
                toShow.add(code);
            } else {
                toShow.remove(code);
            }
        });
        return jCheckBox;
    }

    private WebPanel buildButtonsPanel() {

        WebPanel buttonBar = new WebPanel(new HorizontalFlowLayout(10));

        WebButton ok = new WebButton("OK");
        ok.addActionListener(e1 -> {
            task.setToShow(toShow);
            task.show(simplify, highQuality, tolerance);
        });
        WebButton cancel = new WebButton("Cancel");
        cancel.addActionListener(e -> setVisible(false));
        buttonBar.add(ok);

        buttonBar.add(cancel);
        buttonBar.setMargin(0, 10, 10, 10);

        WebPanel buttonBarMain = new WebPanel(new BorderLayout());
        buttonBarMain.add(buttonBar, BorderLayout.EAST);

        return buttonBarMain;
    }

    @Override
    public void i18Changed(Locale newLocale) {

    }
}
