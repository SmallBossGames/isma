package ru.isma.next.editor.blueprint.controls

import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.effect.DropShadow
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

class EditArrowPopOver(arrow: ITransactionArrowData, x: Double, y: Double): VBox(
    Label("Alias (optional)"),
    TextField().apply {
        minWidth = 300.0

        textProperty().bindBidirectional(arrow.aliasProperty)
    },
    Label("Predicate"),
    TextField().apply {
        minWidth = 300.0

        textProperty().bindBidirectional(arrow.predicateProperty)
    },
) {
    init {
        translateXProperty().bind(widthProperty().divide(-2).add(x))
        translateY = y - 2.0

        padding = Insets(10.0)

        background = Background(
            BackgroundFill(
                Color.WHITE,
                CornerRadii(5.0),
                Insets(0.0)
            )
        )

        effect = DropShadow(20.0, Color.LIGHTGRAY)
    }
}