group = "ru.nstu.isma.next"
version = "1.0.0"

dependencies {
    implementation(project(":isma-solver:lib:euler"))
    implementation(project(":isma-solver:lib:rk2"))
    implementation(project(":isma-solver:lib:rk3"))
    implementation(project(":isma-solver:lib:rk31"))
    implementation(project(":isma-solver:lib:rkmerson"))
    implementation(project(":isma-solver:lib:rkfehlberg"))
}