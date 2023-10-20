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
