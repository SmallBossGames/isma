package ru.nstu.grin.integration

import org.koin.dsl.module
import ru.nstu.grin.concatenation.grinGuiModule

val grinNestedModule = module {
    includes(grinGuiModule)
    single { GrinIntegrationFacade() }
}