module isma.isma.intg.api.main {
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core;
    requires isma.compiler.hsm.jvm.calcmodel;
    requires org.slf4j;
    requires org.apache.commons.lang3;

    exports ru.nstu.isma.intg.api.methods;
    exports ru.nstu.isma.intg.api.solvers;
    exports ru.nstu.isma.intg.api.models;
    exports ru.nstu.isma.intg.api.calcmodel.cauchy;
    exports ru.nstu.isma.intg.api.providers;
    exports ru.nstu.isma.intg.api.utilities;
}