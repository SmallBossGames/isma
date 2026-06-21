package ru.isma.javafx.extensions.controls

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.ScrollPane
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox

open class PropertiesAccordion : ScrollPane() {
    private val titledPanes = FXCollections.observableArrayList<Pair<String, TitledPane>>()

    init {
        content = VBox().apply {
            styleClass.add("properties-accordion-content")
        }
        isFitToWidth = true
        hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        padding = Insets(8.0)
        styleClass.add("properties-accordion")
    }

    fun addItem(title: String, content: VBox) {
        val titledPane = TitledPane(title, content).apply {
            isCollapsible = true
            styleClass.add("properties-titled-pane")
            isExpanded = true
        }
        (this.content as VBox).children.add(titledPane)
        titledPanes.add(title to titledPane)
    }

    fun removeItem(title: String) {
        val index = titledPanes.indexOfFirst { it.first == title }
        if (index >= 0) {
            val pane = titledPanes[index].second
            (this.content as VBox).children.remove(pane)
            titledPanes.removeAt(index)
        }
    }

    companion object {
        inline fun propertiesAccordion(crossinline init: PropertiesAccordion.() -> Unit) =
            PropertiesAccordion().apply { init() }
    }
}
