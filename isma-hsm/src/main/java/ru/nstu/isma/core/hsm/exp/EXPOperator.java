package ru.nstu.isma.core.hsm.exp;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 29.11.13
 * Time: 1:01
 */
public class EXPOperator extends EXPToken implements Serializable {

    private Type type;

    private ArityType arity;

    private int priority;

    private Code code;


    public EXPOperator(Code code, Type type, ArityType arity, int priority) {
        this.type = type;
        this.arity = arity;
        this.priority = priority;
        this.code = code;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArityType getArity() {
        return arity;
    }

    public void setArity(ArityType arity) {
        this.arity = arity;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    @Override
    public String toString() {
        switch (code) {
            case ADDITION:
                return ("+");
            case SUBTRACTION:
                return ("-");
            case MULTIPLICATION:
                return ("*");
            case DIVISION:
                return ("/");
            case INVOLUTION:
                return ("^");

            case GREATER_THAN:
                return (">");
            case LESS_THAN:
                return ("<");
            case GREATER_OR_EQUAL:
                return (">=");
            case LESS_OR_EQUAL:
                return ("<=");
            case EQUAL:
                return ("==");
            case NOT_EQUAL:
                return ("!=");

            case AND:
                return ("and");
            case OR:
                return ("or");
            case NOT:
                return ("!");

        }
        return super.toString();
    }

    public enum ArityType implements Serializable {
        UNARY, BINARY, TRINAR
    }

    public enum Type implements Serializable {
        COMPARE, ALGEBRAIC, LOGICAL
    }

    // аналог символьного отображения
    public enum Code implements Serializable {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        INVOLUTION,

        GREATER_THAN,
        LESS_THAN,
        GREATER_OR_EQUAL,
        LESS_OR_EQUAL,
        EQUAL,
        NOT_EQUAL,

        AND,
        OR,
        NOT
    }

    public static EXPOperator un_plus() {return new EXPOperator(Code.ADDITION, Type.ALGEBRAIC, ArityType.UNARY, 92);}
    public static EXPOperator un_minus() {return new EXPOperator(Code.SUBTRACTION, Type.ALGEBRAIC, ArityType.UNARY, 91);}
    public static EXPOperator not() {return new EXPOperator(Code.NOT, Type.LOGICAL, ArityType.UNARY, 90);}

    public static EXPOperator mult() {return new EXPOperator(Code.MULTIPLICATION, Type.ALGEBRAIC, ArityType.BINARY, 80);}
    public static EXPOperator div() {return new EXPOperator(Code.DIVISION, Type.ALGEBRAIC, ArityType.BINARY, 80);}

    public static EXPOperator add() {return new EXPOperator(Code.ADDITION, Type.ALGEBRAIC, ArityType.BINARY, 70);}
    public static EXPOperator sub() {return new EXPOperator(Code.SUBTRACTION, Type.ALGEBRAIC, ArityType.BINARY, 70);}

    public static EXPOperator less() {return new EXPOperator(Code.LESS_THAN, Type.COMPARE, ArityType.BINARY, 60);}
    public static EXPOperator less_equal() {return new EXPOperator(Code.LESS_OR_EQUAL, Type.COMPARE, ArityType.BINARY, 60);}

    public static EXPOperator greater() {return new EXPOperator(Code.GREATER_THAN, Type.COMPARE, ArityType.BINARY, 50);}
    public static EXPOperator greater_equal() {return new EXPOperator(Code.GREATER_OR_EQUAL, Type.COMPARE, ArityType.BINARY, 50);}

    public static EXPOperator equal() {return new EXPOperator(Code.EQUAL, Type.COMPARE, ArityType.BINARY, 40);}

    public static EXPOperator not_equal() {return new EXPOperator(Code.NOT_EQUAL, Type.COMPARE, ArityType.BINARY, 30);}

    public static EXPOperator and() {return new EXPOperator(Code.AND, Type.LOGICAL, ArityType.BINARY, 20);}

    public static EXPOperator or() {return new EXPOperator(Code.OR, Type.LOGICAL, ArityType.BINARY, 10);}


}
