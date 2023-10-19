dependencies {
    testImplementation(project(":isma-intg-server:isma-intg-server-api"))
    testImplementation(project(":isma-intg-server:isma-intg-server-client"))
    testImplementation(project(":isma-intg-server:isma-intg-server-common"))
    testImplementation(project(":isma-intg-lib:isma-intg-lib-rk2"))
    testImplementation(project(":isma-intg-lib:isma-intg-lib-rk3"))
    testImplementation(project(":isma-intg-lib:isma-intg-lib-rkmerson"))

    implementation(project(":isma-intg-api"))
    implementation(project(":isma-intg-core"))

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.tngtech.java:junit-dataprovider:1.13.1")
    testImplementation(project(":isma-intg-lib:isma-intg-lib-common"))
    testImplementation(project(":isma-intg-demo-problems"))
    testImplementation("mpj:mpj:0.43")
    testImplementation("ch.qos.logback:logback-classic:1.4.7")
    testImplementation("com.github.jbellis:jamm:0.3.3")
}
