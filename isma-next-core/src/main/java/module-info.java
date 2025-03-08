module isma.isma.next.core.main {
    exports ru.nstu.isma.next.core.sim.controller.models;
    exports ru.nstu.isma.next.core.sim.controller.services.controllers;
    exports ru.nstu.isma.next.core.sim.controller.services.eventDetection;
    exports ru.nstu.isma.next.core.sim.controller.services.hsm;
    exports ru.nstu.isma.next.core.sim.controller.services.runners;
    exports ru.nstu.isma.next.core.sim.controller.services.simulators;
    exports ru.nstu.isma.next.core.sim.controller.services.solvers;

    requires kotlin.stdlib;
    requires isma.compiler.hsm.core;
    requires isma.isma.intg.api.main;
    requires transitive isma.compiler.hsm.jvm;
    requires kotlinx.coroutines.core;
    requires transitive isma.isma.intg.core.main;
    requires com.google.common;
    requires java.compiler;
    requires org.slf4j;
}