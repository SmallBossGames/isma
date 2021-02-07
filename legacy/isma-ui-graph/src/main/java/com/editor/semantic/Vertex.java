package com.editor.semantic;

public class Vertex {
	private String id;
	private double Ud;
	private double Uq;
    private boolean isVisited = false;
	
	public Vertex(){}
	
	public Vertex(String id, double ud, double uq) {
		this.id = id;
		Ud = ud;
		Uq = uq;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the ud
	 */
	public double getUd() {
		return Ud;
	}
	/**
	 * @param ud the ud to set
	 */
	public void setUd(double ud) {
		Ud = ud;
	}
	/**
	 * @return the uq
	 */
	public double getUq() {
		return Uq;
	}
	/**
	 * @param uq the uq to set
	 */
	public void setUq(double uq) {
		Uq = uq;
	}


    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
