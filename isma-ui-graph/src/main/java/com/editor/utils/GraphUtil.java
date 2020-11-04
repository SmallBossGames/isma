package com.editor.utils;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class GraphUtil {
	
	// возвращает индекс элемента, к которому прикреплен порт id
	public static String getParentId(mxGraph graph, String port_id, Object bag_cells []){
		for(Object v:bag_cells){
			Object cells [];
			cells = graph.getChildVertices(v);
			mxCell buf = null;
			for(Object item:cells){
				buf = (mxCell) item;
				if(buf.getId().equals(port_id))
					return buf.getParent().getId();
			}		
		}
		return null;
	}
}
