package ru.isma.next.app.models.projects

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Node
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import ru.isma.next.app.di.IsmaEditorQualifier
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import java.io.File

class LismaProjectModel: IProjectModel, KoinScopeComponent {
    override val scope: Scope by lazy { createScope() }
    private val dataProvider by inject<LismaProjectDataProvider>()

    private var lismaTextValue = ""

    var lismaText: String
        get() {
            fetchText()
            return lismaTextValue
        }
        set(value) {
            lismaTextValue = value
            pushText()
        }

    private val nameProperty = SimpleStringProperty("")

    override var name: String by nameProperty

    override var file: File? = null

    override val editor: Node by inject(named<IsmaEditorQualifier>())

    init {
        pushText()
    }

    override fun nameProperty(): SimpleStringProperty = nameProperty

    override fun snapshot() = LismaTextModel(lismaText)

    override fun dispose() { scope.close() }

    fun pushText() {
        dataProvider.text = lismaTextValue
    }

    fun fetchText() {
        lismaTextValue = dataProvider.text
    }
}
