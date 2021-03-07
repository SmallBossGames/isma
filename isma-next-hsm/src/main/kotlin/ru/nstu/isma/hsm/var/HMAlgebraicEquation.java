package ru.nstu.isma.hsm.var;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:28
 */
public class HMAlgebraicEquation extends HMEquation implements Serializable {
    public HMAlgebraicEquation(String code) {
        this.code = code;
    }
}
