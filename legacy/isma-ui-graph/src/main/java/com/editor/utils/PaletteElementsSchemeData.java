package com.editor.utils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaletteElementsSchemeData {

    private static Map<String, List<Scheme>> paletteElements = new HashMap<String, List<Scheme>>();

    public static String[] COLUMN_NAMES = {"Название", "Значение"};
    public static final Object[][] DATA_EMPTY = null;

    public static String current = "Схема замещения линии электропередач";

    public static void addNewPaletteElementData(String name, List<Scheme> schemeList) {
        paletteElements.put(name, schemeList);
    }

    public static Integer getSchemeCount(String itemName) {
        List<Scheme> elementSchemes = paletteElements.get(itemName);
        if (elementSchemes == null)
            return 0;
        else return elementSchemes.size();
    }

	public static JTable initTParams(String itemName){

        List<Scheme> mElementSchemes = paletteElements.get(itemName);
        if (mElementSchemes != null)
        for (Scheme scheme : mElementSchemes) {
            if (scheme.getName().equals(current)) {
                JTable table = new JTable(scheme.getParams(), COLUMN_NAMES);
                table.setPreferredScrollableViewportSize(new Dimension(500, 70));
                table.setFillsViewportHeight(true);
                return table;
            }
        }

        return new JTable();
	}
	
	/**
	 * Инициализация таблицы начальных условий
	 */
	
	public static JTable initIConditions(String itemName){

        List<Scheme> mElementSchemes = paletteElements.get(itemName);
        if (mElementSchemes != null)
        for (Scheme scheme : mElementSchemes) {
            if (scheme.getName().equals(current)) {
                JTable table = new JTable(scheme.getConds(), COLUMN_NAMES);
                table.setPreferredScrollableViewportSize(new Dimension(500, 70));
                table.setFillsViewportHeight(true);
                return table;
            }
        }

        return new JTable();
	}
	
	/**
	 * Рисунок схемы замещения
	 */
	
	public static JLabel getImage(String itemName){
		ImageIcon defaultImage = new ImageIcon(PaletteElementsSchemeData.class
                .getResource("/images/image.gif"));

        List<Scheme> mElementSchemes = paletteElements.get(itemName);
        for (Scheme scheme : mElementSchemes) {
            if (scheme.getName().equals(current)) {
                return new JLabel(scheme.getSchemeImg());
            }
        }

        return new JLabel(defaultImage);
    }

}

