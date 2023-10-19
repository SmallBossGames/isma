rootProject.name = "isma"
//JAVA 11+ projects
include("isma-next-app")

include("isma-next-core")
include("isma-next-tools")
include("isma-next-integration-library")
include("isma-next-common-services")
include("isma-blueprint-editor")
include("isma-text-editor")

include("grin:gui")
include("grin:analytic-fu")
include("grin:math")
include("grin:gui:concatenation")
include("grin:gui:common")
include("grin:integration")
include("grin:app")

//Java 8 projects
include("isma-hsm")
include("isma-intg-api")
include("isma-intg-core")
include("isma-intg-core-solvers-parallel")
include("isma-intg-demo-problems")
include("isma-intg-lib:isma-intg-lib-common")
include("isma-intg-lib:isma-intg-lib-euler")
include("isma-intg-lib:isma-intg-lib-rk2")
include("isma-intg-lib:isma-intg-lib-rk3")
include("isma-intg-lib:isma-intg-lib-rk31")
include("isma-intg-lib:isma-intg-lib-rkmerson")
include("isma-intg-lib:isma-intg-lib-rkfehlberg")
include("isma-intg-server:isma-intg-server-common")
include("isma-intg-server:isma-intg-server-api")
include("isma-intg-server:isma-intg-server-client")
include("isma-intg-server:isma-intg-server-tests")
include("isma-lisma")

include("isma-intg-parallel-ignite")


// Java 8 legacy
//include "grin:gui:simple"
//include("isma-app")
//include("isma-ui")
//include("isma-ui-graph")
//include("isma-distrib")
//include("isma-intg-demo")
//include("isma-core")
//include("isma-tools")
//include("state-chart")

include("isma-next-math-engine")
include("isma-next-math-common")
include("isma-next-services-simulation-abstractions")
include("isma-next-core-fdm")
include("isma-next-core-simulation-gen")
include("isma-javafx-extensions")

dependencyResolutionManagement{
    versionCatalogs {
        create("libs") {
            version("kotlinx-coroutines", "1.6.4")
            library("koin-core", "io.insert-koin:koin-core:3.5.0")
            library("tornadofx-core","no.tornado:tornadofx:1.7.20")
            library("kotlin-reflect","org.jetbrains.kotlin:kotlin-reflect:1.7.22")
            library("kotlinx-coroutines-core","org.jetbrains.kotlinx","kotlinx-coroutines-core").versionRef("kotlinx-coroutines")
            library("kotlinx-coroutines-javafx","org.jetbrains.kotlinx","kotlinx-coroutines-javafx").versionRef("kotlinx-coroutines")
            library("kotlinx-serialization-json","org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            library("antlr4-runtime","org.antlr:antlr4-runtime:4.11.1")
            library("fxmisc-richtext-core","org.fxmisc.richtext:richtextfx:0.11.0")
        }
    }
}

