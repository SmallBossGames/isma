module isma.isma.intg.lib.isma.intg.lib.rk31.main {
    requires isma.isma.intg.api.main;
    requires isma.isma.intg.core.main;
    requires kotlin.stdlib;

    exports ru.nstu.isma.intg.lib.rungeKutta.rk31;

    provides ru.nstu.isma.intg.api.methods.IntgMethod with ru.nstu.isma.intg.lib.rungeKutta.rk31.Rk31IntgMethod;
}