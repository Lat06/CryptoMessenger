package com.lat06.crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PacketReceiverTest {

    @Test
    public void testPacketSendAndReceive() {
        Message original = new Message(100, 555, "Тестове повідомлення");

        PacketSender sender = new PacketSender();
        byte[] packet = sender.buildPacket(original, (byte) 0x01, 9999L);

        PacketReceiver receiver = new PacketReceiver();
        Message received = receiver.receive(packet);

        assertEquals(original.toString(), received.toString());
    }

    @Test
    public void testEncryptedMessageIsNotPlainText() {
        Message msg = new Message(1, 1, "secret");
        byte[] plain = msg.toBytes();
        byte[] encrypted = AesUtil.encrypt(plain);

        assertNotEquals(new String(plain), new String(encrypted));
    }

    @Test
    public void testHeaderCrcMismatchThrows() {
        Message msg = new Message(1, 2, "broken");
        PacketSender sender = new PacketSender();
        byte[] packet = sender.buildPacket(msg, (byte) 0x01, 1);

        packet[14] = 0;
        packet[15] = 0;

        PacketReceiver receiver = new PacketReceiver();
        assertThrows(IllegalArgumentException.class, () -> receiver.receive(packet));
    }

    @Test
    public void testMessageCrcMismatchThrows() {
        Message msg = new Message(1, 2, "CRC error");
        PacketSender sender = new PacketSender();
        byte[] packet = sender.buildPacket(msg, (byte) 0x01, 1);

        packet[packet.length - 1] ^= 1;

        PacketReceiver receiver = new PacketReceiver();
        assertThrows(IllegalArgumentException.class, () -> receiver.receive(packet));
    }
}
