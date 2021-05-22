import tornadofx.App
import tornadofx.DIContainer
import tornadofx.FX
import kotlin.reflect.KClass

class IsmaApp: App(MainView::class){
    init {
        val koinContainer = ismaKoinStart()
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>): T {
                return koinContainer.koin.get<T>(type)
            }
        }
    }
}