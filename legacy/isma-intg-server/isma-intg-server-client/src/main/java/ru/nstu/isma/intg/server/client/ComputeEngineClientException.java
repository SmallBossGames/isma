package ru.nstu.isma.intg.server.client;

public class ComputeEngineClientException extends Exception {

	public ComputeEngineClientException(String message) {
		super(message);
	}

	public ComputeEngineClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComputeEngineClientException(Throwable cause) {
		super(cause);
	}
}
