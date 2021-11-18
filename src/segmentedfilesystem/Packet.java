package segmentedfilesystem;

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

    /**
     * Get the data of the packet
     * @return the data as a byte[]
     */
    public byte[] getData() {
        return this.data;
    }

}
