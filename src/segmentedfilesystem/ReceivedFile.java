package segmentedfilesystem;

import java.util.ArrayList;
import java.util.Comparator;

public class ReceivedFile {
    
    private HeaderPacket header;
    private ArrayList<DataPacket> dataPackets;

    private int expectedNumPackets = -1;

    public ReceivedFile(HeaderPacket headerPacket) {
        this.dataPackets = new ArrayList<>();
        this.addHeader(headerPacket);
    }

    public ReceivedFile(DataPacket dataPacket) {
        this.dataPackets = new ArrayList<>();
        this.addDataPacket(dataPacket);
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
            return (this.expectedNumPackets == getNumReceivedPackets()) && this.header != null;
        }
    }

    public HeaderPacket getHeader() {
        return this.header;
    }

    /**
     * Get the list of DataPackets sorted by packet number
     * @return
     */
    public ArrayList<DataPacket> getDataInOrder() {
        DataPacketComparer comparator = new DataPacketComparer();
        this.dataPackets.sort(comparator);
        return this.dataPackets;
    }

    /**
     * Comparator used to sort DataPackets by packet number
     */
    private class DataPacketComparer implements Comparator<DataPacket> {

        /**
         * Compares the two packets by Packet Number
         */
        @Override
        public int compare(DataPacket o1, DataPacket o2) {
            
            int firstPacketNum = o1.getPacketNum();
            int secondPacketNum = o2.getPacketNum();

            if (firstPacketNum < secondPacketNum) {
                return -1;
            } else if (firstPacketNum > secondPacketNum) {
                return 1;
            } else {
                return 0;
            }
        }

    }
}
