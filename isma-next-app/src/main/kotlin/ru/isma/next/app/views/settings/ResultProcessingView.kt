package ru.isma.next.app.views.settings

import javafx.collections.FXCollections
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import ru.isma.next.app.enumerables.SaveTarget
import ru.isma.next.app.extentions.numberTextField
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.app.views.controls.PropertiesGrid
import tornadofx.*

class ResultProcessingView(
    private val parametersService: SimulationParametersService
) : View("Result processing") {
    override val root =
        ScrollPane(
            PropertiesGrid().apply {
                addComboBox(
                    "Save result",
                    FXCollections.observableArrayList(SaveTarget.values().toList()),
                    parametersService.resultSaving.savingTargetProperty
                )
            }
        )
        /*scrollpane {
            form {
                fieldset {
                    field("Save result") {
                        togglegroup {
                            radiobutton("Memory", value = SaveTarget.MEMORY)
                            radiobutton("File", value = SaveTarget.FILE)
                            bind(parametersService.resultSaving.savingTargetProperty)
                        }
                    }
                    field("Simplify") {
                        checkbox{
                            bind(parametersService.resultProcessing.isSimplifyInUseProperty)
                        }
                    }
                    field("Method") {
                        combobox<String> {
                            items = parametersService.simplifyMethods
                            disableProperty().bind(!parametersService.resultProcessing.isSimplifyInUseProperty)
                            bind(parametersService.resultProcessing.selectedSimplifyMethodProperty)
                        }
                    }
                    field("Tolerance") {
                        numberTextField(parametersService.resultProcessing.toleranceProperty) {
                            disableProperty().bind(!parametersService.resultProcessing.isSimplifyInUseProperty)
                        }
                    }
                }
            }
        }*/
}