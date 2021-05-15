package views.simulation.settings

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject as koinInject
import services.SimulationParametersService
import tornadofx.*


class CauchyInitialsView: View("Initials"), KoinComponent {
    private val parametersService: SimulationParametersService by koinInject()

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
