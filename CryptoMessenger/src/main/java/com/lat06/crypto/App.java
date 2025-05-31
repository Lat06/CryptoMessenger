package com.lat06.crypto;

public class App {
    public static void main(String[] args) {
        Message msg = new Message(1, 42, "Hello from sender");

        PacketSender sender = new PacketSender();
        byte bSrc = 0x01;
        long packetId = 123456789L;
        byte[] packet = sender.buildPacket(msg, bSrc, packetId);

        System.out.println("✅ Пакет створено. Довжина: " + packet.length + " байт");

        PacketReceiver receiver = new PacketReceiver();
        Message decoded = receiver.receive(packet);

        System.out.println("✅ Повідомлення прочитано:");
        System.out.println(decoded);
    }
}

