package ru.isma.next.common.services.lisma.models

import ru.nstu.isma.core.hsm.models.IsmaError
import ru.nstu.isma.core.hsm.models.IsmaSemanticError
import ru.nstu.isma.core.hsm.models.IsmaSyntaxError

data class ErrorViewModel(
    val row: Int,
    val position: Int,
    val message: String
) {
    companion object {
        fun fromIsmaErrorModel(ismaError: IsmaError) : ErrorViewModel {
            return when(ismaError){
                is IsmaSemanticError -> ErrorViewModel(-1, -1, ismaError.msg)
                is IsmaSyntaxError -> ErrorViewModel(ismaError.row, ismaError.col, ismaError.msg)
            }
        }
    }
}