package ru.nstu.isma.intg.server.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.esotericsoftware.kryonet.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.server.ClassDataProvider;
import ru.nstu.isma.intg.server.utils.RemoteClassLoader;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClientContext implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientContext.class);

    private final Connection connection;
    private final Map<String, byte[]> classDataCache = new HashMap<>();
    private final CachingClassDataProvider mainClassDataProvider;
    private final RemoteClassLoader remoteClassLoader;

    public ClientContext(Connection connection, ClassDataProvider clientClassDataLoader) {
        checkNotNull(connection, "connection");
        checkNotNull(clientClassDataLoader, "clientClassDataLoader");

        this.connection = connection;
        this.mainClassDataProvider = new CachingClassDataProvider(clientClassDataLoader);
        this.remoteClassLoader = new RemoteClassLoader(this.getClass().getClassLoader(), this.mainClassDataProvider);
    }

    public Connection getConnection() {
        return connection;
    }

    public CachingClassDataProvider getClassDataProvider() {
        return mainClassDataProvider;
    }

    public RemoteClassLoader getRemoteClassLoader() {
        return remoteClassLoader;
    }

    public Map<String, byte[]> getLoadedClasses() {
        return new HashMap<>(classDataCache);
    }

    public Map<String, byte[]> getLoadedClasses(Set<String> classNames) {
        HashMap<String, byte[]> results = new HashMap<>(classNames.size());
        for (String name : classNames) {
            byte[] data = classDataCache.get(name);
            if (data != null) {
                results.put(name, data);
            }
        }
        return results;
    }

    @Override
    public void close() throws Exception {
        this.classDataCache.clear();
        this.remoteClassLoader.getClassDataProviderRef().clear();
    }

    public class CachingClassDataProvider implements ClassDataProvider {
        private final ClassDataProvider delegate;
        private final Set<String> loadedClassNames = new HashSet<>();

        public CachingClassDataProvider(ClassDataProvider delegate) {
            this.delegate = delegate;
        }

        @Override
        public byte[] getClassData(String name) throws ClassNotFoundException, IOException {
            byte[] classData = classDataCache.get(name);
            if (classData == null) {
                classData = delegate.getClassData(name);
                classDataCache.put(name, classData);
                if (classData == null) {
                    LOGGER.warn("Loaded <null> data for a class {}", name);
                } else {
                    loadedClassNames.add(name);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Loaded class {}, hashCode: {}", name, Arrays.hashCode(classData));
                    }
                }
            }
            return classData;
        }

        public Set<String> getLoadedClassNames() {
            return loadedClassNames;
        }
    }

}
