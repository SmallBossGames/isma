package com.editor.semantic.g3;

public class GeneratorG3 {
	//список переменных
	//напряжения
	public final static String Ud = "Ud";
	public final static String Uq = "Uq";
	public final static String Uf = "Uf";
	//тока
	public final static String If = "If";
	public final static String I_f = "I'f";
	public final static String Ig = "Ig";
	public final static String I_g = "I'g";
	public final static String Ih = "Ih";
	public final static String I_h = "I'h";
	public final static String IGd = "IGd";
	public final static String I_Gd = "I'Gd";
	public final static String IGq = "IGq";
	public final static String I_Gq = "I'Gq";
	//углы
	public final static String W = "W";
	public final static String W_ = "W'";
	public final static String O = "O";
	public final static String O_ = "O'";
	//время
	public final static String t = "t";
	//индуктивности
	public final static String Ld = "Ld";
	public final static String Lq = "Lq";
	public final static String Lf = "Lf";
	public final static String Lg = "Lg";
	public final static String Lh = "Lh";
	public final static String Lad = "Lad";
	public final static String Laq = "Laq";
	//сопротивления
	public final static String R = "R";
	public final static String Rf = "Rf";
	public final static String Rg = "Rg";
	public final static String Rh = "Rh";
	//моменты инерции
	public final static String Td = "Td";
	public final static String Tj = "Tj";
	
	
	
	/**
	 * Метод для сборки дерева F1
	 */
	public static TreeG3 getF1() {
				//0
				TreeG3 f1 = new TreeG3(null, null, "-");
				//1l
				f1.setLeft(new TreeG3(null, null, "-"));
				//2l
				f1.getLeft().setLeft(new TreeG3(null, null, "-"));
				//3l
				f1.getLeft().getLeft().setLeft(new TreeG3(null, null, "+"));
				//4l
				f1.getLeft().getLeft().getLeft().setLeft(new TreeG3(null, null, "+"));
				//5l
				f1.getLeft().getLeft().getLeft().getLeft().setLeft(new TreeG3(null, null, "+"));
				//6l
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().setLeft(new TreeG3(null, null, "*"));
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().getLeft().setLeft(new TreeG3( Ud, null, null));
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().getLeft().setRight(new TreeG3( null, null, "cos"));
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().getLeft().getRight().setRight(new TreeG3(null, null, "-"));
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().getLeft().getRight().getRight().setLeft(new TreeG3( O,null, null));
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().getLeft().getRight().getRight().setRight(new TreeG3( t,null, null));
				//6r
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().getRight().setLeft(new TreeG3( Uq,null, null));
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().getRight().setRight(new TreeG3( null, null, "sin"));
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().getRight().getRight().setRight(new TreeG3(null, null, "-"));
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().getRight().getRight().getRight().setLeft(new TreeG3( O,null, null));
				f1.getLeft().getLeft().getLeft().getLeft().getLeft().getRight().getRight().getRight().setRight(new TreeG3( t,null, null));
				//5r
				f1.getLeft().getLeft().getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
				f1.getLeft().getLeft().getLeft().getLeft().getRight().setLeft(new TreeG3( R,null, null));
				f1.getLeft().getLeft().getLeft().getLeft().getRight().setRight(new TreeG3( IGd,null, null));
				//4r
				f1.getLeft().getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
				f1.getLeft().getLeft().getLeft().getRight().setLeft(new TreeG3( Ld,null, null));
				f1.getLeft().getLeft().getLeft().getRight().setRight(new TreeG3( I_Gd,null, null));
				//3r
				f1.getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
				f1.getLeft().getLeft().getRight().setLeft(new TreeG3( Lad,null, null));
				f1.getLeft().getLeft().getRight().setRight(new TreeG3( I_f,null, null));
				//2r
				f1.getLeft().setRight(new TreeG3(null, null, "*"));
				f1.getLeft().getRight().setLeft(new TreeG3( Lad,null, null));
				f1.getLeft().getRight().setRight(new TreeG3( I_g,null, null));
				//1r
				f1.setRight(new TreeG3(null, null, "*"));
				
				f1.getRight().setLeft(new TreeG3(null, null, "-"));
				
				f1.getRight().getLeft().setLeft(new TreeG3(null, null, "*"));
				f1.getRight().getLeft().getLeft().setLeft(new TreeG3( Lq,null, null));
				f1.getRight().getLeft().getLeft().setRight(new TreeG3( IGq,null, null));
				
				f1.getRight().getLeft().setRight(new TreeG3(null, null, "*"));
				f1.getRight().getLeft().getRight().setLeft(new TreeG3( Laq,null, null));
				f1.getRight().getLeft().getRight().setRight(new TreeG3( Ih,null, null));
				
				f1.getRight().setRight(new TreeG3( W,null, null));
				
				return f1;
	}
	
	/**
	 * Метод для сборки дерева F2
	 */
	public static TreeG3 getF2() {
		TreeG3 f2 = new TreeG3(null, null, "+");
		f2.setRight(new TreeG3(null, null, "*"));
		f2.getRight().setRight(new TreeG3(W, null, null));
		f2.getRight().setLeft(new TreeG3(null, null, "-"));
		f2.getRight().getLeft().setLeft(new TreeG3(null, null, "*"));
		f2.getRight().getLeft().getLeft().setLeft(new TreeG3(Ld, null, null));
		f2.getRight().getLeft().getLeft().setRight(new TreeG3(IGd, null, null));
		f2.getRight().getLeft().setRight(new TreeG3(null, null, "*"));
		f2.getRight().getLeft().getRight().setLeft(new TreeG3(Lad, null, null));
		f2.getRight().getLeft().getRight().setRight(new TreeG3(null, null, "+"));
		f2.getRight().getLeft().getRight().getRight().setLeft(new TreeG3(If, null, null));
		f2.getRight().getLeft().getRight().getRight().setRight(new TreeG3(Ig, null, null));
		
		f2.setLeft(new TreeG3(null, null, "-"));
		
		f2.getLeft().setRight(new TreeG3(null, null, "*"));
		f2.getLeft().getRight().setLeft(new TreeG3(Laq, null, null));
		f2.getLeft().getRight().setRight(new TreeG3(I_h, null, null));
		
		f2.getLeft().setLeft(new TreeG3(null, null, "+"));
		
		f2.getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
		f2.getLeft().getLeft().getRight().setLeft(new TreeG3(Lq, null, null));
		f2.getLeft().getLeft().getRight().setRight(new TreeG3(I_Gq, null, null));
		
		f2.getLeft().getLeft().setLeft(new TreeG3(null, null, "+"));
		
		f2.getLeft().getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
		f2.getLeft().getLeft().getLeft().getRight().setLeft(new TreeG3(R, null, null));
		f2.getLeft().getLeft().getLeft().getRight().setRight(new TreeG3(IGq, null, null));
		
		f2.getLeft().getLeft().getLeft().setLeft(new TreeG3(null, null, "+"));
		
		f2.getLeft().getLeft().getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
		f2.getLeft().getLeft().getLeft().getLeft().getRight().setLeft(new TreeG3(Uq, null, null));
		f2.getLeft().getLeft().getLeft().getLeft().getRight().setRight(new TreeG3(null, null, "cos"));
		f2.getLeft().getLeft().getLeft().getLeft().getRight().getRight().setRight(new TreeG3(null, null, "-"));
		f2.getLeft().getLeft().getLeft().getLeft().getRight().getRight().getRight().setLeft(new TreeG3(O, null, null));
		f2.getLeft().getLeft().getLeft().getLeft().getRight().getRight().getRight().setRight(new TreeG3(t, null, null));
		
		f2.getLeft().getLeft().getLeft().getLeft().setLeft(new TreeG3(null, null, "-"));
		f2.getLeft().getLeft().getLeft().getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
		f2.getLeft().getLeft().getLeft().getLeft().getLeft().getRight().setLeft(new TreeG3(Ud, null, null));
		f2.getLeft().getLeft().getLeft().getLeft().getLeft().getRight().setRight(new TreeG3(null, null, "sin"));
		f2.getLeft().getLeft().getLeft().getLeft().getLeft().getRight().getRight().setRight(new TreeG3(null, null, "-"));
		f2.getLeft().getLeft().getLeft().getLeft().getLeft().getRight().getRight().getRight().setLeft(new TreeG3(O, null, null));
		f2.getLeft().getLeft().getLeft().getLeft().getLeft().getRight().getRight().getRight().setRight(new TreeG3(t, null, null));
		
		
		return f2;
	}
	
	/**
	 * Метод для сборки дерева F3
	 */
	public static TreeG3 getF3() {
		TreeG3 f3 = new TreeG3(null, null, "-");

		f3.setLeft(new TreeG3(null, null, "+"));
		
		f3.getLeft().setRight(new TreeG3(null, null, "*"));
		f3.getLeft().getRight().setLeft(new TreeG3(Lad, null, null));
		f3.getLeft().getRight().setRight(new TreeG3(I_g, null, null));
		
		f3.getLeft().setLeft(new TreeG3(null, null, "+"));
		
		f3.getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
		f3.getLeft().getLeft().getRight().setLeft(new TreeG3(Lf, null, null));
		f3.getLeft().getLeft().getRight().setRight(new TreeG3(I_f, null, null));
		
		f3.getLeft().getLeft().setLeft(new TreeG3(null, null, "+"));
		
		f3.getLeft().getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
		f3.getLeft().getLeft().getLeft().getRight().setLeft(new TreeG3(Rf, null, null));
		f3.getLeft().getLeft().getLeft().getRight().setRight(new TreeG3(If, null, null));
		
		f3.getLeft().getLeft().getLeft().setLeft(new TreeG3(null, null, "-"));
		f3.getLeft().getLeft().getLeft().getLeft().setRight(new TreeG3(Uf, null, null));

		f3.setRight(new TreeG3(null, null, "*"));
		f3.getRight().setLeft(new TreeG3(Lad, null, null));
		f3.getRight().setRight(new TreeG3(I_Gd, null, null));
		
		return f3;
	}
	
	/**
	 * Метод для сборки дерева F4
	 */
	public static TreeG3 getF4() {
		TreeG3 f4 = new TreeG3(null, null, "-");

		f4.setLeft(new TreeG3(null, null, "+"));
		
		f4.getLeft().setRight(new TreeG3(null, null, "*"));
		f4.getLeft().getRight().setLeft(new TreeG3(Lad, null, null));
		f4.getLeft().getRight().setRight(new TreeG3(I_f, null, null));
		
		f4.getLeft().setLeft(new TreeG3(null, null, "+"));
		
		f4.getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
		f4.getLeft().getLeft().getRight().setLeft(new TreeG3(Lg, null, null));
		f4.getLeft().getLeft().getRight().setRight(new TreeG3(I_g, null, null));
		
		f4.getLeft().getLeft().setLeft(new TreeG3(null, null, "*"));
		f4.getLeft().getLeft().getLeft().setLeft(new TreeG3(Rg, null, null));
		f4.getLeft().getLeft().getLeft().setRight(new TreeG3(Ig, null, null));

		f4.setRight(new TreeG3(null, null, "*"));
		f4.getRight().setLeft(new TreeG3(Lad, null, null));
		f4.getRight().setRight(new TreeG3(I_Gd, null, null));
		
		return f4;
	}

	/**
	 * Метод для сборки дерева F5
	 */
	public static TreeG3 getF5() {
		TreeG3 f5 = new TreeG3(null, null, "-");
		
		f5.setLeft(new TreeG3(null, null, "+"));
		
		f5.getLeft().setLeft(new TreeG3(null, null, "*"));
		f5.getLeft().getLeft().setLeft(new TreeG3(Rh, null, null));
		f5.getLeft().getLeft().setRight(new TreeG3(Ih, null, null));
		
		f5.getLeft().setRight(new TreeG3(null, null, "*"));
		f5.getLeft().getRight().setLeft(new TreeG3(Lh, null, null));
		f5.getLeft().getRight().setRight(new TreeG3(I_h, null, null));
		
		f5.setRight(new TreeG3(null, null, "*"));
		f5.getRight().setLeft(new TreeG3(Laq, null, null));
		f5.getRight().setRight(new TreeG3(I_Gq, null, null));
		
		return f5;
	}
	
	/**
	 * Метод для сборки дерева F6
	 */
	public static TreeG3 getF6() {
		TreeG3 f6 = new TreeG3(null, null, "-");
		
		f6.setLeft(new TreeG3(null, null, "-"));
		
		f6.getLeft().setRight(new TreeG3(null, null, "*"));
		f6.getLeft().getRight().setLeft(new TreeG3(Lad, null, null));
		f6.getLeft().getRight().setRight(new TreeG3(null, null, "*"));
		f6.getLeft().getRight().getRight().setLeft(new TreeG3(IGq, null, null));
		f6.getLeft().getRight().getRight().setRight(new TreeG3(null, null, "+"));
		f6.getLeft().getRight().getRight().getRight().setLeft(new TreeG3(If, null, null));
		f6.getLeft().getRight().getRight().getRight().setRight(new TreeG3(Ig, null, null));
		
		f6.getLeft().setLeft(new TreeG3(null, null, "+"));
		f6.getLeft().getLeft().setLeft(new TreeG3(Td, null, null));
		f6.getLeft().getLeft().setRight(new TreeG3(null, null, "*"));
		f6.getLeft().getLeft().getRight().setRight(new TreeG3(IGd, null, null));
		f6.getLeft().getLeft().getRight().setLeft(new TreeG3(null, null, "+"));
		f6.getLeft().getLeft().getRight().getLeft().setLeft(new TreeG3(null, null, "*"));
		f6.getLeft().getLeft().getRight().getLeft().getLeft().setLeft(new TreeG3(null, null, "-"));
		f6.getLeft().getLeft().getRight().getLeft().getLeft().getLeft().setLeft(new TreeG3(Ld, null, null));
		f6.getLeft().getLeft().getRight().getLeft().getLeft().getLeft().setRight(new TreeG3(Lq, null, null));
		f6.getLeft().getLeft().getRight().getLeft().getLeft().setRight(new TreeG3(IGq, null, null));
		f6.getLeft().getLeft().getRight().getLeft().setRight(new TreeG3(null, null, "*"));
		f6.getLeft().getLeft().getRight().getLeft().getRight().setLeft(new TreeG3(Laq, null,null));
		f6.getLeft().getLeft().getRight().getLeft().getRight().setRight(new TreeG3(Ih, null,null));
		
		f6.setRight(new TreeG3(null, null, "*"));
		f6.getRight().setLeft(new TreeG3(Tj, null, null));
		f6.getRight().setRight(new TreeG3(W_, null, null));

		return f6;
	}
	/**
	 * Метод для сборки дерева F7
	 */
	public static TreeG3 getF7() {
		TreeG3 f7 = new TreeG3(null, null, "-");
		f7.setLeft(new TreeG3(O_, null, null));
		f7.setRight(new TreeG3(W, null, null));
		
		return f7;
	}
	/**
	 * Метод для сборки дерева F8
	 */
	public static TreeG3 getF8() {
		TreeG3 f8 = new TreeG3(null, null, "-");
		
		f8.setLeft(new TreeG3(null, null, "*"));
		f8.getLeft().setLeft(new TreeG3(IGd, null,null));
		f8.getLeft().setRight(new TreeG3(null, null, "cos"));
		f8.getLeft().getRight().setRight(new TreeG3(null, null, "-"));
		f8.getLeft().getRight().getRight().setLeft(new TreeG3(O, null, null));
		f8.getLeft().getRight().getRight().setRight(new TreeG3(t, null, null));
		
		f8.setRight(new TreeG3(null, null, "*"));
		f8.getRight().setLeft(new TreeG3(IGq, null,null));
		f8.getRight().setRight(new TreeG3(null, null, "sin"));
		f8.getRight().getRight().setRight(new TreeG3(null, null, "-"));
		f8.getRight().getRight().getRight().setLeft(new TreeG3(O, null, null));
		f8.getRight().getRight().getRight().setRight(new TreeG3(t, null, null));
		
		return f8;
	}
	/**
	 * Метод для сборки дерева F9
	 */
	public static TreeG3 getF9() {
	  TreeG3 f9 = new TreeG3(null, null, "+");
		
		f9.setLeft(new TreeG3(null, null, "*"));
		f9.getLeft().setLeft(new TreeG3(IGd, null,null));
		f9.getLeft().setRight(new TreeG3(null, null, "sin"));
		f9.getLeft().getRight().setRight(new TreeG3(null, null, "-"));
		f9.getLeft().getRight().getRight().setLeft(new TreeG3(O, null, null));
		f9.getLeft().getRight().getRight().setRight(new TreeG3(t, null, null));
		
		f9.setRight(new TreeG3(null, null, "*"));
		f9.getRight().setLeft(new TreeG3(IGq, null,null));
		f9.getRight().setRight(new TreeG3(null, null, "cos"));
		f9.getRight().getRight().setRight(new TreeG3(null, null, "-"));
		f9.getRight().getRight().getRight().setLeft(new TreeG3(O, null, null));
		f9.getRight().getRight().getRight().setRight(new TreeG3(t, null, null));
		
		return f9;
	}
}
