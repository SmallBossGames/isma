package ru.nstu.isma.intg.server;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.server.core.ComputeEngineServer;
import ru.nstu.isma.intg.server.core.ComputeNode;
import ru.nstu.isma.intg.server.services.ServiceFacade;

public final class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	private static final int MAIN_THREAD_SLEEP_PERIOD = 300;

	private static ComputeNode computeNode;
	private static ComputeEngineServer server;

	private Main() {
	}

	public static void main(String[] args) throws Exception {
		Configuration config = new Configuration(System.getProperties());
        ServiceFacade serviceFacade = new ServiceFacade();

		LOGGER.info("Starting {} ...", config.getApplicationName());

		computeNode = new ComputeNode(serviceFacade);
		computeNode.start(args);

		installShutdownHook();

		serviceFacade.getMessageService().startMessagePump();

		if (serviceFacade.getMpiService().isMasterNode()) {
			server = new ComputeEngineServer(serviceFacade);
			try {
				server.start(config.getServerPort());
			} catch (IOException e) {
				LOGGER.error("Failed to start Integration Server", e);
			}
			LOGGER.info("Started Integration Server on port {}.", config.getServerPort());
		}

		waitShutdown();
	}

	private static void installShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				LOGGER.info("Detected JVM shutdown. Stopping...");

				try {
					computeNode.stop();
				} catch (RuntimeException e) {
					LOGGER.error("Failed to stop a compute node", e);
				}

				if (server != null) {
					LOGGER.info("Shutdown Integration Server...");
					server.stop();
					server = null;
				}
			}
		});
	}

	private static void waitShutdown() {
		boolean interrupted = false;
		do {
			try {
				Thread.sleep(MAIN_THREAD_SLEEP_PERIOD);
			} catch (InterruptedException e) {
				interrupted = true;
			}
		} while (!interrupted);
	}

}
