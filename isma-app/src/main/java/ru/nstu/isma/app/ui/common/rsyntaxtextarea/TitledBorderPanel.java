package ru.nstu.isma.app.ui.common.rsyntaxtextarea;

import com.alee.extended.painter.TitledBorderPainter;
import com.alee.laf.panel.WebPanel;

/**
 * Created by Bessonov Alex
 * on 30.07.2016.
 */
public class TitledBorderPanel extends WebPanel {

    public TitledBorderPanel(String title, Integer round) {

        if (round == null)
            round = 0;

        final TitledBorderPainter titledBorderPainter = new TitledBorderPainter(title);

        titledBorderPainter.setTitleOffset(20);

        titledBorderPainter.setRound(round);

        setPainter(titledBorderPainter);

//        setMargin(0, 15, 0, 10);
        setMargin(10);
    }

}
