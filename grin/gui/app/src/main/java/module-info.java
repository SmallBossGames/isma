module isma.grin.gui.app {
    requires kotlin.stdlib;
    requires koin.core.jvm;
    requires isma.grin.gui.concatenation.main;
    requires isma.isma.intg.api.main;
    requires java.base;
    requires javafx.graphics;

    exports ru.nstu.isma.grin.launcher;
}