module isma.isma.next.core.simulation.gen.main {
    exports ru.nstu.isma.next.core.simulation.gen;

    requires kotlin.stdlib;
    requires com.google.common;
    requires transitive isma.isma.hsm.main;
    requires isma.isma.intg.api.main;
    requires isma.isma.next.tools.main;
    requires java.compiler;
    requires org.slf4j;
}