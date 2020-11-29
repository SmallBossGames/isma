package ru.nstu.isma.next.core.sim.fdm

import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.core.hsm.`var`.*
import ru.nstu.isma.core.hsm.`var`.pde.HMBoundaryCondition
import ru.nstu.isma.core.hsm.`var`.pde.HMPartialDerivativeEquation
import ru.nstu.isma.core.hsm.`var`.pde.HMSampledSpatialVariable
import ru.nstu.isma.core.hsm.exp.*
import ru.nstu.isma.core.hsm.service.PDEInitialValueCalculator
import java.lang.StringBuilder
import java.util.*
import java.util.function.Consumer

/**
 * Created by Bessonov Alex on 02.03.14.
 */
class FDMNewConverter(private val model: HSM?) {
    private val approximatedVariables: LinkedList<HMSampledSpatialVariable> = LinkedList<HMSampledSpatialVariable>()
    private val approximatedEquations: LinkedList<HMEquation> = LinkedList<HMEquation>()
    private val notApproximated: LinkedList<HMVariable> = LinkedList<HMVariable>()
    private var indexIterator: FDMIndexIterator? = null
    private val vT: HMVariableTable
        get() {
            if (model == null) {
                throw RuntimeException("Model not init!")
            }
            return model.variableTable
        }

    fun convert(): HSM? {
        try {
            healModelPhase()
            preparePhase()
            generateObjectPhase()
            correctRightPartsPhase()
            initialConditionPhase()
            boundsPhase()
            finishPhase()
        } catch (e: Exception) {
            println(e)
            e.printStackTrace()
        }
        return model
    }

    fun healModelPhase() {
        for (k in vT.keySet()) {
            val vv: HMVariable = vT.get(k)
            if (vv is HMEquation) {
                val eq: HMEquation = vv
                if (eq.rightPart != null) {
                    healModelEquation(eq.rightPart)
                }
            }
        }
    }

    fun healModelEquation(expr: HMExpression) {
        for (tt in expr.tokens) {
            if (tt is EXPFunctionOperand) {
                val f: EXPFunctionOperand = tt
                f.args.stream().forEach { e: HMExpression -> healModelEquation(e) }
            } else if (tt is EXPOperand) {
                val o: EXPOperand = tt
                if (o.variable != null && vT.contain(o.variable.code)) {
                    o.variable = vT.get(o.variable.code)
                }
            }
        }
    }

    fun preparePhase() {
        val variableCodes: Set<String> = vT.keySet()

        // запоминаем все апроксимируемые переменные и ДУЧП
        for (varCode in variableCodes) {
            val variable: HMVariable = vT.get(varCode)
            if (variable is HMPartialDerivativeEquation) {
                approximatedEquations.add(variable as HMEquation)
            } else if (variable is HMSampledSpatialVariable) {
                approximatedVariables.add(variable)
            }
        }

        // выявляем пассивные апроксимируемые элементы
        var isReady = false
        while (!isReady) {
            isReady = true
            for (varCode in variableCodes) {
                val equation: HMVariable = vT.get(varCode)
                if (isNeedToAddIntoApproximateEq(equation)) {
                    approximatedEquations.add(equation as HMEquation)
                    isReady = false
                }
            }
        }

        // запоминаем все не апроксимируемые элементы
        for (varCode in variableCodes) {
            val `var`: HMVariable = vT.get(varCode)
            if (!approximatedEquations.contains(`var`) && !approximatedVariables.contains(`var`)) notApproximated.add(`var`)
        }

        // создаем итератор
        indexIterator = newIterator
    }

    fun generateObjectPhase() {
        indexIterator!!.start()
        doGenerateObjectPhase()
        if (!indexIterator!!.atEnd()) do {
            indexIterator!!.next()
            doGenerateObjectPhase()
        } while (!indexIterator!!.atEnd())
    }

    fun doGenerateObjectPhase() {
        // прописываем все аппроксимируемые переменные как константы
        for (i in indexIterator!!.get()) {
            vT.add(i.toConst())
        }
        // генерируем для каждого уравнения аналог для сетки
        for (equation in approximatedEquations) {
            val newName = equationNameMapping(equation)
            if (vT.contain(newName)) {
                continue
            }
            // создаем объект нового уравнения
            var newEq: HMEquation? = null
            // ОДУ и ДУЧП
            if (equation is HMDerivativeEquation) {
                newEq = HMDerivativeEquation(newName)
                (newEq as HMDerivativeEquation?)!!.initial = equation.initial
                // алгебраические
            } else if (equation is HMAlgebraicEquation) {
                newEq = HMAlgebraicEquation(newName)
            }
            if (newEq == null) {
                throw RuntimeException("FDMConverter -> processEquation error!")
            }
            newEq.rightPart = equation.rightPart
            vT.add(newEq)
        }
    }

    fun correctRightPartsPhase() {
        indexIterator!!.start()
        doCorrectRightPartsPhase()
        if (!indexIterator!!.atEnd()) do {
            indexIterator!!.next()
            doCorrectRightPartsPhase()
        } while (!indexIterator!!.atEnd())
    }

    fun doCorrectRightPartsPhase() {
        for (eq in approximatedEquations) {
            val appEq: HMEquation = getMappedVar(eq) as HMEquation
            appEq.rightPart = correctRightParts(appEq.rightPart)
            if (appEq is HMDerivativeEquation) {
                val der: HMDerivativeEquation = appEq
                val initial = HMConst(der.initial.code)
                initial.rightPart = correctRightParts(der.initial.rightPart)
                der.initial = initial
            }
        }
    }

    private fun correctRightParts(oldRP: HMExpression): HMExpression {
        val newRP = HMExpression()
        newRP.type = oldRP.type
        // пробегаем по правой части и меняем все аппроксимируемые переменные
        for ((index, tt) in oldRP.tokens.withIndex()) {
            if (tt is EXPPDEOperand) {
                val o: EXPPDEOperand = tt as EXPPDEOperand
                var uc: HMUnnamedConst
                val pde: HMPartialDerivativeEquation = vT.get(o.getVariable().getCode()) as HMPartialDerivativeEquation
                val av: HMSampledSpatialVariable = o.getSampledFirstSpatialVar()
                val idx = indexIterator!!.getIndex(av.code)
                addSubst(pde, av, idx, o, newRP)
                /*
                    HMEquation eq_idx_plus_1 = (HMEquation) getVT().get(equationNameMappingSpecIndex(pde, av, idx.getIndex() + 1));
                    HMEquation eq_idx_minus_1 = (HMEquation) getVT().get(equationNameMappingSpecIndex(pde, av, idx.getIndex() - 1));
                    HMEquation eq_cur = (HMEquation) getVT().get(equationNameMapping(pde));

                    if (eq_idx_plus_1 == null || eq_idx_minus_1 == null || eq_cur == null) {
                        throw new RuntimeException("FDM: all is bad");
                    }
                    if (o.getOrder() == EXPPDEOperand.Order.ONE) {
                        uc = new HMUnnamedConst(o.getSampledFirstSpatialVar().getStepSize());
                        newRP.add(new EXPOperand(eq_idx_plus_1));
                        newRP.add(new EXPOperand(eq_idx_minus_1));
                        newRP.add(EXPOperator.sub());
                        newRP.add(new EXPOperand(new HMUnnamedConst(2)));
                        newRP.add(new EXPOperand(uc));
                        newRP.add(EXPOperator.mult());
                        newRP.add(EXPOperator.div());
                    } else if (o.getOrder() == EXPPDEOperand.Order.TWO) {
                        uc = new HMUnnamedConst(Math.pow(o.getSampledFirstSpatialVar().getStepSize(), 2));
                        newRP.add(new EXPOperand(eq_idx_minus_1));
                        newRP.add(new EXPOperand(new HMUnnamedConst(2)));
                        newRP.add(new EXPOperand(eq_cur));
                        newRP.add(EXPOperator.mult());
                        newRP.add(EXPOperator.sub());
                        newRP.add(new EXPOperand(eq_idx_plus_1));
                        newRP.add(EXPOperator.add());
                        newRP.add(new EXPOperand(uc));
                        newRP.add(EXPOperator.div());
                    }*/
            } else if (tt is EXPFunctionOperand) {
                val func = EXPFunctionOperand(tt.name)
                for (exp in tt.args) {
                    func.addArgExpression(correctRightParts(exp))
                }
                newRP.add(func)
            } else if (tt is EXPOperand && tt !is EXPPDEOperand) {
                val o: EXPOperand = tt
                // TODO выражения могут хранить неправедбные объекты
                val mappedVar: HMVariable = getMappedVar(o.variable)
                if (mappedVar !== o.variable) {
                    oldRP.tokens[index] = EXPOperand(mappedVar)
                }
                newRP.add(tt)
            } else {
                newRP.add(tt)
            }
        }
        return newRP
    }

    private fun addSubst(pde: HMPartialDerivativeEquation, av: HMSampledSpatialVariable, idx: FDMIndexedApxVar?, o: EXPPDEOperand,
                         newRP: HMExpression) {
        val eq_idx_plus_1: HMEquation = vT.get(equationNameMappingSpecIndex(pde, av, idx!!.index!! + 1)) as HMEquation
        val eq_idx_minus_1: HMEquation = vT.get(equationNameMappingSpecIndex(pde, av, idx.index!! - 1)) as HMEquation
        val eq_cur: HMEquation = vT.get(equationNameMapping(pde)) as HMEquation
        val uc: HMUnnamedConst
        if (idx!!.isMax && pde.getBound(HMBoundaryCondition.SideType.RIGHT, av).derOrder > 0) {
            pde.getBound(HMBoundaryCondition.SideType.RIGHT, av).value.tokens.forEach(Consumer<EXPToken> { t: EXPToken? -> newRP.add(t) })
        } else if (idx.isFirst && pde.getBound(HMBoundaryCondition.SideType.LEFT, av).derOrder > 0) {
            pde.getBound(HMBoundaryCondition.SideType.LEFT, av).value.tokens.forEach(Consumer<EXPToken> { t: EXPToken? -> newRP.add(t) })
        } else if (o.getOrder() == EXPPDEOperand.Order.ONE) {
//            if (eq_idx_plus_1 == null || eq_idx_minus_1 == null || eq_cur == null) {
//                throw new RuntimeException("FDM: all is bad");
//            }
            uc = HMUnnamedConst(o.getSampledFirstSpatialVar().getStepSize())
            if (idx.isMax) pde.getBound(HMBoundaryCondition.SideType.RIGHT, av).value.tokens.forEach(Consumer<EXPToken> { t: EXPToken? -> newRP.add(t) }) else newRP.add(EXPOperand(eq_idx_plus_1))
            if (idx.isFirst) pde.getBound(HMBoundaryCondition.SideType.LEFT, av).value.tokens.forEach(Consumer<EXPToken> { t: EXPToken? -> newRP.add(t) }) else newRP.add(EXPOperand(eq_idx_minus_1))
            newRP.add(EXPOperator.sub())
            newRP.add(EXPOperand(HMUnnamedConst(2.0)))
            newRP.add(EXPOperand(uc))
            newRP.add(EXPOperator.mult())
            newRP.add(EXPOperator.div())
        } else if (o.getOrder() == EXPPDEOperand.Order.TWO) {
            uc = HMUnnamedConst(Math.pow(o.getSampledFirstSpatialVar().getStepSize(), 2.0))
            if (idx.isFirst) pde.getBound(HMBoundaryCondition.SideType.LEFT, av).value.tokens.forEach(Consumer<EXPToken> { t: EXPToken? -> newRP.add(t) }) else newRP.add(EXPOperand(eq_idx_minus_1))
            newRP.add(EXPOperand(HMUnnamedConst(2.0)))
            newRP.add(EXPOperand(eq_cur))
            newRP.add(EXPOperator.mult())
            newRP.add(EXPOperator.sub())
            if (idx.isMax) pde.getBound(HMBoundaryCondition.SideType.RIGHT, av).value.tokens.forEach(Consumer<EXPToken> { t: EXPToken? -> newRP.add(t) }) else newRP.add(EXPOperand(eq_idx_plus_1))
            newRP.add(EXPOperator.add())
            newRP.add(EXPOperand(uc))
            newRP.add(EXPOperator.div())
        }
    }

    fun initialConditionPhase() {}
    fun boundsPhase() {}
    fun finishPhase() {
        // удаляем все старые аппроксимируемые переменные
        for (v in approximatedVariables) {
            vT.remove(v.code)
        }
        // удаляем все старые аппроксимируемые уравнения
        for (e in approximatedEquations) {
            vT.remove(e.code)
        }

        // высчитываем значения НУ для ОДУ
        for (str in vT.keySet()) {
            val v: HMVariable = vT.get(str)
            if (v is HMDerivativeEquation) {
                val der: HMDerivativeEquation = v
                PDEInitialValueCalculator.calculate(der.initial)
            }
        }
    }

    // -------------------- ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ------------------
    private fun isNeedToAddIntoApproximateEq(`var`: HMVariable): Boolean {
        var eqNeedApx: Boolean
        var res = false
        // проверяем уравенения (не константы) не содержащиеся в списке апроксимации
        res = if (`var` is HMEquation
                && `var` !is HMConst
                && !approximatedEquations.contains(`var`)) {
            checkExpression(`var`.rightPart)
        } else {
            false
        }
        return res
    }

    private fun checkExpression(right: HMExpression): Boolean {
        var eqNeedApx = false
        for (t in right.tokens) {
            // если есть оператор ДУЧП - автоматом попадает в апроксимируемые
            if (t is EXPPDEOperand) {
                eqNeedApx = true
                // проверяем фунции
            } else if (t is EXPFunctionOperand && !eqNeedApx) {
                for (expr in t.args) {
                    if (checkExpression(expr)) {
                        eqNeedApx = true
                    }
                }
                // проверяем только уравнения и только если ранее не было найдено (eqNeedApx)
            } else if (t is EXPOperand && !eqNeedApx) {
                val operandVarisable: HMVariable = t.variable
                if (approximatedEquations.contains(operandVarisable) || approximatedVariables.contains(operandVarisable)) {
                    eqNeedApx = true
                }
            }
        }
        return eqNeedApx
    }

    // инициализировать итератор индексов
    private val newIterator: FDMIndexIterator
        private get() {
            val iterator = FDMIndexIterator()
            for (av in approximatedVariables) {
                iterator.addIndex(FDMIndexedApxVar(av))
            }
            return iterator
        }

    protected fun equationNameMapping(equation: HMVariable): String {
        if (!approximatedEquations.contains(equation)) {
            throw RuntimeException("FDM converter doesn't contain " + equation.code)
        }
        val newName: StringBuilder = StringBuilder(equation.code)
        newName.append(FDMStatic.APX_PREFIX)
        for (v in approximatedVariables) {
            newName.append("_")
            newName.append(indexIterator!!.getIndex(v.code)!!.index)
        }
        return newName.toString()
    }

    protected fun equationNameMappingSpecIndex(equation: HMVariable, av: HMSampledSpatialVariable, specValue: Int?): String {
        if (!approximatedEquations.contains(equation)) {
            throw RuntimeException("FDM converter doesn't contain " + equation.code)
        }
        val newName: StringBuilder = StringBuilder(equation.code)
        newName.append(FDMStatic.APX_PREFIX)
        for (v in approximatedVariables) {
            newName.append("_")
            if (v.code == av.code) {
                newName.append(specValue)
            } else {
                newName.append(indexIterator!!.getIndex(v.code)!!.index)
            }
        }
        return newName.toString()
    }

    private fun apxVarNameMapping(variable: HMSampledSpatialVariable): String? {
        return indexIterator!!.getIndex(variable.code)!!.constCode
    }

    // TODO рефакторинг
    private fun getMappedVar(v: HMVariable?): HMVariable {
        if (v == null) {
            throw RuntimeException("mappedVariable can't be NULL")
        }
        if (notApproximated.contains(v) || v is HMUnnamedConst) {
            return v
        } else if (v is HMSampledSpatialVariable) {
            return vT.get(apxVarNameMapping(v))
        } else if (v is HMEquation) {
            return vT.get(equationNameMapping(v))
        }
        throw RuntimeException("Cant find mapped variable: " + v.code)
    }

}