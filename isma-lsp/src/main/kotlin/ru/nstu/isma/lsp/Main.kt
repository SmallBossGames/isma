package ru.nstu.isma.lsp

import ru.nstu.isma.lsp.lsp.LismaLanguageService
import ru.nstu.isma.lsp.protocol.LspServer

/**
 * LISMA language server entry point.
 *
 * Speaks the Language Server Protocol (JSON-RPC 2.0, Content-Length framing)
 * over stdio. stdout is reserved for the protocol; all diagnostics go to
 * stderr.
 */
fun main() {
    LspServer(System.`in`, System.out, LismaLanguageService()).run()
}
