package ru.nstu.isma.intg.tests;

import error.IsmaErrorList;
import org.junit.Assert;
import org.junit.Test;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.events.HSMEventFunctionGroup;
import ru.nstu.isma.core.hsm.events.HSMEventFunctionGroupEvaluator;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.in.InputTranslator;
import ru.nstu.isma.in.lisma.LismaTranslator;

public class HSMEventFunctionGroupEvaluatorTest {

    @Test
    public void testSimple() {
        //HMExpression transitionCondition = parseTransitionCondition("y < 0", "y");
//        HMExpression transitionCondition = parseTransitionCondition("st<abs(k1*n1-k2*n2-x1*(k1-k2))", "st", "k1", "n1", "k2", "n2", "x1");
//        HMExpression transitionCondition = parseTransitionCondition(
//                "st<abs(k1*n1-k2*n2-x1*(k1-k2))",
//                new String[] {"st", "x1"},
//                new String[] {"k1", "n1", "k2", "n2"});

        HMExpression transitionCondition = parseTransitionCondition(
                "x1>=x2 && v1>=v2",
                new String[] {"x1", "x2", "v1", "v2"},
                new String[0]);

        HSMEventFunctionGroup hsmEventFunctionGroup = HSMEventFunctionGroupEvaluator.evaluate(transitionCondition);
        hsmEventFunctionGroup.getExpressions().forEach(ef -> System.out.println(ef.toString(new StringBuilder(), true)));

        Assert.assertEquals(2, hsmEventFunctionGroup.getExpressions().size());
    }

    private HMExpression parseTransitionCondition(String transitionCondition, String[] deVariables, String[] aeVariables) {
        // Формируем что-то вроде: "y' = 0; state test(y + 2 < 0) {} from init;", чтобы HSM корректно разобрала.
        StringBuilder sb = new StringBuilder();
        for (String deVariable : deVariables) {
            sb.append(deVariable);
            sb.append("' = 0; ");
        }
        for (String aeVariable : aeVariables) {
            sb.append(aeVariable);
            sb.append(" = 1; ");
        }
        sb.append("state test (");
        sb.append(transitionCondition);
        sb.append(") {} from init;");

        String state = sb.toString();

        IsmaErrorList errors = new IsmaErrorList();
        InputTranslator translator = new LismaTranslator(state, errors);

        HSM hsm = translator.translate();
        return hsm.getAutomata().getTransactions().iterator().next().getCondition();
    }
}
