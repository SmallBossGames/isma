import ru.nstu.isma.intg.lib.IntgMethodLibraryLoader
import tornadofx.App
import tornadofx.launch

class IsmaApp: App(MainView::class){
    init {
        IntgMethodLibraryLoader().load();
    }
}