package ru.isma.next.app.views.settings

import ru.isma.next.app.extentions.numberTextField
import ru.isma.next.app.services.simualtion.SimulationParametersService
import tornadofx.*


class CauchyInitialsView(
    private val parametersService: SimulationParametersService
): View("Initials") {
    override val root =
        scrollpane {
            form {
                fieldset {
                    field("Start") {
                        numberTextField(parametersService.cauchyInitials.startTimeProperty())
                    }
                    field("End") {
                        numberTextField(parametersService.cauchyInitials.endTimeProperty())
                    }
                    field("Step") {
                        numberTextField(parametersService.cauchyInitials.stepProperty())
                    }
                }
            }
        }
}
