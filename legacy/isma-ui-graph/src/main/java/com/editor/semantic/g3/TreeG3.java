package com.editor.semantic.g3;



/**
 * Created with IntelliJ IDEA.
 * User: Артем
 * Date: 05.10.13
 * Time: 18:03
 * To change this template use File | Settings | File Templates.
 */

/* Представление Дерева g3 */

public class TreeG3 {
    private String name; // имя переменной
    private Double value; // значение числа
    private String oper; // операция

    private TreeG3 Left; // ссылка на левую ветку дерева
    private TreeG3 Right; // ссылка на правую ветку дерева
    
    /**
     * метод возвращающий свёртку текущего поддерева
     * @return
     */
    public String getFold() {
    	String res = getLeft()!=null?getLeft().getFold() :"";//свёртка левой ветки при наличии
    	if(res.length()>0&&getOper()!=null&&getOper().equals("*")&&getLeft().getOper()!=null&&(getLeft().getOper().equals("+")||getLeft().getOper().equals("-")))//восстонавливаем скобки
    	{
    		res = "("+res+")";
    	}
    	res+=getThisValue();													//добавляем к свёртке значение текущей вершины
    	if(getOper()!=null&&(getOper().length()>1||(getOper().equals("*")&&getRight()!=null&getRight().getOper()!=null&&(getRight().getOper().equals("+")||getRight().getOper().equals("-")))))	//если мат.функция или выражение в скобках, то правую ветку сворачиваем в скобки
		{
			res+="(";
			res+=getRight().getFold();
			res+=")";
		}
    	else res+=getRight()!=null?getRight().getFold():"";//свёртка правой ветки при наличии
    	return res;
    }
    /**
     * метод возвращающий значений(имя перменной или значение числа или операцию) в текущей вершине
     * @return
     */
    private String getThisValue() {
    	return getName() != null ? getName() : getValue() != null? getValue().toString():getOper();
    }
    
    /**
     * Constructor
     * @param name - имя переменной
     * @param value -  значение числа
     * @param oper - операция
     */
    public TreeG3(String name, Double value, String oper) {
        this.name = name;
        this.value = value;
        this.oper = oper;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public TreeG3 getLeft() {
        return Left;
    }

    public void setLeft(TreeG3 left) {
        Left = left;
    }

    public TreeG3 getRight() {
        return Right;
    }

    public void setRight(TreeG3 right) {
        Right = right;
    }
}
