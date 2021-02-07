package ru.nstu.isma.intg.tests;

import org.junit.Test;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

public class SimjaTest {

    @Test
    public void myTest() {
        ExprEvaluator util = new ExprEvaluator();

        IExpr result2 = util.evaluate("D((sin(y(t))), t)");
        System.out.println(result2.toString());
    }

}
