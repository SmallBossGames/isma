package ru.isma.next.app.services

import ru.isma.next.app.models.ErrorViewModel
import javafx.collections.FXCollections

class ModelErrorService {
    val errors = FXCollections.observableArrayList<ErrorViewModel>()

    fun putErrorList(errors: Iterable<ErrorViewModel>){
        this.errors.clear()
        this.errors.addAll(errors)
    }
}