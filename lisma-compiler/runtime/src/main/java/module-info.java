module isma.lisma.runtime {
    exports ru.nstu.isma.lisma.runtime;

    requires transitive isma.compiler.hsm.core;
    requires transitive isma.compiler.hsm.jvm;
    requires transitive isma.compiler.hsm.jvm.calcmodel;
    requires kotlin.stdlib;
}
