rootProject.name = "isma"

//JAVA 11+ projects

// isma-ui and grin are git submodules (standalone Gradle projects)
// built separately by .ci-cd/build-bundle.sh

include("isma-next-core")

include("isma-compiler:hsm-core")
include("isma-compiler:hsm-fdm")
include("isma-compiler:lisma-translator-hsm")
include("isma-compiler:hsm-jvm")
include("isma-compiler:hsm-jvm-calcmodel")

include("isma-solver:api")
include("isma-solver:core")
include("isma-solver:lib-utils")
include("isma-solver:lib-meta")
include("isma-solver:lib:euler")
include("isma-solver:lib:rk2")
include("isma-solver:lib:rk3")
include("isma-solver:lib:rk31")
include("isma-solver:lib:rkmerson")
include("isma-solver:lib:rkfehlberg")

//Java 8 projects


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

// Legacy
//include("isma-intg-server:isma-intg-server-common")
//include("isma-intg-server:isma-intg-server-api")
//include("isma-intg-server:isma-intg-server-client")
//include("isma-intg-server:isma-intg-server-tests")
//include("isma-intg-core-solvers-parallel")
//include("isma-intg-parallel-ignite")
//include("isma-intg-demo-problems")

include("isma-next-math-engine")
include("isma-next-math-common")

include("isma-jvm-lib:exchange-format")

include("isma-server:domain")
include("isma-server:grpc")
include("isma-server:infrastructure")
include("isma-server:app")
