package com.editor.semantic.g1;

import com.editor.semantic.Edge;
import com.editor.semantic.g2.GraphG2;
import com.editor.semantic.g2.LibG2;
import com.editor.utils.GraphUtil;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

// !!! Надо будет еще Graph суперклассом для графов G1, G2 и G3 сделать.
// !!! подумать как это можно реализовать
public class GraphG1{

	protected ArrayList<Edge> bag_edges = null;
	protected ArrayList<VertexG1> bag_cells = null;
   // protected ArrayList<GraphG2> g2 = null;
    //Список возможных состояний графа G1
	protected ArrayList<ArrayList<VertexG1>> statesList = null;


	public GraphG1(){
		bag_edges = new ArrayList<Edge>();
		bag_cells = new ArrayList<VertexG1>();
       // g2 = new ArrayList<GraphG2>();
        //Объявление списка состояний
        statesList = new ArrayList<ArrayList<VertexG1>>();
	}

	// добавить все дуги из "оригинального" графа
	// в parent и child хранятся индексы портов на графе,  а не самих элементов
	// ***** придумать как это реализовать грамотно! ****
	// ! ИСПРАВИЛ теперь хранятся индексы элементов, а не портов
	public void addEdges(Object edges [], Object cells [], mxGraph graph){
		mxCell buf;
		for(Object item:edges){
			buf = (mxCell) item;

			// работа с ЛЭП, ЛЭП - линия. Цель преобразовать в вершину графа G1.
			if(buf.getStyle().equals("boldEWEdge")){
                addEdge(GraphUtil.getParentId(graph, buf.getSource().getId(), cells), buf.getId());
                addEdge(buf.getId(), GraphUtil.getParentId(graph, buf.getTarget().getId(), cells));
                addVertex(buf);
			}
			else
                addEdge(GraphUtil.getParentId(graph, buf.getSource().getId(), cells), GraphUtil.getParentId(graph, buf.getTarget().getId(), cells));

		}
	}

	// добавление всех вершин из "оригинального" графа
	public void addVertecies(Object cells []){
		mxCell buf;
		for(Object item:cells){
			// обработка шин - "склеивание дуг"
			buf = (mxCell) item;
			if(buf.getStyle().equals("wireSouth") ||
					buf.getStyle().equals("wireWest") ||
					buf.getStyle().equals("wireNorth") ||
					buf.getStyle().equals("wireEast")) {
				transformG1duetoWire(buf);
			}
			else
				addVertex(buf);
		}
        findState(new ArrayList<VertexG1>(), 0);

	}

    //Копирует список
    private ArrayList<VertexG1> copyList(ArrayList<VertexG1> list) {
        ArrayList<VertexG1> clone = new ArrayList<VertexG1>(list.size());
        for(VertexG1 item: list) clone.add(new VertexG1(item));
        return clone;
    }

    //Метод рекурсивного поиска всех возможных состояний системы
    private void findState(ArrayList<VertexG1> prevVertexG2GraphsList, int offset) {
        //Создаем новый экземпляр состояния и переписываем в него предыдущие элементы, если они есть
        ArrayList<VertexG1> state = copyList(prevVertexG2GraphsList);

        //Пока есть состояния
        for (int vertexCounter = offset; vertexCounter<bag_cells.size(); vertexCounter++) {
            //Получаем список G2 графов из вершины G1
            VertexG1 copyVertex = new VertexG1(bag_cells.get(vertexCounter));
            ArrayList<GraphG2> vertexG2GraphsList = copyVertex.getElementsG2();
            //Если в вершине нет графов - пропускаем её (пока есть вершины без G2, потом можно будет убрать)
            if (vertexG2GraphsList == null || vertexG2GraphsList.size() == 0)
                continue;
            //Если графов G2 в вершине несколько
            else if (vertexG2GraphsList.size()>1) {
                Iterator<GraphG2> iter = vertexG2GraphsList.iterator();
                //Пока в текущей вершине G1 есть еще графики G2...
                while(iter.hasNext()) {
                    //Берем другой график
                    GraphG2 item = iter.next();
                    copyVertex.setElementsG2(new ArrayList<GraphG2>(
                            Arrays.asList(item)));
                    state.add(copyVertex);
                    //Продолжаем искать с этого места но уже используя новое состояние
                    findState(new ArrayList<VertexG1>(state), ++vertexCounter);
                    state.remove(copyVertex);
                    --vertexCounter;
                    //Выходное условие
                    if (!iter.hasNext())
                            return;
                }
            }
            //Иначе просто записываем графG2 в состояние
            else {
                copyVertex.setElementsG2(new ArrayList<GraphG2>(
                        Arrays.asList(vertexG2GraphsList.get(0))));
                state.add(copyVertex);
            }
        }
        statesList.add(state);
    }

	// true - deleted, false - не удалось удалить дугу
	public boolean deleteEdge(Edge edge){
		return bag_edges.remove(edge);
	}
	
	public void transformG1duetoWire(mxCell buf){
		String id = buf.getId();
		ArrayList<Edge> edges = getEdges(id);
		if(edges == null)
			addVertex(buf);
		else
			stickEdgesTogether(id, edges);
	}
	
	public void addVertex(mxCell buf){
		VertexG1 vertex = new VertexG1();
		
		vertex.setId(buf.getId());
		vertex.setValue(buf.getValue().toString());
		vertex.setStyle(buf.getStyle());
        vertex.setNumberConnects(getVertexG1NumberConnects(buf.getId()));
		
		// дописать инициализацию напряжений
		
		// добавляем подграфы графа G2
		vertex.setElementsG2(LibG2.getElementsG2(buf.getStyle()));
		
		bag_cells.add(vertex);
	}

    public void addEdge(String parent, String child){
        Edge edge = new Edge();

        edge.setParent(parent);
        edge.setChild(child);

        bag_edges.add(edge);
    }
	
	public ArrayList<Edge> getEdges(String id){
		ArrayList<Edge> buf = new ArrayList<Edge>();
		for(Edge item:bag_edges){
			if(item.getParent().equals(id) || item.getChild().equals(id))
				buf.add(item);
		}
		if(buf.size() != 2) return null;
		return buf;
	}
	
	// склеиваем дуги, удаляя две старые и добавляя одну новую
	public void stickEdgesTogether(String id, ArrayList<Edge> edges){		
		String child = null, parent = null;
		for(Edge item:edges){
			if(!item.getChild().equals(id)) child = item.getChild();
			if(!item.getParent().equals(id)) parent = item.getParent();
			deleteEdge(item);
		}
		bag_edges.add(new Edge(child, parent));
	}

    // вернуть количество коннекторов вершины
    public int getVertexG1NumberConnects(String id_vertex){
        int n = 0;
        for(Edge item:bag_edges)
            if(item.getChild().equals(id_vertex) || item.getParent().equals(id_vertex)) n++;
        return n;
    }

    /*  ------------------
        Работа с графом G2
        ------------------ */

    public void stickG2Together(){
        /* Алгоритм обхода по графу G1 */
        /*
        *   1. Ищем все узлы с одним коннектором (генераторы);
        *   2. Сворачиваем до ближайшей шины;
        *   3. Если шина -> ищем узлы в п.1;
        *   4. Сворачиваем оставшиеся "клешни", начиная с шины и до конца.
        *   Как-то так!
        *
        * */

        GraphG2 g2_temp;
        VertexG1 v = findGenerator();
        g2_temp = v.getElementsG2().get(0);
        VertexG1 next = getVertexById(findNextVertex(v.getId()));
        while(next != null){
            g2_temp = stickPartsG2(g2_temp, next.getElementsG2().get(0));
            next = getVertexById(findNextVertex(next.getId()));
        }
    }

    // поиск генератора
    public VertexG1 findGenerator(){
        for(VertexG1 buf:bag_cells){
            if(buf.isVisited() && (buf.getStyle().equals("ismaGenerator") ||
                                           buf.getStyle().equals("ismaElectricalPowerSystem") ||
                                           buf.getStyle().equals("ismaSynchronousMotor") ||
                                           buf.getStyle().equals("ismaInductionMotor"))){
                buf.setVisited(true);
                return buf;
            }
        }
        return null;
    }


    // вернуть следущий по порядку элемент
    public String findNextVertex(String previous){
        for(Edge edge:bag_edges){
            if(edge.getChild().equals(previous)  && !isVertexVisited(edge.getParent())){
                setVertexIsVisitedById(edge.getParent());
                return edge.getParent();
            }
            else if(edge.getParent().equals(previous) && !isVertexVisited(edge.getChild())){
                setVertexIsVisitedById(edge.getChild());
                return edge.getChild();
            }
        }
        return null;        // если нет больше узлов в графе G1
    }

    // проверка, что узел уже ПОСЕЩЕН
    public boolean isVertexVisited(String id){
        for(VertexG1 buf:bag_cells)
            if(buf.getId().equals(id)) return buf.isVisited();
        return false;
    }

    // Утановить флаг посещения для вершины, по id
    public VertexG1 getVertexById(String id){
        for(VertexG1 buf:bag_cells)
            if(buf.getId().equals(id))
                return buf;
        return null;
    }

    // Утановить флаг посещения для вершины, по id
    public void setVertexIsVisitedById(String id){
        for(VertexG1 buf:bag_cells)
            if(buf.getId().equals(id))
                buf.setVisited(true);
    }

    // склеить две части g2
    public GraphG2 stickPartsG2(GraphG2 previous, GraphG2 next){
        /* Алгоритм склейки двух кусков g2:
            1. Ищем "голову" у next.
            2. Ищем "хвост" у previous и удаляем эту конечную вершину, модифицируем все дуги ссылающиеся на эту (удаленную) вершину.
            3. Ищем доп. лишние земли и объединяем..
         */

        previous.removeVertexAndUpgradeEdgesById(previous.getLastInG2(), next.getFirstInG2());
        next.resetFirst(next.getFirstInG2());

        // Cклейка "земли"
        if(next.getEarthInG2() != null && previous.getEarthInG2() != null)
            previous.removeVertexAndUpgradeEdgesById(previous.getEarthInG2(), next.getEarthInG2());

        previous.addG2(next);
        return previous;
    }
}