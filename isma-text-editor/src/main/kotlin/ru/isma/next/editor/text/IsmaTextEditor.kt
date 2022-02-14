package ru.isma.next.editor.text

import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import ru.isma.next.editor.text.services.LismaHighlightingService
import ru.isma.next.editor.text.services.contracts.IHighlightingService
import ru.isma.next.editor.text.services.contracts.ITextEditorService

class IsmaTextEditor(
    private val textEditorService: ITextEditorService,
    private val highlightingService: IHighlightingService,
) : CodeArea() {
    private val cutEvent = {if (isFocused) cut()}
    private val copyEvent = {if (isFocused) copy()}
    private val pasteEvent = {if (isFocused) paste()}

    init {
        setStyle("-fx-font-family: consolas; -fx-font-size: 12pt;");

        textEditorService.apply {
            cutEventHandler.add(cutEvent)
            copyEventHandler.add(copyEvent)
            pasteEventHandler.add(pasteEvent)
        }

        textProperty().addListener{ observable, oldValue, newValue ->
            if(paragraphGraphicFactory == null) {
                paragraphGraphicFactory = LineNumberFactory.get(this)
            }

            val highlighting = highlightingService.createHighlightingStyleSpans(newValue ?: "")

            setStyleSpans(0, highlighting)
        }
    }

    override fun dispose() {
        textEditorService.apply {
            cutEventHandler.remove(cutEvent)
            copyEventHandler.remove(copyEvent)
            pasteEventHandler.remove(pasteEvent)
        }
        super.dispose()
    }
}