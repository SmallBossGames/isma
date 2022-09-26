module isma.isma.javafx.extensions.main {
    requires javafx.controls;
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core.jvm;

    exports ru.isma.javafx.extensions.controls;
    exports ru.isma.javafx.extensions.coroutines.flow;
    exports ru.isma.javafx.extensions.helpers;
}