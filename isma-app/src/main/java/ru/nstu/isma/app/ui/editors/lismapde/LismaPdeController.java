package ru.nstu.isma.app.ui.editors.lismapde;

/**
 * Created by Bessonov Alex
 * on 23.08.2016.
 */
public class LismaPdeController {
    private LismaPdeEditor editor;

    public LismaPdeController(LismaPdeEditor editor) {
        this.editor = editor;
    }

    public boolean validate() {
        return false;
    }
}
