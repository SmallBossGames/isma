package com.swing.editor;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxResources;

import javax.swing.*;

public class EditorPopupMenu extends JPopupMenu
{

	/**
	 * @x = координата фигуры x
	 * @y = координата фигуры y
	 */
	private static final long serialVersionUID = -3132749140550242191L;

	public EditorPopupMenu(BasicGraphEditor editor)
	{
		boolean selected = !editor.getGraphComponent().getGraph()
				.isSelectionEmpty();

		boolean isWire = false;
		// Получаем фигуру, для которой вызвали контекстное меню
		mxCell cell = (mxCell) editor.getGraphComponent().getGraph().getSelectionCell();
		if (cell != null)
		{
			mxCell c = (mxCell) cell;
			String style = c.getStyle();
			if(style.equals("wireEast") || 
					style.equals("wireWest") ||
					style.equals("wireSouth") ||
					style.equals("wireNorth"))
				isWire = true;
		}

		add(editor.bind(mxResources.get("undo"), new EditorActions.HistoryAction(true),
				"/images/undo.gif"));

		addSeparator();

		add(
				editor.bind(mxResources.get("cut"), TransferHandler
						.getCutAction(),
						"/images/cut.gif"))
				.setEnabled(selected);
		add(
				editor.bind(mxResources.get("copy"), TransferHandler
						.getCopyAction(),
						"/images/copy.gif"))
				.setEnabled(selected);
		add(editor.bind(mxResources.get("paste"), TransferHandler
				.getPasteAction(),
				"/images/paste.gif"));

		addSeparator();

		add(
				editor.bind(mxResources.get("delete"), mxGraphActions
						.getDeleteAction(),
						"/images/delete.gif"))
				.setEnabled(selected);

		addSeparator();

        add(editor.bind(mxResources.get("createMacros"),
                new EditorActions.CreateMacrosAction()));

		// Creates the format menu
		JMenu formatMenu = (JMenu) add(new JMenu(mxResources.get("format")));

		EditorMenuBar.populateFormatMenu(formatMenu, editor);
		formatMenu.setEnabled(isWire);

		// Creates the shape menu
//		menu = (JMenu) add(new JMenu(mxResources.get("shape")));
//
//		EditorMenuBar.populateShapeMenu(menu, editor);

		addSeparator();

		add(editor.bind(mxResources.get("edit"), new EditorActions.EditAction()));

//		addSeparator();
//
//		add(editor.bind(mxResources.get("selectVertices"), mxGraphActions
//				.getSelectVerticesAction()));
//		add(editor.bind(mxResources.get("selectEdges"), mxGraphActions
//				.getSelectEdgesAction()));

		addSeparator();

		add(editor.bind(mxResources.get("selectAll"), mxGraphActions
				.getSelectAllAction()));
	}

}
