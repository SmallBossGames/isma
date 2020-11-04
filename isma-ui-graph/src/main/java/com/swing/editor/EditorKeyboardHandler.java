/**
 * $Id: EditorKeyboardHandler.java,v 1.1 2009-10-23 11:32:08 gaudenz Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package com.swing.editor;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;

/**
 * @author Administrator
 * 
 */
public class EditorKeyboardHandler extends mxKeyboardHandler
{

	/**
	 * 
	 * @param graphComponent
	 */
	public EditorKeyboardHandler(mxGraphComponent graphComponent)
	{
		super(graphComponent);
	}

	/**
	 * Return JTree's input map.
	 */
	protected InputMap getInputMap(int condition)
	{
		InputMap map = super.getInputMap(condition);

		if (condition == JComponent.WHEN_FOCUSED && map != null)
		{
			map.put(KeyStroke.getKeyStroke("control S"), "save");
			map.put(KeyStroke.getKeyStroke("control shift S"), "saveAs");
			map.put(KeyStroke.getKeyStroke("control N"), "new");
			map.put(KeyStroke.getKeyStroke("control O"), "open");

			map.put(KeyStroke.getKeyStroke("control Z"), "undo");
			map.put(KeyStroke.getKeyStroke("control Y"), "redo");
			map
					.put(KeyStroke.getKeyStroke("control shift V"),
							"selectVertices");
			map.put(KeyStroke.getKeyStroke("control shift E"), "selectEdges");
			
			// РАБОТА С ШИНОЙ
			// работа с портами
			map.put(KeyStroke.getKeyStroke("control 1"), "addport");
			map.put(KeyStroke.getKeyStroke("control 2"), "deleteport");
			// смена направления
			map.put(KeyStroke.getKeyStroke("control N"), mxConstants.DIRECTION_NORTH);
			map.put(KeyStroke.getKeyStroke("control S"), mxConstants.DIRECTION_SOUTH);
			map.put(KeyStroke.getKeyStroke("control W"), mxConstants.DIRECTION_WEST);
			map.put(KeyStroke.getKeyStroke("control E"), mxConstants.DIRECTION_EAST);
		}

		return map;
	}

	/**
	 * Return the mapping between JTree's input map and JGraph's actions.
	 */
	protected ActionMap createActionMap()
	{
		ActionMap map = super.createActionMap();

		map.put("save", new EditorActions.SaveAction(false));
		map.put("saveAs", new EditorActions.SaveAction(true));
		map.put("new", new EditorActions.NewAction());
		map.put("open", new EditorActions.OpenAction());
		map.put("undo", new EditorActions.HistoryAction(true));
		map.put("redo", new EditorActions.HistoryAction(false));
		map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
		map.put("selectEdges", mxGraphActions.getSelectEdgesAction());
		
		// РАБОТА С ШИНОЙ
		// работа с портами
		map.put("addport", new EditorActions.EditPortAction(true));
		map.put("deleteport", new EditorActions.EditPortAction(false));
		// смена направления
		map.put(mxConstants.DIRECTION_NORTH, new EditorActions.KeyValueAction(
				mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_NORTH));
		map.put(mxConstants.DIRECTION_EAST, new EditorActions.KeyValueAction(
				mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_EAST));
		map.put(mxConstants.DIRECTION_SOUTH, new EditorActions.KeyValueAction(
				mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_SOUTH));
		map.put(mxConstants.DIRECTION_WEST, new EditorActions.KeyValueAction(
				mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_WEST));
		return map;
	}

}
