package ru.nstu.isma.intg.server.models;

import java.io.Serializable;

public class Message implements Serializable {
	private final MessageType type;
	private final Object payload;

    public Message(MessageType type) {
        this.type = type;
        this.payload = null;
    }

	public Message(MessageType type, Object payload) {
		this.type = type;
		this.payload = payload;
	}

	public MessageType getType() {
		return type;
	}

	public Object getPayload() {
		return payload;
	}
}
