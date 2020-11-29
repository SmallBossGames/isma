package ru.nstu.isma.next.core.sim.fdm

import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.core.hsm.`var`.*
import ru.nstu.isma.core.hsm.`var`.pde.HMBoundaryCondition
import ru.nstu.isma.core.hsm.`var`.pde.HMPartialDerivativeEquation
import ru.nstu.isma.core.hsm.`var`.pde.HMSampledSpatialVariable
import ru.nstu.isma.core.hsm.exp.*
import java.lang.StringBuilder
import java.util.*
import java.util.function.Consumer

/**
 * Created by Bessonov Alex
 * Date: 12.12.13
 * Time: 1:53
 * преобразует модель с ДУЧП в модель с ОДУ
 */
class FDMConverter(model: HSM) {
    private val model: HSM = model
    private val variableTable: HMVariableTable = model.variableTable
    private val apxVars: LinkedList<HMSampledSpatialVariable> = LinkedList<HMSampledSpatialVariable>()
    private val notApx: HashSet<HMVariable> = HashSet<HMVariable>()
    private val apxEq: HashSet<HMEquation> = HashSet<HMEquation>()
    private var indexIterator: FDMIndexIterator? = null
    fun convert(): HSM {
        // подготавливаем контекст аппроксимации - информацию для построения сетки
        prepare()

        // совершаем полный обход всех индексов
        indexIterator!!.start()
        processGridNode()
        do {
            indexIterator!!.next()
            processGridNode()
        } while (!indexIterator!!.atEnd())

        // убираем края и строим разностные аналоги для операторов ДУЧП
        indexIterator!!.start()
        completeGrid()
        do {
            indexIterator!!.next()
            completeGrid()
        } while (!indexIterator!!.atEnd())

        // удаляем все старые аппроксимируемые переменные
        for (v in apxVars) {
            variableTable.remove(v.code)
        }
        // удаляем все старые аппроксимируемые уравнения
        for (e in apxEq) {
            variableTable.remove(e.code)
        }
        return model
    }

    // аппроксимируемые переменные разбивают пространство на сетку, где каждому узлу соответствует набор уравнений
    private fun processGridNode() {
        // прописываем все аппроксимируемые переменные как константы
        for (i in indexIterator!!.get()) {
            variableTable.add(i.toConst())
        }
        // генерируем для каждого уравнения аналог для сетки
        for (eq in apxEq) {
            processEquation(eq)
        }
    }

    private fun processEquation(eq: HMEquation): HMEquation {
        val newName = equationNameMapping(eq)
        // если таблица уже содержит такое уравнение - не будем разбирать снова
        if (variableTable.contain(newName)) {
            return variableTable.get(newName) as HMEquation
        }
        // создаем объект нового уравнения
        var newEq: HMEquation? = null
        // ОДУ и ДУЧП
        if (eq is HMDerivativeEquation) {
            newEq = HMDerivativeEquation(newName)
            (newEq as HMDerivativeEquation?)!!.setInitial(eq.initial.value)
            // алгебраические
        } else if (eq is HMAlgebraicEquation) {
            newEq = HMAlgebraicEquation(newName)
        }
        if (newEq == null) {
            throw RuntimeException("FDMConverter -> processEquation error!")
        }
        val rp = HMExpression()
        // пробегаем по правой части и меняем все аппроксимируемые переменные
        for ((index, tt) in eq.rightPart.tokens.withIndex()) {
            if (tt is EXPOperand && tt !is EXPPDEOperand) {
                val o: EXPOperand = tt
                // TODO выражения могут хранить неправедбные объекты
                val mappedVar: HMVariable = getMappedVar(o.variable)
                if (mappedVar !== o.variable) {
                    eq.rightPart.tokens[index] = EXPOperand(mappedVar)
                }
            }
            rp.add(tt)
        }
        newEq.rightPart = rp
        variableTable.add(newEq)
        return newEq
    }

    private fun completeGrid() {
        for (eq in apxEq) {
            val newEq: HMEquation = processEquation(eq)
            val rp = HMExpression()

            // пробегаем по правой части и меняем все аппроксимируемые переменные
            for (tt in eq.rightPart.tokens) {
                if (tt is EXPPDEOperand) {
                    val o: EXPPDEOperand = tt

//                    HMVariable mappedVar = getMappedVar(o.getVariable());
//                    if (mappedVar != o.getVariable()) {
//                        tt = new EXPOperand(mappedVar);
//                    }
                    // System.out.println(o.toString());
                    var uc: HMUnnamedConst?
                    val pde: HMPartialDerivativeEquation = variableTable.get(o.variable.code) as HMPartialDerivativeEquation
                    val av: HMSampledSpatialVariable = o.sampledFirstSpatialVar
                    val idx = indexIterator!!.getIndex(av.code)
                    if (idx!!.isFirst) {
                        pde.getBound(HMBoundaryCondition.SideType.LEFT, av).value.tokens.forEach(Consumer<EXPToken> { t: EXPToken? -> rp.add(t) })
                    } else if (idx.isMax) {
                        pde.getBound(HMBoundaryCondition.SideType.RIGHT, av).value.tokens.forEach(Consumer<EXPToken> { t: EXPToken? -> rp.add(t) })
                    } else {
                        val eq_idx_plus_1: HMEquation = variableTable.get(equationNameMappingSpecIndex(pde, av, idx.index!! + 1)) as HMEquation
                        val eq_idx_minus_1: HMEquation = variableTable.get(equationNameMappingSpecIndex(pde, av, idx.index!! - 1)) as HMEquation
                        val eq_cur: HMEquation = variableTable.get(equationNameMapping(pde)) as HMEquation
                        if (o.order == EXPPDEOperand.Order.ONE) {
                            uc = HMUnnamedConst(o.sampledFirstSpatialVar.stepSize)
                            rp.add(EXPOperand(eq_idx_plus_1))
                            rp.add(EXPOperand(eq_idx_minus_1))
                            rp.add(EXPOperator.sub())
                            rp.add(EXPOperand(HMUnnamedConst(2.0)))
                            rp.add(EXPOperand(uc))
                            rp.add(EXPOperator.mult())
                            rp.add(EXPOperator.div())
                        } else if (o.order == EXPPDEOperand.Order.TWO) {
                            uc = HMUnnamedConst(Math.pow(o.sampledFirstSpatialVar.stepSize, 2.0))
                            rp.add(EXPOperand(eq_idx_minus_1))
                            rp.add(EXPOperand(HMUnnamedConst(2.0)))
                            rp.add(EXPOperand(eq_cur))
                            rp.add(EXPOperator.mult())
                            rp.add(EXPOperator.sub())
                            rp.add(EXPOperand(eq_idx_plus_1))
                            rp.add(EXPOperator.add())
                            rp.add(EXPOperand(uc))
                            rp.add(EXPOperator.div())
                        }
                    }
                } else {
                    rp.add(tt)
                }
            }
            newEq.rightPart = rp
        }
    }

    // Парсинг таблицы переменных и наполнение предварительных данных
    fun prepare() {

        // запоминаем все апроксимируемые переменные и уравнения
        for (s in variableTable.keySet()) {
            val vv: HMVariable = variableTable.get(s)
            if (vv is HMPartialDerivativeEquation) {
                apxEq.add(vv as HMEquation)
            } else if (vv is HMSampledSpatialVariable) {
                apxVars.add(vv)
            }
        }
        var isReady = false
        var eqNeedApx: Boolean
        while (!isReady) {
            // пробегаем все уравнения в которых есть другие аппроксимируемые уравнения или переменные
            isReady = true
            for (s in variableTable.keySet()) {
                val `var`: HMVariable = variableTable.get(s)
                if (`var` is HMEquation
                        && `var` !is HMConst
                        && !apxEq.contains(`var`)) {
                    // проверяем правую часть на аппроксимируемые элементы
                    eqNeedApx = false
                    val right: HMExpression = `var`.rightPart
                    for (t in right.tokens) {
                        // проверяем только уравнения и только если ранее не было найдено (eqNeedApx)
                        if (t is EXPOperand && !eqNeedApx) {
                            val operVar: HMVariable = t.variable
                            if (apxEq.contains(operVar) || apxVars.contains(operVar)) {
                                isReady = false
                                apxEq.add(`var`)
                                eqNeedApx = true
                            }
                        }
                    }
                    if (!eqNeedApx) {
                        notApx.add(`var`)
                    }
                } else {
                    if (!apxVars.contains(`var`) && !apxEq.contains(`var`)) notApx.add(`var`)
                }
            }
        }
        // подготовим итератор по индексам - полный перебор всех индексов
        indexIterator = iterator
    }

    // инициализировать итератор индексов
    private val iterator: FDMIndexIterator
        get() {
            val iterator = FDMIndexIterator()
            for (av in apxVars) {
                iterator.addIndex(FDMIndexedApxVar(av))
            }
            return iterator
        }

    // TODO рефакторинг
    private fun getMappedVar(v: HMVariable): HMVariable {
        if (notApx.contains(v) || v is HMUnnamedConst) {
            return v
        } else if (v is HMSampledSpatialVariable) {
            return variableTable.get(apxVarNameMapping(v))
        } else if (v is HMEquation) {
            val code = equationNameMapping(v)
            return if (variableTable.contain(code)) {
                variableTable.get(code)
            } else {
                processEquation(v)
            }
        }
        throw RuntimeException("Cant find mapped variable: " + v.code)
        //
//        if (!context.containsApxVar(v.getCode())) {
//            return v;
//        }
//        String newName = nameMapping(v);
//        if (variables.contain(newName)) {
//            return variables.get(newName);
//        }
//        if (v instanceof HMEquation) {
//            processEquation((HMEquation) v);
//        }
//        throw new RuntimeException("Cant find mapped variable: " + newName);
    }

    // TODO apxLinkTable
    protected fun equationNameMapping(equation: HMVariable): String {
        if (!apxEq.contains(equation)) {
            throw RuntimeException("FDM converter doesn't contain " + equation.code)
        }
        val newName: StringBuilder = StringBuilder(equation.code)
        newName.append(FDMStatic.APX_PREFIX)
        for (v in apxVars) {
            newName.append("_")
            newName.append(indexIterator!!.getIndex(v.code)!!.index)
            //            if (isEqContains(equation, v)) {
//                newName.append(indexes.get(v.getCode()).getIndex());
//            } else if (!indexes.containsKey(v.getCode())) {
//                newName.append("E");
//            } else {
//                newName.append("0");
//            }
        }
        return newName.toString()
    }

    protected fun equationNameMappingSpecIndex(equation: HMVariable, av: HMSampledSpatialVariable, specValue: Int?): String {
        if (!apxEq.contains(equation)) {
            throw RuntimeException("FDM converter doesn't contain " + equation.code)
        }
        val newName: StringBuilder = StringBuilder(equation.code)
        newName.append(FDMStatic.APX_PREFIX)
        for (v in apxVars) {
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

}