module isma.isma.hsm.main {
    exports ru.nstu.isma.core.hsm.exp;
    exports ru.nstu.isma.core.hsm.models;
    exports ru.nstu.isma.core.hsm.var;
    exports ru.nstu.isma.core.hsm.var.pde;
    exports ru.nstu.isma.core.hsm.service;
    exports ru.nstu.isma.core.hsm;
    exports ru.nstu.isma.core.hsm.hybrid;
    exports ru.nstu.isma.core.hsm.linear;
    exports ru.nstu.isma.core.hsm.events;
    exports common;

    requires java.compiler;
    requires kotlin.stdlib;
    requires org.jetbrains.annotations;
    requires org.apache.commons.lang3;
    requires isma.isma.next.tools.main;
    requires org.slf4j;

}