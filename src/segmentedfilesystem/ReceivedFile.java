package segmentedfilesystem;

import java.util.ArrayList;

public class ReceivedFile {
    
    private HeaderPacket header;
    private ArrayList<DataPacket> dataPackets;

    private int expectedNumPackets = -1;

    public ReceivedFile(HeaderPacket headerPacket) {
        this.header = headerPacket;
        this.dataPackets = new ArrayList<>();
    }

    public ReceivedFile(DataPacket dataPacket) {
        this.dataPackets = new ArrayList<>();
        this.dataPackets.add(dataPacket);
    }

    public void addHeader(HeaderPacket headerPacket) {
        this.header = headerPacket;
    }

    public void addDataPacket(DataPacket packet) {
        if (packet.isLast()) {
            this.expectedNumPackets = packet.getPacketNum() + 1;
        }
        this.dataPackets.add(packet);
    }

    public int getNumReceivedPackets() {
        return this.dataPackets.size();
    }

    public int getExpectedNumPackets() {
        return this.expectedNumPackets;
    }

    public boolean hasAllPackets() {
        if (this.expectedNumPackets == -1) {
            return false;
        } else {
            return this.expectedNumPackets == getNumReceivedPackets();
        }
    }

    public HeaderPacket getHeader() {
        return this.header;
    }
}
