package com.swing.editor;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.view.mxGraph;

public class ElementModifier {
	
	final static int PORT_DIAMETER = 6;

	final static int PORT_RADIUS = PORT_DIAMETER / 2;
	
	/**
	 * 
	 * 	начало координат = верхний левый угол фигуры
	 * @param x = длина фигуры в горизонтальном направлении
	 * @param y = длина фигуры в вертикальном направлении
	 */

    public static mxCell createMacrosPort(double x, double y, boolean leftSide) {
        mxGeometry geo = new mxGeometry(x, y, PORT_DIAMETER,
                PORT_DIAMETER);

        geo.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
        geo.setRelative(true);
        String alignment = "labelPosition=left;align=right;";
        if (leftSide) {
            alignment = "labelPosition=right;align=left;";
        }
        String style = "shape=ellipse;perimeter=ellipsePerimeter;deletable=0;resizable=false;" +
                "verticalLabelPosition=center;fontSize=8;";
        style += alignment;
        mxCell port = new mxCell(null, geo, style);
        port.setVertex(true);

        return port;
    }

	public static mxCell addPortToFigure(double x, double y){
		mxGeometry geo = new mxGeometry(x, y, PORT_DIAMETER,
				PORT_DIAMETER);
		
		geo.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
		geo.setRelative(true);
		
		mxCell port = new mxCell(null, geo,
				"shape=ellipse;perimeter=ellipsePerimeter;deletable=0");
		port.setVertex(true);
		
		return port;
	}

    public static mxCell addPortToTransform(double x, double y, boolean isLeft) {
        final int transformerPortSize = PORT_DIAMETER + 1;
        mxGeometry geo = new mxGeometry(x, y, transformerPortSize,
                transformerPortSize);

        geo.setOffset(new mxPoint(-transformerPortSize / 2, -transformerPortSize / 2));
        geo.setRelative(true);
        mxCell port;
        if (isLeft) {
            port = new mxCell(null, geo,
                    mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_TRIANGLE + ";" +
                            mxConstants.STYLE_DIRECTION + "=" + mxConstants.DIRECTION_EAST + ";" +
                            "deletable=0");
        }
        else {
            port = new mxCell(null, geo,
                    mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_TRIANGLE + ";" +
                            mxConstants.STYLE_DIRECTION + "=" + mxConstants.DIRECTION_EAST + ";" +
                            "deletable=0");
        }
        port.setVertex(true);

        return port;
    }
	
	/**
	 * Добавление портов к Трехобмоточному трансформатору, Автотрансформатору
	 * @param graph = граф элементов
	 * @param cell = сам элемент на палитре
	 */
	
	public static void addTreeTransform(mxGraph graph, mxCell cell){
		graph.addCell(addPortToFigure(0.5, 1), cell);
		graph.addCell(addPortToFigure(1.0, 0.3), cell);
		graph.addCell(addPortToFigure(0, 0.3), cell);
	}	
	
	/**
	 * Добавление портов к шине
	 * @param graph = граф элементов
	 * @param cell = сам элемент на палитре
	 */
	
	public static void addToWire(mxGraph graph, mxCell cell){
		graph.addCell(addPortToFigure(0, 0.33), cell);
		graph.addCell(addPortToFigure(0, 0.66), cell);
		graph.addCell(addPortToFigure(1, 0.5), cell);
	}	
	
	/**
	 * Добавление портов к Выключателям, Размыкателям, Реатору и Трансформатору
	 * @param graph = граф элементов
	 * @param cell = сам элемент на палитре
	 */
	
	public static void addPortsToTheRightAndLeft(mxGraph graph, mxCell cell){
        if (cell.getStyle().equals("ismaTransform")) {
            graph.addCell(addPortToTransform(1.0, 0.5, true), cell);
            graph.addCell(addPortToTransform(0, 0.5, false), cell);
        }
        else {
            graph.addCell(addPortToFigure(1.0, 0.5), cell);
            graph.addCell(addPortToFigure(0, 0.5), cell);
        }
	}	
	
	/**
	 * Добавление портов к Синхронному генератору, Системе электроснабжения, 
	 * Синхронному двигателю и Асинхронному двигателю
	 * @param graph = граф элементов
	 * @param cell = сам элемент на палитре
	 */
	
	public static void addPortsToTheRight(mxGraph graph, mxCell cell){
		graph.addCell(addPortToFigure(1.0, 0.5), cell);
	}	
	
	/**
	 * Добавление порта к Нагрузке
	 * @param graph = граф элементов
	 * @param cell = сам элемент на палитре
	 */
	
	public static void addPortsToTheTop(mxGraph graph, mxCell cell){
		graph.addCell(addPortToFigure(0.5, 0), cell);
	}
	
	/**
	 * Добавление портов к различным фигурам
	 * @param name = название элемента
	 * @param cell = сам элемент на палитре
	 */
	
	public static void addPorts(final String name, mxCell cell, mxGraph graph){
		cell.setConnectable(false);		
		//mxGraph graph = BasicGraphEditor.graphComponent.getGraph();
		
		if(name.equals(mxResources.get("load")))
			addPortsToTheTop(graph, cell);
		
		if(name.equals(mxResources.get("wire"))){
			addToWire(graph, cell);
			//cell.setAttribute(mxConstants.STYLE_ROUNDED, "1");
		}
		
		if(name.equals(mxResources.get("threeWindinTransformer")) ||
				name.equals(mxResources.get("autotransformer")) )
			addTreeTransform(graph, cell);
		
		if(name.equals(mxResources.get("reactor")) ||
				name.equals(mxResources.get("key")) ||
				name.equals(mxResources.get("switch")) ||
				name.equals(mxResources.get("transformer")) ||
                name.equals(mxResources.get("diode") ))
			addPortsToTheRightAndLeft(graph, cell);

		if(name.equals(mxResources.get("generator")) ||
				name.equals(mxResources.get("synchronousMotor")) ||
				name.equals(mxResources.get("inductionMotor")) ||
				name.equals(mxResources.get("electricalPowerSystem")) ||
                name.equals(mxResources.get("macrosIn")))
			addPortsToTheRight(graph, cell);
	}
	
	/**
	 * Перемещение портов по шине
	 * @param oldStyle = прошлое положение шины
	 * @param newStyle = новое положение шины
	 * @param cell = шина с портами
	 */
	
	public static void movingPortsOnWire (String oldStyle, String newStyle, mxCell cell){
		// Проверка того, что новый стиль и старый стиль
		if(newStyle.equals(oldStyle))
			return;
		
		// Повороты по горизонтали или по вертикали
		// Необходимо только заменить значения координат с 0 на 1 и с 1 на 0.
		if(		(newStyle.equals(mxConstants.DIRECTION_NORTH) &&
						oldStyle.equals(mxConstants.DIRECTION_SOUTH)) ||
				(newStyle.equals(mxConstants.DIRECTION_SOUTH) &&
						oldStyle.equals(mxConstants.DIRECTION_NORTH)) ||
				(newStyle.equals(mxConstants.DIRECTION_EAST) &&
						oldStyle.equals(mxConstants.DIRECTION_WEST)) ||
				(newStyle.equals(mxConstants.DIRECTION_WEST) &&
						oldStyle.equals(mxConstants.DIRECTION_EAST)))
			movingPorts(cell, true, false);
		
		// Повороты по верхнему правому углу и нижнему левому углу
		// Необходимо только заменить значения координат с 0 на 1 и с 1 на 0 
		// + обмен значениями координат X и Y 
		if(		(newStyle.equals(mxConstants.DIRECTION_NORTH) &&
						oldStyle.equals(mxConstants.DIRECTION_WEST)) ||
				(newStyle.equals(mxConstants.DIRECTION_WEST) &&
						oldStyle.equals(mxConstants.DIRECTION_NORTH)) ||
				(newStyle.equals(mxConstants.DIRECTION_EAST) &&
						oldStyle.equals(mxConstants.DIRECTION_SOUTH)) ||
				(newStyle.equals(mxConstants.DIRECTION_SOUTH) &&
						oldStyle.equals(mxConstants.DIRECTION_EAST)))
			movingPorts(cell, true, true);
		
		// Повороты по верхнему левому углу и нижнему правому углу
		// обмен значениями координат X и Y
		if(		(newStyle.equals(mxConstants.DIRECTION_NORTH) &&
						oldStyle.equals(mxConstants.DIRECTION_EAST)) ||
				(newStyle.equals(mxConstants.DIRECTION_EAST) &&
						oldStyle.equals(mxConstants.DIRECTION_NORTH)) ||
				(newStyle.equals(mxConstants.DIRECTION_WEST) &&
						oldStyle.equals(mxConstants.DIRECTION_SOUTH)) ||
				(newStyle.equals(mxConstants.DIRECTION_SOUTH) &&
						oldStyle.equals(mxConstants.DIRECTION_WEST)))
			movingPorts(cell, false, true);
	}
	
	/**
	 * Перемещение портов на шине
	 * @param inversion = инверсия координат
	 * @param exchanging = разрешение поменять X на Y and vice versa
	 */
	
	public static void movingPorts(mxCell c, boolean inversion, boolean exchanging){
		// Сколько портов всего
		int children = c.getChildCount();
		if(children > 0)
			for(int i = 0; i < children; i++){
				// получаем порт
				mxCell child = (mxCell) c.getChildAt(i);
				mxGeometry geo = child.getGeometry();
				double X = geo.getX(), Y = geo.getY();
				
				if(inversion){
					if(X == 0) X = 1;
					else if(X == 1) X = 0;
					if(Y == 0) Y = 1;
					else if(Y == 1) Y = 0;
				}
				
				if(exchanging){
					double temp;
					temp = X;
					X = Y;
					Y = temp;
				}

				// обновляем координаты нового расположения порта
				geo.setX(X);
				geo.setY(Y);
				child.setGeometry(geo);
			}
	}


    public static void rotateTransformerPorts(mxIGraphModel graph, mxCell cell) {
        Object[] children = mxGraphModel.getChildren(graph, cell);
        mxCell child = (mxCell) children[0];
        String cellStyle = child.getStyle();
        String direction = getStyleValue(cellStyle, mxConstants.STYLE_DIRECTION);
        if (direction.equals(mxConstants.DIRECTION_WEST)) {
            mxStyleUtils.setCellStyles(graph, children, mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_EAST);
        }
        else {
            mxStyleUtils.setCellStyles(graph, children, mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_WEST);
        }
    }

    public static String getStyleValue(String style, String key) {
        int keyEnd = style.lastIndexOf(key);
        if (keyEnd == -1) {
            return "";
        }
        int valueEnd = style.indexOf(';', keyEnd);
        if (valueEnd == -1) {
            valueEnd = style.length() - 1;
        }
        return style.substring(keyEnd + mxConstants.STYLE_DIRECTION.length() + 1, valueEnd);
    }

	/**
	 * Перерисовка старых портов на шине
	 * @param direction = направление шины
	 * @param removeOrAdd = true = добавить; false = удалить
	 */

	public static void repaintOldPortsOnWire(mxCell c, String direction, boolean removeOrAdd){
		// Сколько портов всего
		int children = c.getChildCount();
		// Коэффециент на который нужно домнажать коодинату точки, чтобы получить новое положение точки
		double k = (double) children/(children+(removeOrAdd?1:-1));
		if(children > 0)
			for(int i = 0; i < children; i++){
				// получаем порт
				mxCell child = (mxCell) c.getChildAt(i);
				mxGeometry geo = child.getGeometry();
				double X = geo.getX(), Y = geo.getY();				
				if(direction.equals(mxConstants.DIRECTION_SOUTH)){
					if(X == 0)
						Y = Y*k;
				}
				else if(direction.equals(mxConstants.DIRECTION_NORTH)){
					if(X == 1)
						Y = Y*k;
				}
				else if(direction.equals(mxConstants.DIRECTION_EAST)){
					if(Y == 1)
						X = X*k;
				}
				else if(direction.equals(mxConstants.DIRECTION_WEST)){
					if(Y == 0)
						X = X*k;
				}
				// обновляем координаты нового расположения порта
				geo.setX(X);
				geo.setY(Y);
				child.setGeometry(geo);
			}
	}
	
	/**
	 * Добавляем новую точку на шине
	 * @param direction = направление шины
	 */
	
	public static void addNewPortOnWire(mxCell wire, mxGraph graph, String direction){
		// Сколько портов всего
		int children = wire.getChildCount();
		// Коэффециент на который нужно домнажать коодинату точки, чтобы получить новое положение точки
		double k = (double) children/(children+1);
		// Когда портов на шине нет
		if(k == 0) k = 0.5;
		double X = 0, Y = 0;
				
		if(direction.equals(mxConstants.DIRECTION_SOUTH)){
			X = 0;
			Y = k;
		}
		else if(direction.equals(mxConstants.DIRECTION_NORTH)){
			X = 1;
			Y = k;
		}
		else if(direction.equals(mxConstants.DIRECTION_EAST)){
			Y = 1;
			X = k;
		}
		else if(direction.equals(mxConstants.DIRECTION_WEST)){
			Y = 0;
			X = k;
		}

		graph.addCell(addPortToFigure(X, Y), wire);
	}
	
	/**
	 * Удалить точку  на шине
	 * @param direction = направление шины
	 */
	
	public static void deletePortOnWire(mxCell wire, mxGraph graph, String direction){
		// Сколько портов всего
		int children = wire.getChildCount();
		mxCell deleteChild = null;
		double Ymax = 0, Xmax = 0;
		
		if(children > 0)
			for(int i = 0; i < children; i++){
				// получаем порт
				mxCell child = (mxCell) wire.getChildAt(i);
				mxGeometry geo = child.getGeometry();
				double X = geo.getX(), Y = geo.getY();
				
				if(direction.equals(mxConstants.DIRECTION_SOUTH) || direction.equals(mxConstants.DIRECTION_NORTH)){
					if(Y>Ymax){
						deleteChild = child;
						Ymax = Y;
					}
				}
				else if(direction.equals(mxConstants.DIRECTION_EAST) || direction.equals(mxConstants.DIRECTION_WEST)){
					if(X>Xmax){
						deleteChild = child;
						Xmax = X;
					}
				}
			}
		
		wire.remove(deleteChild);
	}

}
