package ru.nstu.grin.integration

import org.koin.dsl.module
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import ru.nstu.grin.concatenation.file.CanvasProjectLoader

val grinModule = module {
    single { tornadofx.Scope() }
    single { CanvasProjectLoader(get()) }
    factory { params -> ConcatenationView(get(), get(), params.getOrNull()) }
    single { GrinIntegrationFacade() }
}