package com.editor.semantic;

public class Edge {
	private String child;
	private String parent;


    // для коммита
	public Edge(){
		
	}
	
	public Edge(String child, String parent) {
		super();
		this.child = child;
		this.parent = parent;
	}
	
	/**
	 * 
	 * @return the child
	 */
	public String getChild() {
		return child;
	}
	
	/**
	 * @param child the child to set
	 */
	public void setChild(String child) {
		this.child = child;
	}
	
	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}
	
	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}
}
