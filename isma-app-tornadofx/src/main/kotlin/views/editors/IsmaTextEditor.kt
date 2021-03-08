package views.editors

import controllers.SyntaxHighlightingController
import events.CopyTextInCurrentEditorEvent
import events.CutTextInCurrentEditorEvent
import events.PasteTextInCurrentEditorEvent
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableValue
import javafx.scene.Parent
import javafx.scene.paint.Color
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import models.IsmaProjectModel
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import tornadofx.*

class IsmaTextEditor(): View() {
    private val highlightingController: SyntaxHighlightingController by inject()

    fun isSelectedProperty() = SimpleBooleanProperty(true)
    var isSelected by isSelectedProperty()

    fun textProperty(): ObservableValue<String> = root.textProperty()
    fun replaceText(text: String) = root.replaceText(text);

    override val root = CodeArea().apply {
        paragraphGraphicFactory = LineNumberFactory.get(this)

        subscribe<CutTextInCurrentEditorEvent> { if (isSelected) cut() }
        subscribe<CopyTextInCurrentEditorEvent> { if (isSelected) copy() }
        subscribe<PasteTextInCurrentEditorEvent> { if (isSelected) paste() }

        textProperty().onChange {
            val highlighting = highlightingController.createHighlightingStyleSpans(it ?: "")
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