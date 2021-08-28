module isma.isma.intg.core.main {
    exports ru.nstu.isma.intg.core.methods;
    exports ru.nstu.isma.intg.core.methods.utils;
    exports ru.nstu.isma.intg.core.solvers;

    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires isma.isma.intg.api.main;
    requires org.jetbrains.annotations;
}