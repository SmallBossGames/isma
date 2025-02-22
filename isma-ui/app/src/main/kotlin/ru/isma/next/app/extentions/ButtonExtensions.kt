package ru.isma.next.app.extentions

import org.kordamp.ikonli.javafx.FontIcon

fun matIconAL(iconName: String): FontIcon {
    return FontIcon().apply {
        iconLiteral="mdoal-$iconName"
        iconSize = 20
    }
}

fun matIconMZ(iconName: String): FontIcon {
    return FontIcon().apply {
        iconLiteral="mdomz-$iconName"
        iconSize = 20
    }
}