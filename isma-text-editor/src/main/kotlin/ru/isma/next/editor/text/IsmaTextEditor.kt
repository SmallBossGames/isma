package ru.isma.next.editor.text

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import ru.isma.next.editor.text.services.contracts.IEditorPlatformService
import ru.isma.next.editor.text.services.contracts.IHighlightingService

class IsmaTextEditor(
    private val textEditorService: IEditorPlatformService,
    private val highlightingService: IHighlightingService,
) : CodeArea() {
    private val fxCoroutineScope = CoroutineScope(Dispatchers.JavaFx)

    init {
        style = "-fx-font-family: consolas; -fx-font-size: 12pt;"

        fxCoroutineScope.launch {
            textEditorService.cutEvent.collect{
                if (isFocused) cut()
            }
        }

        fxCoroutineScope.launch {
            textEditorService.copyEvent.collect{
                if (isFocused) copy()
            }
        }

        fxCoroutineScope.launch {
            textEditorService.copyEvent.collect{
                if (isFocused) paste()
            }
        }

        textProperty().addListener{ _, _, newValue ->
            if(paragraphGraphicFactory == null) {
                paragraphGraphicFactory = LineNumberFactory.get(this)
            }

            val highlighting = highlightingService.createHighlightingStyleSpans(newValue ?: "")

            setStyleSpans(0, highlighting)
        }
    }

    override fun dispose() {
        fxCoroutineScope.cancel()

        super.dispose()
    }
}