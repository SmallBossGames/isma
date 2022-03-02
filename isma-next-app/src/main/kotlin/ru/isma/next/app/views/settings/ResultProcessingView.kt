package ru.isma.next.app.views.settings

import javafx.collections.FXCollections
import javafx.scene.control.ScrollPane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.services.simulation.abstractions.enumerables.SaveTarget
import tornadofx.View

class ResultProcessingView(
    private val parametersService: SimulationParametersService
) : View("Result processing") {
    override val root =
        ScrollPane(
            propertiesGrid {
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