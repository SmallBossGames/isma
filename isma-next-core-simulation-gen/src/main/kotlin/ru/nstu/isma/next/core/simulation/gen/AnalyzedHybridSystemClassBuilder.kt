package ru.nstu.isma.next.core.simulation.gen

import com.google.common.collect.ImmutableMap
import common.HMExpressionBuilder
import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup.StepChoiceRule
import java.util.stream.Collectors
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.core.hsm.`var`.HMAlgebraicEquation
import ru.nstu.isma.core.hsm.`var`.HMDerivativeEquation
import ru.nstu.isma.core.hsm.events.HSMEventFunctionGroup
import ru.nstu.isma.core.hsm.events.HSMEventFunctionGroupEvaluator
import ru.nstu.isma.core.hsm.exp.EXPOperator
import ru.nstu.isma.core.hsm.exp.HMExpression
import ru.nstu.isma.core.hsm.hybrid.HMPseudoState
import ru.nstu.isma.core.hsm.hybrid.HMState
import ru.nstu.isma.core.hsm.hybrid.HMTransaction
import ru.nstu.isma.intg.api.calcmodel.*
import java.lang.IllegalArgumentException

import org.apache.commons.text.StringSubstitutor

/**
 * @author Maria Nasyrova
 * @since 05.10.2015
 */
class AnalyzedHybridSystemClassBuilder(private val hsm: HSM, private val indexProvider: EquationIndexProvider, private val packageName: String, private val className: String) {
    private val hmExpressionBuilder: HMExpressionBuilder =
        HMExpressionBuilder(indexProvider)

    fun buildSourceCode(): String {
        val values = ImmutableMap.of(
                "packageName", packageName,
                "importStatements", renderImports(IMPORT_CLASSES),
                "className", className,
                "hsbBody", renderHsbBody()
        )

        return StringSubstitutor(values).replace(GENERATED_CLASS_TEMPLATE)
    }

    private fun renderImports(classes: List<Class<*>>): String {
        val template = "\nimport %s;"
        return classes.stream()
                .map { obj: Class<*> -> obj.canonicalName }
                .map { className: String? -> String.format(template, className) }
                .collect(Collectors.joining())
    }

    private fun renderHsbBody(): String {
        val template = "" +
                "\${initState}" +
                "\${states}" +
                "\${initPseudoState}" +
                "\${pseudoStates}"

        val sub = StringSubstitutor(ImmutableMap.of(
                "initState", renderInitState(),
                "states", renderStates(),
                "initPseudoState", renderInitPseudoState(),
                "pseudoStates", renderPseudoStates()
        ))

        return sub.replace(template)
    }

    private fun renderInitState(): String {
        val template = "\t\t" +
                "hsb.addState(\"\${stateCode}\")" +
                "\${ru.isma.next.math.engine.differentialEquations}" +
                "\${algebraicEquations}" +
                "\${guards}" +
                "\${setters};"

        val sub = StringSubstitutor(ImmutableMap.of(
                "stateCode", HSM.INIT_STATE,
                "ru.isma.next.math.engine.differentialEquations", renderDifferentialEquations(hsm.variableTable.odes),
                "algebraicEquations", renderAlgebraicEquations(hsm.variableTable.algs),
                "guards", renderGuards(getTransactions(HSM.INIT_STATE)),
                "setters", renderSetters(hsm.variableTable.setters)
        ))

        return sub.replace(template)
    }

    private fun renderStates(): String {
        return hsm.automata.states.values.stream()
                .filter { state: HMState -> HSM.INIT_STATE != state.code }
                .map { state: HMState -> renderState(state) }
                .collect(Collectors.joining())
    }

    private fun renderState(state: HMState): String {
        val template = """

		hsb.addState("${"$"}{stateCode}")${"$"}{ru.isma.next.math.engine.differentialEquations}${"$"}{algebraicEquations}${"$"}{guards}${"$"}{setters};"""

        val sub = StringSubstitutor(ImmutableMap.builder<String, String>()
                .put("stateCode", state.code)
                .put("ru.isma.next.math.engine.differentialEquations", renderDifferentialEquations(state.variables.odes))
                .put("algebraicEquations", renderAlgebraicEquations(state.variables.algs))
                .put("guards", renderGuards(getTransactions(state.code)))
                .put("setters", renderSetters(state.variables.setters))
                .build())

        return sub.replace(template)
    }

    private fun renderInitPseudoState(): String {
        if (hsm.automata.allPseudoStates.isEmpty()) {
            return ""
        }
        val template = """

		hsb.addPseudoState("${"$"}{stateCode}")${"$"}{guards};"""
        val guards = hsm.automata.allPseudoStates.stream()
                .map { ps: HMPseudoState -> renderGuard(HybridSystem.INIT_PSEUDO_STATE, ps.code, ps.condition) }
                .collect(Collectors.joining())

        val sub = StringSubstitutor(ImmutableMap.of(
                "stateCode", HybridSystem.INIT_PSEUDO_STATE,
                "guards", guards))

        return sub.replace(template)
    }

    private fun renderPseudoStates(): String {
        return hsm.automata.allPseudoStates.stream()
                .map { state: HMPseudoState -> renderPseudoState(state) }
                .collect(Collectors.joining())
    }

    private fun renderPseudoState(state: HMPseudoState): String {
        val template = """

		hsb.addPseudoState("${"$"}{stateCode}")${"$"}{ru.isma.next.math.engine.differentialEquations}${"$"}{algebraicEquations}${"$"}{setters};"""

        val sub = StringSubstitutor(ImmutableMap.of(
                "stateCode", state.code,
                "ru.isma.next.math.engine.differentialEquations", renderDifferentialEquations(state.variables.odes),
                "algebraicEquations", renderAlgebraicEquations(state.variables.algs),
                "setters", renderSetters(state.variables.setters)
        ))

        return sub.replace(template)
    }

    private fun getTransactions(fromStateCode: String): Set<HMTransaction> {
        return hsm.automata.transactions.stream()
                .filter { t: HMTransaction -> t.source.code == fromStateCode }
                .collect(Collectors.toSet())
    }

    private fun renderDifferentialEquations(odes: List<HMDerivativeEquation>): String {
        val template = """
			.add${"$"}{deClassName}(${"$"}{de})"""

        return odes.stream().map {
            val sub = StringSubstitutor(ImmutableMap.of(
                    "deClassName", DifferentialEquation::class.java.simpleName,
                    "de", renderDifferentialEquation(it.code, it.rightPart)
            ))
            sub.replace(template)
        }.collect(Collectors.joining())
    }

    private fun renderDifferentialEquation(code: String, rightPart: HMExpression): String {
        val template = "new \${deClassName}(\"\${name}\", \${index}, \${de})"
        val de = renderWithRhs(rightPart)
        val sub = StringSubstitutor(ImmutableMap.of(
                "deClassName", DifferentialEquation::class.java.simpleName,
                "index", (indexProvider.getDifferentialEquationIndex(code)!!).toString(),
                "name", code,
                "de", de
        ))

        return sub.replace(template)
    }

    private fun renderWithRhs(rightPart: HMExpression): String {
        val template = "(y, rhs) -> (\${expression}), \"\${desc}\""
        val expression: String = hmExpressionBuilder.buildExpression(rightPart, false, true)
        val sub = StringSubstitutor(ImmutableMap.of(
                "expression", expression,
                "desc", expression
        ))
        return sub.replace(template)
    }

    private fun renderAlgebraicEquations(algEquations: List<HMAlgebraicEquation>): String {
        val template = """
			.add${"$"}{aeClassName}(new ${"$"}{aeClassName}("${"$"}{name}", ${"$"}{index}, (y, a) -> (${"$"}{expression}), "${"$"}{desc}"))"""
        return algEquations.stream().map {
            val expression = hmExpressionBuilder.buildExpression(it.rightPart, true)
            val sub = StringSubstitutor(ImmutableMap.of(
                    "aeClassName", AlgebraicEquation::class.java.simpleName,
                    "index", (indexProvider.getAlgebraicEquationIndex(it.code)!!).toString(),
                    "name", it.code,
                    "expression", expression,
                    "desc", expression
            ))
            sub.replace(template)
        }.collect(Collectors.joining())
    }

    private fun renderGuards(transactions: Set<HMTransaction>): String {
        return transactions.stream()
                .map { renderGuard(it.source.code, it.target.code, it.condition) }
                .collect(Collectors.joining())
    }

    private fun renderGuard(fromState: String, toState: String, rightPart: HMExpression): String {
        val template = """
			.addGuard(new Guard("${"$"}{from}", "${"$"}{to}", ${"$"}{condition}))${"$"}{eventFunctionGroup}"""
        val sub = StringSubstitutor(ImmutableMap.of(
                "from", fromState,
                "to", toState,
                "condition", renderWithRhs(rightPart),
                "eventFunctionGroup", renderEventFunctionGroup(rightPart)
        ))
        return sub.replace(template)
    }

    private fun renderSetters(setters: Map<String, HMExpression>): String {
        val template = """
			.addSetter(${"$"}{de})"""
        return setters.entries.stream().map {
            val sub = StringSubstitutor(ImmutableMap.of(
                    "de", renderDifferentialEquation(it.key, it.value)
            ))
            sub.replace(template)
        }.collect(Collectors.joining())
    }

    private fun renderEventFunctionGroup(guard: HMExpression): String {
        val template = """
				.addEventFunctionGroup(${"$"}{stepChoiceRule})${"$"}{eventFunctions}"""
        val eventFunctionGroup: HSMEventFunctionGroup = HSMEventFunctionGroupEvaluator.evaluate(guard)
        val sub = StringSubstitutor(ImmutableMap.of(
                "stepChoiceRule", renderStepChoiceRule(eventFunctionGroup),
                "eventFunctions", renderEventFunctions(eventFunctionGroup)
        ))
        return sub.replace(template)
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
        return EventFunctionGroup::class.java.simpleName + "." +
                StepChoiceRule::class.java.simpleName + "." +
                ruleValue
    }

    private fun renderEventFunctions(eventFunctionGroup: HSMEventFunctionGroup): String {
        return eventFunctionGroup.expressions.stream()
                .map { renderEventFunction(it) }
                .collect(Collectors.joining())
    }

    private fun renderEventFunction(eventFunctionExpression: HMExpression): String {
        val template = """
					.addEventFunction(new EventFunction(${"$"}{expression}))"""
        val sub = StringSubstitutor(ImmutableMap.of(
                "expression", renderWithRhs(eventFunctionExpression)
        ))
        return sub.replace(template)
    }

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
        private const val GENERATED_CLASS_TEMPLATE = "" +
                "package \${packageName};\n" +
                "\${importStatements}\n" +
                "\n" +
                "/**\n" +
                " * Generated by ISMA\n" +
                " */\n" +
                "public class \${className} extends HybridSystem {\n" +
                "\tpublic \${className}() {\n" +
                "\t\tsuper(hybridSystemBuilder().toHybridSystem());\n" +
                "\t}\n" +
                "\n" +
                "\tpublic static HybridSystemBuilder hybridSystemBuilder() {\n" +
                "\t\tHybridSystemBuilder hsb = new HybridSystemBuilder();\n" +
                "\n" +
                "\${hsbBody}\n" +
                "\n" +
                "\t\treturn hsb;\n" +
                "\t}\n" +
                "\n" +
                "}\n"
    }

}