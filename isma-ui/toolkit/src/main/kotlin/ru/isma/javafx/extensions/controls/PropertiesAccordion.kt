package ru.isma.javafx.extensions.controls

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.Accordion
import javafx.scene.control.ScrollPane
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox

class PropertiesAccordion : ScrollPane() {
    private val accordions = FXCollections.observableArrayList<Pair<StringProperty, VBox>>()
    private val accordion = Accordion()

    init {
        content = accordion
        isFitToWidth = true
        hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        padding = Insets(8.0)
        styleClass.add("properties-accordion")
    }

    fun addItem(title: String, content: VBox) {
        val titledPane = TitledPane(title, content).apply {
            isCollapsible = true
            styleClass.add("properties-titled-pane")
        }
        accordion.openPanes.add(titledPane)
        accordions.add(SimpleStringProperty(title) to content)
    }

    fun removeItem(title: String) {
        val index = accordions.indexOfFirst { it.first.value == title }
        if (index >= 0) {
            accordion.openPanes.removeAt(index)
            accordions.removeAt(index)
        }
    }

    companion object {
        inline fun propertiesAccordion(crossinline init: PropertiesAccordion.() -> Unit) =
            PropertiesAccordion().apply { init() }
    }
}
