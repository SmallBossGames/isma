import ru.nstu.isma.intg.lib.IntgMethodLibraryLoader
import tornadofx.App
import tornadofx.launch
import java.util.*

class MyApp: App(MyView::class){

}

fun main(args: Array<String>) {
    IntgMethodLibraryLoader().load();
    launch<MyApp>(args)
}