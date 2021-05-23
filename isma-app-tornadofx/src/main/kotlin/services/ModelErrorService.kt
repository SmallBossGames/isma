package services

import ru.isma.next.common.services.lisma.models.SyntaxErrorModel
import tornadofx.asObservable

class ModelErrorService {
    val errors = arrayListOf<SyntaxErrorModel>().asObservable()

    fun setErrorList(errors: Iterable<SyntaxErrorModel>){
        this.errors.clear()
        this.errors.addAll(errors)
    }
}