/**
 * $Id: EditorPalette.java,v 1.9 2012-01-13 12:52:28 david Exp $
 * Copyright (c) 2007-2012, JGraph Ltd
 */
package com.swing.editor;

import com.editor.utils.values.MacroMxCellValue;
import com.editor.utils.PaletteElementsSchemeData;
import com.editor.utils.Scheme;
import com.editor.utils.parser.ParseToString;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.*;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

public class EditorPalette extends JPanel
{

    /**
     *
     */
    private static final long serialVersionUID = 7771113885935187066L;

    /**
     *
     */
    protected JLabel selectedEntry = null;

    /**
     *
     */
    protected mxEventSource eventSource = new mxEventSource(this);

    /**
     *
     */
    //protected Color gradientColor = new Color(117, 195, 173);
    protected Color gradientColor = Color.white;

    protected Map<String, mxCell> paletteCellMap = new HashMap<>();

    private BasicGraphEditor editor;

    /**
     *
     */
    @SuppressWarnings("serial")
    public EditorPalette(BasicGraphEditor editor)
    {
        this.editor = editor;
        //setBackground(new Color(149, 230, 190));

        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

        // Clears the current selection when the background is clicked
        addMouseListener(new MouseListener()
        {

            /*
             * (non-Javadoc)
             * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
             */
            public void mousePressed(MouseEvent e)
            {
                clearSelection();
            }

            /*
             * (non-Javadoc)
             * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
             */
            public void mouseClicked(MouseEvent e)
            {
            }

            /*
             * (non-Javadoc)
             * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
             */
            public void mouseEntered(MouseEvent e)
            {
            }

            /*
             * (non-Javadoc)
             * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
             */
            public void mouseExited(MouseEvent e)
            {
            }

            /*
             * (non-Javadoc)
             * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
             */
            public void mouseReleased(MouseEvent e)
            {
            }

        });

        // Shows a nice icon for drag and drop but doesn't import anything
        setTransferHandler(new TransferHandler()
        {
            public boolean canImport(JComponent comp, DataFlavor[] flavors)
            {
                return true;
            }
        });
    }




    /**
     *
     */
    public void setGradientColor(Color c)
    {
        gradientColor = c;
    }

    /**
     *
     */
    public Color getGradientColor()
    {
        return gradientColor;
    }

    /**
     *
     */
    public void paintComponent(Graphics g)
    {
        if (gradientColor == null)
        {
            super.paintComponent(g);
        }
        else
        {
            Rectangle rect = getVisibleRect();

            if (g.getClipBounds() != null)
            {
                rect = rect.intersection(g.getClipBounds());
            }

            Graphics2D g2 = (Graphics2D) g;

            g2.setPaint(new GradientPaint(50, 500, getBackground(), getWidth(), 0,
                    gradientColor));
            g2.fill(rect);
        }
    }

    /**
     *
     */
    public void clearSelection()
    {
        setSelectionEntry(null, null);
    }

    /**
     *
     */
    public void setSelectionEntry(JLabel entry, mxGraphTransferable t)
    {
        JLabel previous = selectedEntry;
        selectedEntry = entry;

        if (previous != null)
        {
            previous.setBorder(null);
            previous.setOpaque(false);
        }

        if (selectedEntry != null)
        {
            selectedEntry.setBorder(ShadowBorder.getSharedInstance());
            selectedEntry.setOpaque(true);
        }

        eventSource.fireEvent(new mxEventObject(mxEvent.SELECT, "entry",
                selectedEntry, "transferable", t, "previous", previous));
    }


    @Override
    public Dimension getPreferredSize() {
        Component[] children = getComponents();
        int fullHeight = 0;
        int maxLineWidth = 20;
        for (Component child : children) {
            fullHeight += child.getPreferredSize().height;
            if (child instanceof JLabel) {
                if (((JLabel) child).getText().replaceAll("\\<[^>]*>","").length() > maxLineWidth) {
                    fullHeight += 25;
                }
            }
        }
        return new Dimension(getSize().width, fullHeight + 20);
    }


    /**
     *
     * @param name name of element
     * @return true if element with this name already exists and false otherwise
     */
    public boolean exists(String name) {
        Component[] components = getComponents();

        for (Component component : components) {
            if (component instanceof JLabel &&
                    ((JLabel) component).getText().replaceAll("\\<[^>]*>", "")
                            .equalsIgnoreCase(name.replaceAll("\\<[^>]*>",""))) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param name
     * @param icon
     * @param itemName
     * @param width
     * @param height
     */
    public void addEdgeTemplate(final String name, ImageIcon icon,
                                String itemName, List<Scheme> schemeList, int width, int height, mxGraph graph)
    {
        if (schemeList != null)
            PaletteElementsSchemeData.addNewPaletteElementData(itemName, schemeList);
        //Cчитываем данные из дефолт таблицы и записываем их в элемент.
        PaletteElementsSchemeData.current = "Схема 1";
        JTable paramTable = PaletteElementsSchemeData.initTParams(itemName);
        JTable condTable = PaletteElementsSchemeData.initIConditions(itemName);

        Object value = "Схема 1" + " " + ParseToString.parsParamsToString(paramTable) +
                ParseToString.parsConditionsToString(condTable);


        mxGeometry geometry = new mxGeometry(0, 0, width, height);
        geometry.setTerminalPoint(new mxPoint(0, height), true);
        geometry.setTerminalPoint(new mxPoint(width, 0), false);
        geometry.setRelative(true);

        mxCell cell = new mxCell(value, geometry, itemName);
        cell.setEdge(true);

        addTemplate(name, icon, cell, graph, false);
    }

    public void addMacros(final mxCell cell, ImageIcon icon, List<mxCell> ports, mxGraph graph) {

		/*mxCell cell = new mxCell(value, new mxGeometry(0, 0, width, height),
				itemName);*/
        //cell.setAttribute("image", "/images/macros.png");

        cell.setVertex(true);
        if (ports != null) {
            for (mxCell port : ports) {
                cell.insert(port);
            }
        }
		/*if (appearance != null) {
			while (appearance.getChildCount() != 0) {
				cell.insert(appearance.getChildAt(0));
			}
		}*/
        String name = ((MacroMxCellValue) cell.getValue()).getMacroDisplayName().split(": ")[1];
        addTemplate(name, icon, cell, graph, true);
    }

    public void addMacros(final String name, ImageIcon icon, String itemName,
                          MacroMxCellValue value, List<mxCell> ports, int width, int height, mxGraph graph) {

        mxCell cell = new mxCell(value, new mxGeometry(0, 0, width, height),
                itemName);

        cell.setVertex(true);
        if (ports != null) {
            for (mxCell port : ports) {
                cell.insert(port);
            }
        }
		/*if (appearance != null) {
			while (appearance.getChildCount() != 0) {
				cell.insert(appearance.getChildAt(0));
			}
		}*/

        addTemplate(name, icon, cell, graph, true);
    }

    /**
     *
     * @param name
     * @param icon
     * @param itemName
     * @param width
     * @param height
     */
    public void addTemplate(final String name, ImageIcon icon, String itemName,
                            List<Scheme> schemeList, int width, int height, mxGraph graph)
    {
        if (schemeList != null) {
            PaletteElementsSchemeData.addNewPaletteElementData(itemName, schemeList);
        }

        //Cчитываем данные из дефолт таблицы и записывает их в элемент.
        PaletteElementsSchemeData.current = "Схема 1";

        JTable table = PaletteElementsSchemeData.initTParams(itemName);
        JTable table2 = PaletteElementsSchemeData.initIConditions(itemName);
        Object value;
        if (schemeList != null) {
            value = "Схема 1" + " " + ParseToString.parsParamsToString(table) +
                    ParseToString.parsConditionsToString(table2);
        }
        else {
            value = "";
        }
        mxCell cell = new mxCell(value, new mxGeometry(0, 0, width, height),
                itemName);

        cell.setVertex(true);

        addTemplate(name, icon, cell, graph, false);
    }


    /**
     *
     * @param name
     * @param icon
     * @param cell
     */
    public void addTemplate(final String name, ImageIcon icon, mxCell cell, mxGraph graph, boolean isMacros)
    {
        mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
        final mxGraphTransferable t = new mxGraphTransferable(
                new Object[] { cell }, bounds);

        paletteCellMap.put(cell.getStyle(), cell);

        // Scales the image if it's too large for the library
        if (icon != null)
        {
            if (icon.getIconWidth() > 32 || icon.getIconHeight() > 32)
            {
                icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32,
                        0));
            }
        }

        final JLabel entry = new JLabel(icon);
        entry.setBackground(EditorPalette.this.getBackground().brighter());
        entry.setFont(new Font(entry.getFont().getFamily(), 0, 14));

        entry.setVerticalTextPosition(JLabel.BOTTOM);

        entry.setIconTextGap(0);

        final String HTML_1 = "<html><body style='text-align:center'>";
        final String HTML_3 = "</html>";
        final int lineMaxWidth = 20;

        entry.setPreferredSize(new Dimension(170, 55 + 25 * (name.length() / lineMaxWidth)));
        entry.setText(HTML_1 + name + HTML_3);
        entry.setHorizontalTextPosition(JLabel.CENTER);
        entry.addMouseListener(new MouseListener()
        {

            /*
             * (non-Javadoc)
             * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
             */
            public void mousePressed(MouseEvent e)
            {
                setSelectionEntry(entry, t);
            }

            /*
             * (non-Javadoc)
             * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
             */
            public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isRightMouseButton(e) && isMacros) {
                    MacrosPalettePopupMenu menu = new MacrosPalettePopupMenu(e);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            class MacrosPalettePopupMenu extends JPopupMenu {

                public MacrosPalettePopupMenu(MouseEvent outerE) {
                    JMenuItem editItem = new JMenuItem(mxResources.get("edit"));
                    editItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String style = cell.getStyle();
                            //String[] styleParts = style.split(":");

                            if (style.equals("macros")) {
                                if (BasicGraphEditor.getCellTabMap().containsValue(cell)) {
                                    editor.selectTab(cell);
                                }
                                else {
                                    mxGraphComponent macrosComponent = editor.addNewGraphComponent((
                                            (MacroMxCellValue) cell.getValue()).getMacroDisplayName().split(": ")[1]
                                            + "(Шаблон)", cell);
                                    //Monitor user trying to add macros in itself
                                    macrosComponent.getGraph().addListener(mxEvent.CELLS_ADDED, new mxIEventListener() {
                                        @Override
                                        public void invoke(Object sender, mxEventObject evt) {
                                            Object[] addedCells = (Object[]) evt.getProperty("cells");
                                            if (addedCells == null) {
                                                return;
                                            }

                                            for (Object aCell : addedCells) {
                                                mxCell mCell = (mxCell) aCell;
                                                if (mCell.getStyle().equals(cell.getStyle())) {
                                                    JOptionPane.showMessageDialog(
                                                            null,
                                                            mxResources.get("addMacrosToItselfError"),
                                                            mxResources.get("error"),
                                                            JOptionPane.ERROR_MESSAGE);
                                                    mCell.removeFromParent();
                                                }
                                            }
                                        }
                                    });
                                    macrosComponent.getGraph().setModel(((MacroMxCellValue) cell.getValue()).getModel());
                                    editor.selectTab(cell);
                                }
                            }
                        }
                    });
                    add(editItem);
                    JMenuItem removeItem = new JMenuItem(mxResources.get("delete"));
                    removeItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JLabel element = (JLabel) outerE.getSource();
                            EditorPalette palette = (EditorPalette) element.getParent();
                            palette.remove(element);
                            deleteMacrosInXML(element.getText().replaceAll("\\<[^>]*>",""));
                            for (Map.Entry entry : editor.getCellTabMap().entrySet()) {
                                if (cell.equals(entry.getValue())) {
                                    editor.getGraphTabs().remove((mxGraphComponent) entry.getKey());
                                }
                            }
                            palette.repaint();
                        }
                    });
                    add(removeItem);
                }

                public void deleteMacrosInXML(String macrosName) {
                    File macrosFile = new File(EditorActions.class.getProtectionDomain()
                            .getCodeSource().getLocation().getPath() + "/elem/macros.mcr");
                    System.out.println(macrosFile.getAbsolutePath());

                    org.jdom2.Document document = null;
                    if (macrosFile.exists()) {
                        try {
                            document = new SAXBuilder().build(macrosFile);
                        } catch (JDOMException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        return;
                    }

                    Element root = document.getRootElement();
                    List children = root.getChildren("macros");
                    Iterator itr = children.iterator();
                    while (itr.hasNext()) {
                        Element child = (Element) itr.next();
                        String att = child.getChildText("name");
                        if(macrosName.equals(att)){
                            itr.remove();
                        }
                    }


                    try(OutputStream out = new FileOutputStream(macrosFile)) {
                        XMLOutputter xmlOutput = new XMLOutputter();

                        // display nice nice
                        xmlOutput.setFormat(Format.getPrettyFormat());
                        xmlOutput.output(document, out);

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            /*
             * (non-Javadoc)
             * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
             */
            public void mouseEntered(MouseEvent e)
            {
            }

            /*
             * (non-Javadoc)
             * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
             */
            public void mouseExited(MouseEvent e)
            {
            }

            /*
             * (non-Javadoc)
             * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
             */
            public void mouseReleased(MouseEvent e)
            {
            }

        });

        // Install the handler for dragging nodes into a graph
        DragGestureListener dragGestureListener = new DragGestureListener()
        {

            public void dragGestureRecognized(DragGestureEvent e)
            {
                e.startDrag(null, mxSwingConstants.EMPTY_IMAGE, new Point(),
                        t, null);
            }

        };

        DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(entry,
                DnDConstants.ACTION_COPY, dragGestureListener);

        // добавление портов к фигурам
        ElementModifier.addPorts(name, cell, graph);

        add(entry);
    }


    public mxCell setCell(String style, MacroMxCellValue value,
                          List<mxCell> ports, int leftPorts) {
        mxCell cell = paletteCellMap.get(style);
        cell.setValue(value);

        double leftOffset = 1.0/((double) leftPorts + 1.0);
        //double rightOffset = 1.0/((double) rightPorts + 1.0);
        double leftYPos = leftOffset;
        //double rightYPos = rightOffset;

        while (cell.getChildCount() != 0) {
            cell.remove(0);
        }
        for (mxICell port : ports) {
            mxGeometry geo = port.getGeometry();
            geo.setY(leftYPos);
            leftYPos += leftOffset;

            try {
                cell.insert((mxICell) port.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return cell;
    }


    /**
     * @param eventName
     * @param listener
     * @see com.mxgraph.util.mxEventSource#addListener(java.lang.String, com.mxgraph.util.mxEventSource.mxIEventListener)
     */
    public void addListener(String eventName, mxIEventListener listener)
    {
        eventSource.addListener(eventName, listener);
    }

    /**
     * @return whether or not event are enabled for this palette
     * @see com.mxgraph.util.mxEventSource#isEventsEnabled()
     */
    public boolean isEventsEnabled()
    {
        return eventSource.isEventsEnabled();
    }

    /**
     * @param listener
     * @see com.mxgraph.util.mxEventSource#removeListener(com.mxgraph.util.mxEventSource.mxIEventListener)
     */
    public void removeListener(mxIEventListener listener)
    {
        eventSource.removeListener(listener);
    }

    /**
     * @param eventName
     * @param listener
     */
    public void removeListener(mxIEventListener listener, String eventName)
    {
        eventSource.removeListener(listener, eventName);
    }

    /**
     * @param eventsEnabled
     * @see com.mxgraph.util.mxEventSource#setEventsEnabled(boolean)
     */
    public void setEventsEnabled(boolean eventsEnabled)
    {
        eventSource.setEventsEnabled(eventsEnabled);
    }

}
