package controllers

import models.IsmaProjectModel
import tornadofx.Controller

class ActiveProjectController: Controller() {
    var activeProject: IsmaProjectModel? = null
}