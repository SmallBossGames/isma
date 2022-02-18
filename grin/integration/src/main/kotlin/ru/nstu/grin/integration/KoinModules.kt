package ru.nstu.grin.integration

import org.koin.dsl.module
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import ru.nstu.grin.concatenation.file.CanvasProjectLoader

class MainGrinScope

val grinModule = module {
    scope<MainGrinScope> {
        scoped { tornadofx.Scope() }
        scoped { CanvasProjectLoader(get()) }
        factory { params -> ConcatenationView(get(), get(), params.getOrNull()) }
    }

    single { GrinIntegrationFacade() }
}