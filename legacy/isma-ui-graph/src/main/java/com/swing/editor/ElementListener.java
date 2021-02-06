package com.swing.editor;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* Класс по обработке событий элементов графа */
public class ElementListener {
	
	/**
	 * Обработка события двойного клика для всех элементов
	 * электрической схемы
	 */
	public static void addListener(final mxGraphComponent graphComponent){
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter()
		{

			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount()==2) {
					Object cell = graphComponent.getCellAt(e.getX(), e.getY());			
					if (cell != null)
					{
						mxCell c = (mxCell) cell;
                        //ElementModifier.rotateElement(c, 90);
						String style = c.getStyle();
						if(style.equals("ismaElectricalPowerSystem") ||
								style.equals("ismaSynchronousMotor") ||
								style.equals("ismaInductionMotor") ||
								style.equals("ismaGenerator") ||
                                style.equals("macrosIn")){
							// получаем порт
							mxCell child = (mxCell) c.getChildAt(0);
							mxGeometry geo = child.getGeometry();

							double newX = 0.5, newY = 0;
							// в дальнейшем этот гавно-код переписать, если будет желание)
							// точка вверху, меняем на точку справа
							if(geo.getX() == 0.5 && geo.getY() == 0){ 
								newX = 1;
								newY = 0.5;
							}
							// точка справа, меняем на точку снизу
							if(geo.getX() == 1 && geo.getY() == 0.5){ 
								newX = 0.5;
								newY = 1;
							}
							// точка внизу, меняем на точку слева
							if(geo.getX() == 0.5 && geo.getY() == 1){ 
								newX = 0;
								newY = 0.5;
							}
							// точка слева, меняем на точку сверху
							if(geo.getX() == 0 && geo.getY() == 0.5){ 
								newX = 0.5;
								newY = 0;
							}
							// обновляем координаты нового расположения порта
							geo.setX(newX);
							geo.setY(newY);
							child.setGeometry(geo);
						}
                        if (style.equals("ismaTransform")) {
                            ElementModifier.rotateTransformerPorts(graphComponent.getGraph().getModel(), c);
                        }
                        graphComponent.refresh();
					}
				}
			}
		});
	}
}
