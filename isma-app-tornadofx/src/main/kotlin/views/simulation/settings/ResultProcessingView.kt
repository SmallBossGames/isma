package views.simulation.settings

import services.SimulationParametersService
import org.koin.core.component.inject as koinInject
import enumerables.SaveTarget
import org.koin.core.component.KoinComponent
import tornadofx.*

class ResultProcessingView : View("Result processing"), KoinComponent {
    private val parametersService: SimulationParametersService by koinInject()

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