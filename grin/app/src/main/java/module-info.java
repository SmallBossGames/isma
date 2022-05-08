module isma.grin.app.main {
    requires kotlin.stdlib;
    requires koin.core.jvm;
    requires isma.grin.integration.main;
    requires java.base;
    requires javafx.graphics;

    exports ru.nstu.isma.grin.launcher;
}