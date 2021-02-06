package ru.nstu.isma.app.ui.windows.mainwindow.statusbar;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.statusbar.WebStatusLabel;
import com.alee.laf.panel.WebPanel;
import ru.nstu.isma.app.env.IsmaEnvironment;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by Bessonov Alex on 17.07.2016.
 */

public class StatusPanel extends WebPanel {

    @Resource
    private IsmaEnvironment env;

    @PostConstruct
    public void init() {
        setPaintSides(true, false, false, false);
        WebStatusBar statusBar = new WebStatusBar();

        // Simple label
//        statusBar.add(new WebStatusLabel("Just a simple status bar", env.loadIcon("info2.png")));

        // Simple memory bar
        WebMemoryBar memoryBar = new WebMemoryBar();
        memoryBar.setShowMaximumMemory(false);
        memoryBar.setDrawBorder(false);
        memoryBar.setFillBackground(false);

        memoryBar.setPreferredWidth(memoryBar.getPreferredSize().width + 20);
        statusBar.add(memoryBar, ToolbarLayout.END);

        add(statusBar);
    }

    /**
     * GET / SET
     * */

    public IsmaEnvironment getEnvironment() {
        return env;
    }

    public void setEnvironment(IsmaEnvironment environment) {
        this.env = environment;
    }
}
