/*
 * $Id: EditorActions.java,v 1.38 2012-09-20 14:59:30 david Exp $
 * Copyright (c) 2001-2012, JGraph Ltd
 */
package com.swing.editor;

import com.editor.utils.PaletteElementsSchemeData;
import com.editor.utils.parser.ParseToString;
import com.editor.utils.parser.ParseToValues;
import com.editor.utils.parser.ValuesForParametersAndConditions;
import com.editor.utils.scheme.Node;
import com.editor.utils.values.MacroMxCellValue;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxGdCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.shape.mxStencilShape;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.swing.view.mxCellEditor;
import com.mxgraph.util.*;
import com.mxgraph.util.mxCellRenderer.CanvasFactory;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import com.mxgraph.util.png.mxPngTextDecoder;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.swing.GraphEditor;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.w3c.dom.Document;
import ru.nstu.isma.ui.common.AppData;
import ru.nstu.isma.ui.common.SimulationForm;
import ru.nstu.isma.ui.common.out.IsmaChart;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

/**
 *
 */
public class EditorActions {
    /**
     * @param e
     * @return Returns the graph for the given action event.
     */

    private static IsmaChart ismaChart;

    public static final GraphEditor getEditor(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component component = (Component) e.getSource();

            while (component != null
                    && !(component instanceof GraphEditor)) {
                component = component.getParent();
            }

            return (GraphEditor) component;
        }

        return null;
    }

    /**
     *
     */
    /*@SuppressWarnings("serial")
    public static class ToggleRulersItem extends JCheckBoxMenuItem {
        *//**
     *
     *//*
        public ToggleRulersItem(final BasicGraphEditor editor, String name) {
            super(name);
            setSelected(editor.getGraphComponent().getColumnHeader() != null);

            addActionListener(new ActionListener() {
                *//**
     *
     *//*
                public void actionPerformed(ActionEvent e) {
                    mxGraphComponent graphComponent = editor
                            .getGraphComponent();

                    if (graphComponent.getColumnHeader() != null) {
                        graphComponent.setColumnHeader(null);
                        graphComponent.setRowHeader(null);
                    } else {
                        graphComponent.setColumnHeaderView(new EditorRuler(
                                graphComponent,
                                EditorRuler.ORIENTATION_HORIZONTAL));
                        graphComponent.setRowHeaderView(new EditorRuler(
                                graphComponent,
                                EditorRuler.ORIENTATION_VERTICAL));
                    }
                }
            });
        }
    }*/

    /**
     *
     */
    /*@SuppressWarnings("serial")
    public static class ToggleGridItem extends JCheckBoxMenuItem {
        *//**
     *
     *//*
        public ToggleGridItem(final BasicGraphEditor editor, String name) {
            super(name);
            setSelected(true);

            addActionListener(new ActionListener() {
                *//**
     *
     *//*
                public void actionPerformed(ActionEvent e) {
                    mxGraphComponent graphComponent = editor
                            .getGraphComponent();
                    mxGraph graph = graphComponent.getGraph();
                    boolean enabled = !graph.isGridEnabled();

                    graph.setGridEnabled(enabled);
                    graphComponent.setGridVisible(enabled);
                    graphComponent.repaint();
                    setSelected(enabled);
                }
            });
        }
    }*/

    /**
     *
     *//*
    @SuppressWarnings("serial")
    public static class ToggleOutlineItem extends JCheckBoxMenuItem {
        *//**
     *
     *//*
        public ToggleOutlineItem(final BasicGraphEditor editor, String name) {
            super(name);
            setSelected(true);

            addActionListener(new ActionListener() {
                *//**
     *
     *//*
                public void actionPerformed(ActionEvent e) {
                    final mxGraphOutline outline = editor.getGraphOutline();
                    outline.setVisible(!outline.isVisible());
                    outline.revalidate();

                    SwingUtilities.invokeLater(new Runnable() {
                        *//*
                         * (non-Javadoc)
                         * @see java.lang.Runnable#run()
                         *//*
                        public void run() {
                            if (outline.getParent() instanceof JSplitPane) {
                                if (outline.isVisible()) {
                                    ((JSplitPane) outline.getParent())
                                            .setDividerLocation(editor
                                                    .getHeight() - 300);
                                    ((JSplitPane) outline.getParent())
                                            .setDividerSize(6);
                                } else {
                                    ((JSplitPane) outline.getParent())
                                            .setDividerSize(0);
                                }
                            }
                        }
                    });
                }
            });
        }
    }*/

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class ExitAction extends AbstractAction {
        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            BasicGraphEditor editor = getEditor(e);

            if (editor != null) {
                editor.exit();
            }
        }
    }

/*    public static class AddModelAsMacrosAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }*/

/*    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class StylesheetAction extends AbstractAction {
        *//**
     *
     *//*
        protected String stylesheet;

        *//**
     *
     *//*
        public StylesheetAction(String stylesheet) {
            this.stylesheet = stylesheet;
        }

        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                mxGraph graph = graphComponent.getGraph();
                mxCodec codec = new mxCodec();
                Document doc = mxUtils.loadDocument(EditorActions.class
                        .getResource(stylesheet).toString());

                if (doc != null) {
                    codec.decode(doc.getDocumentElement(),
                            graph.getStylesheet());
                    graph.refresh();
                }
            }
        }
    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class ZoomPolicyAction extends AbstractAction {
        *//**
     *
     *//*
        protected int zoomPolicy;

        *//**
     *
     *//*
        public ZoomPolicyAction(int zoomPolicy) {
            this.zoomPolicy = zoomPolicy;
        }

        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                graphComponent.setPageVisible(true);
                graphComponent.setZoomPolicy(zoomPolicy);
            }
        }
    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class GridStyleAction extends AbstractAction {
        *//**
     *
     *//*
        protected int style;

        *//**
     *
     *//*
        public GridStyleAction(int style) {
            this.style = style;
        }

        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                graphComponent.setGridStyle(style);
                graphComponent.repaint();
            }
        }
    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class GridColorAction extends AbstractAction {
        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                Color newColor = JColorChooser.showDialog(graphComponent,
                        mxResources.get("gridColor"),
                        graphComponent.getGridColor());

                if (newColor != null) {
                    graphComponent.setGridColor(newColor);
                    graphComponent.repaint();
                }
            }
        }
    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class ScaleAction extends AbstractAction {
        *//**
     *
     *//*
        protected double scale;

        *//**
     *
     *//*
        public ScaleAction(double scale) {
            this.scale = scale;
        }

        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                double scale = this.scale;

                if (scale == 0) {
                    String value = (String) JOptionPane.showInputDialog(
                            graphComponent, mxResources.get("value"),
                            mxResources.get("scale") + " (%)",
                            JOptionPane.PLAIN_MESSAGE, null, null, "");

                    if (value != null) {
                        scale = Double.parseDouble(value.replace("%", "")) / 100;
                    }
                }

                if (scale > 0) {
                    graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
                }
            }
        }
    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class PageSetupAction extends AbstractAction {
        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                PrinterJob pj = PrinterJob.getPrinterJob();
                PageFormat format = pj.pageDialog(graphComponent
                        .getPageFormat());

                if (format != null) {
                    graphComponent.setPageFormat(format);
                    graphComponent.zoomAndCenter();
                }
            }
        }
    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class PrintAction extends AbstractAction {
        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                PrinterJob pj = PrinterJob.getPrinterJob();

                if (pj.printDialog()) {
                    PageFormat pf = graphComponent.getPageFormat();
                    Paper paper = new Paper();
                    double margin = 36;
                    paper.setImageableArea(margin, margin, paper.getWidth()
                            - margin * 2, paper.getHeight() - margin * 2);
                    pf.setPaper(paper);
                    pj.setPrintable(graphComponent, pf);

                    try {
                        pj.print();
                    } catch (PrinterException e2) {
                        System.out.println(e2);
                    }
                }
            }
        }
    }*/


    //Macros models saved in separate tags due to innate library inability to parse nested models
    public static void saveModel(mxGraphModel model, String path) {
        File modelFile = new File(path);

        Element root = new Element("model");
        org.jdom2.Document document = new org.jdom2.Document(root);
        List<Object> macrosValues = new ArrayList<>();
        Object[] macroses = mxGraphModel.filterCells(model.getCells().values().toArray(), new mxGraphModel.Filter() {
            @Override
            public boolean filter(Object cell) {
                return (((mxCell) cell).getStyle() != null && ((mxCell) cell).getStyle().equals("macros"));
            }
        });

        Element macrosInnerModels = new Element("macrosModels");

        mxCodec codec = new mxCodec();

        for (Object macros : macroses) {
            mxCell macrosCell = (mxCell) macros;
            Element element = new Element("macros");
            element.addContent(new Element("id").setText(macrosCell.getId()));
            element.addContent(new Element("name").setText(((MacroMxCellValue) macrosCell.getValue())
                    .getMacroDisplayName().split(": ")[1]));
            macrosValues.add(macrosCell.getValue());
            String xml = null;
            try {
                xml = URLEncoder.encode(mxXmlUtils.getXml(codec.encode((
                        (MacroMxCellValue) macrosCell.getValue()).getModel())), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            element.addContent(new Element("data").setText(xml));
            macrosInnerModels.addContent(element);
            macrosCell.setValue("");
        }

        String xml = null;
        try {
            xml = URLEncoder.encode(mxXmlUtils.getXml(codec.encode(model)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Element mainModel = new Element("mainModel");
        mainModel.setText(xml);
        document.getRootElement().addContent(mainModel);
        document.getRootElement().addContent(macrosInnerModels);

        int macrosCount = 0;
        for (Object macros : macroses) {
            mxCell macrosCell = (mxCell) macros;
            macrosCell.setValue(macrosValues.get(macrosCount));
            macrosCount++;
        }

        try {
            try(OutputStream out = new FileOutputStream(modelFile)) {
                XMLOutputter xmlOutput = new XMLOutputter();

                // display nice nice
                xmlOutput.setFormat(Format.getPrettyFormat());
                xmlOutput.output(document, out);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void saveMacrosInXML(mxIGraphModel model, String macrosName,
                                       mxCell appearance) {
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
            Element tools = new Element("elements");
            document = new org.jdom2.Document(tools);
        }


        mxCodec codec = new mxCodec();
        String xml = null;
        //String xmlAppear = null;
        try {
            xml = URLEncoder.encode(mxXmlUtils.getXml(codec.encode(model)), "UTF-8");
            //xmlAppear = URLEncoder.encode(mxXmlUtils.getXml(codec.encode(appearance)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        boolean isRewrite = false;
        String portData = null;

        java.util.List<Element> contents = document.getRootElement().getChildren();
        for (Element content : contents) {
            String child = content.getChild("name").getText();
            if (child.equals(macrosName)) {
                content.getChild("data").setText(xml);

                Element ports = content.getChild("ports");
                ports.removeChildren("port");

                for (int childCntr = 0; childCntr < appearance.getChildCount(); childCntr++) {
                    Element port = new Element("port");
                    try {
                        portData = URLEncoder.encode(mxXmlUtils.getXml(
                                codec.encode(appearance.getChildAt(childCntr))), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    port.addContent(new Element("data")).setText(portData);
                    ports.addContent(port);
                }

                isRewrite = true;
                break;
            }
        }

        if (!isRewrite) {
            Element element = new Element("macros");
            element.addContent(new Element("name").setText(macrosName));
            element.addContent(new Element("data").setText(xml));
            //Ports children
            Element ports = new Element("ports");
            if (appearance != null) {
                for (int childCntr = 0; childCntr < appearance.getChildCount(); childCntr++) {
                    Element port = new Element("port");
                    try {
                        portData = URLEncoder.encode(mxXmlUtils.getXml(
                                codec.encode(appearance.getChildAt(childCntr))), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    port.addContent(new Element("data")).setText(portData);
                    ports.addContent(port);
                }
            }
            element.addContent(ports);

            document.getRootElement().addContent(element);
        }
        // Java 7 try-with-resources statement; use a try/finally
        // block to close the output stream if you're not using Java 7
        try {
            try(OutputStream out = new FileOutputStream(macrosFile)) {
                XMLOutputter xmlOutput = new XMLOutputter();

                // display nice nice
                xmlOutput.setFormat(Format.getPrettyFormat());
                xmlOutput.output(document, out);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class SaveAction extends AbstractAction {
        /**
         *
         */
        protected boolean showDialog;

        /**
         *
         */
        protected String lastDir = null;

        /**
         *
         */
        public SaveAction(boolean showDialog) {
            this.showDialog = showDialog;
        }

        /**
         * Saves XML+PNG format.
         */
        protected void saveXmlPng(BasicGraphEditor editor, String filename,
                                  Color bg) throws IOException {
            mxGraphComponent graphComponent = editor.getGraphComponent();
            mxGraph graph = graphComponent.getGraph();

            // Creates the image for the PNG file
            BufferedImage image = mxCellRenderer.createBufferedImage(graph,
                    null, 1, bg, graphComponent.isAntiAlias(), null,
                    graphComponent.getCanvas());

            // Creates the URL-encoded XML data
            mxCodec codec = new mxCodec();
            String xml = URLEncoder.encode(
                    mxXmlUtils.getXml(codec.encode(graph.getModel())), "UTF-8");
            mxPngEncodeParam param = mxPngEncodeParam
                    .getDefaultEncodeParam(image);
            param.setCompressedText(new String[]{"mxGraphModel", xml});

            // Saves as a PNG file
            FileOutputStream outputStream = new FileOutputStream(new File(
                    filename));
            try {
                mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream,
                        param);

                if (image != null) {
                    encoder.encode(image);

                    editor.setModified(false);
                    editor.setCurrentFile(new File(filename));
                } else {
                    JOptionPane.showMessageDialog(graphComponent,
                            mxResources.get("noImageData"));
                }
            } finally {
                outputStream.close();
            }
        }



        private mxCell searchPortWithValue(List<mxCell> ports, String value) {
            for (mxCell port : ports) {
                if (port.getValue().equals(value)) {
                    return port;
                }
            }
            return null;
        }

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            BasicGraphEditor editor = getEditor(e);

            if (editor != null) {
                mxGraphComponent macrosGraphComponent = editor.getGraphComponent();
                mxGraph currentGraph = macrosGraphComponent.getGraph();

                if (editor.isMacrosGraph()) {
                    String title = editor.getTabTitle();
                    if (title.endsWith("*")) {
                        title = title.substring(0, title.length()-1);
                        editor.setTabTitle(title);
                    }
                    mxGraphModel macrosModel = (mxGraphModel) currentGraph.getModel();
                    Object[] macrosCells = macrosModel.getCells().values().toArray();
                    //currentGraph.selectAll();
                    //Object[] macrosCells = currentGraph.getSelectionCells();
                    //currentGraph.clearSelection();

                    List<mxCell> macrosEntryPoints = new ArrayList<mxCell>();
                    int entryPoints = 0;
                    //int outPoints = 0;
                    for (Object aCell : macrosCells) {
                        mxCell cell = (mxCell) aCell;
                        if (cell.getStyle() != null && cell.getStyle().equals("macrosIn")) {
                            entryPoints++;
                            cell.setValue("IN_" + entryPoints);
                            currentGraph.getView().clear(cell, false, false);
                            mxCell portCell = ElementModifier.createMacrosPort(0, 0.5, true);
                            portCell.setValue("IN_" + entryPoints);
                            macrosEntryPoints.add(portCell);
                        }
                    }
                    macrosGraphComponent.refresh();
                    if (macrosEntryPoints.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                null,
                                mxResources.get("noOutPortsInMacrosError"),
                                mxResources.get("error"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Object macrosCell = BasicGraphEditor.getCellTabMap().get(macrosGraphComponent);

                    mxCell mxcell = (mxCell) macrosCell;
                    ((MacroMxCellValue) mxcell.getValue()).setModel(macrosModel);
                    double leftOffset = 1.0/((double) entryPoints + 1.0);
                    //double rightOffset = 1.0/((double) outPoints + 1.0);
                    double leftYPos = leftOffset;
                    //double rightYPos = rightOffset;




                    mxGraphComponent savedCellGraphComponent = editor.getGraphComponent(mxcell);
                    if (savedCellGraphComponent != null) {
                        List<mxCell> existingPortsList = new ArrayList<>();
                        while (mxcell.getChildCount() != 0) {
                            existingPortsList.add((mxCell) mxcell.remove(0));
                        }
                        //If macros is in one of the graphComponents
                        for (mxCell port : macrosEntryPoints) {
                            mxGeometry geo = port.getGeometry();
                            geo.setY(leftYPos);
                            leftYPos += leftOffset;

                            savedCellGraphComponent.getGraph().addCell(port, mxcell);
                            mxCell result;
                            mxCell edge;
                            if ((result = searchPortWithValue(existingPortsList, (String) port.getValue())) != null) {
                                if (result.getEdgeCount() != 0) {
                                    edge = (mxCell) result.getEdgeAt(0);
                                    boolean isSource = edge.getSource().equals(result);
                                    savedCellGraphComponent.getGraph().connectCell(edge, port, isSource);
                                    existingPortsList.remove(result);
                                }
                            }
                        }
                        for (mxCell existingPort : existingPortsList) {
                            if (existingPort.getEdgeCount() != 0) {
                                macrosModel.remove(existingPort.getEdgeAt(0));
                            }
                        }
                    }
                    else {
                        //else it's a template macros
                        EditorPalette macrosPalette = editor.getPalette(1);
                        mxCell cellAppearance = macrosPalette.setCell(mxcell.getStyle(),
                                new MacroMxCellValue(((MacroMxCellValue) mxcell.getValue())
                                        .getMacroDisplayName(), macrosModel), macrosEntryPoints, entryPoints);
                        saveMacrosInXML(macrosModel, ((MacroMxCellValue) mxcell.getValue())
                                .getMacroDisplayName().split(": ")[1], cellAppearance);
                    }
                    return;
                }



                FileFilter selectedFilter = null;
                String filename = null;
                boolean dialogShown = false;

                if (showDialog || editor.getCurrentFile() == null) {
                    String wd;

                    if (lastDir != null) {
                        wd = lastDir;
                    } else if (editor.getCurrentFile() != null) {
                        wd = editor.getCurrentFile().getParent();
                    } else {
                        wd = System.getProperty("user.dir");
                    }

                    JFileChooser fc = new JFileChooser(wd);


                    fc.addChoosableFileFilter(new DefaultFileFilter(".esd",
                            mxResources.get("esd") + mxResources.get("file")
                                    + " (.esd)"));

                    int rc = fc.showDialog(null, mxResources.get("save"));
                    dialogShown = true;

                    if (rc != JFileChooser.APPROVE_OPTION) {
                        return;
                    } else {
                        lastDir = fc.getSelectedFile().getParent();
                    }

                    filename = fc.getSelectedFile().getAbsolutePath();
                    selectedFilter = fc.getFileFilter();

                    if (selectedFilter instanceof DefaultFileFilter) {
                        String ext = ((DefaultFileFilter) selectedFilter)
                                .getExtension();

                        if (!filename.toLowerCase().endsWith(ext)) {
                            filename += ext;
                        }
                    }

                    if (new File(filename).exists()
                            && JOptionPane.showConfirmDialog(macrosGraphComponent,
                            mxResources.get("overwriteExistingFile")) != JOptionPane.YES_OPTION) {
                        return;
                    }
                } else {
                    filename = editor.getCurrentFile().getAbsolutePath();
                }

                try {
                    String ext = filename
                            .substring(filename.lastIndexOf('.') + 1);

                    if (ext.equalsIgnoreCase("svg")) {
                        mxSvgCanvas canvas = (mxSvgCanvas) mxCellRenderer
                                .drawCells(currentGraph, null, 1, null,
                                        new CanvasFactory() {
                                            public mxICanvas createCanvas(
                                                    int width, int height) {
                                                mxSvgCanvas canvas = new mxSvgCanvas(
                                                        mxDomUtils.createSvgDocument(
                                                                width, height));
                                                canvas.setEmbedded(true);

                                                return canvas;
                                            }

                                        });

                        mxUtils.writeFile(mxXmlUtils.getXml(canvas.getDocument()),
                                filename);
                    }
                    else if (ext.equalsIgnoreCase("html")) {
                        mxUtils.writeFile(mxXmlUtils.getXml(mxCellRenderer
                                .createHtmlDocument(currentGraph, null, 1, null, null)
                                .getDocumentElement()), filename);
                    } else if (ext.equalsIgnoreCase("esd")) {
                        saveModel((mxGraphModel) currentGraph.getModel(), filename);

                        /*mxCodec codec = new mxCodec();
                        String xml = mxXmlUtils.getXml(codec.encode(currentGraph
                                .getModel()));

                        mxUtils.writeFile(xml, filename);*/

                        editor.setModified(false);
                        editor.setCurrentFile(new File(filename));
                    } else if (ext.equalsIgnoreCase("txt")) {
                        String content = mxGdCodec.encode(currentGraph);

                        mxUtils.writeFile(content, filename);
                    } else {
                        Color bg = null;

                        if ((!ext.equalsIgnoreCase("gif") && !ext
                                .equalsIgnoreCase("png"))
                                || JOptionPane.showConfirmDialog(
                                macrosGraphComponent, mxResources
                                        .get("transparentBackground")) != JOptionPane.YES_OPTION) {
                            bg = macrosGraphComponent.getBackground();
                        }

                        if ((editor.getCurrentFile() != null
                                && ext.equalsIgnoreCase("png") && !dialogShown)) {
                            saveXmlPng(editor, filename, bg);
                        } else {
                            BufferedImage image = mxCellRenderer
                                    .createBufferedImage(currentGraph, null, 1, bg,
                                            macrosGraphComponent.isAntiAlias(), null,
                                            macrosGraphComponent.getCanvas());

                            if (image != null) {
                                ImageIO.write(image, ext, new File(filename));
                            } else {
                                JOptionPane.showMessageDialog(macrosGraphComponent,
                                        mxResources.get("noImageData"));
                            }
                        }
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(macrosGraphComponent,
                            ex.toString(), mxResources.get("error"),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     *
     */
    /*@SuppressWarnings("serial")
    public static class SelectShortestPathAction extends AbstractAction {
        *//**
     *
     *//*
        protected boolean directed;

        *//**
     *
     *//*
        public SelectShortestPathAction(boolean directed) {
            this.directed = directed;
        }

        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                mxGraph graph = graphComponent.getGraph();
                mxIGraphModel model = graph.getModel();

                Object source = null;
                Object target = null;

                Object[] cells = graph.getSelectionCells();

                for (int i = 0; i < cells.length; i++) {
                    if (model.isVertex(cells[i])) {
                        if (source == null) {
                            source = cells[i];
                        } else if (target == null) {
                            target = cells[i];
                        }
                    }

                    if (source != null && target != null) {
                        break;
                    }
                }

                if (source != null && target != null) {
                    int steps = graph.getChildEdges(graph.getDefaultParent()).length;
                    Object[] path = mxGraphAnalysis.getInstance()
                            .getShortestPath(graph, source, target,
                                    new mxDistanceCostFunction(), steps,
                                    directed);
                    graph.setSelectionCells(path);
                } else {
                    JOptionPane.showMessageDialog(graphComponent,
                            mxResources.get("noSourceAndTargetSelected"));
                }
            }
        }
    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class SelectSpanningTreeAction extends AbstractAction {
        *//**
     *
     *//*
        protected boolean directed;

        *//**
     *
     *//*
        public SelectSpanningTreeAction(boolean directed) {
            this.directed = directed;
        }

        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                mxGraph graph = graphComponent.getGraph();
                mxIGraphModel model = graph.getModel();

                Object parent = graph.getDefaultParent();
                Object[] cells = graph.getSelectionCells();

                for (int i = 0; i < cells.length; i++) {
                    if (model.getChildCount(cells[i]) > 0) {
                        parent = cells[i];
                        break;
                    }
                }

                Object[] v = graph.getChildVertices(parent);
                Object[] mst = mxGraphAnalysis.getInstance()
                        .getMinimumSpanningTree(graph, v,
                                new mxDistanceCostFunction(), directed);
                graph.setSelectionCells(mst);
            }
        }
    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class ToggleDirtyAction extends AbstractAction {
        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                graphComponent.showDirtyRectangle = !graphComponent.showDirtyRectangle;
            }
        }

    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class ToggleConnectModeAction extends AbstractAction {
        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                mxConnectionHandler handler = graphComponent
                        .getConnectionHandler();
                handler.setHandleEnabled(!handler.isHandleEnabled());
            }
        }
    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class ToggleCreateTargetItem extends JCheckBoxMenuItem {
        *//**
     *
     *//*
        public ToggleCreateTargetItem(final BasicGraphEditor editor, String name) {
            super(name);
            setSelected(true);

            addActionListener(new ActionListener() {
                *//**
     *
     *//*
                public void actionPerformed(ActionEvent e) {
                    mxGraphComponent graphComponent = editor
                            .getGraphComponent();

                    if (graphComponent != null) {
                        mxConnectionHandler handler = graphComponent
                                .getConnectionHandler();
                        handler.setCreateTarget(!handler.isCreateTarget());
                        setSelected(handler.isCreateTarget());
                    }
                }
            });
        }
    }

    *//**
     *
     *//*
    @SuppressWarnings("serial")
    public static class PromptPropertyAction extends AbstractAction {
        *//**
     *
     *//*
        protected Object target;

        *//**
     *
     *//*
        protected String fieldname, message;

        *//**
     *
     *//*
        public PromptPropertyAction(Object target, String message) {
            this(target, message, message);
        }

        *//**
     *
     *//*
        public PromptPropertyAction(Object target, String message,
                                    String fieldname) {
            this.target = target;
            this.message = message;
            this.fieldname = fieldname;
        }

        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof Component) {
                try {
                    Method getter = target.getClass().getMethod(
                            "get" + fieldname);
                    Object current = getter.invoke(target);

                    // TODO: Support other atomic types
                    if (current instanceof Integer) {
                        Method setter = target.getClass().getMethod(
                                "set" + fieldname, new Class[]{int.class});

                        String value = (String) JOptionPane.showInputDialog(
                                (Component) e.getSource(), "Value", message,
                                JOptionPane.PLAIN_MESSAGE, null, null, current);

                        if (value != null) {
                            setter.invoke(target, Integer.parseInt(value));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // Repaints the graph component
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                graphComponent.repaint();
            }
        }
    }*/

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class TogglePropertyItem extends JCheckBoxMenuItem {
        /**
         *
         */
        public TogglePropertyItem(Object target, String name, String fieldname) {
            this(target, name, fieldname, false);
        }

        /**
         *
         */
        public TogglePropertyItem(Object target, String name, String fieldname,
                                  boolean refresh) {
            this(target, name, fieldname, refresh, null);
        }

        /**
         *
         */
        public TogglePropertyItem(final Object target, String name,
                                  final String fieldname, final boolean refresh,
                                  ActionListener listener) {
            super(name);

            // Since action listeners are processed last to first we add the given
            // listener here which means it will be processed after the one below
            if (listener != null) {
                addActionListener(listener);
            }

            addActionListener(new ActionListener() {
                /**
                 *
                 */
                public void actionPerformed(ActionEvent e) {
                    execute(target, fieldname, refresh);
                }
            });

            PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

                /*
                 * (non-Javadoc)
                 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
                 */
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equalsIgnoreCase(fieldname)) {
                        update(target, fieldname);
                    }
                }
            };

            if (target instanceof mxGraphComponent) {
                ((mxGraphComponent) target)
                        .addPropertyChangeListener(propertyChangeListener);
            } else if (target instanceof mxGraph) {
                ((mxGraph) target)
                        .addPropertyChangeListener(propertyChangeListener);
            }

            update(target, fieldname);
        }

        /**
         *
         */
        public void update(Object target, String fieldname) {
            if (target != null && fieldname != null) {
                try {
                    Method getter = target.getClass().getMethod(
                            "is" + fieldname);

                    if (getter != null) {
                        Object current = getter.invoke(target);

                        if (current instanceof Boolean) {
                            setSelected(((Boolean) current).booleanValue());
                        }
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        /**
         *
         */
        public void execute(Object target, String fieldname, boolean refresh) {
            if (target != null && fieldname != null) {
                try {
                    Method getter = target.getClass().getMethod(
                            "is" + fieldname);
                    Method setter = target.getClass().getMethod(
                            "set" + fieldname, new Class[]{boolean.class});

                    Object current = getter.invoke(target);

                    if (current instanceof Boolean) {
                        boolean value = !((Boolean) current).booleanValue();
                        setter.invoke(target, value);
                        setSelected(value);
                    }

                    if (refresh) {
                        mxGraph graph = null;

                        if (target instanceof mxGraph) {
                            graph = (mxGraph) target;
                        } else if (target instanceof mxGraphComponent) {
                            graph = ((mxGraphComponent) target).getGraph();
                        }

                        graph.refresh();
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class HistoryAction extends AbstractAction {
        /**
         *
         */
        protected boolean undo;

        /**
         *
         */
        public HistoryAction(boolean undo) {
            this.undo = undo;
        }

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            BasicGraphEditor editor = getEditor(e);

            if (editor != null) {
                if (undo) {
                    editor.getUndoManager().undo();
                } else {
                    editor.getUndoManager().redo();
                }
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class FontStyleAction extends AbstractAction {
        /**
         *
         */
        protected boolean bold;

        /**
         *
         */
        public FontStyleAction(boolean bold) {
            this.bold = bold;
        }

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                Component editorComponent = null;

                if (graphComponent.getCellEditor() instanceof mxCellEditor) {
                    editorComponent = ((mxCellEditor) graphComponent
                            .getCellEditor()).getEditor();
                }

                if (editorComponent instanceof JEditorPane) {
                    JEditorPane editorPane = (JEditorPane) editorComponent;
                    int start = editorPane.getSelectionStart();
                    int ende = editorPane.getSelectionEnd();
                    String text = editorPane.getSelectedText();

                    if (text == null) {
                        text = "";
                    }

                    try {
                        HTMLEditorKit editorKit = new HTMLEditorKit();
                        HTMLDocument document = (HTMLDocument) editorPane
                                .getDocument();
                        document.remove(start, (ende - start));
                        editorKit.insertHTML(document, start, ((bold) ? "<b>"
                                        : "<i>") + text + ((bold) ? "</b>" : "</i>"),
                                0, 0, (bold) ? HTML.Tag.B : HTML.Tag.I);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    editorPane.requestFocus();
                    editorPane.select(start, ende);
                } else {
                    mxIGraphModel model = graphComponent.getGraph().getModel();
                    model.beginUpdate();
                    try {
                        graphComponent.stopEditing(false);
                        graphComponent.getGraph().toggleCellStyleFlags(
                                mxConstants.STYLE_FONTSTYLE,
                                (bold) ? mxConstants.FONT_BOLD
                                        : mxConstants.FONT_ITALIC);
                    } finally {
                        model.endUpdate();
                    }
                }
            }
        }
    }

    /**
     *
     */
    public static class RunSolverAction extends AbstractAction {

        //private List<Branch> branches;
        private Set<Node> nodes;
        private GraphEditor editor;
        //helper variable to determine if we enter or exit from amcros with this macrosIn element
        private boolean enterMacros;

        private void goInDirection(Node mNode, mxCell source, mxCell connection, mxCell macrosCell) {
            mxCell connectedElem;

            if (connection.getStyle().equals("boldEWEdge")) {
                connectedElem = connection;
            } else {
                mxCell connectedElemPort;
                connectedElemPort = (mxCell) connection.getTarget();
                connectedElem = (mxCell) connectedElemPort.getParent();
                if (connectedElem.equals(source)) {
                    connectedElemPort = (mxCell) connection.getSource();
                    connectedElem = (mxCell) connectedElemPort.getParent();
                }
                //Handle macros entry point
                if (connectedElem.getStyle().equals("macros")) {
                    macrosCell = connectedElem;
                    //mxCell macrosEntryPoint = null;
                    MacroMxCellValue macroValue = (MacroMxCellValue) connectedElem.getValue();
                    for (Object cell : macroValue.getModel().getCells().values()) {
                        if (((mxCell) cell).getValue() != null && ((mxCell) cell).getValue()
                                .equals(connectedElemPort.getValue())) {
                            connectedElem = (mxCell) cell;
                            break;
                        }
                    }
                    enterMacros = true;
                }
            }
            //Skip macrosIn element
            if (connectedElem.getStyle().equals("macrosIn") && enterMacros) {
                enterMacros = false;
                next(mNode, connectedElem, null, macrosCell, false);
            }
            //Handle macros exit point
            else if (connectedElem.getStyle().equals("macrosIn") && !enterMacros) {
                mxCell macrosExitPoint;
                mxCell nextConnection = null;
                for (int child = 0; child < macrosCell.getChildCount(); child++) {
                    if ((macrosExitPoint = (mxCell) macrosCell.getChildAt(child)).getValue()
                            .equals(connectedElem.getValue())) {
                        nextConnection = (mxCell) macrosExitPoint.getEdgeAt(0);
                        connectedElem = macrosCell;
                        break;
                    }
                }
                next(mNode, connectedElem, nextConnection, null, true);
            }
            //If next element is not wire the next node should be created based on it
            else if (!connectedElem.getStyle().startsWith("wire")) {
                if (connectedElem.getStyle().equals("ismaGenerator")) {
                    if (mNode.isWithGenerator()) {
                        errorDirectGeneratorsConnection = true;
                        return;
                    }
                    mNode.setIsWithGenerator(true);
                }
                mNode.addElement(connectedElem);
                //In case connected element is edge pass port of previous element as previous connection
                if (connectedElem.isEdge()) {
                    next(null, connectedElem, source, macrosCell, false);
                    nodes.add(mNode);
                } else {
                    next(null, connectedElem, connection, macrosCell, false);
                    nodes.add(mNode);
                }
            }
            //else if next element is wire - node will contain more elements
            else {
                next(mNode, connectedElem, connection, macrosCell, false);
            }
        }
        private boolean errorDirectGeneratorsConnection;
        private void next(Node mNode, mxCell source, mxCell prevConnection, mxCell macrosCell,
                          boolean inOneDirection) {
            //If error occurred return from recursion
            if (errorDirectGeneratorsConnection) {
                return;
            }
            assert source != null;
            if (mNode == null) {
                mNode = new Node(source);
            }
            if (source.getStyle().equals("ismaGenerator")) {
                mNode.setIsWithGenerator(true);
            }
            mxCell connection;
            if (source.isEdge()) {
                mxCell connectedElemPort;
                connectedElemPort = (mxCell) source.getTarget();
                mxCell connectedElem = (mxCell) connectedElemPort.getParent();
                if (connectedElem.equals(prevConnection)) {
                    connectedElemPort = (mxCell) source.getSource();
                    connectedElem = (mxCell) connectedElemPort.getParent();
                }
                if (connectedElem.getStyle().equals("macros")) {
                    macrosCell = connectedElem;
                    //mxCell macrosEntryPoint = null;
                    MacroMxCellValue macroValue = (MacroMxCellValue) connectedElem.getValue();
                    for (Object cell : macroValue.getModel().getCells().values()) {
                        if (((mxCell) cell).getValue() != null && ((mxCell) cell).getValue()
                                .equals(connectedElemPort.getValue())) {
                            connectedElem = (mxCell) cell;
                            break;
                        }
                    }
                    enterMacros = true;
                }
                //Skip macrosIn element
                if (connectedElem.getStyle().equals("macrosIn") && enterMacros) {
                    enterMacros = false;
                    next(mNode, connectedElem, null, macrosCell, false);
                }
                //Handle macros exit point
                else if (connectedElem.getStyle().equals("macrosIn") && !enterMacros) {
                    mxCell macrosExitPoint;
                    mxCell nextConnection = null;
                    for (int child = 0; child < macrosCell.getChildCount(); child++) {
                        if ((macrosExitPoint = (mxCell) macrosCell.getChildAt(child)).getValue()
                                .equals(connectedElem.getValue())) {
                            nextConnection = (mxCell) macrosExitPoint.getEdgeAt(0);
                            connectedElem = macrosCell;
                            break;
                        }
                    }
                    next(mNode, connectedElem, nextConnection, null, true);
                }
                //If next element is not wire the next node should be created based on it
                else if (!connectedElem.getStyle().startsWith("wire")) {
                    if (connectedElem.getStyle().equals("ismaGenerator")) {
                        if (mNode.isWithGenerator()) {
                            errorDirectGeneratorsConnection = true;
                            return;
                        }
                        mNode.setIsWithGenerator(true);
                    }
                    mNode.addElement(connectedElem);
                    next(null, connectedElem, source, macrosCell, false);
                    nodes.add(mNode);
                }
                //else if next element is wire - node will contain more elements
                else {
                    next(mNode, connectedElem, source, macrosCell, false);
                }
            }
            else if (inOneDirection) {
                goInDirection(mNode, source, prevConnection, macrosCell);
            }
            else {
                for (int portNum = 0; portNum < source.getChildCount(); portNum++) {
                    connection = (mxCell) source.getChildAt(portNum).getEdgeAt(0);
                    //If port does not lead back from where we came
                    if (connection != null && !connection.equals(prevConnection)) {
                        goInDirection(mNode, source, connection, macrosCell);
                    }
                }
            }
        }


        private Complex[][] matrixY;
        private Complex[][] vectorL;

        private ComplexMatrix calculateMatrix(List<Node> nodeList, int generatorNodesNum) {
            matrixY = new Complex[nodeList.size()][nodeList.size()];
            vectorL = new Complex[nodeList.size()][nodeList.size()];
            for (int arrayNum = 0; arrayNum < matrixY.length; arrayNum++) {

                for (int itemNum = 0; itemNum < matrixY.length; itemNum++) {
                    matrixY[arrayNum][itemNum] = Complex.valueOf(0, 0);
                    vectorL[arrayNum][itemNum] = Complex.valueOf(0, 0);
                }
            }

            List<mxCell> elems;
            String style;
            //This map helps determine nodes connected with the same element
            Map<mxCell, Integer> lepNodes = new HashMap<>();
            for (int nodeId = 0; nodeId < nodeList.size(); nodeId++) {
                elems = nodeList.get(nodeId).getNodeElems();
                for (mxCell elem : elems) {
                    style = elem.getStyle();
                    switch (style) {
                        case "ismaCommonLoad": {
                            ParseToValues parser = new ParseToValues((String) elem.getValue());
                            HashMap<Integer, ValuesForParametersAndConditions> conds = parser.getHashMapForValuesConditions();
                            double g = Double.MAX_VALUE;
                            double b = Double.MAX_VALUE;
                            for (Map.Entry<Integer, ValuesForParametersAndConditions> cond : conds.entrySet()) {
                                if (cond.getValue().getName().equals("g")) {
                                    g = cond.getValue().getValue();
                                } else if (cond.getValue().getName().equals("b")) {
                                    b = cond.getValue().getValue();
                                }
                            }
                            Complex Y = Complex.valueOf(g, b);
                            matrixY[nodeId][nodeId] = matrixY[nodeId][nodeId].plus(Y);
                            vectorL[nodeId][nodeId] = vectorL[nodeId][nodeId].plus(Y);
                            break;
                        }
                        case "boldEWEdge": {
                            ParseToValues parser = new ParseToValues((String) elem.getValue());
                            HashMap<Integer, ValuesForParametersAndConditions> conds = parser.getHashMapForValuesConditions();
                            double r = Double.MAX_VALUE;
                            double x = Double.MAX_VALUE;
                            double g = Double.MAX_VALUE;
                            double b = Double.MAX_VALUE;
                            for (Map.Entry<Integer, ValuesForParametersAndConditions> cond : conds.entrySet()) {
                                if (cond.getValue().getName().equals("r")) {
                                    r = cond.getValue().getValue();
                                } else if (cond.getValue().getName().equals("x")) {
                                    x = cond.getValue().getValue();
                                } else if (cond.getValue().getName().equals("g")) {
                                    g = cond.getValue().getValue();
                                } else if (cond.getValue().getName().equals("b")) {
                                    b = cond.getValue().getValue();
                                }
                            }
                            Complex Z = Complex.valueOf(r, x);
                            Complex Y = Complex.valueOf(g, b);
                            Complex mutualConductivity = Z.inverse();
                            Complex selfConductivity = Y.plus(mutualConductivity);
                            matrixY[nodeId][nodeId] = matrixY[nodeId][nodeId].plus(selfConductivity);
                            //If this LEP was already located in another node
                            if (lepNodes.containsKey(elem)) {
                                int connectedNode = lepNodes.get(elem);
                                matrixY[nodeId][connectedNode] = matrixY[nodeId][connectedNode].plus(mutualConductivity);
                                matrixY[connectedNode][nodeId] = matrixY[connectedNode][nodeId].plus(mutualConductivity);
                            } else {
                                lepNodes.put(elem, nodeId);
                            }
                            break;
                        }
                        case "ismaTransform": {
                            ParseToValues parser = new ParseToValues((String) elem.getValue());
                            HashMap<Integer, ValuesForParametersAndConditions> conds =
                                    parser.getHashMapForValuesConditions();
                            double r = Double.MAX_VALUE;
                            double x = Double.MAX_VALUE;
                            double g = Double.MAX_VALUE;
                            double b = Double.MAX_VALUE;
                            double k = Double.MAX_VALUE;
                            double y = Double.MAX_VALUE;
                            for (Map.Entry<Integer, ValuesForParametersAndConditions> cond : conds.entrySet()) {
                                if (cond.getValue().getName().equals("r")) {
                                    r = cond.getValue().getValue();
                                } else if (cond.getValue().getName().equals("x")) {
                                    x = cond.getValue().getValue();
                                } else if (cond.getValue().getName().equals("g")) {
                                    g = cond.getValue().getValue();
                                } else if (cond.getValue().getName().equals("b")) {
                                    b = cond.getValue().getValue();
                                } else if (cond.getValue().getName().equals("k")) {
                                    k = cond.getValue().getValue();
                                } else if (cond.getValue().getName().equals("y")) {
                                    y = cond.getValue().getValue();
                                }
                            }
                            Complex Y = Complex.valueOf(g, b);
                            Complex Z = Complex.valueOf(r, x);
                            Complex K = Complex.valueOf(k, y);
                            //If the element is first in array it means this node was created based
                            //on this element. For transformer it means it is definitely to the left.
                            Complex selfConductivity;
                            boolean isRotated = false;
                            //Was transformer rotated?
                            if (ElementModifier.getStyleValue(elem.getChildAt(0).getStyle(), mxConstants.STYLE_DIRECTION)
                                    .equals(mxConstants.DIRECTION_WEST)) {
                                isRotated = true;
                            }
                            if (elems.indexOf(elem) == 0 ^ isRotated) {
                                selfConductivity = Complex.valueOf(Math.pow(K.magnitude(), 2), 0).divide(Z);
                            } else {
                                selfConductivity = Y.plus(Z.inverse());
                            }
                            matrixY[nodeId][nodeId] = matrixY[nodeId][nodeId].plus(selfConductivity);
                            //If this transformer was already located in another node
                            if (lepNodes.containsKey(elem)) {
                                int connectedNode = lepNodes.get(elem);
                                Complex mutualConductivityJI = Complex.valueOf(Math.pow(K.magnitude(), 2), 0).divide(Z.times(K));
                                Complex mutualConductivityIJ = K.divide(Z);
                                matrixY[nodeId][connectedNode] = matrixY[nodeId][connectedNode].plus(mutualConductivityJI);
                                matrixY[connectedNode][nodeId] = matrixY[connectedNode][nodeId].plus(mutualConductivityIJ);
                            } else {
                                lepNodes.put(elem, nodeId);
                            }
                            break;
                        }
                    }
                }
            }
            return reduceMatrix(generatorNodesNum);
        }

        private ComplexMatrix reduceMatrix(int generatorNodesNum) {
            ComplexMatrix conductivityMatrix = ComplexMatrix.valueOf(matrixY);
            if (generatorNodesNum == conductivityMatrix.getNumberOfRows()) {
                return conductivityMatrix;
            }

            ComplexMatrix Yee = getSubMatrix(conductivityMatrix, 0, generatorNodesNum - 1,
                    0, generatorNodesNum - 1);
            ComplexMatrix Yeu = getSubMatrix(conductivityMatrix, 0, generatorNodesNum - 1,
                    generatorNodesNum, conductivityMatrix.getNumberOfColumns() - 1);
            ComplexMatrix Yue = getSubMatrix(conductivityMatrix, generatorNodesNum,
                    conductivityMatrix.getNumberOfRows() - 1, 0, generatorNodesNum - 1);
            ComplexMatrix Yuu = getSubMatrix(conductivityMatrix, generatorNodesNum,
                    conductivityMatrix.getNumberOfRows() - 1, generatorNodesNum,
                    conductivityMatrix.getNumberOfColumns() - 1);
            ComplexMatrix YL = ComplexMatrix.valueOf(vectorL);


            Complex[][] data = new Complex[Yuu.getNumberOfRows()][Yuu.getNumberOfRows()];
            int i = 0;
            for (int row = YL.getNumberOfRows() - Yuu.getNumberOfRows(); row < YL.getNumberOfRows(); row++) {
                for (int col = 0; col < Yuu.getNumberOfColumns(); col++) {
                    if (i == col) {
                        //TODO Was data[i][i] = Yuu.get(i, i).plus(YL.get(row, row));
                        data[i][i] = Yuu.get(i, i);
                    }
                    else {
                        data[i][col] = Yuu.get(i, col);
                    }
                }
                i++;
            }
            ComplexMatrix Yu = ComplexMatrix.valueOf(data);
            ComplexMatrix reversedYu = Yu.inverse();
            return Yee.minus(Yeu.times(reversedYu.times(Yue)));
        }

        private ComplexMatrix getSubMatrix(ComplexMatrix base, int startRow, int endRow,
                                           int startColumn, int endColumn) {
            Complex[][] data = new Complex[endRow - startRow + 1][endColumn - startColumn + 1];
            int i = 0, j = 0;
            for (int row = startRow; row < endRow + 1; row++) {
                for (int col = startColumn; col < endColumn + 1; col++) {
                    data[i][j] = base.get(row, col);
                    j++;
                }
                i++;
                j=0;
            }
            return ComplexMatrix.valueOf(data);
        }

        public StringBuilder generateEquations(List<Node> nodeList, int generatorNodes,
                                               ComplexMatrix conductivityMatrix) {
            StringBuilder code = new StringBuilder();

            code.append("omegaStart").append(" = ")
                    .append("1").append(";\n");

            String[] lsEquations = new String[generatorNodes];
            for (int nodeCntr = 0; nodeCntr < generatorNodes; nodeCntr++) {
                Node curNode = nodeList.get(nodeCntr);
                curNode.setGeneratorTooltip(String.valueOf(nodeCntr));
                code.append("delta").append(nodeCntr).append("(0) = ")
                        .append(curNode.getGeneratorValue("Beta")).append(";\n");
                code.append("omega").append(nodeCntr).append("(0) = ")
                        .append("1").append(";\n");
                code.append("delta").append(nodeCntr).append("' = ")
                        .append("omega").append(nodeCntr).append(";\n");
                double bii = conductivityMatrix.get(nodeCntr, nodeCntr).getImaginary();
                double x_qi = curNode.getGeneratorValue("x_q");
                double xdiff_di = curNode.getGeneratorValue("x'_d");
                double x_di = curNode.getGeneratorValue("x_d");
                double E_q = curNode.getGeneratorValue("E_q");
                double T_r = curNode.getGeneratorValue("T_r");
                double E_r = curNode.getGeneratorValue("E_r");
                double P_m = curNode.getGeneratorValue("P_m");
                double T_j = curNode.getGeneratorValue("T_j");
                String genCosSumm = "";
                String genSinSumm = "genSinSumm" + String.valueOf(nodeCntr) + " = 0";
                //Equation (E_q(0))
                String codeStringE_q = "E_q" + String.valueOf(nodeCntr) + "(0) = (1 + " + String.valueOf(bii) +
                        " * " + String.valueOf(x_qi - xdiff_di) + ") * " + String.valueOf(E_q) + " + " +
                        String.valueOf(x_qi - xdiff_di);
                String startGenCosSumm = "0";
                for (int inNodeCntr = 0; inNodeCntr < generatorNodes; inNodeCntr++) {
                    if (inNodeCntr == nodeCntr) {
                        continue;
                    }
                    startGenCosSumm += " + " + nodeList.get(inNodeCntr).getGeneratorValue("E_q") + " * " +
                            String.valueOf(conductivityMatrix.get(nodeCntr, inNodeCntr).magnitude()) +
                            "* cos(" + nodeList.get(nodeCntr).getGeneratorValue("Beta") + " - "
                            + nodeList.get(inNodeCntr).getGeneratorValue("Beta")
                            + " - " + conductivityMatrix.get(nodeCntr, inNodeCntr).argument() + ")";
                    if (!genCosSumm.equals("")) {
                        genCosSumm += " + ";
                    }
                    genCosSumm += "E_Q" + inNodeCntr +
                            " * " + String.valueOf(conductivityMatrix.get(nodeCntr, inNodeCntr).magnitude()) +
                            " * " + "cos(delta" + nodeCntr + " - " + "delta" + inNodeCntr + " - " +
                            conductivityMatrix.get(nodeCntr, inNodeCntr).argument() + ")" + " * " + String.valueOf(x_qi - xdiff_di);
                }
                //If no other generators found just close this equation
                if (startGenCosSumm.equals("0")) {
                    code.append(codeStringE_q).append(";\n");
                }
                //else add cosGeneratorEquations
                else {
                    codeStringE_q += " * (";
                    codeStringE_q += startGenCosSumm;
                    code.append(codeStringE_q).append(");\n");
                }
                for (int inNodeCntr = 0; inNodeCntr < generatorNodes; inNodeCntr++) {
                    if (inNodeCntr == nodeCntr) {
                        continue;
                    }
                    //Node inNode = nodeList.get(inNodeCntr);
                    genSinSumm += " + " + "E_Q" + inNodeCntr +
                            " * " + String.valueOf(conductivityMatrix.get(nodeCntr, inNodeCntr).magnitude()) +
                            " * " + "sin(delta" + nodeCntr + " - " + "delta" + inNodeCntr + " - " +
                            conductivityMatrix.get(nodeCntr, inNodeCntr).argument() + ")";
                }
                if (generatorNodes == 1) {
                    //To prevent from multiplying by 0 when there is only 1 generator
                    genCosSumm = "";
                    genSinSumm = "genSinSumm" + String.valueOf(nodeCntr) + " = 1";
                }
                code.append(genSinSumm).append(";\n");

                //Equation (omega')
                String codeStringOmegaDiff = "omega" + String.valueOf(nodeCntr) + "' = (" + String.valueOf(P_m) +
                        " - " + "pow(E_Q" + String.valueOf(nodeCntr) + ", 2)" + " * "
                        + String.valueOf(conductivityMatrix.get(nodeCntr, nodeCntr).getReal())
                        + " - " + "E_Q" + String.valueOf(nodeCntr) + " * genSinSumm" + String.valueOf(nodeCntr)
                        + ")" + " * " + "omegaStart / " + String.valueOf(T_j);
                code.append(codeStringOmegaDiff).append(";\n");
                //ls equations
                String lsEquationParts[] = genCosSumm.split("\\+");
                String equationEnding = " = E_q" + String.valueOf(nodeCntr);
                String keyEquationPart = "E_Q" + String.valueOf(nodeCntr)+ " * " + "(1 + " + String.valueOf(bii) +
                        " * " + String.valueOf(x_qi - xdiff_di) + ")";
                String lsCodeStringE_q = "ls ";
                for (int lsCntr = 0; lsCntr < generatorNodes - 1; lsCntr++) {
                    if (lsCntr != 0) {
                        lsCodeStringE_q += " + ";
                    }
                    if (nodeCntr == lsCntr) {
                        lsCodeStringE_q += keyEquationPart + " + ";
                    }
                    lsCodeStringE_q += lsEquationParts[lsCntr];
                }
                //If this is last node add key part to the end of equation
                if (nodeCntr == generatorNodes - 1) {
                    if (generatorNodes != 1) {
                        lsCodeStringE_q += " + ";
                    }
                    lsCodeStringE_q += keyEquationPart;
                }
                lsCodeStringE_q += equationEnding;
                lsEquations[nodeCntr] = lsCodeStringE_q;
                //Equation (E_q')
                String codeStringEdiff_q = "E_q" + String.valueOf(nodeCntr) + "' = 1/" + String.valueOf(T_r) +
                        " * (" + String.valueOf(E_r) + " - " + "E_Q" + nodeCntr + " * " + String.valueOf(x_di - xdiff_di) +
                        " / " + String.valueOf(x_qi - xdiff_di) + " + " + "E_q" + String.valueOf(nodeCntr)
                        + " * " + String.valueOf(x_di - x_qi) + "/" + String.valueOf(x_qi - xdiff_di) + ")";
                code.append(codeStringEdiff_q).append(";\n");

                /*//Equation (P_e)
                String codeStringP_e = "P_e" + String.valueOf(nodeCntr) + " = " +
                        String.valueOf(Math.pow(E_q, 2)) + " * " +
                        String.valueOf(conductivityMatrix.get(nodeCntr, nodeCntr).getReal()) + " + "
                        + String.valueOf(E_q) + " * genSinSumm" + String.valueOf(nodeCntr);
                code.append(codeStringP_e).append(";\n");*/
                /*//Equation (I_q)
                String codeStringI_q = "I_q" + String.valueOf(nodeCntr) + " = " + "P_e" + String.valueOf(nodeCntr) +
                        "/" + String.valueOf(E_q);
                code.append(codeStringI_q).append(";\n");
                //Equation (I_d)
                String codeStringI_d = "I_d" + String.valueOf(nodeCntr) + " = " + String.valueOf(E_q) + " * " +
                        String.valueOf(matrixY[nodeCntr][nodeCntr].getImaginary()) + " + " + "genCosSumm" +
                        String.valueOf(nodeCntr);
                code.append(codeStringI_d).append(";\n");
                //Equation (Q_e)
                String codeStringQ_e = "Q_e" + String.valueOf(nodeCntr) + " = " + String.valueOf(E_q) + " * I_d" +
                        String.valueOf(nodeCntr);
                code.append(codeStringQ_e).append(";\n");*/
            }
            //ls
            code.append("\n");
            String var = "var ls ";
            String comma = ", ";
            for (int node = 0; node < generatorNodes; node++) {
                var += "E_Q" + node;

                if (node != generatorNodes - 1) {
                    var += comma;
                }
            }
            code.append(var).append(";\n");
            for (String eq : lsEquations) {
                code.append(eq).append(";\n");
            }

            return code;
        }

        private mxCell validateModelAndGetSource(mxGraphModel model, mxCell macrosCell) {
            mxCell source = null;
            Object[] cells = mxGraphModel.getChildren(model, ((mxCell) model.getRoot()).getChildAt(0));
            for (Object aCell : cells) {
                if (errorNotConnected || errorCellValue != null) {
                    break;
                }
                mxCell cell = (mxCell) aCell;
                if (cell.getStyle().equals("macros")) {
                    mxCell tempSource = validateModelAndGetSource(((MacroMxCellValue) cell.getValue()).getModel(),
                            cell);
                    if (tempSource != null) {
                        source = tempSource;
                    }
                }
                if (cell.isVertex()) {
                    if (!cell.getStyle().startsWith("macros")) {
                        if (!validateCellValues(cell)) {
                            errorCellValue = mxResources.get("errorInvalidCellValues", new String[] {cell.getStyle(), cell.getId()});
                        }
                    }
                    Object[] ports = mxGraphModel.getChildren(model, cell);
                    //Wire can have unconnected ports. But only if it has at least two connected
                    if (cell.getStyle().startsWith("wire")) {
                        int minPortsCount = 0;
                        for (Object port : ports) {
                            mxCell cellPort = (mxCell) port;
                            if (cellPort.getEdgeCount() > 0) {
                                minPortsCount++;
                            }
                        }
                        if (minPortsCount < 2) {
                            errorNotConnected = true;
                            return null;
                        }
                    }
                    else {
                        for (Object port : ports) {
                            mxCell cellPort = (mxCell) port;
                            if (cellPort.getEdgeCount() == 0) {
                                errorNotConnected = true;
                                return null;
                            }
                        }
                    }
                }
                if (cell.getStyle().equals("ismaGenerator")) {
                    if (source != null) {
                        twoGenFound = true;
                    }
                    source = cell;
                    sourceMacrosCell = macrosCell;
                    if (validateGeneratorValues(cell)) {
                        errorCellValue = mxResources.get("errorInvalidGeneratorValues", new String[] {cell.getId()});
                        return null;
                    }
                }
            }
            return source;
        }

        private boolean validateCellValues(mxCell cell) {
            ParseToValues parser = new ParseToValues((String) cell.getValue());
            HashMap<Integer, ValuesForParametersAndConditions> conds
                    = parser.getHashMapForValuesConditions();
            for (ValuesForParametersAndConditions value : conds.values()) {
                if (value.getValue() < 0) {
                    return false;
                }
            }
            return true;
        }

        private boolean validateGeneratorValues(mxCell generator) {
            ParseToValues parser = new ParseToValues((String) generator.getValue());
            HashMap<Integer, ValuesForParametersAndConditions> conds
                    = parser.getHashMapForValuesConditions();
            double x_d_diff = Double.MAX_VALUE;
            double x_q = Double.MAX_VALUE;
            for (Map.Entry<Integer,ValuesForParametersAndConditions> cond : conds.entrySet()) {
                if (cond.getValue().getName().equals("x'_d")) {
                    x_d_diff = cond.getValue().getValue();
                }
                else if (cond.getValue().getName().equals("x_q")) {
                    x_q = cond.getValue().getValue();
                }
            }
            return x_d_diff == x_q;
        }

        //In case source was found inside macros
        private mxCell sourceMacrosCell;
        //If source is not found - error
        private boolean twoGenFound;
        //If one of the elements has not occupied ports
        private boolean errorNotConnected;
        //If x'_d and x_q are equal - error. This condition will lead to division by zero.
        //Also cell values can't be negative.
        private String errorCellValue;

        public void actionPerformed(ActionEvent e) {
            twoGenFound = false;
            errorNotConnected = false;
            sourceMacrosCell = null;
            enterMacros = false;
            errorCellValue = null;
            errorDirectGeneratorsConnection = false;
            editor = getEditor(e);
            mxGraphComponent rootGC = editor.getGraphComponent(0);

            nodes = new HashSet<>();
            mxGraphModel gc = (mxGraphModel) rootGC.getGraph().getModel();

            //diagnostics
            mxCell source = validateModelAndGetSource(gc, null);

            if (errorCellValue != null) {
                JOptionPane.showMessageDialog(editor, errorCellValue);
                return;
            }

            if (errorNotConnected) {
                JOptionPane.showMessageDialog(editor,
                        mxResources.get("errorElementNotConnected"));
                return;
            }

            if (!twoGenFound) {
                JOptionPane.showMessageDialog(editor,
                        mxResources.get("errorNoGenerator"));
                return;
            }

            //Division in nodes
            next(null, source, null, sourceMacrosCell, false);
            if (errorDirectGeneratorsConnection) {
                JOptionPane.showMessageDialog(editor,
                        mxResources.get("errorDirectGeneratorsConnection"));
                return;
            }

            List<Node> nodeList = new ArrayList<>(nodes);
            //Sort to get nodes with generator first
            Collections.sort(nodeList, new Comparator<Node>() {
                @Override
                public int compare(final Node lhs, Node rhs) {
                    if (lhs.isWithGenerator() && !rhs.isWithGenerator()) {
                        return -1;
                    }
                    if (!lhs.isWithGenerator() && rhs.isWithGenerator()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            int generatorNodes = 0;

            for (Node node : nodeList) {
                if (node.isWithGenerator()) {
                    generatorNodes++;
                }
            }

            ComplexMatrix reducedMatrix = calculateMatrix(nodeList, generatorNodes);


            StringBuilder code = generateEquations(nodeList, generatorNodes, reducedMatrix);
            System.out.println("===========================================================");
            System.out.println("GENERATED MODEL");
            System.out.println("===========================================================");
            System.out.println(code);

            //Chart frames
            AppData data = new AppData();

            data.setText(code.toString());
            data.text2hsm();
            if (ismaChart != null) {
                ismaChart.setVisible(false);
            }

            SimulationForm settingsForm = new SimulationForm(data, getEditor(e));
            settingsForm.setVisible(true);
        }
    }




    /**
     *
     */
    @SuppressWarnings("serial")
    public static class WarningAction extends AbstractAction {
        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                Object[] cells = graphComponent.getGraph().getSelectionCells();

                if (cells != null && cells.length > 0) {
                    String warning = JOptionPane.showInputDialog(mxResources
                            .get("enterWarningMessage"));

                    for (int i = 0; i < cells.length; i++) {
                        graphComponent.setCellWarning(cells[i], warning);
                    }
                } else {
                    JOptionPane.showMessageDialog(graphComponent,
                            mxResources.get("noCellSelected"));
                }
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class NewAction extends AbstractAction {
        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            BasicGraphEditor editor = getEditor(e);

            if (editor != null) {
                if (!editor.isModified()
                        || JOptionPane.showConfirmDialog(editor,
                        mxResources.get("loseChanges")) == JOptionPane.YES_OPTION) {
                    mxGraph graph = editor.getGraphComponent().getGraph();

                    // Check modified flag and display save dialog
                    mxCell root = new mxCell();
                    root.insert(new mxCell());
                    graph.getModel().setRoot(root);

                    editor.setModified(false);
                    editor.setCurrentFile(null);
                    editor.getGraphComponent().zoomAndCenter();
                }
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class ImportElementsAction extends AbstractAction {

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            GraphEditor editor = getEditor(e);
            if (editor != null) {
                //Create a file chooser
                JFileChooser elemFileChooser = new JFileChooser();
                elemFileChooser.setDialogTitle("Пожалуйста выберите набор элементов который вы хотите использовать");
                elemFileChooser.setAcceptAllFileFilterUsed(true);
                elemFileChooser.setFileFilter(new DefaultFileFilter(".elmt", ".elmt"));

                elemFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                //In response to a button click:
                int returnVal = elemFileChooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    //ClassLoader cl = this.getClass().getClassLoader();

                    String filePath = elemFileChooser.getSelectedFile().getPath();
                    //System.out.println(filePath);
                    InputStream in = null;
                    try {
                        in = new FileInputStream(filePath);
                    } catch (FileNotFoundException e1) {
                        System.out.println("Unable to find file");
                        e1.printStackTrace();
                    }
                    editor.importElements(in, editor.getGraphComponent());
                }
            }
        }
    }



    /**
     *
     */
    @SuppressWarnings("serial")
    public static class ImportAction extends AbstractAction {
        /**
         *
         */
        protected String lastDir;

        /**
         * Loads and registers the shape as a new shape in mxGraphics2DCanvas and
         * adds a new entry to use that shape in the specified palette
         *
         * @param palette The palette to add the shape to.
         * @param nodeXml The raw XML of the shape
         * @param path    The path to the directory the shape exists in
         * @return the string name of the shape
         */
        public static String addStencilShape(EditorPalette palette,
                                             String nodeXml, String path) {

            // Some editors place a 3 byte BOM at the start of files
            // Ensure the first char is a "<"
            int lessthanIndex = nodeXml.indexOf("<");
            nodeXml = nodeXml.substring(lessthanIndex);
            mxStencilShape newShape = new mxStencilShape(nodeXml);
            String name = newShape.getName();
            ImageIcon icon = null;

            if (path != null) {
                String iconPath = path + newShape.getIconPath();
                icon = new ImageIcon(iconPath);
            }

            // Registers the shape in the canvas shape registry
            mxGraphics2DCanvas.putShape(name, newShape);

            if (palette != null && icon != null) {
                palette.addTemplate(name, icon, "shape=" + name, null, 80, 80, null);
            }

            return name;
        }

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            BasicGraphEditor editor = getEditor(e);

            if (editor != null) {
                String wd = (lastDir != null) ? lastDir : System
                        .getProperty("user.dir");

                JFileChooser fc = new JFileChooser(wd);

                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                // Adds file filter for Dia shape import
                fc.addChoosableFileFilter(new DefaultFileFilter(".shape",
                        "Dia Shape " + mxResources.get("file") + " (.shape)"));

                int rc = fc.showDialog(null, mxResources.get("importStencil"));

                if (rc == JFileChooser.APPROVE_OPTION) {
                    lastDir = fc.getSelectedFile().getParent();

                    try {
                        if (fc.getSelectedFile().isDirectory()) {
                            EditorPalette palette = editor.insertPalette(fc
                                    .getSelectedFile().getName());

                            for (File f : fc.getSelectedFile().listFiles(
                                    new FilenameFilter() {
                                        public boolean accept(File dir,
                                                              String name) {
                                            return name.toLowerCase().endsWith(
                                                    ".shape");
                                        }
                                    })) {
                                String nodeXml = mxUtils.readFile(f
                                        .getAbsolutePath());
                                addStencilShape(palette, nodeXml, f.getParent()
                                        + File.separator);
                            }

                            JComponent scrollPane = (JComponent) palette
                                    .getParent().getParent();
                            editor.getLibraryPane().setSelectedComponent(
                                    scrollPane);

                            // FIXME: Need to update the size of the palette to force a layout
                            // update. Re/in/validate of palette or parent does not work.
                            //editor.getLibraryPane().revalidate();
                        } else {
                            String nodeXml = mxUtils.readFile(fc
                                    .getSelectedFile().getAbsolutePath());
                            String name = addStencilShape(null, nodeXml, null);

                            JOptionPane.showMessageDialog(editor, mxResources
                                    .get("stencilImported",
                                            new String[]{name}));
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class OpenAction extends AbstractAction {
        /**
         *
         */
        protected String lastDir;

        /**
         *
         */
        protected void resetEditor(BasicGraphEditor editor) {
            editor.setModified(false);
            editor.getUndoManager().clear();
            ClosableTabbedPane graphTabs = editor.getGraphTabs();
            for (int tabCount = 1; tabCount < graphTabs.getTabCount(); tabCount++) {
                graphTabs.remove(tabCount);
            }
            editor.getGraphComponent().zoomAndCenter();
        }

        /**
         * Reads XML+PNG format.
         */
        protected void openXmlPng(BasicGraphEditor editor, File file)
                throws IOException {
            Map<String, String> text = mxPngTextDecoder
                    .decodeCompressedText(new FileInputStream(file));

            if (text != null) {
                String value = text.get("mxGraphModel");

                if (value != null) {
                    Document document = mxXmlUtils.parseXml(URLDecoder.decode(
                            value, "UTF-8"));
                    mxCodec codec = new mxCodec(document);
                    codec.decode(document.getDocumentElement(), editor
                            .getGraphComponent().getGraph().getModel());
                    editor.setCurrentFile(file);
                    resetEditor(editor);

                    return;
                }
            }

            JOptionPane.showMessageDialog(editor,
                    mxResources.get("imageContainsNoDiagramData"));
        }

        /**
         * @throws IOException
         */
        protected void openGD(BasicGraphEditor editor, File file,
                              String gdText) {
            mxGraph graph = editor.getGraphComponent().getGraph();

            // Replaces file extension with .esd
            String filename = file.getName();
            filename = filename.substring(0, filename.length() - 4) + ".esd";

            if (new File(filename).exists()
                    && JOptionPane.showConfirmDialog(editor,
                    mxResources.get("overwriteExistingFile")) != JOptionPane.YES_OPTION) {
                return;
            }

            ((mxGraphModel) graph.getModel()).clear();
            mxGdCodec.decode(gdText, graph);
            editor.getGraphComponent().zoomAndCenter();
            editor.setCurrentFile(new File(lastDir + "/" + filename));
        }

        public static void openModel(File file, mxGraph graph) {
            org.jdom2.Document document = null;
            try {
                document = new SAXBuilder().build(file);
            } catch (JDOMException | IOException e) {
                e.printStackTrace();
            }

            Element mainModelElem = document.getRootElement().getChild("mainModel");
            String mainModelRawData = mainModelElem.getText();
            Document mainModelDocData = null;
            try {
                mainModelDocData = mxXmlUtils.parseXml(URLDecoder.decode(mainModelRawData, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mxCodec mainModelCodec = new mxCodec(mainModelDocData);
            Object mainModelValue = mainModelCodec.decode(mainModelDocData.getDocumentElement());
            mxGraphModel mainModel = (mxGraphModel) mainModelValue;


            Element macrosInnerModels = document.getRootElement().getChild("macrosModels");
            List<Element> macrosModelsElem = macrosInnerModels.getChildren();

            for (Element macrosModelElem : macrosModelsElem) {
                mxCell macrosCell = ((mxCell) mainModel.getCell(macrosModelElem.getChildText("id")));
                String macrosName = macrosModelElem.getChildText("name");

                String macrosModelRawData = macrosModelElem.getChildText("data");
                Document macrosModelDocData = null;
                try {
                    macrosModelDocData = mxXmlUtils.parseXml(URLDecoder.decode(macrosModelRawData, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mxCodec macrosModelCodec = new mxCodec(macrosModelDocData);
                Object macrosModelValue = macrosModelCodec.decode(macrosModelDocData.getDocumentElement());
                macrosCell.setValue(new MacroMxCellValue("M: " + macrosName, (mxGraphModel) macrosModelValue));
            }

            graph.setModel(mainModel);
        }

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            BasicGraphEditor editor = getEditor(e);

            if (editor != null) {
                if (!editor.isModified()
                        || JOptionPane.showConfirmDialog(editor,
                        mxResources.get("loseChanges")) == JOptionPane.YES_OPTION) {
                    mxGraph graph = editor.getGraphComponent().getGraph();

                    if (graph != null) {
                        String wd = (lastDir != null) ? lastDir : System
                                .getProperty("user.dir");

                        JFileChooser fc = new JFileChooser(wd);

                        fc.addChoosableFileFilter(new DefaultFileFilter(".esd",
                                mxResources.get("esd") + mxResources.get("file")
                                        + " (.esd)"));

                        // Adds file filter for supported file format
//						DefaultFileFilter defaultFilter = new DefaultFileFilter(
//								".mxe", mxResources.get("allSupportedFormats")
//										+ " (.mxe, .png, .vdx)")
//						{
//
//							public boolean accept(File file)
//							{
//								String lcase = file.getName().toLowerCase();
//
//								return super.accept(file)
//										|| lcase.endsWith(".png")
//										|| lcase.endsWith(".vdx");
//							}
//						};
//						fc.addChoosableFileFilter(defaultFilter);
//
//						fc.addChoosableFileFilter(new DefaultFileFilter(".mxe",
//								"mxGraph Editor " + mxResources.get("file")
//										+ " (.mxe)"));
//						fc.addChoosableFileFilter(new DefaultFileFilter(".png",
//								"PNG+XML  " + mxResources.get("file")
//										+ " (.png)"));
//
//						// Adds file filter for VDX import
//						fc.addChoosableFileFilter(new DefaultFileFilter(".vdx",
//								"XML Drawing  " + mxResources.get("file")
//										+ " (.vdx)"));
//
//						// Adds file filter for GD import
//						fc.addChoosableFileFilter(new DefaultFileFilter(".txt",
//								"Graph Drawing  " + mxResources.get("file")
//										+ " (.txt)"));
//
//						fc.setFileFilter(defaultFilter);

                        int rc = fc.showDialog(null,
                                mxResources.get("openFile"));

                        if (rc == JFileChooser.APPROVE_OPTION) {
                            lastDir = fc.getSelectedFile().getParent();

                            try {
                                if (fc.getSelectedFile().getAbsolutePath()
                                        .toLowerCase().endsWith(".png")) {
                                    openXmlPng(editor, fc.getSelectedFile());
                                } else if (fc.getSelectedFile().getAbsolutePath()
                                        .toLowerCase().endsWith(".txt")) {
                                    openGD(editor, fc.getSelectedFile(),
                                            mxUtils.readFile(fc
                                                    .getSelectedFile()
                                                    .getAbsolutePath()));
                                } else {
                                    openModel(fc.getSelectedFile(), graph);
                                    editor.setCurrentFile(fc
                                            .getSelectedFile());

                                    resetEditor(editor);
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(
                                        editor.getGraphComponent(),
                                        ex.toString(),
                                        mxResources.get("error"),
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }

/**
 *
 *//*
    @SuppressWarnings("serial")
    public static class ToggleAction extends AbstractAction {
        *//**
     *
     *//*
        protected String key;

        *//**
     *
     *//*
        protected boolean defaultValue;

        *//**
     * @param key
     *//*
        public ToggleAction(String key) {
            this(key, false);
        }

        *//**
     * @param key
     *//*
        public ToggleAction(String key, boolean defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            mxGraph graph = mxGraphActions.getGraph(e);

            if (graph != null) {
                graph.toggleCellStyles(key, defaultValue);
            }
        }
    }*/

/**
 *
 */
    /*@SuppressWarnings("serial")
    public static class SetLabelPositionAction extends AbstractAction {
        *//**
     *
     *//*
        protected String labelPosition, alignment;

        *//**
     * /*//* @param key
         *//*
        public SetLabelPositionAction(String labelPosition, String alignment) {
            this.labelPosition = labelPosition;
            this.alignment = alignment;
        }

        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            mxGraph graph = mxGraphActions.getGraph(e);

            if (graph != null && !graph.isSelectionEmpty()) {
                graph.getModel().beginUpdate();
                try {
                    // Checks the orientation of the alignment to use the correct constants
                    if (labelPosition.equals(mxConstants.ALIGN_LEFT)
                            || labelPosition.equals(mxConstants.ALIGN_CENTER)
                            || labelPosition.equals(mxConstants.ALIGN_RIGHT)) {
                        graph.setCellStyles(mxConstants.STYLE_LABEL_POSITION,
                                labelPosition);
                        graph.setCellStyles(mxConstants.STYLE_ALIGN, alignment);
                    } else {
                        graph.setCellStyles(
                                mxConstants.STYLE_VERTICAL_LABEL_POSITION,
                                labelPosition);
                        graph.setCellStyles(mxConstants.STYLE_VERTICAL_ALIGN,
                                alignment);
                    }
                } finally {
                    graph.getModel().endUpdate();
                }
            }
        }
    }*/

/**
 *
 /* *//*
    @SuppressWarnings("serial")
    public static class SetStyleAction extends AbstractAction {
        *//**
     *
     *//*
        protected String value;

        *//**
     * //	 * @param key
     *//*
        public SetStyleAction(String value) {
            this.value = value;
        }

        *//**
     *
     *//*
        public void actionPerformed(ActionEvent e) {
            mxGraph graph = mxGraphActions.getGraph(e);

            if (graph != null && !graph.isSelectionEmpty()) {
                graph.setCellStyle(value);
            }
        }
    }
*/
    /**
     *
     */
    @SuppressWarnings("serial")
    public static class KeyValueAction extends AbstractAction {
        /**
         *
         */
        protected String key, value;

        /**
         * @param key
         */
        public KeyValueAction(String key) {
            this(key, null);
        }

        /**
         * @param key
         */
        public KeyValueAction(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Возвращаем фигуру по координатам
         */
        public mxCell getCell(int x, int y, mxGraphComponent graphComponent) {
            Object cell = graphComponent.getCellAt(x, y);
            return (mxCell) cell;
        }

        // ЭТО ГАВНО-КОД = НУЖНО ПЕРЕПИСАТЬ (пока оставил т.к. работает)
        // Проверка не вертикально ли лежат направления
        public boolean isVertical(String oldStyle, String newStyle) {
            boolean result = false;
            if (newStyle.equals(mxConstants.DIRECTION_NORTH) ||
                    newStyle.equals(mxConstants.DIRECTION_SOUTH))
                if (oldStyle.equals(mxConstants.DIRECTION_NORTH) ||
                        oldStyle.equals(mxConstants.DIRECTION_SOUTH))
                    result = true;
            return result;
        }

        // Проверка не горизонтально ли лежат направления
        public boolean isHorizontal(String oldStyle, String newStyle) {
            boolean result = false;
            if (newStyle.equals(mxConstants.DIRECTION_EAST) ||
                    newStyle.equals(mxConstants.DIRECTION_WEST))
                if (oldStyle.equals(mxConstants.DIRECTION_EAST) ||
                        oldStyle.equals(mxConstants.DIRECTION_WEST))
                    result = true;
            return result;
        }

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            mxGraph graph = mxGraphActions.getGraph(e);
            mxStylesheet style = graph.getStylesheet();
            mxGraphComponent graphComponent = (mxGraphComponent) e
                    .getSource();
            Map<String, Map<String, Object>> vstyle =
                    new HashMap<String, Map<String, Object>>();
            Map<String, Object> wirestyle =
                    new HashMap<String, Object>();
            mxCell cell = (mxCell) graph.getSelectionCell();
            if (cell != null) {
                String cellStyle = cell.getStyle();
                Rectangle r = cell.getGeometry().getRectangle();
                if (cellStyle.equals("wireSouth") ||
                        cellStyle.equals("wireNorth") ||
                        cellStyle.equals("wireEast") ||
                        cellStyle.equals("wireWest")) {
                    vstyle = style.getStyles();
                    wirestyle = vstyle.get(cellStyle);

                    ElementModifier.movingPortsOnWire(wirestyle.get(mxConstants.STYLE_DIRECTION).toString(), value, cell);
                    if (!isVertical(wirestyle.get(mxConstants.STYLE_DIRECTION).toString(), value) &&
                            !isHorizontal((String) wirestyle.get(mxConstants.STYLE_DIRECTION).toString(), value)) {
                        cell.setGeometry(new mxGeometry(r.getX(), r.getY(), r.getHeight(), r.getWidth()));
                    }
                    if (value.equals(mxConstants.DIRECTION_WEST)) cellStyle = "wireWest";
                    else if (value.equals(mxConstants.DIRECTION_EAST)) cellStyle = "wireEast";
                    else if (value.equals(mxConstants.DIRECTION_NORTH)) cellStyle = "wireNorth";
                    else if (value.equals(mxConstants.DIRECTION_SOUTH)) cellStyle = "wireSouth";
                    cell.setStyle(cellStyle);
                }
                else if (cellStyle.equals("ismaTransform")) {
                    ElementModifier.rotateTransformerPorts(graph.getModel(), cell);
                }
                graphComponent.refresh();
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class PromptValueAction extends AbstractAction {
        /**
         *
         */
        protected String key, message;

        /**
         * @param key
         */
        public PromptValueAction(String key, String message) {
            this.key = key;
            this.message = message;
        }

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof Component) {
                mxGraph graph = mxGraphActions.getGraph(e);

                if (graph != null && !graph.isSelectionEmpty()) {
                    String value = (String) JOptionPane.showInputDialog(
                            (Component) e.getSource(),
                            mxResources.get("value"), message,
                            JOptionPane.PLAIN_MESSAGE, null, null, "");

                    if (value != null) {
                        if (value.equals(mxConstants.NONE)) {
                            value = null;
                        }

                        graph.setCellStyles(key, value);
                    }
                }
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class AlignCellsAction extends AbstractAction {
        /**
         *
         */
        protected String align;

        /**
         *
         */
        public AlignCellsAction(String align) {
            this.align = align;
        }

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            mxGraph graph = mxGraphActions.getGraph(e);

            if (graph != null && !graph.isSelectionEmpty()) {
                graph.alignCells(align);
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class AutosizeAction extends AbstractAction {
        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            mxGraph graph = mxGraphActions.getGraph(e);

            if (graph != null && !graph.isSelectionEmpty()) {
                Object[] cells = graph.getSelectionCells();
                mxIGraphModel model = graph.getModel();

                model.beginUpdate();
                try {
                    for (int i = 0; i < cells.length; i++) {
                        graph.updateCellSize(cells[i]);
                    }
                } finally {
                    model.endUpdate();
                }
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class ColorAction extends AbstractAction {
        /**
         *
         */
        protected String name, key;

        /**
         * @param key
         */
        public ColorAction(String name, String key) {
            this.name = name;
            this.key = key;
        }

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                mxGraph graph = graphComponent.getGraph();

                if (!graph.isSelectionEmpty()) {
                    Color newColor = JColorChooser.showDialog(graphComponent,
                            name, null);

                    if (newColor != null) {
                        graph.setCellStyles(key, mxUtils.hexString(newColor));
                    }
                }
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class BackgroundImageAction extends AbstractAction {
        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                String value = (String) JOptionPane.showInputDialog(
                        graphComponent, mxResources.get("backgroundImage"),
                        "URL", JOptionPane.PLAIN_MESSAGE, null, null,
                        "http://www.callatecs.com/images/background2.JPG");

                if (value != null) {
                    if (value.length() == 0) {
                        graphComponent.setBackgroundImage(null);
                    } else {
                        Image background = mxUtils.loadImage(value);
                        // Incorrect URLs will result in no image.
                        // TODO provide feedback that the URL is not correct
                        if (background != null) {
                            graphComponent.setBackgroundImage(new ImageIcon(
                                    background));
                        }
                    }

                    // Forces a repaint of the outline
                    graphComponent.getGraph().repaint();
                }
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class BackgroundAction extends AbstractAction {
        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                Color newColor = JColorChooser.showDialog(graphComponent,
                        mxResources.get("background"), null);

                if (newColor != null) {
                    graphComponent.getViewport().setOpaque(true);
                    graphComponent.getViewport().setBackground(newColor);
                }

                // Forces a repaint of the outline
                graphComponent.getGraph().repaint();
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class PageBackgroundAction extends AbstractAction {
        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                Color newColor = JColorChooser.showDialog(graphComponent,
                        mxResources.get("pageBackground"), null);

                if (newColor != null) {
                    graphComponent.setPageBackgroundColor(newColor);
                }

                // Forces a repaint of the component
                graphComponent.repaint();
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("serial")
    public static class StyleAction extends AbstractAction {
        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                mxGraph graph = graphComponent.getGraph();
                String initial = graph.getModel().getStyle(
                        graph.getSelectionCell());
                String value = (String) JOptionPane.showInputDialog(
                        graphComponent, mxResources.get("style"),
                        mxResources.get("style"), JOptionPane.PLAIN_MESSAGE,
                        null, null, initial);

                if (value != null) {
                    graph.setCellStyle(value);
                }
            }
        }
    }

    public static class CreateMacrosAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Создается макрос");
            GraphEditor editor = getEditor(e);


            String macrosName = JOptionPane.showInputDialog(
                    null,
                    mxResources.get("newMacrosDialogLabel"),
                    mxResources.get("createMacros"),
                    JOptionPane.PLAIN_MESSAGE);

            //If a string was returned, say so.
            if ((macrosName != null) && (macrosName.length() > 0)) {
                if (macrosName.contains(":") || macrosName.contains("*")) {
                    JOptionPane.showMessageDialog(
                            null,
                            mxResources.get("macrosSymbolError"),
                            mxResources.get("error"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if (!editor.getMacrosPalette().exists(macrosName)) {
                    System.out.println("Macros name: " + macrosName);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            mxResources.get("macrosNameTakenError"),
                            mxResources.get("error"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            else {
                //If you're here, the return value was null/empty.
                System.out.println("Macros cancelled");
                return;
            }
            mxIGraphModel model = editor.getGraphComponent().getGraph().getModel();

            mxCell cell = new mxCell(new MacroMxCellValue("М: " + macrosName, (mxGraphModel) model), new mxGeometry(0, 0, 60, 60),
                    "macros");
            saveMacrosInXML(model, macrosName, cell);
            editor.addNewGraphComponent(macrosName + "(Шаблон)", cell);
            //copy cells to new tab
            //macrosComponent.importCells(cells, 0, 0, null, null);

            BufferedImage img = null;
            URL imgURL = getClass().getResource("/images/macros.png");
            try {
                img = ImageIO.read(imgURL);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            editor.getMacrosPalette()
                    .addMacros(cell, new ImageIcon(img), null, editor.getGraphComponent().getGraph());

        }
    }

    @SuppressWarnings("serial")
    public static class EditAction extends AbstractAction {
        /**
         *
         */
        private static mxCell cell;

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof mxGraphComponent) {
                mxGraphComponent graphComponent = (mxGraphComponent) e
                        .getSource();
                mxGraph graph = graphComponent.getGraph();
                cell = (mxCell) graph.getSelectionCell();
                if (cell == null) {
                    return;
                }
                String style = cell.getStyle();
                //String[] styleParts = style.split(":");

                if (style.equals("macros")) {
                    GraphEditor editor = getEditor(e);
                    if (BasicGraphEditor.getCellTabMap().containsValue(cell)) {
                        editor.selectTab(cell);
                    }
                    else {
                        mxGraphComponent macrosComponent = editor.addNewGraphComponent(
                                ((MacroMxCellValue) cell.getValue()).getMacroDisplayName().split(": ")[1] +
                                        " ID:" + cell.getId(), cell);
                        //TODO Внутренняя схема шаблона применяется ко всем последующим
                        // добавленным элементам без сохранения
                        //TODO при запуске решателя сохранить все вкладки
                        //TODO Обновление GUI во вкладках макросов работает некорректно
                        //TODO Undo redo во вкладках макроса не работают
                        //TODO Не к месту появляются звездочки в именах вкладок
                        //OPTIMIZATION
                        //TODO Instead of storing ports in xml it would be better to install them based
                        //on scheme inside macros
                            /*Object[] blocks = mxGraphModel.getChildren(
                                    (mxGraphModel) cell.getParams(), ((mxGraphModel) cell.getParams()).getRoot());*/
                        macrosComponent.getGraph().setModel(((MacroMxCellValue) cell.getValue()).getModel());
                        editor.selectTab(cell);
                    }
                    return;
                }

                /*//This is a port
                if (cell.isConnectable()) {
                    if (!cell.getParent().getStyle().startsWith("wire")) {
                        return;
                    }
                    BasicGraphEditor editor = getEditor(e);
                    String portName = JOptionPane.showInputDialog(null,
                            mxResources.get("portEditDialogLabel"),
                            cell.getParams().toString());

                    //If a string was returned, say so.
                    if ((portName != null) && (portName.length() > 0)) {
                        cell.setParams(portName);
                        return;
                    }
                    else {
                        //If you're here, the return value was null/empty.
                        System.out.println("Port edit cancelled");
                        return;
                    }
                }*/
                //Универсальный диалог редактирования элементов
                EditDialog dialog = new EditDialog();

                Integer schemeCount = PaletteElementsSchemeData.getSchemeCount(style);

                if (schemeCount != 0) {
                    JComboBox box = new JComboBox<ComboBoxObj>();

                    for (int i = 1; i <= schemeCount; i++)
                        box.addItem(new ComboBoxObj("Схема " + i));

                    dialog.setComboBox(box);
                }

                dialog.setData(style, (new ParseToValues(cell.getValue().toString()))
                        .getScheme());

                dialog.pack();
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                dialog.setLocation(dim.width / 2 - dialog.getSize().width / 2,
                        dim.height / 2 - dialog.getSize().height / 2);
                dialog.setVisible(true);

            }
        }

        //В поле cell.value данные хранятся по следующему принципу: Имя выбранной схемы + данные таблицы параметров
        //+ данные таблицы условий.
        public static void writeInGraph(JTable table, JTable table2, String scheme) {
            cell.setValue(scheme + " " + ParseToString.parsParamsToString(table)
                    + ParseToString.parsConditionsToString(table2));
        }

        //Метод возвращает таблицы параметров и условий
        public static String getData() {
            return cell.getValue().toString();
        }

        //Cчитывает данные из дефолт таблицы и сразу записывает их в элемент.
        public static void getDefaultTableValues(String scheme, String style, EditDialog dialog) {
            PaletteElementsSchemeData.current = scheme;

            JTable table = PaletteElementsSchemeData.initTParams(style);
            JTable table2 = PaletteElementsSchemeData.initIConditions(style);

            //Запись значения в граф
            writeInGraph(table, table2, scheme);
            dialog.setData(style, scheme);
        }
    }

    /**
     * Класс, который будет отвечать за добавление портов на шину
     * Процесс добавления точки происходит по следующей схеме:
     * 1. Вытягиваем шину на константу (хотя может быть случай, когда не нужно этого делать)
     * 2. Перерисовка старых точек
     * 3. Добавление новой точки или удаление последней добавленной
     */
    @SuppressWarnings("serial")
    public static class EditPortAction extends AbstractAction {
        // Константа на которую вытянуть шину.
        public int EXTENDING = 25;

        /**
         * true = добавить
         * false = удалить
         */
        protected boolean removeOrAdd;

        /**
         *
         */
        public EditPortAction(boolean removeOrAdd) {
            this.removeOrAdd = removeOrAdd;
        }

        /**
         * Вытягиваем или укорачиваем шину
         */
        public void resizeTheWire(mxCell wire, String direction) {
            Rectangle r = wire.getGeometry().getRectangle();
            if (direction.equals(mxConstants.DIRECTION_NORTH) ||
                    direction.equals(mxConstants.DIRECTION_SOUTH))
                wire.setGeometry(new mxGeometry(r.getX(), r.getY(), r.getWidth(), r.getHeight() + EXTENDING * (removeOrAdd ? 1 : -1)));
            else
                wire.setGeometry(new mxGeometry(r.getX(), r.getY(), r.getWidth() + EXTENDING * (removeOrAdd ? 1 : -1), r.getHeight()));
        }

        /**
         * Получаем направление шины
         */
        public String getWireDirection(mxCell wire, mxGraph graph) {
            mxStylesheet style = graph.getStylesheet();
            Map<String, Map<String, Object>> vstyle =
                    new HashMap<String, Map<String, Object>>();
            Map<String, Object> wirestyle =
                    new HashMap<String, Object>();
            String wStyle = wire.getStyle();
            vstyle = style.getStyles();
            wirestyle = vstyle.get(wStyle);
            Rectangle r = wire.getGeometry().getRectangle();
            return wirestyle.get(mxConstants.STYLE_DIRECTION).toString();
        }

        /**
         *
         */
        public void actionPerformed(ActionEvent e) {
            mxGraph graph = mxGraphActions.getGraph(e);
            mxGraphComponent graphComponent = (mxGraphComponent) e
                    .getSource();

            mxCell cell = (mxCell) graph.getSelectionCell();
            if (cell != null) {
                String direction = getWireDirection(cell, graph);
                //1. Вытягиваем шину на константу (хотя может быть случай, когда не нужно этого делать)
                resizeTheWire(cell, direction);
                //2. Перерисовка старых точек
                ElementModifier.repaintOldPortsOnWire(cell, direction, removeOrAdd);
                //3. Добавление новой точки или удаление последней добавленной
                if (removeOrAdd) ElementModifier.addNewPortOnWire(cell, graph, direction);
                else ElementModifier.deletePortOnWire(cell, graph, direction);

                graphComponent.refresh();
            }
        }
    }
}

