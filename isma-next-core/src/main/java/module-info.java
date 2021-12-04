module isma.isma.next.core.main {
    exports ru.nstu.isma.next.core.sim.controller.models;
    exports ru.nstu.isma.next.core.sim.controller.services.controllers;
    exports ru.nstu.isma.next.core.sim.controller.services.eventDetection;
    exports ru.nstu.isma.next.core.sim.controller.services.hsm;
    exports ru.nstu.isma.next.core.sim.controller.services.runners;
    exports ru.nstu.isma.next.core.sim.controller.services.simulators;
    exports ru.nstu.isma.next.core.sim.controller.services.solvers;

    requires kotlin.stdlib;
    requires isma.isma.hsm.main;
    requires isma.isma.intg.api.main;
    requires transitive isma.isma.next.core.simulation.gen.main;
    requires kotlinx.coroutines.core.jvm;
    requires transitive isma.isma.intg.core.main;
    requires transitive isma.isma.intg.server.isma.intg.server.client.main;
    requires org.apache.commons.text;
    requires com.google.common;
    requires java.compiler;
    requires isma.isma.next.tools.main;
    requires org.slf4j;
}