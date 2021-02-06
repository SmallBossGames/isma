package ru.nstu.isma.app.ui.editors;

import ru.nstu.isma.core.hsm.HSM;

/**
 * Created by Bessonov Alex on 17.07.2016.
 */
public interface IsmaEditor {

    HSM getModel();

    void validateModel();

    void copy();

    void cut();

    void paste();

}
