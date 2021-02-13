package views.simulation.settings

import controllers.SimulationParametersController
import enumerables.SaveTarget
import tornadofx.*

class ResultProcessingView : View("Result processing") {
    private val parametersController: SimulationParametersController by inject()

    override val root =
        scrollpane {
            form {
                fieldset {
                    field("Save result") {
                        togglegroup {
                            radiobutton("Memory", value = SaveTarget.MEMORY)
                            radiobutton("File", value = SaveTarget.FILE)
                            bind(parametersController.resultSaving.savingTargetProperty)
                        }
                    }
                    field("Simplify") {
                        checkbox{
                            bind(parametersController.resultProcessing.isSimplifyInUseProperty)
                        }
                    }
                    field("Method") {
                        combobox<String> {
                            items = parametersController.simplifyMethods
                            disableProperty().bind(!parametersController.resultProcessing.isSimplifyInUseProperty)
                            bind(parametersController.resultProcessing.selectedSimplifyMethodProperty)
                        }
                    }
                    field("Tolerance") {
                        textfield {
                            disableProperty().bind(!parametersController.resultProcessing.isSimplifyInUseProperty)
                            bind(parametersController.resultProcessing.toleranceProperty)
                        }
                    }
                }
            }
        }
}