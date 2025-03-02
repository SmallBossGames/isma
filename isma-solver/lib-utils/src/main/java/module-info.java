module isma.isma.next.integration.library.main {
    requires kotlin.stdlib;
    requires org.slf4j;
    requires isma.isma.intg.api.main;

    exports ru.nstu.isma.next.integration.services;

    uses ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory;
}