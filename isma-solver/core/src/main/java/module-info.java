module isma.isma.intg.core.main {
    exports ru.nstu.isma.intg.core.methods;
    exports ru.nstu.isma.intg.core.solvers;
    exports ru.nstu.isma.intg.core.calcmodel;
    exports ru.nstu.isma.intg.core.providers;
    exports ru.nstu.isma.intg.core.utilities;

    requires org.jetbrains.annotations;
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core;

    requires isma.isma.intg.api.main;
    requires isma.compiler.hsm.jvm.calcmodel;
}