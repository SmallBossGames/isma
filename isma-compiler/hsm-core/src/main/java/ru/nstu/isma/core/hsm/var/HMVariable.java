package ru.nstu.isma.core.hsm.var;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:18
 */
public class HMVariable implements Serializable {
    protected String code;

    public HMVariable() {
    }

    public HMVariable(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
