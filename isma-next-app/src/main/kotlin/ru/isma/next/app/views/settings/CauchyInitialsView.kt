package ru.isma.next.app.views.settings

import ru.isma.next.app.services.simualtion.SimulationParametersService
import tornadofx.*


class CauchyInitialsView: View("Initials") {
    private val parametersService: SimulationParametersService by di()

    override val root =
        scrollpane {
            form {
                fieldset {
                    field("Start") {
                        textfield {
                            bind(parametersService.cauchyInitials.startTimeProperty)
                        }
                    }
                    field("End") {
                        textfield {
                            bind(parametersService.cauchyInitials.endTimeProperty)
                        }
                    }
                    field("Step") {
                        textfield {
                            bind(parametersService.cauchyInitials.stepProperty)
                        }
                    }
                }
            }
        }
}
