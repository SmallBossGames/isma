package views.simulation.settings

import controllers.SimulationParametersController
import tornadofx.*

class EventDetectionView : View("Event detection") {
    private val parametersController: SimulationParametersController by inject()

    override val root =
        scrollpane {
            form {
                fieldset {
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