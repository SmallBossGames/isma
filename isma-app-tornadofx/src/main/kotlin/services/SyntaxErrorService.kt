package services

import models.SyntaxErrorModel
import tornadofx.asObservable

class SyntaxErrorService {
    val errors = arrayListOf<SyntaxErrorModel>().asObservable()

    fun setErrorList(errors: Iterable<SyntaxErrorModel>){
        this.errors.clear()
        this.errors.addAll(errors)
    }
}