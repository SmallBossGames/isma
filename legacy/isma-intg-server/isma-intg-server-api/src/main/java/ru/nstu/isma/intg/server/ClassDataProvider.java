package ru.nstu.isma.intg.server;

import java.io.IOException;

public interface ClassDataProvider {

    byte[] getClassData(String name) throws ClassNotFoundException, IOException;

}
