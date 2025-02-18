package ru.nstu.isma.core.hsm.hybrid;

import ru.nstu.isma.core.hsm.exp.HMExpression;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:46
 */
public class HMTransaction implements Serializable {
    protected HMState source;

    protected HMState target;

    protected HMExpression condition;

    public HMState getSource() {
        return source;
    }

    public void setSource(HMState source) {
        this.source = source;
    }

    public HMState getTarget() {
        return target;
    }

    public void setTarget(HMState target) {
        this.target = target;
    }

    public HMExpression getCondition() {
        return condition;
    }

    public void setCondition(HMExpression condition) {
        this.condition = condition;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HMTransaction that = (HMTransaction) o;

        if(source == null || that.source == null){
            return source == null && that.source == null;
        }

        if (!source.equals(that.source)) return false;
        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        final var result = source == null ? 0 : source.hashCode();
        return 31 * result + target.hashCode();
    }
}
