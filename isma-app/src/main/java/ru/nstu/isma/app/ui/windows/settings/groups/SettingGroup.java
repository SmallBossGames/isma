package ru.nstu.isma.app.ui.windows.settings.groups;

import java.awt.*;

/**
 * Created by Bessonov Alex
 * on 23.08.2016.
 */
public interface SettingGroup {
    String getGroupName();

    void saveGroup();

    Component getPanel();
}
