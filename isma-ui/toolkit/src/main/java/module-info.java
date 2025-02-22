module isma.ui.toolkit {
    requires javafx.controls;
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core;

    exports ru.isma.javafx.extensions.controls;
    exports ru.isma.javafx.extensions.coroutines.flow;
    exports ru.isma.javafx.extensions.helpers;
}