package ru.nstu.isma.hsm.common;

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
public interface IndexProvider {

    String getDifferentialArrayCode(String code);

    String getAlgebraicArrayCode(String code);

    String getAlgebraicArrayCodeForDifferentialEquation(String aeCode);

}
