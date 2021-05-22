package views.simulation.settings

import services.SimulationParametersService
import tornadofx.*


class CauchyInitialsView: View("Initials") {
    private val parametersService: SimulationParametersService by di()

    override val root =
        scrollpane {
            form {
                fieldset {
                    field("Start") {
                        textfield {
                            bind(parametersService.cauchyInitialsModel.startTimeProperty)
                        }
                    }
                    field("End") {
                        textfield {
                            bind(parametersService.cauchyInitialsModel.endTimeProperty)
                        }
                    }
                    field("Step") {
                        textfield {
                            bind(parametersService.cauchyInitialsModel.stepProperty)
                        }
                    }
                }
            }
        }
}
