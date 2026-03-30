module isma.ui.editor.text {
    requires kotlin.stdlib;
    requires org.fxmisc.richtext;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;
    requires javafx.graphics;

    exports ru.isma.next.editor.text;
    exports ru.isma.next.editor.text.services;
    exports ru.isma.next.editor.text.services.contracts;
}