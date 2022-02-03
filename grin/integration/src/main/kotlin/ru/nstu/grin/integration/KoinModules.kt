package ru.nstu.grin.integration

import org.koin.dsl.module
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView

val grinModule = module {
    factory { params -> ConcatenationView(params.getOrNull() ?: tornadofx.Scope(), params.getOrNull()) }
    single { GrinIntegrationFacade() }
}