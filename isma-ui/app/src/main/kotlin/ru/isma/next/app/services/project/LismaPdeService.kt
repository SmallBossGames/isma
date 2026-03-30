package ru.isma.next.app.services.project

import ru.isma.next.app.services.ModelErrorService
import ru.isma.next.app.models.ErrorViewModel
import ru.isma.next.app.models.projects.LismaTextModel
import ru.isma.next.external.CompilationErrorDto
import ru.isma.next.external.SimulationServerFacade

class LismaPdeService(
    private val serverFacade: SimulationServerFacade,
    private val modelService: ModelErrorService
) {
    fun translateLisma(sourceSnapshot: LismaTextModel): LismaPdeTranslationResult {
        val validationResult = serverFacade.validateModel(sourceSnapshot.fullText)

        val errorViewModels = validationResult.errors.map { error: CompilationErrorDto ->
            ErrorViewModel(error.row, error.column, LismaTextModel.DefaultFragment.name, error.message)
        }

        modelService.putErrorList(errorViewModels)

        if (validationResult.errors.isEmpty()) {
            return SuccessTranslation
        }

        return FailedTranslation
    }
}