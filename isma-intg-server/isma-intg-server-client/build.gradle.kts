plugins {
    id("org.javamodularity.moduleplugin")
}

dependencies {
    implementation (project(":isma-intg-server:isma-intg-server-api"))
    implementation (project(":isma-intg-api"))

    implementation ("com.google.guava:guava:31.1-jre")
    implementation ("com.esotericsoftware:kryonet:2.22.0-RC1")

    testImplementation ("junit:junit:4.13.2")
    testImplementation ("com.tngtech.java:junit-dataprovider:1.13.1")
    testImplementation (project(":isma-intg-lib:isma-intg-lib-common"))
    testImplementation ("mpj:mpj:0.43")
    testImplementation ("ch.qos.logback:logback-classic:1.4.5")
    testImplementation ("com.github.jbellis:jamm:0.3.3")
    testImplementation (project(":isma-intg-demo-problems"))
}
