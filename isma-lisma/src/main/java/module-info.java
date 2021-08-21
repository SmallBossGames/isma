module isma.isma.lisma.main {
    requires org.antlr.antlr4.runtime;
    requires isma.isma.hsm.main;

    exports ru.nstu.isma.lisma.analysis.gen;
    exports ru.nstu.isma.lisma.analysis.parser;
    exports ru.nstu.isma.lisma.service;
}