package com.editor.utils.parser;

import java.util.HashMap;
import java.util.Scanner;

public class ParseToValues {

    private HashMap<Integer, ValuesForParametersAndConditions> hashMapForValuesParameters = new HashMap<Integer, ValuesForParametersAndConditions>();
    private HashMap<Integer, ValuesForParametersAndConditions> hashMapForValuesConditions = new HashMap<Integer, ValuesForParametersAndConditions>();

    private String scheme = "";



    public ParseToValues(String inputString) {
        boolean isParameter = false;
        String boofName;
        String boofValue;
        Scanner scanner = new Scanner(inputString);
        int indexForParameters = 0;
        int indexForConditions = 0;
        for (int i = 0; scanner.hasNext(); i++) {
            boofName = scanner.next();
            if (boofName.equals("Схема")) {
                boofName = boofName + " " + scanner.next();
                scheme = boofName;
            }
            else if (boofName.equals("Parameters")) {
                isParameter = true;
            } else if (boofName.equals("Conditions")) {
                isParameter = false;
            } else if (isParameter) {
                boofValue = scanner.next();
                if (boofValue.equals("=")) {
                    boofValue = scanner.next();
                }
                hashMapForValuesParameters.put(indexForParameters, new ValuesForParametersAndConditions(boofName,
                        Double.parseDouble(boofValue)));
                indexForParameters++;
            } else {
                boofValue = scanner.next();
                if (boofValue.equals("=")) {
                    boofValue = scanner.next();
                }
                hashMapForValuesConditions.put(indexForConditions, new ValuesForParametersAndConditions(boofName,
                        Double.parseDouble(boofValue)));
                indexForConditions++;
            }
        }
    }


    public void printParameters() {
        System.out.println("Parameters: ");
        for (int i = 0; i < hashMapForValuesParameters.size(); i++) {
            System.out.println("Name = " + hashMapForValuesParameters.get(i).name + " Value = " + hashMapForValuesParameters.get(i).value);
        }
    }

    public void printConditions() {
        System.out.println("Conditions: ");
        for (int i = 0; i < hashMapForValuesConditions.size(); i++) {
            System.out.println("Name = " + hashMapForValuesConditions.get(i).name + " Value = " + hashMapForValuesConditions.get(i).value);
        }
    }

    public HashMap<Integer, ValuesForParametersAndConditions> getHashMapForValuesParameters() {
        return hashMapForValuesParameters;
    }

    public HashMap<Integer, ValuesForParametersAndConditions> getHashMapForValuesConditions() {
        return hashMapForValuesConditions;
    }

    public String getScheme() {
        return scheme;
    }


}
