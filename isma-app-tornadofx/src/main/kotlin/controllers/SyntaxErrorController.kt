package controllers

import models.SyntaxErrorModel
import tornadofx.Controller
import tornadofx.asObservable

class SyntaxErrorController : Controller() {
    val errors = arrayListOf<SyntaxErrorModel>().asObservable()
}