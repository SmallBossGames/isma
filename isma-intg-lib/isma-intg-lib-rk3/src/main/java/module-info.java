module isma.isma.intg.lib.isma.intg.lib.rk3.main {
    requires isma.isma.intg.api.main;
    requires isma.isma.intg.core.main;
    requires kotlin.stdlib;

    exports ru.nstu.isma.intg.lib.rungeKutta.rk3;

    provides ru.nstu.isma.intg.api.methods.IntgMethod with ru.nstu.isma.intg.lib.rungeKutta.rk3.Rk3IntgMethod;
}