package com.editor.semantic.g2;

import java.util.ArrayList;

public class GraphG2 {

	private ArrayList<EdgeG2> bag_edges = null;
	private ArrayList<VertexG2> bag_cells = null;
	
	public GraphG2(){
		bag_edges = new ArrayList<EdgeG2>();
		bag_cells = new ArrayList<VertexG2>();
	}
	
	public void addEdge(EdgeG2 edge){
		bag_edges.add(edge);
	}
	
	public void addVertex(VertexG2 v){
		bag_cells.add(v);
	}

	public ArrayList<EdgeG2> getBag_edges() {
		return bag_edges;
	}

	public ArrayList<VertexG2> getBag_cells() {
		return bag_cells;
	}

    // удаление вершины и дуги по id
    public void removeVertexAndUpgradeEdgesById(String id, String new_v_id){
        // преобразуем дуги, что-бы указывали на новую вершину (new_v_id)
        for(EdgeG2 edge:bag_edges){
            if(edge.getChild().equals(id)) edge.setChild(new_v_id);
            if(edge.getParent().equals((id))) edge.setParent(new_v_id);
        }

        for(VertexG2 buf:bag_cells)
            if(buf.getId().equals(id)){
                bag_cells.remove(buf);
                break;
            }
    }

    // вернуть начальный элемент (first) в подграфе g2
    public String getFirstInG2(){
        for(VertexG2 buf:bag_cells)
            if(buf.isBeginning()) return buf.getId();
        return null;
    }

    // вернуть последний элемент (last) в подграфе g2
    public String getLastInG2(){
        for(VertexG2 buf:bag_cells)
            if(buf.isEnd()) return buf.getId();
        return null;
    }

    // вернуть землю если есть в подграфе g2
    public String getEarthInG2(){
        for(VertexG2 buf:bag_cells)
            if(buf.isEarth()) return buf.getId();
        return null;
    }

    // добавить кусок g2 к существующему графу
    public void addG2(GraphG2 g2){
        bag_edges.addAll(g2.getBag_edges());
        bag_cells.addAll(g2.getBag_cells());
    }

    // сбросить флаг first
    public void resetFirst(String id){
        for(VertexG2 buf:bag_cells)
            if(buf.getId().equals(id)) buf.setBeginning(false);
    }
}
