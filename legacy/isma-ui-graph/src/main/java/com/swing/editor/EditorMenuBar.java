package com.swing.editor;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxResources;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorMenuBar extends JMenuBar
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4060203894740766714L;

	public enum AnalyzeType { IS_CONNECTED, IS_SIMPLE, IS_CYCLIC_DIRECTED, IS_CYCLIC_UNDIRECTED, COMPLEMENTARY, REGULARITY, COMPONENTS, MAKE_CONNECTED, MAKE_SIMPLE, IS_TREE, ONE_SPANNING_TREE, IS_DIRECTED, GET_CUT_VERTEXES, GET_CUT_EDGES, GET_SOURCES, GET_SINKS, PLANARITY, IS_BICONNECTED, GET_BICONNECTED, SPANNING_TREE, FLOYD_ROY_WARSHALL }

	/**
	 * The graph creator factory
	 */
	//protected static GraphEditor graphEditor = new GraphEditor();

	@SuppressWarnings("serial")
	public EditorMenuBar(final BasicGraphEditor editor)
	{
		//final mxGraphComponent graphComponent = editor.getGraphComponent();
		//final mxGraph graph = graphComponent.getGraph();
		JMenu menu = null;
		JMenu submenu = null;

		// Creates the file menu
		menu = add(new JMenu(mxResources.get("file")));

		menu.add(editor.bind(mxResources.get("new"), new EditorActions.NewAction(),
				"/images/new.gif"));
		menu.add(editor.bind(mxResources.get("openFile"), new EditorActions.OpenAction(),
				"/images/open.gif"));
		menu.add(editor.bind(mxResources.get("importStencil"), new EditorActions.ImportElementsAction(),
				"/images/open.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("save"), new EditorActions.SaveAction(false),
				"/images/save.gif"));
		menu.add(editor.bind(mxResources.get("saveAs"), new EditorActions.SaveAction(true),
				"/images/saveas.gif"));

		menu.addSeparator();
//
        /*menu.add(editor.bind(mxResources.get("addMacros"),
				new PageSetupAction(),
				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/pagesetup.gif"));*/
//		menu.add(editor.bind(mxResources.get("pageSetup"),
//				new PageSetupAction(),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/pagesetup.gif"));
//		menu.add(editor.bind(mxResources.get("print"), new PrintAction(),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/print.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("exit"), new EditorActions.ExitAction()));

		// Creates the edit menu
		menu = add(new JMenu(mxResources.get("edit")));

		menu.add(editor.bind(mxResources.get("undo"), new EditorActions.HistoryAction(true),
				"/images/undo.gif"));
		menu.add(editor.bind(mxResources.get("redo"), new EditorActions.HistoryAction(false),
				"/images/redo.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("cut"), TransferHandler
				.getCutAction(), "/images/cut.gif"));
		menu.add(editor
				.bind(mxResources.get("copy"), TransferHandler.getCopyAction(),
						"/images/copy.gif"));
		menu.add(editor.bind(mxResources.get("paste"), TransferHandler
				.getPasteAction(),
				"/images/paste.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("delete"), mxGraphActions
				.getDeleteAction(),
				"/images/delete.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("selectAll"), mxGraphActions
				.getSelectAllAction()));
		//menu.add(editor.bind(mxResources.get("selectNone"), mxGraphActions
				//.getSelectNoneAction()));

		menu.addSeparator();

		//menu.add(editor.bind(mxResources.get("warning"), new WarningAction()));
		menu.add(editor.bind(mxResources.get("edit"), mxGraphActions
				.getEditAction()));

		// Creates the view menu
		//menu = add(new JMenu(mxResources.get("view")));

//		JMenuItem item = menu.add(new TogglePropertyItem(graphComponent,
//				mxResources.get("pageLayout"), "PageVisible", true,
//				new ActionListener()
//				{
//					/**
//					 * 
//					 */
//					public void actionPerformed(ActionEvent e)
//					{
//						if (graphComponent.isPageVisible()
//								&& graphComponent.isCenterPage())
//						{
//							graphComponent.zoomAndCenter();
//						}
//						else
//						{
//							graphComponent.getGraphControl()
//									.updatePreferredSize();
//						}
//					}
//				}));
//
//		item.addActionListener(new ActionListener()
//		{
//			/*
//			 * (non-Javadoc)
//			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//			 */
//			public void actionPerformed(ActionEvent e)
//			{
//				if (e.getSource() instanceof TogglePropertyItem)
//				{
//					final mxGraphComponent graphComponent = editor
//							.getGraphComponent();
//					TogglePropertyItem toggleItem = (TogglePropertyItem) e
//							.getSource();
//
//					if (toggleItem.isSelected())
//					{
//						// Scrolls the view to the center
//						SwingUtilities.invokeLater(new Runnable()
//						{
//							/*
//							 * (non-Javadoc)
//							 * @see java.lang.Runnable#run()
//							 */
//							public void run()
//							{
//								graphComponent.scrollToCenter(true);
//								graphComponent.scrollToCenter(false);
//							}
//						});
//					}
//					else
//					{
//						// Resets the translation of the view
//						mxPoint tr = graphComponent.getGraph().getView()
//								.getTranslate();
//
//						if (tr.getX() != 0 || tr.getY() != 0)
//						{
//							graphComponent.getGraph().getView().setTranslate(
//									new mxPoint());
//						}
//					}
//				}
//			}
//		});
//
//		menu.add(new TogglePropertyItem(graphComponent, mxResources
//				.get("antialias"), "AntiAlias", true));
//
//		menu.addSeparator();
//
//		menu.add(new ToggleGridItem(editor, mxResources.get("grid")));
//		menu.add(new ToggleRulersItem(editor, mxResources.get("rulers")));
//
//		menu.addSeparator();
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("zoom")));
//
//		submenu.add(editor.bind("400%", new ScaleAction(4)));
//		submenu.add(editor.bind("200%", new ScaleAction(2)));
//		submenu.add(editor.bind("150%", new ScaleAction(1.5)));
//		submenu.add(editor.bind("100%", new ScaleAction(1)));
//		submenu.add(editor.bind("75%", new ScaleAction(0.75)));
//		submenu.add(editor.bind("50%", new ScaleAction(0.5)));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("custom"), new ScaleAction(0)));
//
//		menu.addSeparator();
//
//		menu.add(editor.bind(mxResources.get("zoomIn"), mxGraphActions
//				.getZoomInAction()));
//		menu.add(editor.bind(mxResources.get("zoomOut"), mxGraphActions
//				.getZoomOutAction()));
//
//		menu.addSeparator();
//
//		menu.add(editor.bind(mxResources.get("page"), new ZoomPolicyAction(
//				mxGraphComponent.ZOOM_POLICY_PAGE)));
//		menu.add(editor.bind(mxResources.get("width"), new ZoomPolicyAction(
//				mxGraphComponent.ZOOM_POLICY_WIDTH)));
//
//		menu.addSeparator();
//
//		menu.add(editor.bind(mxResources.get("actualSize"), mxGraphActions
//				.getZoomActualAction()));
//
//		// Creates the format menu
//		menu = add(new JMenu(mxResources.get("format")));
//
//		populateFormatMenu(menu, editor);
//
//		// Creates the shape menu
//		menu = add(new JMenu(mxResources.get("shape")));
//
//		populateShapeMenu(menu, editor);
//
//		// Creates the diagram menu
//		menu = add(new JMenu(mxResources.get("diagram")));
//
//		menu.add(new ToggleOutlineItem(editor, mxResources.get("outline")));
//
//		menu.addSeparator();
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("background")));
//
//		submenu.add(editor.bind(mxResources.get("backgroundColor"),
//				new BackgroundAction()));
//		submenu.add(editor.bind(mxResources.get("backgroundImage"),
//				new BackgroundImageAction()));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("pageBackground"),
//				new PageBackgroundAction()));
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("grid")));
//
//		submenu.add(editor.bind(mxResources.get("gridSize"),
//				new PromptPropertyAction(graph, "Grid Size", "GridSize")));
//		submenu.add(editor.bind(mxResources.get("gridColor"),
//				new GridColorAction()));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("dashed"), new GridStyleAction(
//				mxGraphComponent.GRID_STYLE_DASHED)));
//		submenu.add(editor.bind(mxResources.get("dot"), new GridStyleAction(
//				mxGraphComponent.GRID_STYLE_DOT)));
//		submenu.add(editor.bind(mxResources.get("line"), new GridStyleAction(
//				mxGraphComponent.GRID_STYLE_LINE)));
//		submenu.add(editor.bind(mxResources.get("cross"), new GridStyleAction(
//				mxGraphComponent.GRID_STYLE_CROSS)));
//
//		menu.addSeparator();
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("layout")));
//
//		submenu.add(editor.graphLayout("verticalHierarchical", true));
//		submenu.add(editor.graphLayout("horizontalHierarchical", true));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.graphLayout("verticalPartition", false));
//		submenu.add(editor.graphLayout("horizontalPartition", false));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.graphLayout("verticalStack", false));
//		submenu.add(editor.graphLayout("horizontalStack", false));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.graphLayout("verticalTree", true));
//		submenu.add(editor.graphLayout("horizontalTree", true));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.graphLayout("placeEdgeLabels", false));
//		submenu.add(editor.graphLayout("parallelEdges", false));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.graphLayout("organicLayout", true));
//		submenu.add(editor.graphLayout("circleLayout", true));
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("selection")));
//
//		submenu.add(editor.bind(mxResources.get("selectPath"),
//				new SelectShortestPathAction(false)));
//		submenu.add(editor.bind(mxResources.get("selectDirectedPath"),
//				new SelectShortestPathAction(true)));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("selectTree"),
//				new SelectSpanningTreeAction(false)));
//		submenu.add(editor.bind(mxResources.get("selectDirectedTree"),
//				new SelectSpanningTreeAction(true)));
//
//		menu.addSeparator();
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("stylesheet")));
//
//		submenu
//				.add(editor
//						.bind(
//								mxResources.get("basicStyle"),
//								new StylesheetAction(
//										"/com/mxgraph/ru.isma.next.math.engine.examples/swing/resources/basic-style.xml")));
//		submenu
//				.add(editor
//						.bind(
//								mxResources.get("defaultStyle"),
//								new StylesheetAction(
//										"/com/mxgraph/ru.isma.next.math.engine.examples/swing/resources/default-style.xml")));
//
//		// Creates the options menu
//		menu = add(new JMenu(mxResources.get("options")));
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("display")));
//		submenu.add(new TogglePropertyItem(graphComponent, mxResources
//				.get("buffering"), "TripleBuffered", true));
//
//		submenu.add(new TogglePropertyItem(graphComponent, mxResources
//				.get("preferPageSize"), "PreferPageSize", true,
//				new ActionListener()
//				{
//					/**
//					 * 
//					 */
//					public void actionPerformed(ActionEvent e)
//					{
//						graphComponent.zoomAndCenter();
//					}
//				}));
//
//		// TODO: This feature is not yet implemented
//		//submenu.add(new TogglePropertyItem(graphComponent, mxResources
//		//		.get("pageBreaks"), "PageBreaksVisible", true));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("tolerance"),
//				new PromptPropertyAction(graphComponent, "Tolerance")));
//
//		submenu.add(editor.bind(mxResources.get("dirty"),
//				new ToggleDirtyAction()));
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("zoom")));
//
//		submenu.add(new TogglePropertyItem(graphComponent, mxResources
//				.get("centerZoom"), "CenterZoom", true));
//		submenu.add(new TogglePropertyItem(graphComponent, mxResources
//				.get("zoomToSelection"), "KeepSelectionVisibleOnZoom", true));
//
//		submenu.addSeparator();
//
//		submenu.add(new TogglePropertyItem(graphComponent, mxResources
//				.get("centerPage"), "CenterPage", true, new ActionListener()
//		{
//			/**
//			 * 
//			 */
//			public void actionPerformed(ActionEvent e)
//			{
//				if (graphComponent.isPageVisible()
//						&& graphComponent.isCenterPage())
//				{
//					graphComponent.zoomAndCenter();
//				}
//			}
//		}));
//
//		menu.addSeparator();
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("dragAndDrop")));
//
//		submenu.add(new TogglePropertyItem(graphComponent, mxResources
//				.get("dragEnabled"), "DragEnabled"));
//		submenu.add(new TogglePropertyItem(graph, mxResources
//				.get("dropEnabled"), "DropEnabled"));
//
//		submenu.addSeparator();
//
//		submenu.add(new TogglePropertyItem(graphComponent.getGraphHandler(),
//				mxResources.get("imagePreview"), "ImagePreview"));
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("labels")));
//
//		submenu.add(new TogglePropertyItem(graph,
//				mxResources.get("htmlLabels"), "HtmlLabels", true));
//		submenu.add(new TogglePropertyItem(graph,
//				mxResources.get("showLabels"), "LabelsVisible", true));
//
//		submenu.addSeparator();
//
//		submenu.add(new TogglePropertyItem(graph, mxResources
//				.get("moveEdgeLabels"), "EdgeLabelsMovable"));
//		submenu.add(new TogglePropertyItem(graph, mxResources
//				.get("moveVertexLabels"), "VertexLabelsMovable"));
//
//		submenu.addSeparator();
//
//		submenu.add(new TogglePropertyItem(graphComponent, mxResources
//				.get("handleReturn"), "EnterStopsCellEditing"));
//
//		menu.addSeparator();
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("connections")));
//
//		submenu.add(new TogglePropertyItem(graphComponent, mxResources
//				.get("connectable"), "Connectable"));
//		submenu.add(new TogglePropertyItem(graph, mxResources
//				.get("connectableEdges"), "ConnectableEdges"));
//
//		submenu.addSeparator();
//
//		submenu.add(new ToggleCreateTargetItem(editor, mxResources
//				.get("createTarget")));
//		submenu.add(new TogglePropertyItem(graph, mxResources
//				.get("disconnectOnMove"), "DisconnectOnMove"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("connectMode"),
//				new ToggleConnectModeAction()));
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("validation")));
//
//		submenu.add(new TogglePropertyItem(graph, mxResources
//				.get("allowDanglingEdges"), "AllowDanglingEdges"));
//		submenu.add(new TogglePropertyItem(graph, mxResources
//				.get("cloneInvalidEdges"), "CloneInvalidEdges"));
//
//		submenu.addSeparator();
//
//		submenu.add(new TogglePropertyItem(graph,
//				mxResources.get("allowLoops"), "AllowLoops"));
//		submenu.add(new TogglePropertyItem(graph,
//				mxResources.get("multigraph"), "Multigraph"));
//
//		// Creates the window menu
//		menu = add(new JMenu(mxResources.get("window")));
//
//		UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
//
//		for (int i = 0; i < lafs.length; i++)
//		{
//			final String clazz = lafs[i].getClassName();
//			menu.add(new AbstractAction(lafs[i].getName())
//			{
//				public void actionPerformed(ActionEvent e)
//				{
//					editor.setLookAndFeel(clazz);
//				}
//			});
//		}

		// Creates a developer menu
//		menu = add(new JMenu("Generate"));
//		menu.add(editor.bind("Null Graph", new InsertGraph(GraphType.NULL)));
//		menu.add(editor.bind("Complete Graph", new InsertGraph(GraphType.COMPLETE)));
//		menu.add(editor.bind("N-regular Graph", new InsertGraph(GraphType.NREGULAR)));
//		menu.add(editor.bind("Grid", new InsertGraph(GraphType.GRID)));
//		menu.add(editor.bind("Bipartite", new InsertGraph(GraphType.BIPARTITE)));
//		menu.add(editor.bind("Complete Bipartite", new InsertGraph(GraphType.COMPLETE_BIPARTITE)));
//		menu.add(editor.bind("Knight's Graph", new InsertGraph(GraphType.KNIGHT)));
//		menu.add(editor.bind("King's Graph", new InsertGraph(GraphType.KING)));
//		menu.add(editor.bind("Input from adj. matrix", new InsertGraph(GraphType.FROM_ADJ_MATRIX)));
//		menu.add(editor.bind("Petersen", new InsertGraph(GraphType.PETERSEN)));
//		menu.add(editor.bind("Path", new InsertGraph(GraphType.PATH)));
//		menu.add(editor.bind("Star", new InsertGraph(GraphType.STAR)));
//		menu.add(editor.bind("Wheel", new InsertGraph(GraphType.WHEEL)));
//		menu.add(editor.bind("Friendship Windmill", new InsertGraph(GraphType.FRIENDSHIP_WINDMILL)));
//		menu.add(editor.bind("Full Windmill", new InsertGraph(GraphType.FULL_WINDMILL)));
//		menu.addSeparator();
//		menu.add(editor.bind("Simple Random", new InsertGraph(GraphType.SIMPLE_RANDOM)));
//		menu.add(editor.bind("Simple Random Tree", new InsertGraph(GraphType.SIMPLE_RANDOM_TREE)));
//		menu.addSeparator();
//		menu.add(editor.bind("Reset Style", new InsertGraph(GraphType.RESET_STYLE)));

//		menu = add(new JMenu("Analyze"));
//		menu.add(editor.bind("Is Connected", new AnalyzeGraph(AnalyzeType.IS_CONNECTED)));
//		menu.add(editor.bind("Is Simple", new AnalyzeGraph(AnalyzeType.IS_SIMPLE)));
//		menu.add(editor.bind("Is Directed Cyclic", new AnalyzeGraph(AnalyzeType.IS_CYCLIC_DIRECTED)));
//		menu.add(editor.bind("Is Undirected Cyclic", new AnalyzeGraph(AnalyzeType.IS_CYCLIC_UNDIRECTED)));
//		menu.add(editor.bind("BFS (Breadth-First Seach)", new InsertGraph(GraphType.BFS)));
//		menu.add(editor.bind("DFS (Depth-First Seach)", new InsertGraph(GraphType.DFS)));
//		menu.add(editor.bind("Complementary", new AnalyzeGraph(AnalyzeType.COMPLEMENTARY)));
//		menu.add(editor.bind("Regularity", new AnalyzeGraph(AnalyzeType.REGULARITY)));
//		menu.add(editor.bind("Dijkstra", new InsertGraph(GraphType.DIJKSTRA)));
//		menu.add(editor.bind("Bellman-Ford", new InsertGraph(GraphType.BELLMAN_FORD)));
//		menu.add(editor.bind("Flory-Roy-Warshall", new AnalyzeGraph(AnalyzeType.FLOYD_ROY_WARSHALL)));
//		menu.add(editor.bind("Get Components", new AnalyzeGraph(AnalyzeType.COMPONENTS)));
//		menu.add(editor.bind("Make Connected", new AnalyzeGraph(AnalyzeType.MAKE_CONNECTED)));
//		menu.add(editor.bind("Make Simple", new AnalyzeGraph(AnalyzeType.MAKE_SIMPLE)));
//		menu.add(editor.bind("Is Tree", new AnalyzeGraph(AnalyzeType.IS_TREE)));
//		menu.add(editor.bind("One Spanning Tree", new AnalyzeGraph(AnalyzeType.ONE_SPANNING_TREE)));
//		menu.add(editor.bind("Make tree directed", new InsertGraph(GraphType.MAKE_TREE_DIRECTED)));
//		menu.add(editor.bind("Knight's Tour", new InsertGraph(GraphType.KNIGHT_TOUR)));
//		menu.add(editor.bind("Get adjacency matrix", new InsertGraph(GraphType.GET_ADJ_MATRIX)));
//		menu.add(editor.bind("Is directed", new AnalyzeGraph(AnalyzeType.IS_DIRECTED)));
//		menu.add(editor.bind("Indegree", new InsertGraph(GraphType.INDEGREE)));
//		menu.add(editor.bind("Outdegree", new InsertGraph(GraphType.OUTDEGREE)));
//		menu.add(editor.bind("Is cut vertex", new InsertGraph(GraphType.IS_CUT_VERTEX)));
//		menu.add(editor.bind("Get cut vertexes", new AnalyzeGraph(AnalyzeType.GET_CUT_VERTEXES)));
//		menu.add(editor.bind("Is cut edge", new InsertGraph(GraphType.IS_CUT_EDGE)));
//		menu.add(editor.bind("Get cut edges", new AnalyzeGraph(AnalyzeType.GET_CUT_EDGES)));
//		menu.add(editor.bind("Get sources", new AnalyzeGraph(AnalyzeType.GET_SOURCES)));
//		menu.add(editor.bind("Get sinks", new AnalyzeGraph(AnalyzeType.GET_SINKS)));
//		menu.add(editor.bind("Planarity", new AnalyzeGraph(AnalyzeType.PLANARITY)));
//		menu.add(editor.bind("Is biconnected", new AnalyzeGraph(AnalyzeType.IS_BICONNECTED)));
//		menu.add(editor.bind("Get biconnected components", new AnalyzeGraph(AnalyzeType.GET_BICONNECTED)));
//		menu.add(editor.bind("Get spanning tree", new AnalyzeGraph(AnalyzeType.SPANNING_TREE)));

		// Creates the help menu
		menu = add(new JMenu(mxResources.get("help")));

		JMenuItem item = menu.add(new JMenuItem(mxResources.get("aboutGraphEditor")));
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				editor.about();
			}
		});

        JMenuItem item2 = menu.add(new JMenuItem(mxResources.get("errorsTable")));
        item2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                editor.errors();
            }
        });
	}

/*	*//**
	 * Adds menu items to the given shape menu. This is factored out because
	 * the shape menu appears in the menubar and also in the popupmenu.
	 *//*
	public static void populateShapeMenu(JMenu menu, BasicGraphEditor editor)
	{
		menu.add(editor.bind(mxResources.get("home"), mxGraphActions
				.getHomeAction(),
                "/images/house.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("exitGroup"), mxGraphActions
				.getExitGroupAction(),
				"/images/up.gif"));
		menu.add(editor.bind(mxResources.get("enterGroup"), mxGraphActions
				.getEnterGroupAction(),
				"/images/down.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("group"), mxGraphActions
				.getGroupAction(),
				"/images/group.gif"));
		menu.add(editor.bind(mxResources.get("ungroup"), mxGraphActions
				.getUngroupAction(),
				"/images/ungroup.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("removeFromGroup"), mxGraphActions
				.getRemoveFromParentAction()));

		menu.add(editor.bind(mxResources.get("updateGroupBounds"),
				mxGraphActions.getUpdateGroupBoundsAction()));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("collapse"), mxGraphActions
				.getCollapseAction(),
                "/images/collapse.gif"));
		menu.add(editor.bind(mxResources.get("expand"), mxGraphActions
				.getExpandAction(),
                "/images/expand.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("toBack"), mxGraphActions
				.getToBackAction(),
				"/images/toback.gif"));
		menu.add(editor.bind(mxResources.get("toFront"), mxGraphActions
				.getToFrontAction(),
				"/images/tofront.gif"));

		menu.addSeparator();

		JMenu submenu = (JMenu) menu.add(new JMenu(mxResources.get("align")));

		submenu.add(editor.bind(mxResources.get("left"), new AlignCellsAction(
				mxConstants.ALIGN_LEFT),
                "/images/alignleft.gif"));
		submenu.add(editor.bind(mxResources.get("center"),
				new AlignCellsAction(mxConstants.ALIGN_CENTER),
                "/images/aligncenter.gif"));
		submenu.add(editor.bind(mxResources.get("right"), new AlignCellsAction(
				mxConstants.ALIGN_RIGHT),
                "/images/alignright.gif"));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("top"), new AlignCellsAction(
				mxConstants.ALIGN_TOP),
                "/images/aligntop.gif"));
		submenu.add(editor.bind(mxResources.get("middle"),
				new AlignCellsAction(mxConstants.ALIGN_MIDDLE),
                "/images/alignmiddle.gif"));
		submenu.add(editor.bind(mxResources.get("bottom"),
				new AlignCellsAction(mxConstants.ALIGN_BOTTOM),
                "/images/alignbottom.gif"));

		menu.addSeparator();

		menu
				.add(editor.bind(mxResources.get("autosize"),
						new AutosizeAction()));

	}*/

	/**
	 * Adds menu items to the given format menu. This is factored out because
	 * the format menu appears in the menubar and also in the popupmenu.
	 */
	public static void populateFormatMenu(JMenu menu, BasicGraphEditor editor)
	{
		// Флаг запрета на удаление, когда портов на шине меньше или равно двум.
		boolean deletable = true;
		
		// Получаем фигуру, для которой вызвали контекстное меню
		mxCell cell = (mxCell) editor.getGraphComponent().getGraph().getSelectionCell();
		if(cell != null)
			if(cell.getChildCount() <= 2)
				deletable = false;
		
//		JMenu submenu = (JMenu) menu.add(new JMenu(mxResources
//				.get("background")));

//		submenu.add(editor.bind(mxResources.get("fillcolor"), new ColorAction(
//				"Fillcolor", mxConstants.STYLE_FILLCOLOR),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/fillcolor.gif"));
//		submenu.add(editor.bind(mxResources.get("gradient"), new ColorAction(
//				"Gradient", mxConstants.STYLE_GRADIENTCOLOR)));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("image"),
//				new PromptValueAction(mxConstants.STYLE_IMAGE, "Image")));
//		submenu.add(editor.bind(mxResources.get("shadow"), new ToggleAction(
//				mxConstants.STYLE_SHADOW)));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("opacity"),
//				new PromptValueAction(mxConstants.STYLE_OPACITY,
//						"Opacity (0-100)")));
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("label")));
//
//		submenu.add(editor.bind(mxResources.get("fontcolor"), new ColorAction(
//				"Fontcolor", mxConstants.STYLE_FONTCOLOR),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/fontcolor.gif"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("labelFill"), new ColorAction(
//				"Label Fill", mxConstants.STYLE_LABEL_BACKGROUNDCOLOR)));
//		submenu.add(editor.bind(mxResources.get("labelBorder"),
//				new ColorAction("Label Border",
//						mxConstants.STYLE_LABEL_BORDERCOLOR)));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("rotateLabel"),
//				new ToggleAction(mxConstants.STYLE_HORIZONTAL, true)));
//
//		submenu.add(editor.bind(mxResources.get("textOpacity"),
//				new PromptValueAction(mxConstants.STYLE_TEXT_OPACITY,
//						"Opacity (0-100)")));
//
//		submenu.addSeparator();
//
//		JMenu subsubmenu = (JMenu) submenu.add(new JMenu(mxResources
//				.get("position")));
//
//		subsubmenu.add(editor.bind(mxResources.get("top"),
//				new SetLabelPositionAction(mxConstants.ALIGN_TOP,
//						mxConstants.ALIGN_BOTTOM)));
//		subsubmenu.add(editor.bind(mxResources.get("middle"),
//				new SetLabelPositionAction(mxConstants.ALIGN_MIDDLE,
//						mxConstants.ALIGN_MIDDLE)));
//		subsubmenu.add(editor.bind(mxResources.get("bottom"),
//				new SetLabelPositionAction(mxConstants.ALIGN_BOTTOM,
//						mxConstants.ALIGN_TOP)));
//
//		subsubmenu.addSeparator();
//
//		subsubmenu.add(editor.bind(mxResources.get("left"),
//				new SetLabelPositionAction(mxConstants.ALIGN_LEFT,
//						mxConstants.ALIGN_RIGHT)));
//		subsubmenu.add(editor.bind(mxResources.get("center"),
//				new SetLabelPositionAction(mxConstants.ALIGN_CENTER,
//						mxConstants.ALIGN_CENTER)));
//		subsubmenu.add(editor.bind(mxResources.get("right"),
//				new SetLabelPositionAction(mxConstants.ALIGN_RIGHT,
//						mxConstants.ALIGN_LEFT)));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("wordWrap"),
//				new KeyValueAction(mxConstants.STYLE_WHITE_SPACE, "wrap")));
//		submenu.add(editor.bind(mxResources.get("noWordWrap"),
//				new KeyValueAction(mxConstants.STYLE_WHITE_SPACE, null)));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("hide"), new ToggleAction(
//				mxConstants.STYLE_NOLABEL)));
//
//		menu.addSeparator();
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("line")));
//
//		submenu.add(editor.bind(mxResources.get("linecolor"), new ColorAction(
//				"Linecolor", mxConstants.STYLE_STROKECOLOR),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/linecolor.gif"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("orthogonal"), new ToggleAction(
//				mxConstants.STYLE_ORTHOGONAL)));
//		submenu.add(editor.bind(mxResources.get("dashed"), new ToggleAction(
//				mxConstants.STYLE_DASHED)));
//
//		submenu.addSeparator();
//		
//		submenu.add(editor.bind(mxResources.get("linewidth"),
//				new PromptValueAction(mxConstants.STYLE_STROKEWIDTH,
//						"Linewidth")));
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("connector")));
//
//		submenu.add(editor.bind(mxResources.get("straight"),
//				new SetStyleAction("straight"),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/straight.gif"));
//
//		submenu.add(editor.bind(mxResources.get("horizontal"),
//				new SetStyleAction(""),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/connect.gif"));
//		submenu.add(editor.bind(mxResources.get("vertical"),
//				new SetStyleAction("vertical"),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/vertical.gif"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("entityRelation"),
//				new SetStyleAction("edgeStyle=mxEdgeStyle.EntityRelation"),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/entity.gif"));
//		submenu.add(editor.bind(mxResources.get("arrow"), new SetStyleAction(
//				"arrow"), "/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/arrow.gif"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("plain"), new ToggleAction(
//				mxConstants.STYLE_NOEDGESTYLE)));
//
//		menu.addSeparator();
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("linestart")));
//
//		submenu.add(editor.bind(mxResources.get("open"), new KeyValueAction(
//				mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OPEN),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/open_start.gif"));
//		submenu.add(editor.bind(mxResources.get("classic"), new KeyValueAction(
//				mxConstants.STYLE_STARTARROW, mxConstants.ARROW_CLASSIC),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/classic_start.gif"));
//		submenu.add(editor.bind(mxResources.get("block"), new KeyValueAction(
//				mxConstants.STYLE_STARTARROW, mxConstants.ARROW_BLOCK),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/block_start.gif"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("diamond"), new KeyValueAction(
//				mxConstants.STYLE_STARTARROW, mxConstants.ARROW_DIAMOND),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/diamond_start.gif"));
//		submenu.add(editor.bind(mxResources.get("oval"), new KeyValueAction(
//				mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OVAL),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/oval_start.gif"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("none"), new KeyValueAction(
//				mxConstants.STYLE_STARTARROW, mxConstants.NONE)));
//		submenu.add(editor.bind(mxResources.get("size"), new PromptValueAction(
//				mxConstants.STYLE_STARTSIZE, "Linestart Size")));
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("lineend")));
//
//		submenu.add(editor.bind(mxResources.get("open"), new KeyValueAction(
//				mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OPEN),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/open_end.gif"));
//		submenu.add(editor.bind(mxResources.get("classic"), new KeyValueAction(
//				mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/classic_end.gif"));
//		submenu.add(editor.bind(mxResources.get("block"), new KeyValueAction(
//				mxConstants.STYLE_ENDARROW, mxConstants.ARROW_BLOCK),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/block_end.gif"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("diamond"), new KeyValueAction(
//				mxConstants.STYLE_ENDARROW, mxConstants.ARROW_DIAMOND),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/diamond_end.gif"));
//		submenu.add(editor.bind(mxResources.get("oval"), new KeyValueAction(
//				mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OVAL),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/oval_end.gif"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("none"), new KeyValueAction(
//				mxConstants.STYLE_ENDARROW, mxConstants.NONE)));
//		submenu.add(editor.bind(mxResources.get("size"), new PromptValueAction(
//				mxConstants.STYLE_ENDSIZE, "Lineend Size")));
//
//		menu.addSeparator();
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("alignment")));
//
//		submenu.add(editor.bind(mxResources.get("left"), new KeyValueAction(
//				mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/left.gif"));
//		submenu.add(editor.bind(mxResources.get("center"), new KeyValueAction(
//				mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/center.gif"));
//		submenu.add(editor.bind(mxResources.get("right"), new KeyValueAction(
//				mxConstants.STYLE_ALIGN, mxConstants.ALIGN_RIGHT),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/right.gif"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("top"), new KeyValueAction(
//				mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_TOP),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/top.gif"));
//		submenu.add(editor.bind(mxResources.get("middle"), new KeyValueAction(
//				mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/middle.gif"));
//		submenu.add(editor.bind(mxResources.get("bottom"), new KeyValueAction(
//				mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM),
//				"/com/mxgraph/ru.isma.next.math.engine.examples/swing/images/bottom.gif"));
//
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("spacing")));
//
//		submenu.add(editor.bind(mxResources.get("top"), new PromptValueAction(
//				mxConstants.STYLE_SPACING_TOP, "Top Spacing")));
//		submenu.add(editor.bind(mxResources.get("right"),
//				new PromptValueAction(mxConstants.STYLE_SPACING_RIGHT,
//						"Right Spacing")));
//		submenu.add(editor.bind(mxResources.get("bottom"),
//				new PromptValueAction(mxConstants.STYLE_SPACING_BOTTOM,
//						"Bottom Spacing")));
//		submenu.add(editor.bind(mxResources.get("left"), new PromptValueAction(
//				mxConstants.STYLE_SPACING_LEFT, "Left Spacing")));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("global"),
//				new PromptValueAction(mxConstants.STYLE_SPACING, "Spacing")));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("sourceSpacing"),
//				new PromptValueAction(
//						mxConstants.STYLE_SOURCE_PERIMETER_SPACING, mxResources
//								.get("sourceSpacing"))));
//		submenu.add(editor.bind(mxResources.get("targetSpacing"),
//				new PromptValueAction(
//						mxConstants.STYLE_TARGET_PERIMETER_SPACING, mxResources
//								.get("targetSpacing"))));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("perimeter"),
//				new PromptValueAction(mxConstants.STYLE_PERIMETER_SPACING,
//						"Perimeter Spacing")));

		JMenu submenu = (JMenu) menu.add(new JMenu(mxResources.get("direction")));

		submenu.add(editor.bind(mxResources.get("north"), new EditorActions.KeyValueAction(
				mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_NORTH)));
		submenu.add(editor.bind(mxResources.get("east"), new EditorActions.KeyValueAction(
				mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_EAST)));
		submenu.add(editor.bind(mxResources.get("south"), new EditorActions.KeyValueAction(
				mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_SOUTH)));
		submenu.add(editor.bind(mxResources.get("west"), new EditorActions.KeyValueAction(
				mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_WEST)));
		
		menu.add(editor.bind(mxResources.get("addport"), new EditorActions.EditPortAction(true)));
		menu.add(editor.bind(mxResources.get("removeport"), new EditorActions.EditPortAction(false))).setEnabled(deletable);

//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("rotation"),
//				new PromptValueAction(mxConstants.STYLE_ROTATION,
//						"Rotation (0-360)")));
//
//		menu.addSeparator();
//
//		menu.add(editor.bind(mxResources.get("rounded"), new ToggleAction(
//				mxConstants.STYLE_ROUNDED)));
//
//		menu.add(editor.bind(mxResources.get("style"), new StyleAction()));
	}
	
//	/**
//	 *
//	 */
//	@SuppressWarnings("serial")
//	public static class InsertGraph extends AbstractAction
//	{
//
//		/**
//		 * 
//		 */
//		protected GraphType graphType;
//
//		/**
//		 * 
//		 */
//		public InsertGraph(GraphType tree)
//		{
//			this.graphType = tree;
//		}
//
//		/**
//		 * 
//		 */
//		public void actionPerformed(ActionEvent e)
//		{
//			if (e.getSource() instanceof mxGraphComponent)
//			{
//				mxGraphComponent graphComponent = (mxGraphComponent) e
//						.getSource();
//				mxGraph graph = graphComponent.getGraph();
//				graphEditor.insertGraph(graph, graphType);
//			}
//		}
//	}
//
//	/**
//	 *
//	 */
//	@SuppressWarnings("serial")
//	public static class AnalyzeGraph extends AbstractAction
//	{
//
//		/**
//		 * 
//		 */
//		protected AnalyzeType analyzeType;
//
//		/**
//		 * 
//		 */
//		public AnalyzeGraph(AnalyzeType analyzeType)
//		{
//			this.analyzeType = analyzeType;
//		}
//		public void actionPerformed(ActionEvent e)
//		{
//			if (e.getSource() instanceof mxGraphComponent)
//			{
//				mxGraphComponent graphComponent = (mxGraphComponent) e
//						.getSource();
//				mxGraph graph = graphComponent.getGraph();
//				
//				if (analyzeType == AnalyzeType.IS_CONNECTED)
//				{
//					mxGraphStructure.isConnected(graph, graph.getDefaultParent(), mxAnalysisUtils.emptyProps);
//				}
//				else if (analyzeType == AnalyzeType.IS_SIMPLE)
//				{
//					mxGraphStructure.isSimple(graph, graph.getDefaultParent(), mxAnalysisUtils.emptyProps);
//				}
//				else if (analyzeType == AnalyzeType.IS_CYCLIC_DIRECTED)
//				{
//					//TODO implement
////					mxGraphStructure.isCyclicDirected(graph);
//				}
//				else if (analyzeType == AnalyzeType.IS_CYCLIC_UNDIRECTED)
//				{
//					mxGraphStructure.isCyclicUndirected(graph, graph.getDefaultParent(), mxAnalysisUtils.emptyProps);
//				}
//				else if (analyzeType == AnalyzeType.COMPLEMENTARY)
//				{
//					//TODO implement
////					mxGraphStructure.complementaryGraph(graph);
//				}
//				else if (analyzeType == AnalyzeType.REGULARITY)
//				{
//					//TODO implement
////					mxGraphStructure.regularity(graph);
//				}
//				else if (analyzeType == AnalyzeType.COMPONENTS)
//				{
//					//TODO implement
////					mxGraphStructure.getGraphComponents(graph);
//				}
//				else if (analyzeType == AnalyzeType.MAKE_CONNECTED)
//				{
//					//TODO implement
////					mxGraphStructure.makeConnected(graph, false, false, 0, 0);
//				}
//				else if (analyzeType == AnalyzeType.MAKE_SIMPLE)
//				{
//					//TODO implement
////					mxGraphStructure.makeSimple(graph);
//				}
//				else if (analyzeType == AnalyzeType.IS_TREE)
//				{
//					mxGraphStructure.isTree(graph, graph.getDefaultParent(), mxAnalysisUtils.emptyProps);
//				}
//				else if (analyzeType == AnalyzeType.ONE_SPANNING_TREE)
//				{
//					//TODO implement
////					mxGraphStructure.oneSpanningTree(graph,true,true, false, false, 0, 0);
//				}
//				else if (analyzeType == AnalyzeType.IS_DIRECTED)
//				{
//					//TODO implement
////					mxGraphStructure.isDirected(graph);
//				}
//				else if (analyzeType == AnalyzeType.GET_CUT_VERTEXES)
//				{
//					//TODO implement
////					mxGraphStructure.getCutVertexes(graph);
//				}
//				else if (analyzeType == AnalyzeType.GET_CUT_EDGES)
//				{
//					//TODO implement
////					mxGraphStructure.getCutEdges(graph);
//				}
//				else if (analyzeType == AnalyzeType.GET_SOURCES)
//				{
//					//TODO implement
////					mxGraphStructure.getSourceVertexes(graph);
//				}
//				else if (analyzeType == AnalyzeType.GET_SINKS)
//				{
//					//TODO implement
////					mxGraphStructure.getSinkVertexes(graph);
//				}
//				else if (analyzeType == AnalyzeType.PLANARITY)
//				{
//					//TODO implement
////					mxGraphStructure.isPlanar(graph);
//				}
//				else if (analyzeType == AnalyzeType.IS_BICONNECTED)
//				{
//					//TODO implement
////					mxGraphStructure.isBiconnected(graph);
//				}
//				else if (analyzeType == AnalyzeType.GET_BICONNECTED)
//				{
//					//TODO implement
////					mxGraphStructure.getBiconnectedComponents(graph);
//				}
//				else if (analyzeType == AnalyzeType.SPANNING_TREE)
//				{
//					//TODO implement
////					mxGraphStructure.getSpanningTree(graph);
//				}
//				else if (analyzeType == AnalyzeType.FLOYD_ROY_WARSHALL)
//				{
//					//TODO implement
////					mxGraphStructure.floydRoyWarshall(graph);
//				}
//			}
//		}
//	}

}
