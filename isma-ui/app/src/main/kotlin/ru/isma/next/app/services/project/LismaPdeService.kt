package ru.isma.next.app.services.project

import ru.isma.next.app.models.CompilationErrorItem
import ru.isma.next.app.models.LismaTextModel
import ru.isma.next.app.services.ModelErrorService
import ru.isma.next.external.SimulationServerFacade
import ru.isma.next.external.dtos.CompilationErrorDto

class LismaPdeService(
    private val serverFacade: SimulationServerFacade,
    private val modelService: ModelErrorService
) {
    fun translateLisma(sourceSnapshot: LismaTextModel): LismaPdeTranslationResult {
        val validationResult = serverFacade.validateModel(sourceSnapshot.fullText)

        val errorItems = validationResult.errors.map { error: CompilationErrorDto ->
            CompilationErrorItem(error.row, error.column, sourceSnapshot.fragmentNameByLine(error.row), error.message)
        }

        modelService.putErrorList(errorItems)

        return if (validationResult.errors.isEmpty()) SuccessTranslation else FailedTranslation
    }
}
