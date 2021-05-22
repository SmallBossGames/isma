package views

import org.koin.dsl.module

val viewsModule = module {
    single { IsmaMenuBar(get(), get(), get(), get()) }
}