package ru.nstu.isma.intg.server.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.input.ClassLoaderObjectInputStream;

public final class BinaryObjectDeserializer {
    private BinaryObjectDeserializer() {
    }

    public static <T> T deserialize(Class<T> targetClass, byte[] serializedObject, ClassLoader classLoader)
            throws IOException, ClassNotFoundException {
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject);
                ClassLoaderObjectInputStream ois = new ClassLoaderObjectInputStream(classLoader, bis)
        ) {
            return targetClass.cast(ois.readObject());
        }
    }

}
