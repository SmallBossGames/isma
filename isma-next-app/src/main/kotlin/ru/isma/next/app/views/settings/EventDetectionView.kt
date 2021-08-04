package ru.isma.next.app.views.settings

import ru.isma.next.app.extentions.bindDouble
import ru.isma.next.app.extentions.numberTextField
import ru.isma.next.app.services.simualtion.SimulationParametersService
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
                        numberTextField(parametersService.eventDetection.gammaProperty) {
                            disableProperty().bind(!parametersService.eventDetection.isEventDetectionInUseProperty)
                        }
                    }
                    field("Step limit") {
                        checkbox {
                            bind(parametersService.eventDetection.isStepLimitInUseProperty)
                        }
                    }
                    field("Low border") {
                        numberTextField(parametersService.eventDetection.lowBorderProperty) {
                            disableProperty().bind(!parametersService.eventDetection.isStepLimitInUseProperty)
                        }
                    }
                }
            }
        }
}