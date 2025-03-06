package ru.nstu.isma.compiler.hsm.jvm

import common.HMExpressionBuilder
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.core.hsm.events.HSMEventFunctionGroup
import ru.nstu.isma.core.hsm.events.HSMEventFunctionGroupEvaluator
import ru.nstu.isma.core.hsm.exp.EXPOperator
import ru.nstu.isma.core.hsm.exp.HMExpression
import ru.nstu.isma.core.hsm.hybrid.HMPseudoState
import ru.nstu.isma.core.hsm.hybrid.HMState
import ru.nstu.isma.core.hsm.hybrid.HMTransaction
import ru.nstu.isma.core.hsm.`var`.HMAlgebraicEquation
import ru.nstu.isma.core.hsm.`var`.HMDerivativeEquation
import ru.nstu.isma.intg.api.calcmodel.*
import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup.StepChoiceRule

/**
 * @author Maria Nasyrova
 * @since 05.10.2015
 */
class AnalyzedHybridSystemClassBuilder(
    private val hsm: HSM,
    private val indexProvider: EquationIndexProvider,
    private val packageName: String,
    private val className: String
) {
    private val hmExpressionBuilder = HMExpressionBuilder(indexProvider)

    fun buildSourceCode(): String {
        return renderClassTemplate(
            packageName = packageName,
            importStatements = renderImports(),
            className = className,
            hsbBody = renderHsbBody()
        )
    }

    private fun renderImports(): String {
        return IMPORT_CLASSES.joinToString(separator = "\n") {
            "import ${it.canonicalName};"
        }
    }

    private fun renderHsbBody(): String {
        val initState = renderInitState()
        val states = renderStates()
        val initPseudoState = renderInitPseudoState()
        val pseudoStates = renderPseudoStates()

        return """
            $initState
            $states
            $initPseudoState
            $pseudoStates
        """.trimIndent()
    }

    private fun renderInitState(): String {
        val stateCode = HSM.INIT_STATE
        val differentialEquations = renderDifferentialEquations(hsm.variableTable.odes)
        val algebraicEquations = renderAlgebraicEquations(hsm.variableTable.algs)
        val guards = renderGuards(getTransactions(HSM.INIT_STATE))
        val setters = renderSetters(hsm.variableTable.setters)

        return """
            hsb.addState("$stateCode")
                $differentialEquations
                $algebraicEquations
                $guards
                $setters;
        """.trimIndent()
    }

    private fun renderStates(): String {
        return hsm.automata.states.values
            .filter { HSM.INIT_STATE != it.code }
            .joinToString(separator = "") { renderState(it) }
    }

    private fun renderState(state: HMState): String {
        val stateCode = state.code
        val differentialEquations = renderDifferentialEquations(state.variables.odes)
        val algebraicEquations = renderAlgebraicEquations(state.variables.algs)
        val guards = renderGuards(getTransactions(state.code))
        val setters = renderSetters(state.variables.setters)

        return """

		hsb.addState("$stateCode")
            $differentialEquations
            $algebraicEquations
            $guards
            $setters;"""
    }

    private fun renderInitPseudoState(): String {
        if (hsm.automata.allPseudoStates.isEmpty()) {
            return ""
        }

        val stateCode = HybridSystem.INIT_PSEUDO_STATE
        val guards = hsm.automata.allPseudoStates.joinToString(separator = "") {
            renderGuard(stateCode, it.code, it.condition)
        }

        return """

		hsb.addPseudoState("$stateCode")
            $guards;
        """
    }

    private fun renderPseudoStates(): String {
        return hsm.automata.allPseudoStates.joinToString(separator = "\n") {
            renderPseudoState(it)
        }
    }

    private fun renderPseudoState(state: HMPseudoState): String {
        val stateCode = state.code
        val differentialEquations = renderDifferentialEquations(state.variables.odes)
        val algebraicEquations = renderAlgebraicEquations(state.variables.algs)
        val setters = renderSetters(state.variables.setters)

        return """
		hsb
            .addPseudoState("$stateCode")
            $differentialEquations
            $algebraicEquations
            $setters;
        """
    }

    private fun getTransactions(fromStateCode: String): Set<HMTransaction> {
        return hsm.automata.transactions
            .filter { it.source.code == fromStateCode }
            .toSet()
    }

    private fun renderDifferentialEquations(odes: List<HMDerivativeEquation>): String {
        return odes.joinToString(separator = "\n") {
            val deClassName = DifferentialEquation::class.java.simpleName
            val de = renderDifferentialEquation(it.code, it.rightPart)

            """.add$deClassName($de)"""
        }
    }

    private fun renderDifferentialEquation(code: String, rightPart: HMExpression): String {
        val deClassName = DifferentialEquation::class.java.simpleName
        val index = (indexProvider.getDifferentialEquationIndex(code)!!).toString()
        val de = renderWithRhs(rightPart)

        return """new $deClassName("$code", $index, $de)"""
    }

    private fun renderWithRhs(rightPart: HMExpression): String {
        val expression = hmExpressionBuilder.buildExpression(rightPart, false, true)

        return """(y, rhs) -> ($expression), "$expression""""
    }

    private fun renderAlgebraicEquations(algEquations: List<HMAlgebraicEquation>): String {
        val aeClassName = AlgebraicEquation::class.java.simpleName

        return algEquations.joinToString(separator = "\n") {
            val name = it.code
            val expression = hmExpressionBuilder.buildExpression(it.rightPart, true)
            val index = (indexProvider.getAlgebraicEquationIndex(it.code)!!).toString()

            """.add$aeClassName(new $aeClassName("$name", $index, (y, a) -> ($expression), "$expression"))"""
        }
    }

    private fun renderGuards(transactions: Set<HMTransaction>): String {
        return transactions.joinToString(separator = "") {
            renderGuard(it.source.code, it.target.code, it.condition)
        }
    }

    private fun renderGuard(fromState: String, toState: String, rightPart: HMExpression): String {
        return """
			.addGuard(new Guard("$fromState", "$toState", ${renderWithRhs(rightPart)}))
            ${renderEventFunctionGroup(rightPart)}
            """
    }

    private fun renderSetters(setters: Map<String, HMExpression>) =
        setters.entries.joinToString(separator = "\n") {
            """.addSetter(${renderDifferentialEquation(it.key, it.value)})"""
        }

    private fun renderEventFunctionGroup(guard: HMExpression): String {
        val eventFunctionGroup = HSMEventFunctionGroupEvaluator.evaluate(guard)

        return """
			.addEventFunctionGroup(${renderStepChoiceRule(eventFunctionGroup)})
            ${renderEventFunctions(eventFunctionGroup)}
            """
    }

    private fun renderStepChoiceRule(eventFunctionGroup: HSMEventFunctionGroup): String {
        val ruleValue = when (val operatorCode = eventFunctionGroup.operatorCode) {
            null -> {
                StepChoiceRule.NONE.name
            }
            EXPOperator.Code.AND -> {
                StepChoiceRule.MAX.name
            }
            EXPOperator.Code.OR -> {
                StepChoiceRule.MIN.name
            }
            else -> {
                throw IllegalArgumentException("Unknown operator code \"$operatorCode\"")
            }
        }

        return """${EventFunctionGroup::class.java.simpleName}.${StepChoiceRule::class.java.simpleName}.$ruleValue"""
    }

    private fun renderEventFunctions(
        eventFunctionGroup: HSMEventFunctionGroup
    ) = eventFunctionGroup.expressions
        .joinToString(separator = "") { renderEventFunction(it) }

    private fun renderEventFunction(
        eventFunctionExpression: HMExpression
    ) = """
        .addEventFunction(new EventFunction(${renderWithRhs(eventFunctionExpression)}))
        """

    companion object {
        private val IMPORT_CLASSES = listOf(
                DifferentialEquation::class.java,
                AlgebraicEquation::class.java,
                HybridSystem::class.java,
                HybridSystemBuilder::class.java,
                Guard::class.java,
                EventFunctionGroup::class.java,
                EventFunction::class.java
        )

        @JvmStatic
        private fun renderClassTemplate(
            packageName: String,
            importStatements: String,
            className: String,
            hsbBody: String
        ) = """
            package $packageName;
            $importStatements
            
            /**
             * Generated by ISMA
            */
            
            public class $className extends HybridSystem {
                public $className() {
                    super(hybridSystemBuilder().toHybridSystem());
                }
                
                public static HybridSystemBuilder hybridSystemBuilder() {
                    HybridSystemBuilder hsb = new HybridSystemBuilder();
                    
                    $hsbBody
                    
                    return hsb;
                }
            }
            
            """
    }

}