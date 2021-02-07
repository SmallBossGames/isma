package launcher

import IsmaApp
import javafx.stage.Stage
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import tornadofx.*
import kotlin.reflect.KClass

fun main(args: Array<String>) {
    launch<IsmaApp>(args = args)
}