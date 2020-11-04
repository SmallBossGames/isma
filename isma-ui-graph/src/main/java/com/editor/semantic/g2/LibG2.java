package com.editor.semantic.g2;

import java.util.ArrayList;

// библиотека схем замещения
public class LibG2 {
	
	public static ArrayList<GraphG2> getElementsG2(String style){
		ArrayList<GraphG2> buf = new ArrayList<GraphG2>();
		if(style.equals("ismaCommonLoad")) getCommonLoadG2(buf, style);
		else if(style.equals("ismaGenerator")) getGenerator(buf, style);
		else if(style.equals("ismaTransform")) getTransform(buf, style);
		else if(style.equals("boldEWEdge")) getPowerLine(buf, style);
        else if(style.equals("wireSouth") ||
                style.equals("wireWest") ||
                style.equals("wireNorth") ||
                style.equals("wireEast")) getWireG2(buf, style);
		else return null;
		return buf;
	}

    // Граф g2 для шины
    public static void getWireG2(ArrayList<GraphG2> grElementsG2, String style){
        GraphG2 graph = new GraphG2();
        VertexG2 A = new VertexG2("A"+style, 0, 0, false, true, true);
        graph.addVertex(A);
        grElementsG2.add(graph);
    }
	
	// Возвращает граф для нагрузки
	public static void getCommonLoadG2(ArrayList<GraphG2> grElementsG2, String style){
		GraphG2 graph = new GraphG2();
		
		VertexG2 A = new VertexG2("A"+style, 0, 0, false, true, false);
		VertexG2 B = new VertexG2("B"+style, 0, 0);
		VertexG2 N = new VertexG2("N", 0, 0, true, false, true);
		graph.addVertex(A);
		graph.addVertex(B);
		graph.addVertex(N);
		
		EdgeG2 edgeAB = new EdgeG2("A"+style, "B"+style);
		EdgeG2 edgeBN = new EdgeG2("B"+style, "N");
		graph.addEdge(edgeAB);
		graph.addEdge(edgeBN);
		
		grElementsG2.add(graph);
	}
	
	public static void getGenerator(ArrayList<GraphG2> grElementsG2, String style){
		GraphG2 graph = new GraphG2();

        VertexG2 N = new VertexG2("N", 0, 0, true, true, false);
		VertexG2 A = new VertexG2("A"+style, 0, 0);
		VertexG2 B = new VertexG2("B"+style, 0, 0);
		VertexG2 C = new VertexG2("C"+style, 0, 0, false, false, true);
        graph.addVertex(N);
		graph.addVertex(A);
		graph.addVertex(B);
		graph.addVertex(C);
		
		EdgeG2 edgeNA = new EdgeG2("N", "A"+style);
		EdgeG2 edgeAB = new EdgeG2("A"+style, "B"+style);
		EdgeG2 edgeBC = new EdgeG2("B"+style, "C"+style);
		graph.addEdge(edgeNA);
		graph.addEdge(edgeAB);
		graph.addEdge(edgeBC);
		
		grElementsG2.add(graph);
	}
	
	public static void getTransform(ArrayList<GraphG2> grElementsG2, String style){
		GraphG2 graph = new GraphG2();
		
		VertexG2 A = new VertexG2("A"+style, 0, 0, false, true, false);
		VertexG2 B = new VertexG2("B"+style, 0, 0);
		VertexG2 C = new VertexG2("C"+style, 0, 0);
		VertexG2 D = new VertexG2("D"+style, 0, 0, false, false, true);
		graph.addVertex(A);
		graph.addVertex(B);
		graph.addVertex(C);
		graph.addVertex(D);
		
		EdgeG2 edgeAB = new EdgeG2("A"+style, "B"+style);
		EdgeG2 edgeBC = new EdgeG2("B"+style, "C"+style);
		EdgeG2 edgeCD = new EdgeG2("C"+style, "D"+style);
		graph.addEdge(edgeAB);
		graph.addEdge(edgeBC);
		graph.addEdge(edgeCD);
		
		grElementsG2.add(graph);
	}
	
	// Возвращает графы G2 для ЛЭП
	public static void getPowerLine(ArrayList<GraphG2> grElementsG2, String style){
		// Первый граф G2 (до КЗ)
		GraphG2 graph1 = new GraphG2();
		
		VertexG2 A = new VertexG2("A"+style, 0, 0, false, true, false);
		VertexG2 B = new VertexG2("B"+style, 0, 0);
		VertexG2 C = new VertexG2("C"+style, 0, 0);
		VertexG2 D = new VertexG2("D"+style, 0, 0);
		VertexG2 E = new VertexG2("E"+style, 0, 0, false, false, true);
		graph1.addVertex(A);
		graph1.addVertex(B);
		graph1.addVertex(C);
		graph1.addVertex(D);
		graph1.addVertex(E);
		
		EdgeG2 edgeAB = new EdgeG2("A"+style, "B"+style);
		EdgeG2 edgeBC = new EdgeG2("B"+style, "C"+style);
		EdgeG2 edgeCD = new EdgeG2("C"+style, "D"+style);
		EdgeG2 edgeDE = new EdgeG2("D"+style, "E"+style);
		graph1.addEdge(edgeAB);
		graph1.addEdge(edgeBC);
		graph1.addEdge(edgeCD);
		graph1.addEdge(edgeDE);
		
		grElementsG2.add(graph1);
		
		// Второй граф G2 (после КЗ)
		GraphG2 graph2 = new GraphG2();
		
		VertexG2 N = new VertexG2("N", 0, 0, true, false, false);
		graph2.addVertex(A);
		graph2.addVertex(B);
		graph2.addVertex(N);
		graph2.addVertex(D);
		graph2.addVertex(E);
		
		EdgeG2 edgeBN = new EdgeG2("B"+style, "N");
		EdgeG2 edgeND = new EdgeG2("N", "D"+style);
		graph2.addEdge(edgeAB);
		graph2.addEdge(edgeBN);
		graph2.addEdge(edgeND);
		graph2.addEdge(edgeDE);
		
		grElementsG2.add(graph2);
	}
}
