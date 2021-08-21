package ru.nstu.isma.ismanextpurejfx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val group = MainView()

        val scene = Scene(group.root, 320.0, 240.0)

        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}