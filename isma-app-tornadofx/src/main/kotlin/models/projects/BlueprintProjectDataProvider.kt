package models.projects

import ru.isma.next.editor.blueprint.IsmaBlueprintEditor
import ru.isma.next.editor.blueprint.models.BlueprintModel

class BlueprintProjectDataProvider(private val blueprintEditor: IsmaBlueprintEditor) {
    var blueprint: BlueprintModel
        get() = blueprintEditor.getBlueprintModel()
        set(value) = blueprintEditor.setBlueprintModel(value)
}