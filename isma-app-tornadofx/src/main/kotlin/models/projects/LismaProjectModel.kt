package models.projects

import javafx.beans.property.SimpleStringProperty
import models.projects.IProjectModel
import java.io.File
import tornadofx.*

class LismaProjectModel: IProjectModel {
    private val nameProperty = SimpleStringProperty("")

    private val lismaTextProperty = SimpleStringProperty("")

    override var name: String by nameProperty

    override var lismaText: String by lismaTextProperty

    override var file: File? = null

    override fun nameProperty(): SimpleStringProperty = nameProperty

    fun lismaTextProperty(): SimpleStringProperty = lismaTextProperty
}
