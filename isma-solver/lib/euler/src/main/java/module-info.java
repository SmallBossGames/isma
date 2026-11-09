import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory;
import ru.nstu.isma.intg.lib.euler.IntegrationMethodFactory;

module isma.isma.intg.lib.isma.intg.lib.euler.main {
    requires isma.isma.intg.api.main;
    requires kotlin.stdlib;

    exports ru.nstu.isma.intg.lib.euler;

    provides IIntegrationMethodFactory with IntegrationMethodFactory;
}