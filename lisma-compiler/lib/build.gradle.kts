plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    // hsm-core is exposed through the public API (LismaCompilationResult.Success.model)
    api(project(":lisma-compiler:hsm-core"))
    implementation(project(":lisma-compiler:lisma-translator-hsm"))
    implementation(project(":lisma-compiler:hsm-fdm"))
    implementation(project(":lisma-compiler:hsm-jvm"))
}
