package ru.nstu.isma.intg.server;

import java.util.Properties;

public class Configuration {
	private static final String APPLICATION_NAME = "ISMA Integration Server";

	private static final String PORT_PROPERTY = "INTG_SERVER_RMI_REGISTRY_PORT";

	private static final int DEFAULT_RMI_REGISTRY_PORT = 7890;

	private final int rmiRegistryPort;

	public Configuration(Properties properties) {
		this.rmiRegistryPort = getIntProperty(properties, PORT_PROPERTY, DEFAULT_RMI_REGISTRY_PORT);
	}

	private int getIntProperty(Properties properties, String name, int defaultValue) {
		return Integer.parseInt(properties.getProperty(name, String.valueOf(defaultValue)));
	}

	public String getApplicationName() {
		return APPLICATION_NAME;
	}

	public int getServerPort() {
		return rmiRegistryPort;
	}

}
