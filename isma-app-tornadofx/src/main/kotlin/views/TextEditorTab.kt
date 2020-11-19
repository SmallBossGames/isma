package views

import javafx.scene.Parent
import models.IsmaProjectModel
import tornadofx.Fragment
import tornadofx.textarea
import tornadofx.toProperty

class TextEditorTab : Fragment() {
    private lateinit var _project: IsmaProjectModel

    fun bind (project: IsmaProjectModel){
        _project = project
        title = _project.name
        root.text = _project.projectText

        titleProperty.bindBidirectional(_project.name.toProperty())
        root.textProperty().bindBidirectional(_project.projectText.toProperty())
    }

    override val root = textarea()
}