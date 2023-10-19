module isma.isma.blueprint.editor.main {
    requires kotlin.stdlib;
    requires org.fxmisc.richtext;
    requires kotlinx.serialization.core;
    requires javafx.graphics;
    requires javafx.controls;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;

    exports ru.isma.next.editor.blueprint;
    exports ru.isma.next.editor.blueprint.services;
    exports ru.isma.next.editor.blueprint.models;
}