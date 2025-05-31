package com.lat06.crypto;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PacketReceiver {

    public static final byte MAGIC_BYTE = 0x13;

    public Message receive(byte[] packetBytes) {
        if (packetBytes.length < 18) throw new IllegalArgumentException("Packet too short.");
        if (packetBytes[0] != MAGIC_BYTE) throw new IllegalArgumentException("Invalid magic byte.");

        byte bSrc = packetBytes[1];
        long bPktId = ByteBuffer.wrap(packetBytes, 2, 8).getLong();
        int wLen = ByteBuffer.wrap(packetBytes, 10, 4).getInt();
        int headerCrc = ByteBuffer.wrap(packetBytes, 14, 2).getShort() & 0xFFFF;

        int computedHeaderCrc = Crc16Util.compute(packetBytes, 0, 14);
        if (headerCrc != computedHeaderCrc) throw new IllegalArgumentException("Header CRC mismatch");

        byte[] encryptedMsg = Arrays.copyOfRange(packetBytes, 16, 16 + wLen);
        int msgCrc = ByteBuffer.wrap(packetBytes, 16 + wLen, 2).getShort() & 0xFFFF;
        int computedMsgCrc = Crc16Util.compute(packetBytes, 16, wLen);
        if (msgCrc != computedMsgCrc) throw new IllegalArgumentException("Message CRC mismatch");

        byte[] decrypted = AesUtil.decrypt(encryptedMsg);
        return Message.fromBytes(decrypted);
    }
}
