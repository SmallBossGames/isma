package ru.isma.next.app.views.settings

import ru.isma.next.app.enumerables.SaveTarget
import ru.isma.next.app.extentions.numberTextField
import ru.isma.next.app.services.simualtion.SimulationParametersService
import tornadofx.*

class ResultProcessingView : View("Result processing") {
    private val parametersService: SimulationParametersService by di()

    override val root =
        scrollpane {
            form {
                fieldset {
                    field("Save result") {
                        togglegroup {
                            radiobutton("Memory", value = SaveTarget.MEMORY)
                            radiobutton("File", value = SaveTarget.FILE)
                            bind(parametersService.resultSaving.savingTargetProperty)
                        }
                    }
                    field("Simplify") {
                        checkbox{
                            bind(parametersService.resultProcessing.isSimplifyInUseProperty)
                        }
                    }
                    field("Method") {
                        combobox<String> {
                            items = parametersService.simplifyMethods
                            disableProperty().bind(!parametersService.resultProcessing.isSimplifyInUseProperty)
                            bind(parametersService.resultProcessing.selectedSimplifyMethodProperty)
                        }
                    }
                    field("Tolerance") {
                        numberTextField(parametersService.resultProcessing.toleranceProperty) {
                            disableProperty().bind(!parametersService.resultProcessing.isSimplifyInUseProperty)
                        }
                    }
                }
            }
        }
}