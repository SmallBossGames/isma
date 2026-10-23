package ru.nstu.isma.intg.server.client.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.google.common.io.ByteStreams;
import ru.nstu.isma.intg.server.ClassDataProvider;

public class ClientClassDataProvider implements ClassDataProvider, Serializable {

	private static ClassLoader classLoader;

	/**
	 * @param classLoader
	 * 		cannot ne <tt>null</tt>.
	 */
    public ClientClassDataProvider(ClassLoader classLoader) {
        if (classLoader == null) {
			throw new IllegalArgumentException("classLoader is null");
		}
        ClientClassDataProvider.classLoader = classLoader;
    }

	@Override
    public byte[] getClassData(String name) throws ClassNotFoundException, IOException {
        Class<?> clazz = Class.forName(name, false, classLoader);

		InputStream is = null;
		try {
			is = clazz.getResourceAsStream(clazz.getName() + ".class");
			if (is == null) {
				is = clazz.getResourceAsStream(clazz.getSimpleName() + ".class");
			}
			if (is == null) {
				String[] parts = clazz.getName().split("\\.");
				is = clazz.getResourceAsStream(parts[parts.length - 1] + ".class");
			}
			if (is != null) {
				return ByteStreams.toByteArray(is);
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
		throw new IOException("Failed to serialize class: " + name);
	}

}
