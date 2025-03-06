module isma.ui.app {
    requires org.kordamp.ikonli.javafx;
    requires kotlin.stdlib;
    requires javafx.controls;
    requires tornadofx;
    requires koin.core.jvm;
    requires kotlinx.serialization.json;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;
    requires isma.compiler.hsm.jvm;
    requires isma.isma.intg.api.main;
    requires isma.isma.next.integration.library.main;
    requires isma.isma.next.core.main;
    requires isma.isma.lisma.main;
    requires isma.isma.next.core.fdm.main;
    requires isma.ui.editor.text;
    requires isma.ui.editor.blueprint;
    requires isma.ui.toolkit;
    requires isma.ui.grin.nested;
    requires org.controlsfx.controls;

    exports ru.isma.next.app.launcher;

    opens ru.isma.next.app.models.preferences to kotlinx.serialization.core;
    opens ru.isma.next.app.models to javafx.base;
    opens ru.isma.next.app.launcher;
}