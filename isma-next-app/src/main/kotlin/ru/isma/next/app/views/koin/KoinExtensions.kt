package ru.isma.next.app.views.koin

import org.koin.core.KoinApplication
import org.koin.dsl.module
import ru.isma.next.editor.text.IsmaTextEditor
import ru.isma.next.editor.text.services.LismaHighlightingService
import ru.isma.next.editor.text.services.contracts.IHighlightingService

fun KoinApplication.addLismaTextEditor() {
    val module = module {
        factory { IsmaTextEditor(get(), get()) }
        single<IHighlightingService> { LismaHighlightingService() }
    }
    modules(module)
}