package com.lat06.crypto;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PacketSender {

    public static final byte MAGIC_BYTE = 0x13;

    public byte[] buildPacket(Message message, byte bSrc, long bPktId) {
        byte[] plainMsg = message.toBytes();
        byte[] encrypted = AesUtil.encrypt(plainMsg);

        int wLen = encrypted.length;

        ByteBuffer buffer = ByteBuffer.allocate(16 + wLen + 2);
        buffer.put(MAGIC_BYTE);
        buffer.put(bSrc);
        buffer.putLong(bPktId);
        buffer.putInt(wLen);

        byte[] header = Arrays.copyOfRange(buffer.array(), 0, 14);
        int crcHeader = Crc16Util.compute(header, 0, 14);
        buffer.putShort((short) crcHeader);

        buffer.put(encrypted);
        int crcMsg = Crc16Util.compute(encrypted, 0, encrypted.length);
        buffer.putShort((short) crcMsg);

        return buffer.array();
    }
}
