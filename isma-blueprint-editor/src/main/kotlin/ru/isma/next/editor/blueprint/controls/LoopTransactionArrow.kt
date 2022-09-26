package ru.isma.next.editor.blueprint.controls

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Polygon
import javafx.scene.text.Font
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.isma.next.editor.blueprint.utilities.getValue
import ru.isma.next.editor.blueprint.utilities.setValue


class LoopTransactionArrow(
    val onClick: (LoopTransactionArrow, MouseEvent) -> Unit = { _, _ -> },
    val onArrowClick: (LoopTransactionArrow, MouseEvent) -> Unit = { _, _ -> },
    val onArrowDoubleClick: (LoopTransactionArrow, MouseEvent) -> Unit = { _, _ -> },
    var text: String = "",
    alias: String = "",
    predicate: String = "",
): Group(), ITransactionArrowData {
    override val aliasProperty = SimpleStringProperty(alias)
    override val predicateProperty = SimpleStringProperty(predicate)

    var alias: String by aliasProperty
    var predicate: String by predicateProperty

    init {
        viewOrder = 4.0

        children.addAll(
            Circle(40.0, Color.TRANSPARENT).apply {
                fill = Color.TRANSPARENT
                stroke = Color.BLACK
                strokeWidth = 3.0

                centerX = 60.0
            },
            Group(
                Polygon(0.0, -7.0, 7.0, 0.0, -7.0, 0.0).apply {
                    strokeWidth = 3.0
                    viewOrder = 6.0
                }
            ).apply {
                layoutX = 100.0

                addEventHandler(MouseEvent.MOUSE_CLICKED){ handleMouseClick(it) }
            },
            Label().apply {
                font = Font("Arial", 16.0)
                translateY = -10.0
                translateX = 120.0
                alignment = Pos.CENTER

                fun updatePredicateText(){
                    val localAlias = aliasProperty.value
                    val localPredicate = predicateProperty.value

                    text = if (localAlias != "") localAlias else localPredicate
                }

                aliasProperty.addListener { _,_,_ -> updatePredicateText() }
                predicateProperty.addListener { _,_,_ -> updatePredicateText() }

                updatePredicateText()
            }
        )

        setOnMouseClicked {
            onClick(this@LoopTransactionArrow, it)
        }
    }

    private var singleClickAction: Job? = null

    private fun handleMouseClick(event: MouseEvent){
        when(event.clickCount){
            1 -> {
                if(singleClickAction == null) {
                    singleClickAction = coroutineScope.launch {
                        delay(200)

                        singleClickAction = null

                        onArrowClick(this@LoopTransactionArrow, event)
                    }
                }

            }
            2 -> {
                if(singleClickAction != null){
                    singleClickAction?.cancel()
                    singleClickAction = null

                    onArrowDoubleClick(this@LoopTransactionArrow, event)
                }
            }
        }
    }

    companion object {
        private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    }
}