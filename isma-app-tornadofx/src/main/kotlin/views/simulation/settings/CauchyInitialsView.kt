package views.simulation.settings

import controllers.SimulationParametersController
import tornadofx.*

class CauchyInitialsView: View("Initials") {
    private val parametersController: SimulationParametersController by inject()

    override val root =
        scrollpane {
            form {
                fieldset {
                    field("Start") {
                        textfield {
                            bind(parametersController.cauchyInitialsModel.startTimeProperty)
                        }
                    }
                    field("End") {
                        textfield {
                            bind(parametersController.cauchyInitialsModel.endTimeProperty)
                        }
                    }
                    field("Step") {
                        textfield {
                            bind(parametersController.cauchyInitialsModel.stepProperty)
                        }
                    }
                }
            }
        }
}
