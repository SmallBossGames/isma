rootProject.name = "isma"
//JAVA 11+ projects
include("isma-ui:app")
include("isma-ui:text-editor")
include("isma-ui:blueprint-editor")
include("isma-ui:toolkit")
include("isma-ui:grin-nested")

include("isma-next-core")

include("grin:gui")
include("grin:analytic-fu")
include("grin:math")
include("grin:gui:concatenation")
include("grin:gui:common")
include("grin:gui:app")

include("isma-compiler:hsm-core")
include("isma-compiler:hsm-fdm")
include("isma-compiler:lisma-translator-hsm")
include("isma-compiler:jvm-utils")
include("isma-compiler:hsm-jvm")

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
include("isma-intg-demo-problems")


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

include("isma-next-math-engine")
include("isma-next-math-common")
