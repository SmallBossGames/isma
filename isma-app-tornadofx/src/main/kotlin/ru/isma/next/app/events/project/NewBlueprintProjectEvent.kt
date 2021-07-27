package ru.isma.next.app.events.project

import ru.isma.next.app.models.projects.BlueprintProjectModel
import tornadofx.FXEvent

class NewBlueprintProjectEvent(val blueprintProject: BlueprintProjectModel) : FXEvent()