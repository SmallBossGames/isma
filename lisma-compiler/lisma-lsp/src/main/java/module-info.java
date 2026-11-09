module isma.lsp {
    requires kotlin.stdlib;
    requires org.antlr.antlr4.runtime;
    requires kotlinx.serialization.json;
    requires kotlinx.coroutines.core;
    requires org.slf4j;
    requires isma.isma.lisma.main;

    exports ru.nstu.isma.lsp;
    exports ru.nstu.isma.lsp.protocol;
    exports ru.nstu.isma.lsp.lsp;
}
