rootProject.name = "isma"

//JAVA 11+ projects

// isma-ui and grin are git submodules (standalone Gradle projects)
// built separately by .ci-cd/build-bundle.sh

include("lisma-compiler:hsm-core")
include("lisma-compiler:hsm-fdm")
include("lisma-compiler:lisma-translator-hsm")
include("lisma-compiler:hsm-jvm")
include("lisma-compiler:hsm-jvm-calcmodel")
include("lisma-compiler:lib")
include("lisma-compiler:cli")
include("lisma-compiler:runtime")
include("lisma-compiler:lisma-lsp")

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
include("isma-solver:cli")

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

