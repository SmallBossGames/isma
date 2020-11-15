package events

import models.IsmaProjectModel
import tornadofx.FXEvent

class NewProjectEvent(val ismaProject: IsmaProjectModel) : FXEvent()