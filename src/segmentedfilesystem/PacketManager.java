package segmentedfilesystem;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PacketManager {

    // Map of fileID to file data we have so far
    // Each ArrayList of DataPackets represents one file
    private Map<Integer, ReceivedFile> packetMap;

    private ArrayList<Integer> fileIDList;

    public PacketManager() {
        this.packetMap = new TreeMap<>();
        fileIDList = new ArrayList<>();
    }

    /**
     * Checks whether or not all packets have been received based on current info
     * @return Whether all packets have been received
     */
    public boolean allPacketsReceived() {

        boolean result = true;

        // For each (file) entry in the map,
        for (Map.Entry fileElement : packetMap.entrySet()) {
            // Get the file as a ReceivedFile
            ReceivedFile file = (ReceivedFile) fileElement.getValue();

            // Check whether it's missing any packets
            if (!file.hasAllPackets()) {
                result = false;
            }
        }

        // Return true if none of the files were missing any packets
        return result;
    }
    
    public void handlePacket(DatagramPacket packet) {
        int status = packet.getData()[0];

        // If the status is even, it's a header packet
        if (status % 2 == 0) {
            HeaderPacket headerP = new HeaderPacket(packet);
            handleHeaderPacket(headerP);
        } else {
            DataPacket dataP = new DataPacket(packet);
            handleDataPacket(dataP);
        }
    }

    private void handleDataPacket(DataPacket packet) {

        ReceivedFile correspFile;
        int fileID = packet.getFileID();

        // Check whether the packet belongs to a new file, if it does...
        if (!packetMap.containsKey(fileID)) {

            // Create a new list of packets
            correspFile = new ReceivedFile(packet);

            // Add the list to the map
            packetMap.put(fileID, correspFile);

            // Add the ID to our list of IDs
            fileIDList.add(fileID);

        // Otherwise...
        } else {

            // Get the Arraylist for the appropriate file
            correspFile = packetMap.get(fileID);

            // Add the current packet to the list
            correspFile.addDataPacket(packet);

            // Replace the old list with a new one, containing the current packet
            packetMap.put(fileID, correspFile);
        }
    }

    private void handleHeaderPacket(HeaderPacket packet) {

        ReceivedFile correspFile;
        int fileID = packet.getFileID();

        // Check whether the packet belongs to a new file, if it does...
        if (!packetMap.containsKey(fileID)) {

            // Create a new list of packets
            correspFile = new ReceivedFile(packet);

            // Add the list to the map
            packetMap.put(fileID, correspFile);

            // Add the ID to our list of IDs
            fileIDList.add(fileID);

        // Otherwise...
        } else {

            // Get the Arraylist for the appropriate file
            correspFile = packetMap.get(fileID);

            // Add the current packet to the list
            correspFile.addHeader(packet);

            // Replace the old list with a new one, containing the current packet
            packetMap.put(fileID, correspFile);
        }
    }

    public ReceivedFile getFile(int fileID) {
        return packetMap.get(fileID);
    }

    public ArrayList<Integer> getFileIDs() {
        return fileIDList;
    }
}
