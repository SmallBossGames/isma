module isma.compiler.hsm.jvm {
    exports ru.nstu.isma.compiler.hsm.jvm;

    requires kotlin.stdlib;
    requires com.google.common;
    requires transitive isma.isma.hsm.main;
    requires isma.isma.intg.api.main;
    requires isma.isma.next.tools.main;
    requires java.compiler;
    requires org.slf4j;
}