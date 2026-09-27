module isma.daemon.infrastructure {
    requires kotlin.stdlib;
    requires koin.core.jvm;
    requires org.slf4j;
    requires isma.daemon.domain;
    requires isma.isma.next.integration.library.main;

    exports ru.nstu.isma.daemon.infrastructure;
    exports ru.nstu.isma.daemon.infrastructure.stores.compiledModels;
    exports ru.nstu.isma.daemon.infrastructure.stores.integrationMethods;
    exports ru.nstu.isma.daemon.infrastructure.stores.simulationSessions;
    exports ru.nstu.isma.daemon.infrastructure.simulation;
    exports ru.nstu.isma.daemon.infrastructure.translation;
}
