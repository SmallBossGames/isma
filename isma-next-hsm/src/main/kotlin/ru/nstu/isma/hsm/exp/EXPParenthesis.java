package ru.nstu.isma.hsm.exp;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 02.12.13
 * Time: 12:28
 */
public class EXPParenthesis extends EXPToken implements Serializable {
    protected Type type;

    public EXPParenthesis(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type implements Serializable {
        OPEN, CLOSE
    }

    @Override
    public String toString() {
        String res = null;
        switch (type) {
            case OPEN:
                res = "(";
                break;
            case CLOSE:
                res = ")";
                break;
        }
        return res;
    }
}
