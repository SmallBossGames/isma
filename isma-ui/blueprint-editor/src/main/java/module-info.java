module isma.ui.editor.blueprint {
    requires kotlin.stdlib;
    requires kotlinx.serialization.core;
    requires kotlinx.serialization.json;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;

    exports ru.isma.next.editor.blueprint;
    exports ru.isma.next.editor.blueprint.constants;
    exports ru.isma.next.editor.blueprint.controls;
    exports ru.isma.next.editor.blueprint.models;
    exports ru.isma.next.editor.blueprint.services;
    exports ru.isma.next.editor.blueprint.utilities;
    exports ru.isma.next.editor.blueprint.views;
}
