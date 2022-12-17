dependencies {
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-intg-core"))
    implementation(project(":isma-intg-lib:isma-intg-lib-euler"))
    implementation(project(":isma-intg-lib:isma-intg-lib-rk2"))
    implementation(project(":isma-intg-lib:isma-intg-lib-rk3"))
    implementation(project(":isma-intg-lib:isma-intg-lib-rk31"))
    implementation(project(":isma-intg-lib:isma-intg-lib-rkmerson"))
    implementation(project(":isma-intg-lib:isma-intg-lib-rkfehlberg"))
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("com.google.guava:guava:31.1-jre")
}