package models.projects

import views.editors.blueprint.IsmaBlueprintEditor
import views.editors.blueprint.models.BlueprintModel

class BlueprintProjectDataProvider(private val blueprintEditor: IsmaBlueprintEditor) {
    var blueprint: BlueprintModel
        get() = blueprintEditor.getBlueprintModel()
        set(value) = blueprintEditor.setBlueprintModel(value)
}