package ru.nstu.isma.app.ui.windows.simulation;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import ru.nstu.isma.app.env.IsmaEnvironment;
import ru.nstu.isma.app.ui.common.i18n.I18ChangedListener;
import ru.nstu.isma.app.ui.common.i18n.I18nUtils;
import ru.nstu.isma.app.util.UiUtils;
import ru.nstu.isma.core.hsm.HSM;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

/**
 * Created by Bessonov Alex on 22.07.2016.
 */

public class SimulationWindow extends WebDialog implements I18ChangedListener {

    private IsmaEnvironment env;

    private HSM model;

    private SimulationWindowController controller;

    private ConfigurationFields confParamPanel;

    private SimulationToolbar toolbar;

    public SimulationWindow(IsmaEnvironment env, HSM model) {
        this.env = env;
        this.model = model;

        controller = new SimulationWindowController(this);

        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(mainPanel(), BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.stopSim();
            }
        });

        setTitle(I18nUtils.getMessage("simulationForm.title"));
        pack();
//        setSize(900, 650);
        setSize(700, 750);
        setResizable(false);
        UiUtils.centerWindowLocation(this);
        setModal(true);
        env.addI18NChangedListener(this);
    }

    private WebPanel mainPanel() {
        WebPanel panel = new WebPanel(new BorderLayout(10, 10));
        panel.add(getToolbar(), BorderLayout.NORTH);
        panel.add(contentConfPanel(), BorderLayout.CENTER);
        return panel;
    }


    private WebPanel contentConfPanel() {
        WebPanel lists = new WebPanel(new BorderLayout(10, 10));

        // TODO
//        lists.add(new SimulationConfigurationPanel(env, controller), BorderLayout.CENTER);
//        lists.add(new ResultViewConfigurationPanel(env, controller), BorderLayout.SOUTH);

        WebPanel confPanel = new WebPanel(new BorderLayout());

        confPanel.setMargin(5, 10, 10, 10);
        confPanel.add(lists, BorderLayout.WEST);
        confPanel.add(confParamPanel(), BorderLayout.CENTER);

        return confPanel;
    }


    public ConfigurationFields confParamPanel() {
        if (confParamPanel == null)
            confParamPanel = new ConfigurationFields();
        return confParamPanel;
    }

    public SimulationToolbar getToolbar() {
        if (toolbar == null)
            toolbar = new SimulationToolbar(env, controller);
        return toolbar;

    }

    @Override
    public void i18Changed(Locale newLocale) {
        setTitle(I18nUtils.getMessage("simulationForm.title"));
    }

    public HSM getModel() {
        return model;
    }
}
