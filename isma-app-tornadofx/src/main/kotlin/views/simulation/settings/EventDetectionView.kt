package views.simulation.settings

import services.simualtion.SimulationParametersService
import tornadofx.*

class EventDetectionView : View("Event detection") {
    private val parametersService: SimulationParametersService by di()

    override val root =
        scrollpane {
            form {
                fieldset {
                    field("In use") {
                        checkbox {
                            bind(parametersService.eventDetection.isEventDetectionInUseProperty)
                        }
                    }
                    field("Gamma") {
                        textfield {
                            disableProperty().bind(!parametersService.eventDetection.isEventDetectionInUseProperty)
                            bind(parametersService.eventDetection.gammaProperty)
                        }
                    }
                    field("Step limit") {
                        checkbox {
                            bind(parametersService.eventDetection.isStepLimitInUseProperty)
                        }
                    }
                    field("Low border") {
                        textfield {
                            disableProperty().bind(!parametersService.eventDetection.isStepLimitInUseProperty)
                            bind(parametersService.eventDetection.lowBorderProperty)
                        }
                    }
                }
            }
        }
}