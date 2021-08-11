package ru.isma.next.app.models.projects

import javafx.beans.property.SimpleStringProperty
import java.io.File
import tornadofx.*

class LismaProjectModel: IProjectModel {
    private val nameProperty = SimpleStringProperty("")

    private val lismaTextProperty = SimpleStringProperty("")

    override var name: String by nameProperty

    var lismaText: String by lismaTextProperty

    override var file: File? = null

    override fun nameProperty(): SimpleStringProperty = nameProperty

    override fun snapshot() = LismaTextModel(lismaText)

    fun lismaTextProperty(): SimpleStringProperty = lismaTextProperty
}
