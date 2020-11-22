package controllers

import tornadofx.Controller
import tornadofx.doubleProperty
import tornadofx.getValue
import tornadofx.setValue

class SimulationProgressController : Controller() {
    val progressProperty = doubleProperty()
    var progress by progressProperty
}