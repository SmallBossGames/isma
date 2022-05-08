package ru.nstu.grin.concatenation.function.view

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.IntersectionFunctionViewModel

class IntersectionFunctionView(
    viewModel: IntersectionFunctionViewModel,
) : BorderPane() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    val title = "Search Intersections"

    init {
        center = VBox(
            ListView(viewModel.functions).apply {
                selectionModel.selectionMode = SelectionMode.MULTIPLE

                viewModel.selectedFunctions.set(selectionModel.selectedItems)

                VBox.setVgrow(this, Priority.ALWAYS)

                setCellFactory {
                    object : ListCell<ConcatenationFunction>() {
                        override fun updateItem(item: ConcatenationFunction?, empty: Boolean) {
                            super.updateItem(item, empty)

                            text = item?.name
                        }
                    }
                }
            },
            propertiesGrid {
                addNode("Merge Distance", viewModel.mergeIntervalsDistanceProperty)
            }
        ).apply {
            padding = Insets(10.0)
        }

        bottom = HBox(
            Button("Ok").apply {
                setOnAction {
                    if(viewModel.selectedFunctions.size == 2){
                        coroutineScope.launch {
                            withContext(Dispatchers.JavaFx){
                                isDisable = true
                            }

                            viewModel.commit()

                            withContext(Dispatchers.JavaFx){
                                (scene.window as Stage).close()
                            }
                        }
                    }
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }

    fun dispose(){
        coroutineScope.cancel()
    }
}