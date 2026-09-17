module isma.server.infrastructure {
    requires kotlin.stdlib;
    requires koin.core.jvm;
    requires org.slf4j;
    requires org.antlr.antlr4.runtime;
    requires isma.server.domain;
    requires isma.compiler.hsm.core;
    requires isma.compiler.hsm.jvm;
    requires isma.compiler.hsm.jvm.calcmodel;
    requires isma.isma.lisma.main;
    requires isma.isma.next.core.fdm.main;
    requires isma.isma.next.core.main;
    requires isma.isma.intg.api.main;
    requires isma.isma.intg.core.main;
    requires isma.isma.next.integration.library.main;
    requires isma.jvm.lib.exchange.format;

    exports ru.nstu.isma.server.infrastructure;
    exports ru.nstu.isma.server.infrastructure.stores.compiledModels;
    exports ru.nstu.isma.server.infrastructure.stores.integrationMethods;
    exports ru.nstu.isma.server.infrastructure.stores.simulationSessions;
    exports ru.nstu.isma.server.infrastructure.simulation;
    exports ru.nstu.isma.server.infrastructure.translation;
    exports ru.nstu.isma.server.infrastructure.highlight;
}
