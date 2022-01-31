package ru.nstu.grin.integration

import org.koin.dsl.module

val grinModule = module {
    single { GrinIntegrationFacade() }
}