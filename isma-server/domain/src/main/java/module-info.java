module isma.server.domain {
    requires kotlin.stdlib;
    requires koin.core.jvm;
    requires isma.compiler.hsm.core;

    exports ru.nstu.isma.domain;
    exports ru.nstu.isma.domain.compiler;
    exports ru.nstu.isma.domain.handlers.cancelSimulation;
    exports ru.nstu.isma.domain.handlers.compileLisma;
    exports ru.nstu.isma.domain.handlers.deleteCompiledModel;
    exports ru.nstu.isma.domain.handlers.getSimulationResult;
    exports ru.nstu.isma.domain.handlers.highlightLisma;
    exports ru.nstu.isma.domain.handlers.listSimulationMethods;
    exports ru.nstu.isma.domain.handlers.monitorSimulation;
    exports ru.nstu.isma.domain.handlers.runSimulation;
    exports ru.nstu.isma.domain.handlers.validateLisma;
    exports ru.nstu.isma.domain.integration;
    exports ru.nstu.isma.domain.simulation;
}
