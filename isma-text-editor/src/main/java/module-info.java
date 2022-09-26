module isma.isma.text.editor.main {
    requires kotlin.stdlib;
    requires isma.isma.lisma.main;
    requires org.fxmisc.richtext;
    requires org.antlr.antlr4.runtime;
    requires javafx.base;
    requires kotlinx.coroutines.core.jvm;
    requires kotlinx.coroutines.javafx;

    exports ru.isma.next.editor.text;
    exports ru.isma.next.editor.text.services;
    exports ru.isma.next.editor.text.services.contracts;
}