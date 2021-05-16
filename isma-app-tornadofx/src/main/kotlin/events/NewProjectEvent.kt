package events

import models.projects.LismaProjectModel
import tornadofx.FXEvent

class NewProjectEvent(val lismaProject: LismaProjectModel) : FXEvent()