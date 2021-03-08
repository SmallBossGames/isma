package events

import models.IsmaProjectModel
import tornadofx.FXEvent

class NewBlueprintProjectEvent(val ismaProject: IsmaProjectModel) : FXEvent()