package com.lat06.crypto;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Message {
    private int cType;
    private int bUserId;
    private String payload;

    public Message() {}

    public Message(int cType, int bUserId, String payload) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.payload = payload;
    }

    public byte[] toBytes() {
        byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(8 + payloadBytes.length);
        buffer.putInt(cType);
        buffer.putInt(bUserId);
        buffer.put(payloadBytes);
        return buffer.array();
    }

    public static Message fromBytes(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int cType = buffer.getInt();
        int bUserId = buffer.getInt();
        byte[] payloadBytes = new byte[data.length - 8];
        buffer.get(payloadBytes);
        String payload = new String(payloadBytes, StandardCharsets.UTF_8);

        return new Message(cType, bUserId, payload);
    }

    @Override
    public String toString() {
        return "Message{" +
                "cType=" + cType +
                ", bUserId=" + bUserId +
                ", payload='" + payload + '\'' +
                '}';
    }

}
