package ru.nstu.isma.hsm.error;

/**
 * Created by Bessonov Alex
 * Date: 04.01.14
 * Time: 22:00
 */
public class IsmaError {
    private Integer row;

    private Integer col;

    private Type type;

    private String msg;

    public IsmaError(int row, int col, String msg) {
        this.row = row;
        this.col = col;
        this.msg = msg;
    }

    public IsmaError(String msg) {
        this.msg = msg;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type != null) {
           switch (type) {
               case SYNTAX:
                   sb.append("[cинтаксическая ошибка ");
                   if (row != null && col != null) {
                       sb.append("в ");
                       sb.append(row);
                       sb.append(", ");
                       sb.append(col);
                       sb.append(" ");
                   }
                   sb.append("] ");
                   break;
               case SEM:
                   sb.append("[семантическая ошибка] ");
                   break;
           }
        }

        sb.append(msg);
        return sb.toString();
    }

    public enum Type {

        SYNTAX, SEM

    }
}
