/**
 * $Id: GraphEditor.java,v 1.16 2012-10-29 10:49:06 david Exp $
 * Copyright (c) 2006-2012, JGraph Ltd */
package com.swing;

import com.editor.utils.Scheme;
import com.editor.utils.values.MacroMxCellValue;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.*;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.*;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.swing.editor.BasicGraphEditor;
import com.swing.editor.EditorMenuBar;
import com.swing.editor.EditorPalette;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ru.nstu.isma.ui.common.OutputAware;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class GraphEditor extends BasicGraphEditor implements OutputAware
{
	/**
	 * Test change
	 */
	private static final long serialVersionUID = -4601740824088314699L;

	/**
	 * Holds the shared number formatter.
	 * 
	 * @see NumberFormat#getInstance()
	 */
	public static final NumberFormat numberFormat = NumberFormat.getInstance();

	/**
	 * Holds the URL for the icon to be used as a handle for creating new
	 * connections. This is currently unused.
	 */
	public static URL url = null;

	//GraphEditor.class.getResource("/com/mxgraph/examples/swing/images/connector.gif");
    private EditorPalette ewPalette = null;

	private EditorPalette macrosPalette = null;

    public static Map getGeneratorLabels() {
        return generatorLabels;
    }

    public static void addGeneratorLabel(int id, String label) {
        generatorLabels.put(id, label);
    }

    private static Map<Integer, String> generatorLabels = new HashMap<>();

	static {
        mxResources.add("editor_ru");
    }

	public GraphEditor()
	{
        this(mxResources.get("caption"), new CustomGraphComponent(new CustomGraph()));
	}

	/**
	 * 
	 */
	public GraphEditor(String appTitle, mxGraphComponent component)
	{
		super(appTitle, component);

		mxGraph graph = component.getGraph();

        //Настройки редактора
        graph.setLabelsVisible(true);
        graph.setDisconnectOnMove(false);

		graph.setAllowDanglingEdges(false);
        //graph.getView();


		/*
		 * Элементы редактора
		 */
        //TODO remake port adding mechanism to be more flexible
        ClassLoader cl = this.getClass().getClassLoader();
        java.io.InputStream in = cl.getResourceAsStream("elem/energetics.elmt");
		java.io.InputStream macr = cl.getResourceAsStream("elem/macros.mcr");
        /*File elements = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() +
                "elem/energetics.elmt");*/
		if (in != null) {
			importElements(in, component);
            //addMacrosInOutElements(component);
		}

		if (macr != null) {
			importMacroses(macr, component);
		}
		else {
			macrosPalette = insertPalette(mxResources.get("macrosTab"), 1);
		}

	}

  	public void importMacroses(java.io.InputStream macrosFile, mxGraphComponent component) {
		org.jdom2.Document document = null;

		try {
			document = new SAXBuilder().build(macrosFile);
		} catch (JDOMException | IOException e) {
			JOptionPane.showMessageDialog(null, mxResources.get("error"), "Не удается обработать файл",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		assert document != null;
		Element rootNode = document.getRootElement();
		List list = rootNode.getChildren("macros");

		macrosPalette = insertPalette(mxResources.get("macrosTab"), 1);

		for (Object aMacros : list) {

			Element node = (Element) aMacros;

			String name = node.getChildText("name");
			if (name == null) throw new NullPointerException("Ошибка при получении имени элемента");
			String data = node.getChildText("data");
			if (data == null) throw new NullPointerException("Отсутствуют данные в файле " + name);

			Element ports = node.getChild("ports");
			//if (ports == null) throw new NullPointerException("Отсутствуют данные в файле " + name);
			List<Element> portsList = ports.getChildren("port");
			String portData;
			List<mxCell> portCells = new ArrayList<mxCell>();
			Document portDoc = null;
			for (Element port : portsList) {
				portData = port.getText();
				try {
					portDoc = mxXmlUtils.parseXml(URLDecoder.decode(
                            portData, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				mxCodec portCodec = new mxCodec(portDoc);
				Object portValue = portCodec.decode(portDoc.getDocumentElement());
				portCells.add((mxCell) portValue);
			}

			Document dataDoc = null;
			try {
				dataDoc = mxXmlUtils.parseXml(URLDecoder.decode(
                        data, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			mxCodec dataCodec = new mxCodec(dataDoc);
			//mxCodec appearCodec = new mxCodec(appearDoc);
			Object dataValue = dataCodec.decode(dataDoc.getDocumentElement());
			//Object appearanceValue = appearCodec.decode(appearDoc.getDocumentElement());

			BufferedImage img = null;
			URL imgURL = getClass().getResource("/images/macros.png");
			try {
				img = ImageIO.read(imgURL);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			macrosPalette.addMacros(
					name,
					new ImageIcon(img),
					"macros", new MacroMxCellValue("М: " + name, ((mxGraphModel) dataValue)), portCells, 60, 60,
                    component.getGraph());

		}

		//TODO Continue with ports
	}

    //Imports elements from a file to palette
    public void importElements(java.io.InputStream elemFile, mxGraphComponent graphComponent) {
        org.jdom2.Document document = null;
        try {
            document = new SAXBuilder().build(elemFile);
        } catch (JDOMException | IOException e) {
            JOptionPane.showMessageDialog(null, "Ошибка", "Не удается обработать файл",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        assert document != null;
        Element rootNode = document.getRootElement();
        List list = rootNode.getChildren("item");

        //Очистка палитры от предыдущих элементов и создание новой
        if (ewPalette != null)
            removePalette();
		createNewPalette(mxResources.get("electronicWorkbench"), graphComponent.getGraph());

        for (Object aList : list) {

            Element node = (Element) aList;

            String name = node.getChildText("name");
            if (name == null) throw new NullPointerException("Ошибка при получении имени элемента");
            String style = node.getChildText("style");
            if (style == null) throw new NullPointerException("Ошибка при получении стиля элемента");
            BufferedImage img = decodeImage(node.getChildText("img"));
            if (img == null) throw new NullPointerException("Ошибка при декодировании изображения");
            Integer width = Integer.valueOf(node.getChildText("width"));
            Integer height = Integer.valueOf(node.getChildText("height"));
            //TODO There is also a canvasimg tag in XML, but it seems that the only way to make
            //JXGraph aknowledge this images is to write path to images in default-style.xml manually
            String type = node.getChildText("type");
            if (type == null) throw new NullPointerException("Ошибка при получении типа элемента");

            List<Scheme> schemeList = null;
            Element schemes = node.getChild("schemes");
            if (schemes != null) {
                List mSchemes = schemes.getChildren("scheme");
                schemeList = new ArrayList<>();

                for (Object aScheme : mSchemes) {
                    Element scheme = (Element) aScheme;
                    String schemeName = scheme.getChildText("name");
                    if (schemeName == null) throw new NullPointerException("Ошибка при получении имени схемы");
                    String imgText = scheme.getChildText("img");
                    ImageIcon schemeIcon = null;
                    if (imgText != null) {
                        schemeIcon = new ImageIcon(decodeImage(imgText));
                    }

                    Element initConds = scheme.getChild("initialconds");
                    List<Object[]> condsArray = new ArrayList<>();
                    if (initConds != null) {
                        List conds = initConds.getChildren("cond");
                        for (Object cond : conds) {
                            Element condElem = (Element) cond;
                            String condName = condElem.getChildText("name");
                            Double value = Double.valueOf(condElem.getChildText("value"));
                            condsArray.add(new Object[]{condName, value});

                    } }

                    Element params = scheme.getChild("params");
                    List<Object[]> paramsArray = new ArrayList<>();
                    if (params != null) {
                        List paramsList = params.getChildren("param");
                        for (Object param : paramsList) {
                            Element paramElem = (Element) param;
                            String paramName = paramElem.getChildText("name");
                            Double value = Double.valueOf(paramElem.getChildText("value"));
                            paramsArray.add(new Object[]{paramName, value});

                    } }



                    Scheme schemeObj = new Scheme(schemeName, paramsArray.toArray(new Object[paramsArray.size()][]),
                            condsArray.toArray(new Object[condsArray.size()][]), schemeIcon);
                    schemeList.add(schemeObj);
                }
            }

            //TODO Continue with ports

            switch (type) {
                case "edge":
                    ewPalette
                            .addEdgeTemplate(
                                    name,
                                    new ImageIcon(img),
                                    style, schemeList, width, height, graphComponent.getGraph());
                    break;
                case "vertex":
                    ewPalette
                            .addTemplate(
                                    name,
                                    new ImageIcon(img),
                                    style, schemeList, width, height, graphComponent.getGraph());
                    break;
                default:
                    throw new IllegalArgumentException("Неправильное значение в теге \"type\"");
            }
        }
    }


	private void createNewPalette(String name, final mxGraph graph) {
		ewPalette = insertPalette(name);

		ewPalette.addListener(mxEvent.SELECT, new mxEventSource.mxIEventListener()
		{
			public void invoke(Object sender, mxEventObject evt)
			{
				Object tmp = evt.getProperty("transferable");

				if (tmp instanceof mxGraphTransferable)
				{
					mxGraphTransferable t = (mxGraphTransferable) tmp;
					Object cell = t.getCells()[0];

					if (graph.getModel().isEdge(cell))
					{
						((CustomGraph) graph).setEdgeTemplate(cell);
					}
				}
			}
		});

	}

    private BufferedImage decodeImage(String imgText) {
        // DECODING
        byte[] bytes;
        try {
            bytes = Base64.decode(imgText);
        } catch (Base64DecodingException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EditorPalette getPalette() {
        return ewPalette;
    }

	public EditorPalette getMacrosPalette() {
		return macrosPalette;
	}

    @Override
    public RSyntaxTextArea getOutputArea() {
        return null;
    }

    /**
	 * 
	 */
	public static class CustomGraphComponent extends mxGraphComponent
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6833603133512882012L;

		/**
		 * 
		 * @param graph
		 */
		public CustomGraphComponent(mxGraph graph)
		{
			super(graph);

			// Sets switches typically used in an editor
			setPageVisible(false);

            // убрать сетку
			setGridVisible(false);
			setToolTips(true);

			//getConnectionHandler().setCreateTarget(true);
			// Loads the defalt stylesheet from an external file
			mxCodec codec = new mxCodec();

			/*Document doc = mxUtils.loadDocument(getClass().getProtectionDomain()
                    .getCodeSource().getLocation().getPath() + "/default-style.xml");*/

            ClassLoader cl = this.getClass().getClassLoader();
            String fileName = "default-style.xml";
            java.io.InputStream in = cl.getResourceAsStream(fileName);

            Document doc = null;
            try {
                doc = XMLUtils.readXml(in);
            } catch (SAXException | IOException | ParserConfigurationException e) {
                System.out.println("Unable to load " + fileName);
                e.printStackTrace();
                System.exit(2);
            }

            codec.decode(doc.getDocumentElement(), graph.getStylesheet());

			// Sets the background to white
			getViewport().setOpaque(true);
			getViewport().setBackground(Color.WHITE);
		}




		/**
		 * Overrides drop behaviour to set the cell style if the target
		 * is not a valid drop target and the cells are of the same
		 * type (eg. both vertices or both edges). 
		 */
		public Object[] importCells(Object[] cells, double dx, double dy,
				Object target, Point location)
		{
			if (target == null && cells.length == 1 && location != null)
			{
				target = getCellAt(location.x, location.y);

				if (target instanceof mxICell && cells[0] instanceof mxICell)
				{
					mxICell targetCell = (mxICell) target;
					mxICell dropCell = (mxICell) cells[0];

					if (targetCell.isVertex() == dropCell.isVertex()
							|| targetCell.isEdge() == dropCell.isEdge())
					{
						mxIGraphModel model = graph.getModel();
						model.setStyle(target, model.getStyle(cells[0]));
						graph.setSelectionCell(target);

						return null;
					}
				}
			}

			return super.importCells(cells, dx, dy, target, location);
		}

	}

	/**
	 * A graph that creates new edges from a given template edge.
	 */
	public static class CustomGraph extends mxGraph
	{
		/**
		 * Holds the edge to be used as a template for inserting new edges.
		 */
		protected Object edgeTemplate;

		/**
		 * Custom graph that defines the alternate edge style to be used when
		 * the middle control point of edges is double clicked (flipped).
		 */
		public CustomGraph()
		{
			//setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=horizontal");
		}


		/**
		 * Sets the edge template to be used to inserting edges.
		 */
		public void setEdgeTemplate(Object template)
		{
			edgeTemplate = template;
		}

		/**
		 * Prints out some useful information about the cell in the tooltip.
		 */
		public String getToolTipForCell(Object cell)
		{
			String tip = "<html>";
			mxGeometry geo = getModel().getGeometry(cell);
			mxCellState state = getView().getState(cell);

			if (getModel().isEdge(cell))
			{
				tip += "points={";

				if (geo != null)
				{
					List<mxPoint> points = geo.getPoints();

					if (points != null)
					{
						Iterator<mxPoint> it = points.iterator();

						while (it.hasNext())
						{
							mxPoint point = it.next();
							tip += "[x=" + numberFormat.format(point.getX())
									+ ",y=" + numberFormat.format(point.getY())
									+ "],";
						}

						tip = tip.substring(0, tip.length() - 1);
					}
				}

				tip += "}<br>";
				tip += "absPoints={";

				if (state != null)
				{

					for (int i = 0; i < state.getAbsolutePointCount(); i++)
					{
						mxPoint point = state.getAbsolutePoint(i);
						tip += "[x=" + numberFormat.format(point.getX())
								+ ",y=" + numberFormat.format(point.getY())
								+ "],";
					}

					tip = tip.substring(0, tip.length() - 1);
				}

				tip += "}";
			}
			else
			{
				//Show name of the macros in tooltip
                mxCell mCell = (mxCell) cell;
				if (mCell.getStyle().equals("macros")) {
					return " Id:" + ((mxCell) cell).getId();
				}

                else if (mCell.getStyle().equals("ismaGenerator")) {
                    String nodeNum = generatorLabels.get(mCell.hashCode());
                    if (nodeNum != null) {
                        tip = "N: " + nodeNum;
                        return tip;
                    }
                }
				tip += "geo=[";

				if (geo != null)
				{
					tip += "x=" + numberFormat.format(geo.getX()) + ",y="
							+ numberFormat.format(geo.getY()) + ",width="
							+ numberFormat.format(geo.getWidth()) + ",height="
							+ numberFormat.format(geo.getHeight());
				}

				tip += "]<br>";
				tip += "state=[";

				if (state != null)
				{
					tip += "x=" + numberFormat.format(state.getX()) + ",y="
							+ numberFormat.format(state.getY()) + ",width="
							+ numberFormat.format(state.getWidth())
							+ ",height="
							+ numberFormat.format(state.getHeight());
				}

				tip += "]";
			}

			mxPoint trans = getView().getTranslate();

			tip += "<br>scale=" + numberFormat.format(getView().getScale())
					+ ", translate=[x=" + numberFormat.format(trans.getX())
					+ ",y=" + numberFormat.format(trans.getY()) + "]";
			tip += "</html>";

			return tip;
		}

		/**
		 * Overrides the method to use the currently selected edge template for
		 * new edges.
		 * 
		// * @param graph
		 * @param parent
		 * @param id
		 * @param value
		 * @param source
		 * @param target
		 * @param style
		 * @return
		 */
		public Object createEdge(Object parent, String id, Object value,
				Object source, Object target, String style)
		{
			if (edgeTemplate != null)
			{
				mxCell edge = (mxCell) cloneCells(new Object[] { edgeTemplate })[0];
				edge.setId(id);

				return edge;
			}

			return super.createEdge(parent, id, value, source, target, style);
		}

	}


    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }


    /**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            setUIFont (new javax.swing.plaf.FontUIResource("Calibri", Font.PLAIN, 14));
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

        String jdkPath = System.getProperty("java.home");
        if (jdkPath == null) {
            throw new RuntimeException("java.home property is not defined. ISMA will exit");
        }

		mxSwingConstants.SHADOW_COLOR = Color.LIGHT_GRAY;
		mxConstants.W3C_SHADOWCOLOR = "#000000";

		GraphEditor editor = new GraphEditor();


		editor.createFrame(new EditorMenuBar(editor)).setVisible(true);
	}
}
