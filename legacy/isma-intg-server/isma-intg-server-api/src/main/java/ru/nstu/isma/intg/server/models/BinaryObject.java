package ru.nstu.isma.intg.server.models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;

public class BinaryObject<T> implements Serializable {

    private byte[] bytes;

    /** Конструктор, необходимый десериализатору. */
    @SuppressWarnings("unused")
    private BinaryObject() {
    }

    public BinaryObject(byte[] bytes) {
		Preconditions.checkNotNull(bytes, "bytes cannot be null");

        this.bytes = bytes;
    }

    public static <T> BinaryObject<T> serialize(T object) throws IOException {
        Preconditions.checkNotNull(object, "object cannot be null");

        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            oos.writeObject(object);
            return new BinaryObject<>(bos.toByteArray());
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

}
