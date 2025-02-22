module isma.ui.grin.nested {
    requires kotlin.stdlib;
    requires javafx.graphics;
    requires koin.core.jvm;
    requires isma.grin.gui.common.main;
    requires isma.grin.gui.concatenation.main;
    requires tornadofx;

    exports ru.nstu.grin.integration;

    opens ru.nstu.grin.integration;
}