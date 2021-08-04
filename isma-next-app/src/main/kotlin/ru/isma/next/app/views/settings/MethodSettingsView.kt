package ru.isma.next.app.views.settings

import ru.isma.next.app.extentions.bindDouble
import ru.isma.next.app.extentions.integerTextField
import ru.isma.next.app.extentions.numberTextField
import ru.isma.next.app.services.simualtion.SimulationParametersService
import tornadofx.*

class MethodSettingsView: View("Method") {
    private val parametersService: SimulationParametersService by di()

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
                        numberTextField(parametersService.integrationMethod.accuracyProperty){
                            disableProperty().bind(!parametersService.integrationMethod.isAccuracyInUseProperty)
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
                        integerTextField(parametersService.integrationMethod.portProperty) {
                            disableProperty().bind(!parametersService.integrationMethod.isParallelInUseProperty)
                        }
                    }
                }
            }
        }
}