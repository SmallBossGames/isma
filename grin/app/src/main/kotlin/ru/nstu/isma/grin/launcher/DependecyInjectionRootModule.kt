package ru.nstu.isma.grin.launcher

import org.koin.core.context.GlobalContext.startKoin
import ru.nstu.grin.concatenation.grinGuiModule


fun grinKoinStart() = startKoin {
    modules(grinGuiModule)
}