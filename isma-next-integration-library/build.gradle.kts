plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

group = "ru.nstu.isma.next"
version = "1.0.0"

dependencies {
    implementation(project(":isma-intg-lib"))
    implementation(project(":isma-intg-api"))
    implementation (project(":isma-intg-lib:isma-intg-lib-euler"))
    implementation (project(":isma-intg-lib:isma-intg-lib-rk2"))
    implementation (project(":isma-intg-lib:isma-intg-lib-rk3"))
    implementation (project(":isma-intg-lib:isma-intg-lib-rk31"))
    implementation (project(":isma-intg-lib:isma-intg-lib-rkmerson"))
    implementation (project(":isma-intg-lib:isma-intg-lib-rkfehlberg"))

    implementation(libs.slf4j.api)
}
