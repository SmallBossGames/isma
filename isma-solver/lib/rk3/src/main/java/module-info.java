import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory;
import ru.nstu.isma.intg.lib.rungeKutta.rk3.IntegrationMethodFactory;

module isma.isma.intg.lib.isma.intg.lib.rk3.main {
    requires isma.isma.intg.api.main;
    requires isma.isma.intg.core.main;
    requires kotlin.stdlib;

    exports ru.nstu.isma.intg.lib.rungeKutta.rk3;

    provides IIntegrationMethodFactory with IntegrationMethodFactory;

}