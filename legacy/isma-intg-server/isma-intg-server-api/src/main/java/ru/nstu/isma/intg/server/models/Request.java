package ru.nstu.isma.intg.server.models;

import java.io.Serializable;

/**
 * @author Maria Nasyrova
 * @since 29.08.2015
 */
public interface Request<T> extends Serializable {

    BinaryObject<T> getBinaryObject();

}
