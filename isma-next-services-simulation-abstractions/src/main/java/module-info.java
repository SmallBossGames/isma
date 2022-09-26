module isma.isma.next.services.simulation.abstractions.main {
    requires kotlinx.serialization.json;

    exports ru.isma.next.services.simulation.abstractions.enumerables;
    exports ru.isma.next.services.simulation.abstractions.models;

    opens ru.isma.next.services.simulation.abstractions.models to kotlinx.serialization.core;
}