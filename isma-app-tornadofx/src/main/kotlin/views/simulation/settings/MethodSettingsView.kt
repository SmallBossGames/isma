package views.simulation.settings

import controllers.SimulationParametersController
import tornadofx.*

class MethodSettingsView: View("Method") {
    private val parametersController: SimulationParametersController by inject()

    override val root =
        scrollpane {
            form {
                fieldset {
                    field("Method") {
                        combobox<String> {
                            items = parametersController.integrationMethods
                            bind(parametersController.integrationMethod.selectedMethodProperty)
                        }
                    }
                    field("Accurate") {
                        checkbox {
                            bind(parametersController.integrationMethod.isAccuracyInUseProperty)
                        }
                    }
                    field("Accuracy") {
                        textfield {
                            disableProperty().bind(!parametersController.integrationMethod.isAccuracyInUseProperty)
                            bind(parametersController.integrationMethod.accuracyProperty)
                        }
                    }
                    field("Stable") {
                        checkbox {
                            bind(parametersController.integrationMethod.isStableInUseProperty)
                        }
                    }
                    field("Parallel") {
                        checkbox {
                            bind(parametersController.integrationMethod.isParallelInUseProperty)
                        }
                    }
                    field("Server") {
                        textfield {
                            disableProperty().bind(!parametersController.integrationMethod.isParallelInUseProperty)
                            bind(parametersController.integrationMethod.serverProperty)
                        }
                    }
                    field("Port") {
                        textfield {
                            disableProperty().bind(!parametersController.integrationMethod.isParallelInUseProperty)
                            bind(parametersController.integrationMethod.portProperty)
                        }
                    }
                }
            }
        }
}