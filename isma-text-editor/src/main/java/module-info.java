module isma.isma.text.editor.main {
    requires kotlin.stdlib;
    requires javafx.base;
    requires javafx.graphics;
    requires isma.isma.lisma.main;
    requires org.fxmisc.richtext;

    exports ru.isma.next.editor.text;
    exports ru.isma.next.editor.text.services;
    exports ru.isma.next.editor.text.services.contracts;
}