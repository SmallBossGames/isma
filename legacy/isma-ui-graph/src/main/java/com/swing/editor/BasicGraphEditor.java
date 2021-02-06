package com.swing.editor;

import com.swing.GraphEditor;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.*;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicGraphEditor extends JPanel
{

    /**
     *
     */
    private static final long serialVersionUID = -6561623072112577140L;

    /**
     * Adds required resources for i18n
     */
    static
    {
        try
        {
            //TODO fix resource path?
            //mxResources.add("com/mxgraph/examples/swing/resources/editor");
        }
        catch (Exception e)
        {
            // ignore
        }
    }

    /**
     *
     */
    //public mxGraphComponent graphComponent;
    //protected List<mxGraphComponent> graphComponentList = new ArrayList<>();
    /**
     *
     */
    protected mxGraphOutline graphOutline;

    /**
     *
     */
    protected JTabbedPane libraryPane;

    public ClosableTabbedPane getGraphTabs() {
        return graphTabs;
    }

    /**
     *
     */
    private static ClosableTabbedPane graphTabs;

    /**
     *
     */
    //protected mxUndoManager undoManager;
    protected List<mxUndoManager> undoManagerList = new ArrayList<>();
    /**
     *
     */
    protected String appTitle;

    /**
     *
     */
    protected JLabel statusBar;

    /**
     *
     */
    protected File currentFile;

    /**
     * Flag indicating whether the current graph has been modified
     */
    protected boolean modified = false;

    /**
     *
     */
    protected mxRubberband rubberband;

    /**
     *
     */
    protected mxKeyboardHandler keyboardHandler;

    public static Map<mxGraphComponent, mxCell> getCellTabMap() {
        return cellTabMap;
    }

    //Here will be stored connections between macros mxCells and tabs representing inside of this cells
    private static Map<mxGraphComponent, mxCell> cellTabMap = new HashMap<>();

    /**
     *
     */
    protected mxIEventListener changeTracker = new mxIEventListener()
    {
        public void invoke(Object source, mxEventObject evt)
        {
            setModified(true);
        }
    };


    //When we need to add a tab for macros
    public mxGraphComponent addNewGraphComponent(String tabName, mxCell baseCell) {
        mxGraphComponent component = new GraphEditor.CustomGraphComponent(new GraphEditor.CustomGraph());

        component.setFoldingEnabled(false);
        final mxGraph graph = component.getGraph();

        EditorPalette palette = getPalette(0);
        palette.addListener(mxEvent.SELECT, new mxEventSource.mxIEventListener() {
            public void invoke(Object sender, mxEventObject evt) {
                Object tmp = evt.getProperty("transferable");

                if (tmp instanceof mxGraphTransferable) {
                    mxGraphTransferable t = (mxGraphTransferable) tmp;
                    Object cell = t.getCells()[0];

                    if (graph.getModel().isEdge(cell)) {
                        ((GraphEditor.CustomGraph) graph).setEdgeTemplate(cell);
                    }
                }
            }
        });

        //Настройки редактора
        //graph.setLabelsVisible(false);
        //graph.setLabelsClipped(true);
        graph.setDisconnectOnMove(false);
        graph.setAllowDanglingEdges(false);



        //Cancel macros cell deletion if macros is opened and user does not want to discard progress
        graph.addListener(mxEvent.CELLS_ADDED, new mxIEventListener() {
            @Override
            public void invoke(Object sender, mxEventObject evt) {
                Object[] newEntries = (Object[]) evt.getProperty("cells");
                if (newEntries == null) {
                    return;
                }
                for (Object cell : newEntries) {
                    if (((mxCell) cell).getStyle().equals("macros")) {
                        JOptionPane.showMessageDialog(BasicGraphEditor.this,
                                new Object[]{mxResources.get("addMacroToMacroError")});
                        ((mxGraph) sender).removeCells(newEntries);
                        break;
                    }
                }
            }
        });

        // Updates the modified flag if the graph model changes
        graph.getModel().addListener(mxEvent.CHANGE, changeTracker);

        final mxUndoManager undoManager = createUndoManager();

        mxIEventListener undoableUndoHandler = new mxIEventListener()
        {
            public void invoke(Object source, mxEventObject evt)
            {
                undoManager.undoableEditHappened((mxUndoableEdit) evt
                        .getProperty("edit"));
            }
        };

        // Do not change the scale and translation after files have been loaded
        graph.setResetViewOnRootChange(false);

        // Скрыть текст фигур и дуг
        //graph.setLabelsClipped(true);
        //graph.setLabelsVisible(false);

        // Запретить редактировать текст фигур и дуг
        graph.setCellsEditable(false);

        // Updates the modified flag if the graph model changes
        //graph.getModel().addListener(mxEvent.CHANGE, changeTracker);

        // Adds the command history to the model and view
        graph.getModel().addListener(mxEvent.UNDO, undoableUndoHandler);
        graph.getView().addListener(mxEvent.UNDO, undoableUndoHandler);

        // Keeps the selection in sync with the command history
        mxIEventListener undoHandler = new mxIEventListener()
        {
            public void invoke(Object source, mxEventObject evt)
            {
                List<mxUndoableChange> changes = ((mxUndoableEdit) evt
                        .getProperty("edit")).getChanges();
                graph.setSelectionCells(graph
                        .getSelectionCellsForChanges(changes));
            }
        };

        undoManager.addListener(mxEvent.UNDO, undoHandler);
        undoManager.addListener(mxEvent.REDO, undoHandler);

        // Creates the graph outline component
        //graphOutline = new mxGraphOutline(graphComponent);
        undoManagerList.add(undoManager);
        graphTabs.add(tabName, component);
        //Store connection between tab and mxCell in map.
        cellTabMap.put(component, baseCell);
        // Display some useful information about repaint events
        installRepaintListener(component);

        installHandlers(component);
        installListeners(component);

        component.getVerticalScrollBar().setUnitIncrement(16);
        updateTitle();
        ElementListener.addListener(component);

        return component;
    }


    /**
     *
     */
    public BasicGraphEditor(String appTitle, mxGraphComponent component)
    {
        // Stores and updates the frame title
        this.appTitle = appTitle;

        // Stores a reference to the graph and creates the command history
        //graphComponent = component;
        // disabled sign minus
        component.setFoldingEnabled(false);
        final mxGraph graph = component.getGraph();

        final mxUndoManager undoManager = createUndoManager();

        // Do not change the scale and translation after files have been loaded
        graph.setResetViewOnRootChange(false);

        // Скрыть текст фигур и дуг
        //graph.setLabelsClipped(true);
        graph.setLabelsVisible(true);
        // Запретить редактировать текст фигур и дуг
        graph.setCellsEditable(false);

        //Cancel macros cell deletion if macros is opened and user does not want to discard progress
        graph.addListener(mxEvent.REMOVE_CELLS, new mxIEventListener() {
            @Override
            public void invoke(Object sender, mxEventObject evt) {
                Object[] deletedEntries = (Object[]) evt.getProperty("cells");
                Map<mxGraphComponent, mxCell> cellGraphMap = BasicGraphEditor.getCellTabMap();
                if (deletedEntries == null) {
                    return;
                }

                for (Object cell : deletedEntries) {
                    if (cellGraphMap.containsValue(cell)) {
                        int reply = JOptionPane.showConfirmDialog(BasicGraphEditor.this,
                                new Object[]{mxResources.get("openMacrosDeleteWarning1"),
                                        mxResources.get("openMacrosDeleteWarning2")},
                                mxResources.get("titleWarning"), JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.NO_OPTION) {
                            ((mxGraph) sender).addCells(deletedEntries);
                        }
                        else {
                            for (Map.Entry entry : cellGraphMap.entrySet()) {
                                if (cell.equals(entry.getValue())) {
                                    graphTabs.remove((mxGraphComponent) entry.getKey());
                                }
                            }
                        }
                        break;
                    }
                }
            }
        });

        graph.addListener(mxEvent.CELLS_ADDED, new mxIEventListener() {
            @Override
            public void invoke(Object sender, mxEventObject evt) {
                Object[] newEntries = (Object[]) evt.getProperty("cells");
                Map<mxGraphComponent, mxCell> cellGraphMap = BasicGraphEditor.getCellTabMap();
                if (newEntries == null) {
                    return;
                }

                for (Object cell : newEntries) {
                    if (((mxCell) cell).getStyle() != null &&
                            (((mxCell) cell).getStyle().equals("macrosIn"))) {
                        JOptionPane.showMessageDialog(BasicGraphEditor.this,
                                new Object[]{mxResources.get("addMacroServiceElementsToMainGraphError")});
                        ((mxGraph) sender).removeCells(newEntries);
                    }
                }
            }});

        // Updates the modified flag if the graph model changes
        graph.getModel().addListener(mxEvent.CHANGE, changeTracker);

        mxIEventListener undoableUndoHandler = new mxIEventListener()
        {
            public void invoke(Object source, mxEventObject evt)
            {
                undoManager.undoableEditHappened((mxUndoableEdit) evt
                        .getProperty("edit"));
            }
        };

        // Adds the command history to the model and view
        graph.getModel().addListener(mxEvent.UNDO, undoableUndoHandler);
        graph.getView().addListener(mxEvent.UNDO, undoableUndoHandler);


        //TODO make macros placement undoable
        // Keeps the selection in sync with the command history
        mxIEventListener undoHandler = new mxIEventListener()
        {
            public void invoke(Object source, mxEventObject evt)
            {
                List<mxUndoableChange> changes = ((mxUndoableEdit) evt
                        .getProperty("edit")).getChanges();
                graph.setSelectionCells(graph
                        .getSelectionCellsForChanges(changes));
            }
        };

        undoManager.addListener(mxEvent.UNDO, undoHandler);
        undoManager.addListener(mxEvent.REDO, undoHandler);


        undoManagerList.add(undoManager);
        // Creates the graph outline component
        //graphOutline = new mxGraphOutline(graphComponent);

        // Creates the library pane that contains the tabs with the palettes
        libraryPane = new JTabbedPane();

        // Creates the inner split pane that contains the library with the
        // palettes and the graph outline on the left side of the window
        // Для того, чтобы отображалась схема в нижнем левом углу заменить null на graphOutline
        JSplitPane inner = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                libraryPane, null);
        inner.setDividerLocation(320);
        inner.setResizeWeight(1);
        inner.setDividerSize(20);
        inner.setBorder(null);

        graphTabs = new ClosableTabbedPane();

        graphTabs.add(component);

        // Creates the outer split pane that contains the inner split pane and
        // the graph component on the right side of the window
        JSplitPane outer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inner,
                graphTabs);
        outer.setEnabled(false);
        outer.setOneTouchExpandable(true);
        outer.setDividerLocation(200);
        outer.setDividerSize(6);
        outer.setBorder(null);

        component.getVerticalScrollBar().setUnitIncrement(16);

        // Creates the status bar
        statusBar = createStatusBar();

        // Display some useful information about repaint events
        installRepaintListener(component);

        // Puts everything together
        setLayout(new BorderLayout());
        add(outer, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        installToolBar();

        // Installs rubberband selection and handling for some special
        // keystrokes such as F2, Control-C, -V, X, A etc.
        installHandlers(component);
        installListeners(component);
        updateTitle();
        ElementListener.addListener(component);
    }

    /**
     *
     */
    protected mxUndoManager createUndoManager()
    {
        return new mxUndoManager();
    }

    /**
     *
     */
    protected void installHandlers(mxGraphComponent graphComponent)
    {
        rubberband = new mxRubberband(graphComponent);
        keyboardHandler = new EditorKeyboardHandler(graphComponent);
    }

    /**
     *
     */
    protected void installToolBar()
    {
        add(new EditorToolBar(this, JToolBar.HORIZONTAL), BorderLayout.NORTH);
    }

    /**
     *
     */
    protected JLabel createStatusBar()
    {
        JLabel statusBar = new JLabel(mxResources.get("ready"));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        return statusBar;
    }

    /**
     *
     */
    protected void installRepaintListener(final mxGraphComponent graphComponent)
    {
        graphComponent.getGraph().addListener(mxEvent.REPAINT,
                new mxIEventListener()
                {
                    public void invoke(Object source, mxEventObject evt)
                    {
                        String buffer = (graphComponent.getTripleBuffer() != null) ? ""
                                : " (unbuffered)";
                        mxRectangle dirty = (mxRectangle) evt
                                .getProperty("region");

                        if (dirty == null)
                        {
                            status("Repaint all" + buffer);
                        }
                        else
                        {
                            status("Repaint: x=" + (int) (dirty.getX()) + " y="
                                    + (int) (dirty.getY()) + " w="
                                    + (int) (dirty.getWidth()) + " h="
                                    + (int) (dirty.getHeight()) + buffer);
                        }
                    }
                });
    }

    public void removePalette() {
        libraryPane.removeTabAt(0);
    }

    public EditorPalette insertPalette(String title) {
        return insertPalette(title, 0);
    }

    /**
     *
     */
    public EditorPalette insertPalette(String title, int id)
    {
        final EditorPalette palette = new EditorPalette(this);
        final JScrollPane scrollPane = new JScrollPane(palette);

        scrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        libraryPane.insertTab(title, null, scrollPane, null, id);

        libraryPane.setSelectedIndex(0);

        // Updates the widths of the palettes if the container size changes
        libraryPane.addComponentListener(new ComponentAdapter()
        {
            /**
             *
             */
            public void componentResized(ComponentEvent e)
            {
                int w = scrollPane.getWidth()
                        + scrollPane.getVerticalScrollBar().getWidth();
                if (w == 0)
                    w = 150;
                //FIXME
                palette.setSize(w, (palette.getComponentCount() * 55) / (w / 150) + 20);
            }

        });
        scrollPane.getViewport().setExtentSize(new Dimension(300, 300));
        scrollPane.getViewport().invalidate();


        return palette;
    }

    public EditorPalette getPalette(int id) {
        JScrollPane pane = (JScrollPane) libraryPane.getComponentAt(id);
        JViewport viewport = pane.getViewport();
        EditorPalette palette = (EditorPalette) viewport.getView();
        return palette;
    }

    /**
     *
     */
    protected void mouseWheelMoved(MouseWheelEvent e)
    {
        mxGraphComponent graphComponent = (mxGraphComponent)graphTabs.getSelectedComponent();

        if (e.getWheelRotation() < 0)
        {
            graphComponent.zoomIn();
        }
        else
        {
            graphComponent.zoomOut();
        }

        status(mxResources.get("scale") + ": "
                + (int) (100 * graphComponent.getGraph().getView().getScale())
                + "%");
    }

    /**
     *
     */
	/*protected void showOutlinePopupMenu(MouseEvent e)
	{
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
				graphComponent);
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(
				mxResources.get("magnifyPage"));
		item.setSelected(graphOutline.isFitPage());

		item.addActionListener(new ActionListener()
		{
			*//**
 *
 *//*
			public void actionPerformed(ActionEvent e)
			{
				graphOutline.setFitPage(!graphOutline.isFitPage());
				graphOutline.repaint();
			}
		});

		JCheckBoxMenuItem item2 = new JCheckBoxMenuItem(
				mxResources.get("showLabels"));
		item2.setSelected(graphOutline.isDrawLabels());

		item2.addActionListener(new ActionListener()
		{
			*//**
 *
 *//*
			public void actionPerformed(ActionEvent e)
			{
				graphOutline.setDrawLabels(!graphOutline.isDrawLabels());
				graphOutline.repaint();
			}
		});

		JCheckBoxMenuItem item3 = new JCheckBoxMenuItem(
				mxResources.get("buffering"));
		item3.setSelected(graphOutline.isTripleBuffered());

		item3.addActionListener(new ActionListener()
		{
			*//**
 *
 *//*
			public void actionPerformed(ActionEvent e)
			{
				graphOutline.setTripleBuffered(!graphOutline.isTripleBuffered());
				graphOutline.repaint();
			}
		});

		JPopupMenu menu = new JPopupMenu();
		menu.add(item);
		menu.add(item2);
		menu.add(item3);
		menu.show(graphComponent, pt.x, pt.y);

		e.consume();
	}*/

    /**
     *
     */
    protected void showGraphPopupMenu(MouseEvent e)
    {

        mxGraphComponent graphComponent = (mxGraphComponent) graphTabs.getSelectedComponent();

        Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
                graphComponent);
        EditorPopupMenu menu = new EditorPopupMenu(BasicGraphEditor.this);
        menu.show(graphComponent, pt.x, pt.y);

        e.consume();
    }

    /**
     *
     */
    protected void mouseLocationChanged(MouseEvent e)
    {
        status(e.getX() + ", " + e.getY());
    }

    /**
     *
     */
    protected void installListeners(mxGraphComponent graphComponent)
    {
        // Installs mouse wheel listener for zooming
        MouseWheelListener wheelTracker = new MouseWheelListener()
        {
            /**
             *
             */
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                if (e.getSource() instanceof mxGraphOutline
                        || e.isControlDown())
                {
                    BasicGraphEditor.this.mouseWheelMoved(e);
                }
            }

        };

        // Handles mouse wheel events in the outline and graph component
//		graphOutline.addMouseWheelListener(wheelTracker);
        graphComponent.addMouseWheelListener(wheelTracker);

		/*// Installs the popup menu in the outline
		graphOutline.addMouseListener(new MouseAdapter()
		{

			*//**
     *
     *//*
			public void mousePressed(MouseEvent e)
			{
				// Handles context menu on the Mac where the trigger is on mousepressed
				mouseReleased(e);
			}

			*//**
     *
     *//*
			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showOutlinePopupMenu(e);
				}
			}

		});*/

        // Installs the popup menu in the graph component
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter()
        {

            /**
             *
             */
            public void mousePressed(MouseEvent e)
            {
                // Handles context menu on the Mac where the trigger is on mousepressed
                mouseReleased(e);
            }

            /**
             *
             */
            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    showGraphPopupMenu(e);
                }
            }

        });

        // Installs a mouse motion listener to display the mouse location
        graphComponent.getGraphControl().addMouseMotionListener(
                new MouseMotionListener()
                {

                    /*
                     * (non-Javadoc)
                     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
                     */
                    public void mouseDragged(MouseEvent e)
                    {
                        mouseLocationChanged(e);
                    }

                    /*
                     * (non-Javadoc)
                     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
                     */
                    public void mouseMoved(MouseEvent e)
                    {
                        mouseDragged(e);
                    }

                });
    }

    /**
     *
     */
    public void setCurrentFile(File file)
    {
        File oldValue = currentFile;
        currentFile = file;

        firePropertyChange("currentFile", oldValue, file);

        if (oldValue != file)
        {
            updateTitle();
        }
    }

    /**
     *
     */
    public File getCurrentFile()
    {
        return currentFile;
    }

    /**
     *
     * @param modified
     */
    public void setModified(boolean modified)
    {
        boolean oldValue = this.modified;
        this.modified = modified;

        firePropertyChange("modified", oldValue, modified);

        if (oldValue != modified)
        {
            updateTitle();
        }
    }

    /**
     *
     * @return whether or not the current graph has been modified
     */
    public boolean isModified()
    {
        return modified;
    }

    /**
     *
     */
    public mxGraphComponent getGraphComponent()
    {
        return (mxGraphComponent) graphTabs.getSelectedComponent();
    }

    public mxGraphComponent getGraphComponent(int id)
    {
        return (mxGraphComponent) graphTabs.getComponentAt(id);
    }

    public mxGraphComponent getGraphComponent(mxCell cell)
    {
        for (int i = 0; i < graphTabs.getTabCount(); i++) {
            mxGraphComponent curComponent = ((mxGraphComponent) graphTabs.getComponentAt(i));
            if (curComponent.getGraph().getModel().contains(cell)){
                return curComponent;
            }
        }
        return null;
    }

    public String getTabTitle() {
        return getTabTitle(graphTabs.getSelectedIndex());
    }

    public String getTabTitle(int id) {
        return graphTabs.getTitleAt(id);
    }

    public void setTabTitle(String newTitle) {
        graphTabs.setTitleAt(graphTabs.getSelectedIndex(), newTitle);
    }

    public boolean isMacrosGraph() {
        return graphTabs.getSelectedIndex() != 0;
    }


	/*Select tab with provided name. Returns true if tab was selected succesfully. False if no tab with
	 such title exists.
	 */
	/*public boolean selectTab(String name) {
		int tabIndex = graphTabs.indexOfTab(name);
		if (tabIndex != -1) {
			graphTabs.setSelectedIndex(tabIndex);
			return true;
		}
		return false;
	}*/

    /*public boolean selectTab(mxGraphComponent component) {
        if (graphTabs.indexOfTabComponent(component) == -1) {
            return false;
        }
        graphTabs.setSelectedComponent(component);
        return true;
    }*/

    public boolean selectTab(mxCell cell) {
        for (Map.Entry<mxGraphComponent, mxCell> entry : getCellTabMap().entrySet()) {
            if (cell.equals(entry.getValue())) {
                graphTabs.setSelectedComponent(entry.getKey());
                return true;
            }
        }

        return false;
    }


    /**
     *
     */
    public mxGraphOutline getGraphOutline()
    {
        return graphOutline;
    }

    /*
     *
     */
    public JTabbedPane getLibraryPane()
    {
        return libraryPane;
    }

    /**
     *
     */
    public mxUndoManager getUndoManager()
    {
        return undoManagerList.get(graphTabs.getSelectedIndex());
    }

    /**
     *
     * @param name
     * @param action
     * @return a new Action bound to the specified string name
     */
    public Action bind(String name, final Action action)
    {
        return bind(name, action, null);
    }

    /**
     *
     * @param name
     * @param action
     * @return a new Action bound to the specified string name and icon
     */
    @SuppressWarnings("serial")
    public Action bind(String name, final Action action, String iconUrl)
    {
        AbstractAction newAction = new AbstractAction(name, (iconUrl != null) ? new ImageIcon(
                BasicGraphEditor.class.getResource(iconUrl)) : null)
        {
            public void actionPerformed(ActionEvent e)
            {
                action.actionPerformed(new ActionEvent(getGraphComponent(), e
                        .getID(), e.getActionCommand()));
            }
        };

        newAction.putValue(Action.SHORT_DESCRIPTION, action.getValue(Action.SHORT_DESCRIPTION));

        return newAction;
    }

    /**
     *
     * @param msg
     */
    public void status(String msg)
    {
        statusBar.setText(msg);
    }

    /**
     *
     */
    public void updateTitle()
    {
        javax.swing.JFrame frame = (javax.swing.JFrame) SwingUtilities.windowForComponent(this);

        if (frame != null && graphTabs.getSelectedIndex() == 0)
        {
            String title = (currentFile != null) ? currentFile
                    .getName() : mxResources.get("newDiagram");

            frame.setTitle(appTitle);

            title = FilenameUtils.removeExtension(title);

            if (modified)
            {
                title += "*";
            }

            graphTabs.setTitleAt(graphTabs.getSelectedIndex(), title);
        }
        else {
            graphTabs.setTitleAt(graphTabs.getSelectedIndex(), getTabTitle()+"*");
        }
    }

    /**
     *
     */
    public void about()
    {
        javax.swing.JFrame frame = (javax.swing.JFrame) SwingUtilities.windowForComponent(this);

        if (frame != null)
        {
            EditorAboutFrame about = new EditorAboutFrame(frame);
            about.setModal(true);

            // Centers inside the application frame
            int x = frame.getX() + (frame.getWidth() - about.getWidth()) / 2;
            int y = frame.getY() + (frame.getHeight() - about.getHeight()) / 2;
            about.setLocation(x, y);

            // Shows the modal dialog and waits
            about.setVisible(true);
        }
    }


    /**
     *
     */
    public void errors()
    {
        javax.swing.JFrame frame = (javax.swing.JFrame) SwingUtilities.windowForComponent(this);

        if (frame != null)
        {
            ErrorsDialog errors = new ErrorsDialog();
            errors.setTitle(mxResources.get("errorsTable"));
            errors.setModal(true);
            errors.setResizable(false);
            errors.pack();

            // Centers inside the application frame
            int x = frame.getX() + (frame.getWidth() - errors.getWidth()) / 2;
            int y = frame.getY() + (frame.getHeight() - errors.getHeight()) / 2;
            errors.setLocation(x, y);

            // Shows the modal dialog and waits
            errors.setVisible(true);
        }
    }

    /**
     *
     */
    public void exit()
    {
        javax.swing.JFrame frame = (javax.swing.JFrame) SwingUtilities.windowForComponent(this);

        if (frame != null)
        {
            frame.dispose();
        }
    }

    /**
     *
     */
	/*public void setLookAndFeel(String clazz)
	{
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null)
		{
			try
			{
				UIManager.setLookAndFeel(clazz);
				SwingUtilities.updateComponentTreeUI(frame);

				// Needs to assign the key bindings again
				keyboardHandler = new EditorKeyboardHandler(graphComponent);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}*/

    /**
     *
     */
    public JFrame createFrame(JMenuBar menuBar)
    {
        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(870, 640);

        // Updates the frame title
        updateTitle();

        return frame;
    }

    /**
     * Creates an action that executes the specified layout.
     *
     * @param key Key to be used for getting the label from mxResources and also
     * to create the layout instance for the commercial graph editor example.
     * @return an action that executes the specified layout
     *//*
	@SuppressWarnings("serial")
	public Action graphLayout(final String key, boolean animate)
	{
		final mxIGraphLayout layout = createLayout(key, animate);

		if (layout != null)
		{
			return new AbstractAction(mxResources.get(key))
			{
				public void actionPerformed(ActionEvent e)
				{
					final mxGraph graph = graphComponent.getGraph();
					Object cell = graph.getSelectionCell();

					if (cell == null
							|| graph.getModel().getChildCount(cell) == 0)
					{
						cell = graph.getDefaultParent();
					}

					graph.getModel().beginUpdate();
					try
					{
						long t0 = System.currentTimeMillis();
						layout.execute(cell);
						status("Layout: " + (System.currentTimeMillis() - t0)
								+ " ms");
					}
					finally
					{
						mxMorphing morph = new mxMorphing(graphComponent, 20,
								1.2, 20);

						morph.addListener(mxEvent.DONE, new mxIEventListener()
						{

							public void invoke(Object sender, mxEventObject evt)
							{
								graph.getModel().endUpdate();
							}

						});

						morph.startAnimation();
					}

				}

			};
		}
		else
		{
			return new AbstractAction(mxResources.get(key))
			{

				public void actionPerformed(ActionEvent e)
				{
					JOptionPane.showMessageDialog(graphComponent,
							mxResources.get("noLayout"));
				}

			};
		}
	}*/

/*	*//**
 * Creates a layout instance for the given identifier.
 *//*
	protected mxIGraphLayout createLayout(String ident, boolean animate)
	{
		mxIGraphLayout layout = null;

		if (ident != null)
		{
			mxGraph graph = graphComponent.getGraph();

			if (ident.equals("verticalHierarchical"))
			{
				layout = new mxHierarchicalLayout(graph);
			}
			else if (ident.equals("horizontalHierarchical"))
			{
				layout = new mxHierarchicalLayout(graph, JLabel.WEST);
			}
			else if (ident.equals("verticalTree"))
			{
				layout = new mxCompactTreeLayout(graph, false);
			}
			else if (ident.equals("horizontalTree"))
			{
				layout = new mxCompactTreeLayout(graph, true);
			}
			else if (ident.equals("parallelEdges"))
			{
				layout = new mxParallelEdgeLayout(graph);
			}
			else if (ident.equals("placeEdgeLabels"))
			{
				layout = new mxEdgeLabelLayout(graph);
			}
			else if (ident.equals("organicLayout"))
			{
				layout = new mxOrganicLayout(graph);
			}
			if (ident.equals("verticalPartition"))
			{
				layout = new mxPartitionLayout(graph, false)
				{
					*//**
 * Overrides the empty implementation to return the size of the
 * graph control.
 *//*
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("horizontalPartition"))
			{
				layout = new mxPartitionLayout(graph, true)
				{
					*//**
 * Overrides the empty implementation to return the size of the
 * graph control.
 *//*
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("verticalStack"))
			{
				layout = new mxStackLayout(graph, false)
				{
					*//**
 * Overrides the empty implementation to return the size of the
 * graph control.
 *//*
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("horizontalStack"))
			{
				layout = new mxStackLayout(graph, true)
				{
					*//**
 * Overrides the empty implementation to return the size of the
 * graph control.
 *//*
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("circleLayout"))
			{
				layout = new mxCircleLayout(graph);
			}
		}

		return layout;
	}*/

}
