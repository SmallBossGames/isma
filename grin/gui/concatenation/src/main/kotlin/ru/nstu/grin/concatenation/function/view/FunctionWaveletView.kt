package ru.nstu.grin.concatenation.function.view

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.function.model.FunctionWaveletViewModel

class FunctionWaveletView(
    private val viewModel: FunctionWaveletViewModel,
) : BorderPane() {
    val title = "Wavelet Function"

    init {
        val transformerViewModel = viewModel.transformerViewModel
        center = VBox(
            propertiesGrid {
                addNode("Wavelet Function", FXCollections.observableList(WaveletTransformFun.values().asList()), transformerViewModel.waveletTransformFunProperty)
                addNode("Wavelet Direction", FXCollections.observableList(WaveletDirection.values().asList()), transformerViewModel.waveletDirectionProperty)
            }
        ).apply {
            padding = Insets(10.0)
        }

        bottom = HBox(
            Button("Ok").apply {
                setOnAction {
                    viewModel.commit()
                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }
}