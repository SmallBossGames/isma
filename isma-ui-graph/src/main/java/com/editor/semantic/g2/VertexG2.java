package com.editor.semantic.g2;

import com.editor.semantic.Vertex;

public class VertexG2 extends Vertex {
    boolean isEarth = false;    // является ли землей данный узел
    boolean isBeginning = false; // является ли началом подграфа данный узел
    boolean isEnd = false;      // является ли концом подграфа данный узел
	
	public VertexG2(String id, double ud, double uq) {
		super(id, ud, uq);
	}
	
	public VertexG2(String id, double ud, double uq, boolean isEarth, boolean isBeginning, boolean isEnd) {
		super(id, ud, uq);
		this.isEarth = isEarth;
        this.isBeginning = isBeginning;
        this.isEnd = isEnd;
	}

    public boolean isEarth() {
        return isEarth;
    }

    public boolean isBeginning() {
        return isBeginning;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setBeginning(boolean beginning) {
        isBeginning = beginning;
    }
}
