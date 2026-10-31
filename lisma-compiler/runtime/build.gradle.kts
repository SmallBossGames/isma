plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    // All three are exposed through the public API (CompiledLismaModel)
    api(project(":lisma-compiler:hsm-core"))
    api(project(":lisma-compiler:hsm-jvm"))
    api(project(":lisma-compiler:hsm-jvm-calcmodel"))
}
