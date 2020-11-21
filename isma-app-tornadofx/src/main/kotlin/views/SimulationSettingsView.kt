package views

import app.util.SaveTarget
import controllers.SimulationParametersController
import javafx.collections.FXCollections
import tornadofx.*

class SimulationSettingsView: View("Simulation settings") {
    val parametersController: SimulationParametersController by inject()

    val texasCities = FXCollections.observableArrayList("Austin",
            "Dallas","Midland", "San Antonio","Fort Worth")

    override val root = vbox {
        minHeight = 400.0
        minWidth = 300.0

        prefHeight = 400.0

        scrollpane {
            form {
                fieldset("Parameters") {
                    field("Start") {
                        textfield{
                            bind(parametersController.cauchyInitialsModel.startTimeProperty)
                        }
                    }
                    field("End") {
                        textfield{
                            bind(parametersController.cauchyInitialsModel.endTimeProperty)
                        }
                    }
                    field("Step") {
                        textfield {
                            bind(parametersController.cauchyInitialsModel.stepProperty)
                        }
                    }
                }
                fieldset("Method") {
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
                            bind(parametersController.integrationMethod.serverProperty)
                        }
                    }
                    field("Port") {
                        textfield {
                            bind(parametersController.integrationMethod.portProperty)
                        }
                    }
                }
                fieldset("Result saving") {
                    togglegroup() {
                        radiobutton("Memory", value = SaveTarget.MEMORY)
                        radiobutton("File", value = SaveTarget.FILE)
                        parametersController.resultSaving.savingTargetProperty.bind(selectedValueProperty())
                    }
                }
                fieldset("Result processing") {
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
                fieldset("Event detection") {
                    field("In use") {
                        checkbox {
                            bind(parametersController.eventDetection.isEventDetectionInUseProperty)
                        }
                    }
                    field("Gamma") {
                        textfield {
                            disableProperty().bind(!parametersController.eventDetection.isEventDetectionInUseProperty)
                            bind(parametersController.eventDetection.gammaProperty)
                        }
                    }
                    field("Step limit") {
                        checkbox {
                            bind(parametersController.eventDetection.isStepLimitInUseProperty)
                        }
                    }
                    field("Low border") {
                        textfield {
                            disableProperty().bind(!parametersController.eventDetection.isStepLimitInUseProperty)
                            bind(parametersController.eventDetection.lowBorderProperty)
                        }
                    }
                }
            }
        }


    }
}
