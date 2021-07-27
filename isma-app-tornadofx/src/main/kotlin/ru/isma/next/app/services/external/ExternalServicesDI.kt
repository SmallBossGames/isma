package ru.isma.next.app.services.external

import org.koin.dsl.module
import ru.isma.next.editor.text.services.TextEditorService
import ru.isma.next.editor.text.services.contracts.ITextEditorService
import ru.nstu.grin.integration.IntegrationController
import ru.nstu.isma.next.integration.services.IntegrationMethodLibraryLoader

val externalServicesModule = module {
    single { IntegrationController() }
    single { IntegrationMethodLibraryLoader("methods/").load() }
    single<ITextEditorService> { TextEditorService() }
}