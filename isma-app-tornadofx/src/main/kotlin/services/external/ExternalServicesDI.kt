package services.external

import org.koin.dsl.module
import ru.isma.next.editor.text.services.LismaHighlightingService
import ru.nstu.grin.integration.IntegrationController
import ru.nstu.isma.next.integration.services.IntegrationMethodLibraryLoader

val externalServicesModule = module {
    single { IntegrationController() }
    single { IntegrationMethodLibraryLoader("methods/").load() }
    single { LismaHighlightingService(get()) }
}