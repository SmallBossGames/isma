package ru.isma.next.editor.blueprint.controls

import javafx.geometry.Insets
import javafx.scene.control.Label
import ru.isma.next.editor.blueprint.constants.*
import javafx.scene.control.TextField
import javafx.scene.effect.DropShadow
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import ru.isma.next.editor.blueprint.viewmodels.TransactionViewModel

class EditArrowPopOver(val viewModel: TransactionViewModel, x: Double, y: Double): VBox(
    Label("Alias (optional)"),
    TextField().apply {
        minWidth = POPOVER_MIN_WIDTH

        textProperty().bindBidirectional(viewModel.aliasProperty)
    },
    Label("Predicate"),
    TextField().apply {
        minWidth = POPOVER_MIN_WIDTH

        textProperty().bindBidirectional(viewModel.predicateProperty)
    },
) {
    init {
        translateXProperty().bind(widthProperty().divide(-2).add(x))
        translateY = y - 2.0

        padding = Insets(POPOVER_PADDING)

        background = Background(
            BackgroundFill(
                Color.WHITE,
                CornerRadii(POPOVER_CORNER_RADIUS),
                Insets(0.0)
            )
        )

        effect = DropShadow(POPOVER_SHADOW_RADIUS, Color.LIGHTGRAY)
    }
}
