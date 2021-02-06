package ru.nstu.isma.next.core.sim.controller.gen

import com.google.common.collect.ImmutableMap
import common.HMExpressionBuilder
import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup.StepChoiceRule
import java.lang.IllegalStateException
import java.util.stream.Collectors
import common.IndexMapper
import ru.nstu.isma.core.hsm.HSM
import common.JavaClassBuilder
import javax.tools.JavaFileManager
import java.util.Arrays
import javax.tools.JavaFileObject
import java.lang.RuntimeException
import java.lang.ClassNotFoundException
import java.lang.IllegalAccessException
import java.util.HashMap
import common.IndexProvider
import org.apache.commons.lang3.text.StrSubstitutor
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
import java.util.function.Function
import java.util.function.Predicate

/**
 * @author Maria Nasyrova
 * @since 05.10.2015
 */
class AnalyzedHybridSystemClassBuilder(private val hsm: HSM, private val indexProvider: EquationIndexProvider, private val packageName: String, private val className: String) {
    private val hmExpressionBuilder: HMExpressionBuilder
    fun buildSourceCode(): String {
        val values = ImmutableMap.of(
                "packageName", packageName,
                "importStatements", renderImports(IMPORT_CLASSES),
                "className", className,
                "hsbBody", renderHsbBody()
        )
        return StrSubstitutor.replace(GENERATED_CLASS_TEMPLATE, values)
    }

    private fun renderImports(classes: Collection<Class<*>>): String {
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
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "initState", renderInitState(),
                "states", renderStates(),
                "initPseudoState", renderInitPseudoState(),
                "pseudoStates", renderPseudoStates()
        ))
    }

    private fun renderInitState(): String {
        val template = "\t\t" +
                "hsb.addState(\"\${stateCode}\")" +
                "\${differentialEquations}" +
                "\${algebraicEquations}" +
                "\${guards}" +
                "\${setters};"
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "stateCode", HSM.INIT_STATE,
                "differentialEquations", renderDifferentialEquations(hsm.variableTable.odes),
                "algebraicEquations", renderAlgebraicEquations(hsm.variableTable.algs),
                "guards", renderGuards(getTransactions(HSM.INIT_STATE)),
                "setters", renderSetters(hsm.variableTable.setters)
        ))
    }

    private fun renderStates(): String {
        return hsm.automata.states.values.stream()
                .filter(Predicate { state: HMState -> HSM.INIT_STATE != state.code })
                .map(Function { state: HMState -> renderState(state) })
                .collect(Collectors.joining())
    }

    private fun renderState(state: HMState): String {
        val template = """

		hsb.addState("${"$"}{stateCode}")${"$"}{differentialEquations}${"$"}{algebraicEquations}${"$"}{guards}${"$"}{setters};"""
        return StrSubstitutor.replace(template, ImmutableMap.builder<String, String>()
                .put("stateCode", state.code)
                .put("differentialEquations", renderDifferentialEquations(state.variables.odes))
                .put("algebraicEquations", renderAlgebraicEquations(state.variables.algs))
                .put("guards", renderGuards(getTransactions(state.code)))
                .put("setters", renderSetters(state.variables.setters))
                .build()
        )
    }

    private fun renderInitPseudoState(): String {
        if (hsm.automata.allPseudoStates.isEmpty()) {
            return ""
        }
        val template = """

		hsb.addPseudoState("${"$"}{stateCode}")${"$"}{guards};"""
        val guards = hsm.automata.allPseudoStates.stream()
                .map(Function { ps: HMPseudoState -> renderGuard(HybridSystem.INIT_PSEUDO_STATE, ps.code, ps.condition) })
                .collect(Collectors.joining())
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "stateCode", HybridSystem.INIT_PSEUDO_STATE,
                "guards", guards
        ))
    }

    private fun renderPseudoStates(): String {
        return hsm.automata.allPseudoStates.stream()
                .map(Function { state: HMPseudoState -> renderPseudoState(state) })
                .collect(Collectors.joining())
    }

    private fun renderPseudoState(state: HMPseudoState): String {
        val template = """

		hsb.addPseudoState("${"$"}{stateCode}")${"$"}{differentialEquations}${"$"}{algebraicEquations}${"$"}{setters};"""
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "stateCode", state.code,
                "differentialEquations", renderDifferentialEquations(state.variables.odes),
                "algebraicEquations", renderAlgebraicEquations(state.variables.algs),
                "setters", renderSetters(state.variables.setters)
        ))
    }

    private fun getTransactions(fromStateCode: String): Collection<HMTransaction> {
        return hsm.automata.transactions.stream()
                .filter(Predicate { t: HMTransaction -> t.source.code == fromStateCode })
                .collect(Collectors.toSet())
    }

    private fun renderDifferentialEquations(odes: Collection<HMDerivativeEquation>): String {
        val template = """
			.add${"$"}{deClassName}(${"$"}{de})"""
        return odes.stream().map(Function<HMDerivativeEquation, String> { ode: HMDerivativeEquation ->
            StrSubstitutor.replace(template, ImmutableMap.of(
                    "deClassName", DifferentialEquation::class.java.simpleName,
                    "de", renderDifferentialEquation(ode.code, ode.rightPart)
            ))
        }).collect(Collectors.joining())
    }

    private fun renderDifferentialEquation(code: String, rightPart: HMExpression): String {
        val template = "new \${deClassName}(\"\${name}\", \${index}, \${de})"
        val de = renderWithRhs(rightPart)
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "deClassName", DifferentialEquation::class.java.simpleName,
                "index", Integer.toString(indexProvider.getDifferentialEquationIndex(code)!!),
                "name", code,
                "de", de
        ))
    }

    private fun renderWithRhs(rightPart: HMExpression): String {
        val template = "(y, rhs) -> (\${expression}), \"\${desc}\""
        val expression: String = hmExpressionBuilder.buildExpression(rightPart, false, true)
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "expression", expression,
                "desc", expression
        ))
    }

    private fun renderAlgebraicEquations(algEquations: Collection<HMAlgebraicEquation>): String {
        val template = """
			.add${"$"}{aeClassName}(new ${"$"}{aeClassName}("${"$"}{name}", ${"$"}{index}, (y, a) -> (${"$"}{expression}), "${"$"}{desc}"))"""
        return algEquations.stream().map(Function<HMAlgebraicEquation, String> { ae: HMAlgebraicEquation ->
            val expression: String = hmExpressionBuilder.buildExpression(ae.rightPart, true)
            StrSubstitutor.replace(template, ImmutableMap.of(
                    "aeClassName", AlgebraicEquation::class.java.simpleName,
                    "index", Integer.toString(indexProvider.getAlgebraicEquationIndex(ae.code)!!),
                    "name", ae.code,
                    "expression", expression,
                    "desc", expression
            ))
        }).collect(Collectors.joining())
    }

    private fun renderGuards(transactions: Collection<HMTransaction>): String {
        return transactions.stream()
                .map(Function<HMTransaction, String> { t: HMTransaction -> renderGuard(t.source.code, t.target.code, t.condition) })
                .collect(Collectors.joining())
    }

    private fun renderGuard(fromState: String, toState: String, rightPart: HMExpression): String {
        val template = """
			.addGuard(new Guard("${"$"}{from}", "${"$"}{to}", ${"$"}{condition}))${"$"}{eventFunctionGroup}"""
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "from", fromState,
                "to", toState,
                "condition", renderWithRhs(rightPart),
                "eventFunctionGroup", renderEventFunctionGroup(rightPart)
        ))
    }

    private fun renderSetters(setters: Map<String, HMExpression>): String {
        val template = """
			.addSetter(${"$"}{de})"""
        return setters.entries.stream().map {
            StrSubstitutor.replace(template, ImmutableMap.of(
                    "de", renderDifferentialEquation(it.key, it.value)
            ))
        }.collect(Collectors.joining())
    }

    private fun renderEventFunctionGroup(guard: HMExpression): String {
        val template = """
				.addEventFunctionGroup(${"$"}{stepChoiceRule})${"$"}{eventFunctions}"""
        val eventFunctionGroup: HSMEventFunctionGroup = HSMEventFunctionGroupEvaluator.evaluate(guard)
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "stepChoiceRule", renderStepChoiceRule(eventFunctionGroup),
                "eventFunctions", renderEventFunctions(eventFunctionGroup)
        ))
    }

    private fun renderStepChoiceRule(eventFunctionGroup: HSMEventFunctionGroup): String {
        val operatorCode = eventFunctionGroup.operatorCode
        var ruleValue: String? = null
        ruleValue = if (operatorCode == null) {
            StepChoiceRule.NONE.name
        } else if (operatorCode == EXPOperator.Code.AND) {
            StepChoiceRule.MAX.name
        } else if (operatorCode == EXPOperator.Code.OR) {
            StepChoiceRule.MIN.name
        } else {
            throw IllegalArgumentException("Unknown operator code \"$operatorCode\"")
        }
        return EventFunctionGroup::class.java.simpleName + "." +
                StepChoiceRule::class.java.simpleName + "." +
                ruleValue
    }

    private fun renderEventFunctions(eventFunctionGroup: HSMEventFunctionGroup): String {
        return eventFunctionGroup.expressions.stream()
                .map<String>(Function<HMExpression, String> { eventFunctionExpression: HMExpression -> renderEventFunction(eventFunctionExpression) })
                .collect(Collectors.joining())
    }

    private fun renderEventFunction(eventFunctionExpression: HMExpression): String {
        val template = """
					.addEventFunction(new EventFunction(${"$"}{expression}))"""
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "expression", renderWithRhs(eventFunctionExpression)
        ))
    }

    companion object {
        private val IMPORT_CLASSES = Arrays.asList(
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

    init {
        hmExpressionBuilder = HMExpressionBuilder(indexProvider)
    }
}