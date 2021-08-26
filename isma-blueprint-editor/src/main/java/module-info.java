module isma.isma.blueprint.editor.main {
    requires kotlin.stdlib;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires isma.isma.text.editor.main;
    requires org.fxmisc.richtext;

    exports ru.isma.next.editor.blueprint;
    exports ru.isma.next.editor.blueprint.services;
}