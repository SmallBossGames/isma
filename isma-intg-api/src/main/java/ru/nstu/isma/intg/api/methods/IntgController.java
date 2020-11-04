package ru.nstu.isma.intg.api.methods;

import java.io.Serializable;

/**
 * @author Mariya Nasyrova
 * @since 25.08.14
 */
public abstract class IntgController implements Serializable {

    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}