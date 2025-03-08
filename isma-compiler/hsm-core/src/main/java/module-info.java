module isma.compiler.hsm.core {
    exports ru.nstu.isma.compiler.hsm.core.exp;
    exports ru.nstu.isma.compiler.hsm.core.models;
    exports ru.nstu.isma.compiler.hsm.core.var;
    exports ru.nstu.isma.compiler.hsm.core.var.pde;
    exports ru.nstu.isma.compiler.hsm.core.service;
    exports ru.nstu.isma.compiler.hsm.core;
    exports ru.nstu.isma.compiler.hsm.core.hybrid;
    exports ru.nstu.isma.compiler.hsm.core.linear;
    exports ru.nstu.isma.compiler.hsm.core.events;
    exports common;

    requires java.compiler;
    requires kotlin.stdlib;
    requires org.jetbrains.annotations;
    requires org.apache.commons.lang3;
    requires isma.isma.next.tools.main;
    requires org.slf4j;
}