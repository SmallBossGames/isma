package views.simulation.settings

import enumerables.SaveTarget
import services.SimulationParametersService
import tornadofx.*

class ResultProcessingView : View("Result processing") {
    private val parametersService: SimulationParametersService by di()

    override val root =
        scrollpane {
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
                        textfield {
                            disableProperty().bind(!parametersService.resultProcessing.isSimplifyInUseProperty)
                            bind(parametersService.resultProcessing.toleranceProperty)
                        }
                    }
                }
            }
        }
}