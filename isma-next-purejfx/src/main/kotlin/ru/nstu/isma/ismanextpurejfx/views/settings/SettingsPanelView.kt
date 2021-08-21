package ru.nstu.isma.ismanextpurejfx.views.settings

import javafx.beans.property.SimpleDoubleProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.TextField
import org.controlsfx.control.HiddenSidesPane
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.BeanPropertyUtils
import org.controlsfx.property.editor.Editors
import ru.nstu.isma.ismanextpurejfx.javafx.IView
import java.io.Serializable

class SettingsPanelView: IView {
    override val root = PropertySheet(BeanPropertyUtils.getProperties(MyBean()))

}


data class MyBean(var names: String = ""):Serializable