package ru.nstu.grin.concatenation.file.utilities

import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.stage.Window

fun showError(message: String, owner: Window? = null) {
    val alert = Alert(AlertType.ERROR)
    alert.contentText = message
    if (owner != null) {
        alert.initOwner(owner)
    }
    alert.showAndWait()
}
