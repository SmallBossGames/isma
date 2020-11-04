package ru.nstu.isma.intg.demo.app.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public abstract class FormController<T> {

    private List<T> listeners = new ArrayList<T>();

    public void addListener(T listener) {
        listeners.add(listener);
    }

    public void removeListener(T listener) {
        listeners.remove(listener);
    }

    public List<T> getListeners() {
        return listeners;
    }
}
