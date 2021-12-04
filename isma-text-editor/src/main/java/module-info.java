module isma.isma.text.editor.main {
    requires kotlin.stdlib;
    requires isma.isma.lisma.main;
    requires org.fxmisc.richtext;
    requires javafx.controls;
    requires javafx.fxml;

    exports ru.isma.next.editor.text;
    exports ru.isma.next.editor.text.services;
    exports ru.isma.next.editor.text.services.contracts;
}