package ru.nstu.isma.ui.minor.beditor.editor;

import ru.nstu.isma.core.sim.IsmaSimulator;
import ru.nstu.isma.core.common.SimulationResult;
import ru.nstu.isma.core.sim.engine0.E0SimulationContext;
import ru.nstu.isma.core.sim.engine0.E0Simulator;
import ru.nstu.isma.ui.common.AppData;
import ru.nstu.isma.ui.common.out.IsmaChart;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Bessonov Alex
 * on 05.10.2014.
 */
public class SimPanel extends JFrame {
    JTextField start = new JTextField("0");
    JTextField end  = new JTextField("1");
    JTextField step = new JTextField("0.1");

    JTextField out = new JTextField("x");

    AppData data;

    IsmaChart ismaChart;

    public SimPanel(IsmaChart ismaChart, AppData data) {
        this.ismaChart = ismaChart;
        this.data = data;
        setSize(830, 600);
            setLayout(new FlowLayout());
        init();
        centerWindowLocation();
    }

    private void init() {


        start.setColumns(10);
        end.setColumns(10);
        step.setColumns(10);
        out.setColumns(10);

        add(new JLabel("начало: ")); add(start);
        add(new JLabel("конец: ")); add(end);
        add(new JLabel("шаг: ")); add(step);
        add(new JLabel("вывод: ")); add(out);


        JButton run = new JButton("Пуск");
        run.addActionListener(e -> {
            E0SimulationContext context = new E0SimulationContext();
            context.setStep(Double.valueOf(end.getText()));
            context.setTimeStart(Double.valueOf(start.getText()));
            context.setTimeStop(Double.valueOf(end.getText()));

            context.getOut().add(out.getText()); // todo список

            IsmaSimulator sim = new E0Simulator(data.getModel(), context, data.getErrors());
            SimulationResult pointList = sim.simulate();
            ismaChart.draw(pointList);
        });

        add(run);

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
}
