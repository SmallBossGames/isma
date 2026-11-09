module isma.lisma.compiler {
    exports ru.nstu.isma.lisma.compiler;

    requires transitive isma.compiler.hsm.core;
    requires isma.isma.lisma.main;
    requires isma.compiler.hsm.fdm;
    requires isma.compiler.hsm.jvm;
    requires kotlin.stdlib;
    requires java.compiler;
}
