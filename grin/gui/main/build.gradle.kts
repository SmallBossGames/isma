plugins {
    id("application")
}

application {
    mainClass.set("ru.nstu.grin.main.EntryPointKt")
    applicationDefaultJvmArgs = listOf(
        "--add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED",
        "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED")
}

dependencies {
    implementation(project(":grin:gui:concatenation"))
    api(project(":grin:gui:common"))
}
