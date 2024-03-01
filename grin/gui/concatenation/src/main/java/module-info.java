module isma.grin.gui.concatenation.main {
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;
    requires kotlinx.serialization.json;
    requires isma.isma.javafx.extensions.main;
    requires javafx.graphics;
    requires javafx.controls;
    requires transitive isma.grin.gui.common.main;
    requires isma.grin.math.main;
    requires tornadofx;
    requires koin.core.jvm;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires jwave;

    opens ru.nstu.grin.concatenation.canvas.model.project to kotlinx.serialization.core;

    exports ru.nstu.grin.concatenation.axis.controller;
    exports ru.nstu.grin.concatenation.axis.view;
    exports ru.nstu.grin.concatenation.axis.model;
    exports ru.nstu.grin.concatenation.canvas.model;
    exports ru.nstu.grin.concatenation.canvas.view;
    exports ru.nstu.grin.concatenation.cartesian.model;
    exports ru.nstu.grin.concatenation.function.controller;
    exports ru.nstu.grin.concatenation.function.model;
    exports ru.nstu.grin.concatenation.function.view;
    exports ru.nstu.grin.concatenation.koin;
    exports ru.nstu.grin.concatenation.axis.service;
    exports ru.nstu.grin.concatenation.canvas.controller;
    exports ru.nstu.grin.concatenation.canvas.handlers;
    exports ru.nstu.grin.concatenation.cartesian.controller;
    exports ru.nstu.grin.concatenation.cartesian.service;
    exports ru.nstu.grin.concatenation.cartesian.view;
    exports ru.nstu.grin.concatenation.description.controller;
    exports ru.nstu.grin.concatenation.description.model;
    exports ru.nstu.grin.concatenation.description.service;
    exports ru.nstu.grin.concatenation.description.view;
    exports ru.nstu.grin.concatenation.file;
    exports ru.nstu.grin.concatenation.file.options.view;
    exports ru.nstu.grin.concatenation.function.service;
}