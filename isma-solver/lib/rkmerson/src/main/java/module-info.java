import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory;
import ru.nstu.isma.intg.lib.rungeKutta.rkMerson.IntegrationMethodFactory;

module isma.isma.intg.lib.isma.intg.lib.rkmerson.main {
    requires isma.isma.intg.api.main;
    requires isma.isma.intg.core.main;
    requires kotlin.stdlib;

    exports ru.nstu.isma.intg.lib.rungeKutta.rkMerson;

    provides IIntegrationMethodFactory with IntegrationMethodFactory;
}