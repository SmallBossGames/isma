package views.simulation.settings

import org.koin.core.component.KoinComponent
import services.SimulationParametersService
import tornadofx.*
import org.koin.core.component.inject as koinInject

class MethodSettingsView: View("Method"), KoinComponent {
    private val parametersService: SimulationParametersService by koinInject()

    override val root =
        scrollpane {
            form {
                fieldset {
                    field("Method") {
                        combobox<String> {
                            items = parametersService.integrationMethods
                            bind(parametersService.integrationMethod.selectedMethodProperty)
                        }
                    }
                    field("Accurate") {
                        checkbox {
                            bind(parametersService.integrationMethod.isAccuracyInUseProperty)
                        }
                    }
                    field("Accuracy") {
                        textfield {
                            disableProperty().bind(!parametersService.integrationMethod.isAccuracyInUseProperty)
                            bind(parametersService.integrationMethod.accuracyProperty)
                        }
                    }
                    field("Stable") {
                        checkbox {
                            bind(parametersService.integrationMethod.isStableInUseProperty)
                        }
                    }
                    field("Parallel") {
                        checkbox {
                            bind(parametersService.integrationMethod.isParallelInUseProperty)
                        }
                    }
                    field("Server") {
                        textfield {
                            disableProperty().bind(!parametersService.integrationMethod.isParallelInUseProperty)
                            bind(parametersService.integrationMethod.serverProperty)
                        }
                    }
                    field("Port") {
                        textfield {
                            disableProperty().bind(!parametersService.integrationMethod.isParallelInUseProperty)
                            bind(parametersService.integrationMethod.portProperty)
                        }
                    }
                }
            }
        }
}