package segmentedfilesystem;

import java.net.DatagramPacket;
import java.util.Arrays;

public class HeaderPacket extends Packet {

    public HeaderPacket(DatagramPacket packet) {
        // Parse the packet according to protocol
        int packetLength = packet.getLength();
        byte[] allPacketData = packet.getData();
        this.dataLength = packetLength - 2;

        this.status = allPacketData[0];
        this.fileID = allPacketData[1];
        this.data = Arrays.copyOfRange(allPacketData, 2, packetLength);
    }
}
