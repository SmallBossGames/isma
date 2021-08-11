package ru.isma.next.app.models.projects

import javafx.beans.property.SimpleStringProperty
import ru.isma.next.app.utilities.convertToLisma
import ru.isma.next.editor.blueprint.models.BlueprintModel
import java.io.File
import tornadofx.*

class BlueprintProjectModel : IProjectModel {
    private var blueprintValue: BlueprintModel = BlueprintModel.empty

    private val nameProperty = SimpleStringProperty("")

    override var name: String by nameProperty

    override var file: File? = null

    override fun nameProperty() = nameProperty

    override fun snapshot() = blueprint.convertToLisma()

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

    fun pushBlueprint() {
        val provider = dataProvider ?: return
        provider.blueprint = blueprintValue
    }

    fun fetchBlueprint() {
        val provider = dataProvider ?: return
        blueprintValue = provider.blueprint
    }
}