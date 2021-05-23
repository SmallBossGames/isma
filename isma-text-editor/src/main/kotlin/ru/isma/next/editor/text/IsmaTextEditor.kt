package ru.isma.next.editor.text

import javafx.beans.value.ObservableValue
import javafx.scene.paint.Color
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import ru.isma.next.editor.text.events.CopyTextInCurrentEditorEvent
import ru.isma.next.editor.text.events.CutTextInCurrentEditorEvent
import ru.isma.next.editor.text.events.PasteTextInCurrentEditorEvent
import ru.isma.next.editor.text.services.LismaHighlightingService
import tornadofx.*

class IsmaTextEditor: Fragment() {
    private val highlightingService: LismaHighlightingService by di()

    fun textProperty(): ObservableValue<String> = root.textProperty()
    fun replaceText(text: String) = root.replaceText(text)

    override val root = CodeArea().apply {
        subscribe<CutTextInCurrentEditorEvent> { if (isFocused) cut() }
        subscribe<CopyTextInCurrentEditorEvent> { if (isFocused) copy() }
        subscribe<PasteTextInCurrentEditorEvent> { if (isFocused) paste() }

        textProperty().onChange {
            if(paragraphGraphicFactory == null) {
                paragraphGraphicFactory = LineNumberFactory.get(this)
            }
            val highlighting = highlightingService.createHighlightingStyleSpans(it ?: "")
            setStyleSpans(0, highlighting)
        }

        stylesheet {
            addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("keyword")))) {
                fill = Color.ORANGE
                fontWeight = FontWeight.BOLD
            })
            addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("default")))) {
                fill = Color.BLACK
                fontWeight = FontWeight.NORMAL
            })
            addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("decimal")))) {
                fill = Color.BLUE
                fontWeight = FontWeight.NORMAL
            })
            addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("comment")))) {
                fill = Color.GRAY
                fontWeight = FontWeight.NORMAL
                fontStyle = FontPosture.ITALIC
            })
        }
    }
}