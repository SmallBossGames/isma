package ru.isma.next.app.services

import ru.isma.next.app.models.ErrorViewModel
import tornadofx.asObservable

class ModelErrorService {
    val errors = arrayListOf<ErrorViewModel>().asObservable()

    fun putErrorList(errors: Iterable<ErrorViewModel>){
        this.errors.clear()
        this.errors.addAll(errors)
    }
}