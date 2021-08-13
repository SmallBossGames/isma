dependencies {
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-intg-core"))
    implementation(project(":isma-intg-lib:isma-intg-lib-euler"))
    implementation(project(":isma-intg-lib:isma-intg-lib-rk2"))
    implementation(project(":isma-intg-lib:isma-intg-lib-rk3"))
    implementation(project(":isma-intg-lib:isma-intg-lib-rk31"))
    implementation(project(":isma-intg-lib:isma-intg-lib-rkmerson"))
    implementation(project(":isma-intg-lib:isma-intg-lib-rkfehlberg"))
    implementation("org.reflections:reflections:0.9.12")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("com.google.guava:guava:30.0-jre")
}