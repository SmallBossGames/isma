package ru.nstu.isma.intg.demo.app.models;

import ru.nstu.isma.intg.lib.IntgMethodType;

public final class MethodModelFactory {

    private MethodModelFactory() {
    }

    public static MethodModel createMethodModel(IntgMethodType methodType) {
        MethodModel model = new MethodModel();
        model.setType(methodType);
        model.setAccuracy(0.01);
        return model;
    }

}
