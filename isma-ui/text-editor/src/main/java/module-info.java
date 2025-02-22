module isma.ui.editor.text {
    requires kotlin.stdlib;
    requires isma.isma.lisma.main;
    requires org.fxmisc.richtext;
    requires org.antlr.antlr4.runtime;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;
    requires javafx.graphics;

    exports ru.isma.next.editor.text;
    exports ru.isma.next.editor.text.services;
    exports ru.isma.next.editor.text.services.contracts;
}