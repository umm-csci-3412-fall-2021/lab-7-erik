package segmentedfilesystem;

import java.net.DatagramPacket;
import java.util.Arrays;

public class DataPacket extends Packet {

    private int packetNum;
    private boolean isLast;

    public DataPacket (DatagramPacket packet) {
        // Parse the packet according to protocol
        int packetLength = packet.getLength();
        byte[] allPacketData = packet.getData();
        this.dataLength = packetLength - 4;

        this.status = allPacketData[0];
        this.fileID = allPacketData[1];
        this.packetNum = extractPacketNum(allPacketData[2], allPacketData[3]);
        this.data = Arrays.copyOfRange(allPacketData, 4, packetLength);

        this.isLast = determineIsLast(this.status);
    }

    /**
     * Given two bytes, returns the corresponding unsigned 2-byte integer
     * @param firstByte first byte
     * @param secondByte second byte
     * @return the unsigned 2-byte integer
     */
    private int extractPacketNum(byte firstByte, byte secondByte) {
        int x = Byte.toUnsignedInt(firstByte);
        int y = Byte.toUnsignedInt(secondByte);

        return (256 * x) + y;
    }

    private boolean determineIsLast(int status) {
        if (status % 4 == 3) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether this is the last packet for the corresponding file
     * @return true if it is the last packet, false otherwise
     */
    public boolean isLast() {
        return this.isLast;
    }

    /**
     * Get the packet number of this packet
     * @return the packet number
     */
    public int getPacketNum() {
        return this.packetNum;
    }
    
}
