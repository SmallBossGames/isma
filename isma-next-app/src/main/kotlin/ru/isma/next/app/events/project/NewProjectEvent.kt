package ru.isma.next.app.events.project

import ru.isma.next.app.models.projects.LismaProjectModel
import tornadofx.FXEvent

class NewProjectEvent(val lismaProject: LismaProjectModel) : FXEvent()