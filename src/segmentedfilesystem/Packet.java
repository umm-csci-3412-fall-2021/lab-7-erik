package segmentedfilesystem;

import java.net.DatagramPacket;

public abstract class Packet {

    int status;
    int fileID;

    int dataLength;
    byte[] data;
    
    /**
     * Get the ID of the file that this packet belongs to
     * @return
     */
    public int getFileID() {
        return this.fileID;
    }

    /**
     * Get the status of the packet as an int
     * @return the status
     */
    public int getStatus() {
        return this.status;
    }

}
