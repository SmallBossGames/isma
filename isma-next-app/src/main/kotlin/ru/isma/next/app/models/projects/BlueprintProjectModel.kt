package ru.isma.next.app.models.projects

import javafx.beans.property.SimpleStringProperty
import ru.isma.next.editor.blueprint.models.BlueprintModel
import java.io.File
import tornadofx.*
import ru.isma.next.editor.blueprint.utilities.convertToLisma

class BlueprintProjectModel() : IProjectModel {
    private var blueprintValue: BlueprintModel = BlueprintModel.empty

    private val nameProperty = SimpleStringProperty("")

    override var name: String by nameProperty

    override var file: File? = null

    override fun nameProperty() = nameProperty

    var dataProvider: BlueprintProjectDataProvider? = null

    var blueprint: BlueprintModel
        get() {
            fetchBlueprint()
            return blueprintValue
        }
        set(value) {
            blueprintValue = value
            pushBlueprint()
        }

    override val lismaText: String
        get() = blueprint.convertToLisma()

    fun pushBlueprint() {
        val provider = dataProvider ?: return
        provider.blueprint = blueprintValue
    }

    fun fetchBlueprint() {
        val provider = dataProvider ?: return
        blueprintValue = provider.blueprint
    }
}