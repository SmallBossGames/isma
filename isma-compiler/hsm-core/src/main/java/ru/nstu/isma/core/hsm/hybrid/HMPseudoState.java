package ru.nstu.isma.core.hsm.hybrid;

import ru.nstu.isma.core.hsm.exp.HMExpression;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Bessonov Alex
 * on 08.02.2015.
 */
public class HMPseudoState extends HMState implements Serializable {

    protected HMExpression condition;

    public HMPseudoState() {
        super(UUID.randomUUID().toString());
    }

    public HMExpression getCondition() {
        return condition;
    }

    public void setCondition(HMExpression condition) {
        this.condition = condition;
    }
}
