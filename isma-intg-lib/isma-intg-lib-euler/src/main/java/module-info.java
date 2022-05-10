module isma.isma.intg.lib.isma.intg.lib.euler.main {
    requires isma.isma.intg.api.main;

    exports ru.nstu.isma.intg.lib.euler;

    provides ru.nstu.isma.intg.api.methods.IntgMethod with ru.nstu.isma.intg.lib.euler.EulerIntgMethod;
}