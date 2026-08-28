module isma.compiler.hsm.jvm {
    exports ru.nstu.isma.compiler.hsm.jvm;

    requires kotlin.stdlib;
    requires com.google.common;
    requires transitive isma.compiler.hsm.core;
    requires java.compiler;
    requires org.slf4j;
    requires isma.compiler.hsm.jvm.calcmodel;
}