module isma.grin.integration.main {
    requires kotlin.stdlib;
    requires javafx.graphics;
    requires koin.core.jvm;
    requires isma.grin.gui.common.main;
    requires transitive isma.grin.gui.concatenation.main;
    requires tornadofx;

    exports ru.nstu.grin.integration;
}