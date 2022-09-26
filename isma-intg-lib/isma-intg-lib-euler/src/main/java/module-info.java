import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.lib.euler.EulerIntgMethod;

module isma.isma.intg.lib.isma.intg.lib.euler.main {
    requires isma.isma.intg.api.main;

    exports ru.nstu.isma.intg.lib.euler;

    provides IntgMethod with EulerIntgMethod;
}