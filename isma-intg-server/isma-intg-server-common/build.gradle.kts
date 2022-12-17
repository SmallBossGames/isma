dependencies {
    implementation(project(":isma-intg-server:isma-intg-server-api"))
    implementation(project(":isma-intg-core-solvers-parallel"))
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-intg-core"))

    implementation("com.google.guava:guava:31.1-jre")
    implementation("commons-io:commons-io:2.11.0")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("mpj:mpj:0.43")
    implementation("com.esotericsoftware:kryonet:2.22.0-RC1")
}
