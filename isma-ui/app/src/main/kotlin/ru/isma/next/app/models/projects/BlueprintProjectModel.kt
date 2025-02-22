package ru.isma.next.app.models.projects

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Node
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import ru.isma.next.app.utilities.convertToLisma
import ru.isma.next.app.views.koin.IsmaEditorQualifier
import ru.isma.next.editor.blueprint.models.BlueprintModel
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

class BlueprintProjectModel : IProjectModel, KoinScopeComponent {
    override val scope: Scope by lazy { createScope() }

    private val dataProvider by inject<BlueprintProjectDataProvider>()

    private var blueprintValue: BlueprintModel = BlueprintModel.empty

    private val nameProperty = SimpleStringProperty("")

    override var name: String by nameProperty

    override var file: File? = null

    override val editor: Node by inject(named<IsmaEditorQualifier>())

    init {
        pushBlueprint()
    }

    override fun nameProperty() = nameProperty

    override fun snapshot() = blueprint.convertToLisma()

    override fun dispose() { scope.close() }

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
        dataProvider.blueprint = blueprintValue
    }

    fun fetchBlueprint() {
        blueprintValue = dataProvider.blueprint
    }
}