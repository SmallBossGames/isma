package views.editors.text

import services.SyntaxHighlightingService
import events.CopyTextInCurrentEditorEvent
import events.CutTextInCurrentEditorEvent
import events.PasteTextInCurrentEditorEvent
import javafx.beans.value.ObservableValue
import javafx.scene.paint.Color
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject as koinInject
import tornadofx.*

class IsmaTextEditor: Fragment(), KoinComponent {
    private val highlightingService: SyntaxHighlightingService by koinInject()

    fun textProperty(): ObservableValue<String> = root.textProperty()
    fun replaceText(text: String) = root.replaceText(text);

    override val root = CodeArea().apply {
        paragraphGraphicFactory = LineNumberFactory.get(this)

        subscribe<CutTextInCurrentEditorEvent> { if (isFocused) cut() }
        subscribe<CopyTextInCurrentEditorEvent> { if (isFocused) copy() }
        subscribe<PasteTextInCurrentEditorEvent> { if (isFocused) paste() }

        textProperty().onChange {
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