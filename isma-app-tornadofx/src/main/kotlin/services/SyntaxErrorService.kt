package services

import models.SyntaxErrorModel
import org.koin.core.component.KoinComponent
import tornadofx.Controller
import tornadofx.asObservable

class SyntaxErrorService {
    val errors = arrayListOf<SyntaxErrorModel>().asObservable()

    fun setErrorList(errors: Iterable<SyntaxErrorModel>){
        this.errors.clear()
        this.errors.addAll(errors)
    }
}