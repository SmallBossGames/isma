package ru.nstu.isma.ui.minor.beditor.editor;

import ru.nstu.isma.core.common.SimulationResult;
import ru.nstu.isma.core.sim.IsmaSimulator;
import ru.nstu.isma.core.sim.controller.Controller;
import ru.nstu.isma.core.sim.controller.HybridSystemIntgResult;
import ru.nstu.isma.core.sim.engine0.E0SimulationContext;
import ru.nstu.isma.core.sim.engine0.E0Simulator;
import ru.nstu.isma.core.sim.fdm.FDMNewConverter;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.lib.rungeKutta.rkMerson.RkMersonIntgMethod;
import ru.nstu.isma.ui.common.AppData;
import ru.nstu.isma.ui.common.SimulationForm;
import ru.nstu.isma.ui.common.out.IsmaChart;
import ru.nstu.isma.ui.i18n.I18nUtils;
import ru.nstu.isma.ui.minor.beditor.PrintResultService;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Bessonov Alex
 * on 04.10.2014.
 */

public class Toolbar extends JToolBar {
    private MainWindow mw;

    private AppData data;

    private IsmaChart ismaChart;

    private JButton dumpButton;
    private JButton runButton;
    private JButton toIsma2007Button;
    private JButton fdmButton;

    public Toolbar(MainWindow mainWindow) {
        this.mw = mainWindow;
        this.data = mw.getData();
        init();
    }

    private void init() {
//        fillButton();
//        fillButton2();
//        add(new Separator());
//        runButton();
//        run2Button();
        run3Button();
        add(new Separator());
        dumpButton();
        to2007();
        add(new Separator());
        fdmButton();

        add(new Separator());
        JButton localeButton = createLocaleButton();
        add(localeButton);
    }

    private void fillButton() {
        JButton fillButton = new JButton("1. Произвольная модель");
        add(fillButton);

        fillButton.addActionListener(e -> mw.getCodeArea().setText("// Константы\n" +
                "const a =s=t= 1 + 1;\n" +
                "const L = G = 2;\n" +
                "\n" +
                "// Переменные, подлежащие разностной аппроксимации\n" +
                "var h[0, 20] apx 60;\n" +
                "\n" +
                "// Алгебраические уравнения\n" +
                "N = x*2 - x*x*y;\n" +
                "// Система ДУЧП \n" +
                "x' = 2;\n" +
                "y' = 0;\n" +
                "HL' = -a*N+L*D(HL, h);\n" +
                "HG' = a*N - G*D(HL, h);\n" +
                "\n" +
                "// Краевые условия\n" +
                "edge HL=0 on h both;\n" +
                "edge HG=0 on h both;\n" +
                "\n" +
                "// Начальные условия \n" +
                "x(0) = 50;\n" +
                "y(0) = 30;\n" +
                "HL(0) = 0;\n" +
                "HG(0) = 0;\n" +
                "\n" +
                "state st1 (x>4) {\n" +
                "\tZZ' = 3;\n" +
                "} from init; "));
    }

    private void fillButton2() {
        JButton fillButton1 = new JButton("2. Модель конкуренции Лотки-Вольтерра");
        add(fillButton1);

        fillButton1.addActionListener(e -> mw.getCodeArea().setText("// Константы\n" +
                "const d1 = 0.05;\n" +
                "const d2 = 1.0;\n" +
                " \n" +
                "const a12 = 0.1;\n" +
                "const a21 = 100;\n" +
                "\n" +
                "const b1 = 1;\n" +
                "const b2 = 1000;\n" +
                "\n" +
                "// Переменные, подлежащие разностной аппроксимации\n" +
                "var x[0, 1] apx 6;\n" +
                "var z[0, 1] apx 6;\n" +
                "\n" +
                "// Система ДУЧП \n" +
                "c1 '= d1*(D(c1, x, 2) + D(c1, z, 2)) + (b1 - a12);\n" +
                "c2 '= d2*(D(c2, x, 2) + D(c2, z, 2)) + (a21 - b2);\n" +
                "\n" +
                "// Краевые условия\n" +
                "edge c1=0 on x both;\n" +
                "edge c2=0 on x both;\n" +
                "edge c1=0 on z both;\n" +
                "edge c2=0 on z both;\n" +
                "\n" +
                "// Начальные условия \n" +
                "c1(0) = 10;\n" +
                "c2(0) = 17;\n"));
    }

    private void fdmButton() {
        fdmButton = new JButton();
        fdmButton.setText(I18nUtils.getMessage("toolbar.fdm"));
        add(fdmButton);


        fdmButton.addActionListener(e -> {


            data.setText(mw.getCodeArea().getText());
            data.text2hsm();

            FDMNewConverter converter = new FDMNewConverter(data.getModel());
            data.setModel(converter.convert());

            mw.getOutputArea().setText(data.getDump());
            mw.getErrorTable().repaint();
            repaint();
        });
    }

    private void dumpButton() {
        dumpButton = new JButton();
        dumpButton.setText(I18nUtils.getMessage("toolbar.dump"));
        add(dumpButton);

        dumpButton.addActionListener(e -> {
            data.setText(mw.getCodeArea().getText());
            data.text2hsm();
            mw.getOutputArea().setText(data.getDump());
            mw.getErrorTable().repaint();
            repaint();
        });
    }

    private void runButton() {
        JButton grinButton = new JButton("RUN");
        add(grinButton);


        grinButton.addActionListener(e -> {
            E0SimulationContext context = new E0SimulationContext();
            context.setStep(0.1);
            context.setTimeStart(0);
            context.setTimeStop(100);

            try {
                saveFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            data.setText(mw.getCodeArea().getText());
            data.text2hsm();
            data.fdm();

            if (ismaChart != null)
                ismaChart.setVisible(false);
            ismaChart = new IsmaChart(mw);
            IsmaSimulator sim = new E0Simulator(data.getModel(), context, data.getErrors());
            SimulationResult pointList = sim.simulate();
            ismaChart.draw(pointList);
        });
    }

    private void run2Button() {
        JButton grinButton = new JButton("RUN2");
        add(grinButton);

        grinButton.addActionListener(e -> {

            try {
                saveFile();
                data.setText(mw.getCodeArea().getText());
                data.text2hsm();
                // data.fdm();

                if (ismaChart != null)
                    ismaChart.setVisible(false);

                ismaChart = new IsmaChart(mw);

                CauchyInitials cauchyInitials = new CauchyInitials();
                cauchyInitials.setStart(0);
                cauchyInitials.setEnd(20);
                cauchyInitials.setStepSize(0.1);
                IntgMethod intgMethod = new RkMersonIntgMethod();

                Controller isma = new Controller(data.getModel(), cauchyInitials, intgMethod);
                HybridSystemIntgResult result = isma.simulate();
                // TODO
//                ismaChart.show(result);
                PrintResultService.print("out.txt", result);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void run3Button() {
        runButton = new JButton();
        runButton.setText(I18nUtils.getMessage("toolbar.run"));
        add(runButton);

        runButton.addActionListener(e -> {
            try {
                saveFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            data.setText(mw.getCodeArea().getText());
            data.text2hsm();
            if (ismaChart != null)
                ismaChart.setVisible(false);

            SimulationForm settingsForm = new SimulationForm(data, mw);
            settingsForm.setVisible(true);
        });
    }

    private void to2007() {
        toIsma2007Button = new JButton();
        toIsma2007Button.setText(I18nUtils.getMessage("toolbar.isma2007"));
        add(toIsma2007Button);

        toIsma2007Button.addActionListener(e -> {
            data.setText(mw.getCodeArea().getText());
            data.text2hsm();
            data.fdm();
            mw.getOutputArea().setText(data.getISMA2007());
        });
    }

    private void saveFile() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("lastModel.txt"), "UTF-8")));
        out.print(mw.getCodeArea().getText());
        out.flush();
    }

    private JButton createLocaleButton() {
        Map<String, ImageIcon> localeIcons = new HashMap<>();
        ImageIcon ruIcon = new ImageIcon(getClass().getResource("/icons/ru.png"));
        localeIcons.put(I18nUtils.RU.getLanguage(), ruIcon);
        ImageIcon enIcon = new ImageIcon(getClass().getResource("/icons/en.png"));
        localeIcons.put(I18nUtils.EN.getLanguage(), enIcon);

        JButton button = new JButton();
        button.setActionCommand(I18nUtils.DEFAULT.getLanguage());
        button.setIcon(localeIcons.get(I18nUtils.DEFAULT.getLanguage()));
        mw.changeI18N(I18nUtils.DEFAULT);
        button.addActionListener(l -> {
            String currentCommand = button.getActionCommand();
            Locale newLocale = currentCommand.equals(I18nUtils.RU.getLanguage()) ? I18nUtils.EN : I18nUtils.RU;
            mw.changeI18N(newLocale);
            button.setIcon(localeIcons.get(newLocale.getLanguage()));
            button.setActionCommand(newLocale.getLanguage());
            changeI18N();
        });
        return button;
    }

    private void changeI18N() {
        dumpButton.setText(I18nUtils.getMessage("toolbar.dump"));
        runButton.setText(I18nUtils.getMessage("toolbar.run"));
        toIsma2007Button.setText(I18nUtils.getMessage("toolbar.isma2007"));
        fdmButton.setText(I18nUtils.getMessage("toolbar.fdm"));
    }

}
