package events

import models.projects.BlueprintProjectModel
import models.projects.LismaProjectModel
import tornadofx.FXEvent

class NewBlueprintProjectEvent(val blueprintProject: BlueprintProjectModel) : FXEvent()