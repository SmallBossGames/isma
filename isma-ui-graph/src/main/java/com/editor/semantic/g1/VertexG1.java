package com.editor.semantic.g1;

import com.editor.semantic.Vertex;
import com.editor.semantic.g2.GraphG2;

import java.util.ArrayList;

public class VertexG1 extends Vertex {
	private String value;
	private String style;
	private ArrayList<GraphG2> elementsG2;
    private int numberConnects;

	public VertexG1(){}

    public VertexG1(VertexG1 clonedVertex) {
        super(clonedVertex.getId(), clonedVertex.getUd(), clonedVertex.getUq());
        this.value = clonedVertex.getValue();
        this.style = clonedVertex.getStyle();
        this.elementsG2 = clonedVertex.getElementsG2();
        this.numberConnects = clonedVertex.getNumberConnects();
    }

	public VertexG1(String id, String value, String style, double ud, double uq) {
		super(id, ud, uq);
		this.value = value;
		this.style = style;
	}

    public int getNumberConnects() {
        return numberConnects;
    }

    public void setNumberConnects(int numberConnects) {
        this.numberConnects = numberConnects;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public ArrayList<GraphG2> getElementsG2() {
		return elementsG2;
	}

	public void setElementsG2(ArrayList<GraphG2> elementsG2) {
		this.elementsG2 = elementsG2;
	}
	
}
