module isma.isma.next.app.main {
    requires org.kordamp.ikonli.javafx;
    requires kotlin.stdlib;
    requires javafx.controls;
    requires tornadofx;
    requires koin.core.jvm;
    requires kotlinx.serialization.json;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;
    requires isma.isma.blueprint.editor.main;
    requires isma.isma.text.editor.main;
    requires isma.isma.next.core.simulation.gen.main;
    requires isma.isma.intg.api.main;
    requires isma.isma.next.services.simulation.abstractions.main;
    requires isma.isma.next.integration.library.main;
    requires isma.isma.next.core.main;
    requires isma.isma.lisma.main;
    requires isma.isma.next.common.services.main;
    requires isma.isma.next.core.fdm.main;
    requires isma.isma.javafx.extensions.main;
    requires isma.grin.integration.main;
    requires org.controlsfx.controls;

    exports ru.isma.next.app.launcher;

    opens ru.isma.next.app.models.preferences to kotlinx.serialization.core;
    opens ru.isma.next.app.models to javafx.base;
    opens ru.isma.next.app.launcher;
}