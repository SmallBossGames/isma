plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
    id("antlr")
}

dependencies {
    antlr(libs.antlr4)
    implementation(libs.antlr4.runtime)

    testImplementation(libs.junit)

    implementation(project(":lisma-compiler:hsm-core"))
}

tasks.generateGrammarSource {
    arguments = listOf("-visitor", "-package", "ru.nstu.isma.lisma.analysis.gen")
}
