package ru.isma.next.app.services.project

import ru.isma.next.app.services.ModelErrorService
import ru.nstu.isma.core.hsm.models.IsmaErrorList
import ru.isma.next.common.services.lisma.models.FailedTranslation
import ru.isma.next.common.services.lisma.models.LismaTranslationResult
import ru.isma.next.common.services.lisma.models.SuccessTranslation
import ru.isma.next.app.models.ErrorViewModel
import ru.isma.next.app.models.projects.LismaTextModel
import ru.nstu.isma.core.hsm.models.IsmaSemanticError
import ru.nstu.isma.core.hsm.models.IsmaSyntaxError
import ru.nstu.isma.lisma.InputTranslator
import ru.nstu.isma.next.core.fdm.FDMNewConverter

class LismaPdeService(
    private val translator: InputTranslator,
    private val modelService: ModelErrorService
) {
    fun translateLisma(sourceSnapshot: LismaTextModel): LismaTranslationResult {
        val errors = IsmaErrorList()
        val model = translator.translate(sourceSnapshot.fullText, errors)

        val errorViewModels = errors.map {

            when(it){
                is IsmaSemanticError -> {
                    val fragment = LismaTextModel.DefaultFragment
                    ErrorViewModel(-1, -1, fragment.name, it.msg)
                }
                is IsmaSyntaxError -> {
                    val fragment = sourceSnapshot.fragmentNameByIndex(it.row)
                    val rowWithOffset = it.row - fragment.startLine
                    ErrorViewModel(rowWithOffset, it.col, fragment.name, it.msg)
                }
            }
        }

        modelService.putErrorList(errorViewModels)

        val processedModel = if (model.isPDE) model else FDMNewConverter(model).convert()

        if (processedModel != null && errors.isEmpty()) {
            return SuccessTranslation(processedModel)
        }

        return FailedTranslation
    }
}