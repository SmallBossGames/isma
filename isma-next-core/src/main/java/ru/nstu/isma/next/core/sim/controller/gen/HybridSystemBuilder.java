package ru.nstu.isma.next.core.sim.controller.gen;

import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.intg.api.calcmodel.AlgebraicEquation;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.calcmodel.HybridSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Компоновщик гибридной системы.
 *
 * Фрагмент кода на примере ГС "мячик": см. AnalyzedHybridSystemClassBuilderTest.
 *
 * Поскольку для расчетной модели удобнее указывать исходящие связи, то для псевдосостояний необходимо создавать
 * дополнительное начальное псевдосостояние с именем {@link HybridSystem.INIT_PSEUDO_STATE}.
 *
 * Основная система ДАУ формируется по состоянию (@link ru.nstu.isma.core.hsm.HSM.INIT_STATE}.
 * В остальных состояниях уравнения только переопределяют первоначальные. Т.е. дублировать их необязательно.
 *
 * // TODO: алгебраические и СЛАУ.
 *
 * @author Maria Nasyrova
 * @since  05.10.2015
 */
public class HybridSystemBuilder {

    /** Компоновщики состояний гибридной системы */
    private Map<String, StateBuilder> stateBuilders;

    /** Компоновщики псевдосостояний гибридной системы (например, предикаты условных блоков) */
    private Map<String, StateBuilder> pseudoStateBuilders;

    public HybridSystemBuilder() {
        stateBuilders = new HashMap<>();
        pseudoStateBuilders = new HashMap<>();
    }

    /**
     * Добавляет новое состояние.
     * @param  name имя состояния
     * @return      добавленное состояние
     */
    public StateBuilder addState(String name) {
        StateBuilder stateBuilder = new StateBuilder(name, this);
        stateBuilders.put(name, stateBuilder);
        return stateBuilder;
    }

    /**
     * Добавляет новое псевдосостояние.
     * @param  name имя псевдосостояния
     * @return      добавленное псевдосостояние
     */
    public StateBuilder addPseudoState(String name) {
        StateBuilder pseudoStateBuilder = new StateBuilder(name, this);
        pseudoStateBuilders.put(name, pseudoStateBuilder);
        return pseudoStateBuilder;
    }

    /** Компонует ГС по сформированному описанию. */
    public HybridSystem toHybridSystem() {
        // Компонуем локальные состояния ГС.
        Map<String, HybridSystem.State> states = new HashMap<>();
        for (StateBuilder stateBuilder : stateBuilders.values()) {
            HybridSystem.State state = stateBuilder.toHybridSystemState();
            states.put(state.getName(), state);
        }

        // Компонуем псевдосостояния ГС.
        Map<String, HybridSystem.State> pseudoStates = new HashMap<>();
        for (StateBuilder stateBuilder : pseudoStateBuilders.values()) {
            HybridSystem.State pseudoState = stateBuilder.toHybridSystemState();
            pseudoStates.put(pseudoState.getName(), pseudoState);
        }

        // Компонуем основную систему ДАУ.
        HybridSystem.State initState = states.get(HSM.INIT_STATE);

        // Получаем сформированнный набор ОДУ
        List<DifferentialEquation> initStateDes = initState.getDifferentialEquations();
        DifferentialEquation[] des = new DifferentialEquation[initStateDes.size()];
        initStateDes.toArray(des);

        // Получаем сформированный набор алгебраических функций
        List<AlgebraicEquation> initStateAes = initState.getAlgebraicEquations();
        AlgebraicEquation[] aes = new AlgebraicEquation[initStateAes.size()];
        initStateAes.toArray(aes);

        DaeSystem daeSystem = new DaeSystem(des, aes);

        return new HybridSystem(daeSystem, states, pseudoStates);
    }

}
