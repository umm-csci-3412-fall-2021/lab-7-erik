package segmentedfilesystem;

import static org.junit.Assert.*;

import java.net.DatagramPacket;

import org.junit.Test;

public class PacketManagerTests {

    DatagramPacket testPacket;
    byte[] dataTestData = new byte[6];
    byte[] headerTestData = new byte[4];

    String lastStatus = "00000111";
    int expectedLastStatus = 7;

    String notLastStatus = "00000101";
    int expectedNotLastStatus = 5;

    String headerStatus = "00000110";
    int expectedHeaderStatus = 6;

    String fileID1 = "00001010";
    int expectedFileID1 = 10;

    String fileID2 = "00001110";
    int expectedFileID2 = 14;

    String firstPacketNum = "00001100";
    String secondPacketNum = "00100010";

    int expectedPacketNum = 12 * 256 + 34;

    String onlyPacketNum1 = "00000000";
    String onlyPacketNum2 = "00000000";

    int expectedOnlyPacketNum = 0;

    byte data1 = 5;
    byte data2 = 9;

    /**
     * Populates a DatagramPacket with the appropriate data to be a DataPacket
     * @param isLast whether the packet is the last one of the file
     * @param onlyPacket whether the packet number should be 0 or an arb number
     */
    private void populateDataPacket(boolean isLast, boolean onlyPacket, boolean diffFile) {

        if(isLast) {
            dataTestData[0] = Byte.parseByte(lastStatus, 2);
        } else {
            dataTestData[0] = Byte.parseByte(notLastStatus, 2);
        }

        if(diffFile) {
            dataTestData[1] = Byte.parseByte(fileID2, 2);
        } else {
            dataTestData[1] = Byte.parseByte(fileID1, 2);
        }

        if(onlyPacket) {
            dataTestData[2] = Byte.parseByte(onlyPacketNum1, 2);
            dataTestData[3] = Byte.parseByte(onlyPacketNum2, 2);
        } else {
            dataTestData[2] = Byte.parseByte(firstPacketNum, 2);
            dataTestData[3] = Byte.parseByte(secondPacketNum, 2);
        }

        dataTestData[4] = data1;
        dataTestData[5] = data2;

        testPacket = new DatagramPacket(dataTestData, dataTestData.length);
    }

    /**
     * Populates a DatagramPacket with the appropriate data for a HeaderPacket
     */
    private void populateHeaderPacket(boolean diffFile) {

        headerTestData[0] = Byte.parseByte(headerStatus, 2);

        if(diffFile) {
            headerTestData[1] = Byte.parseByte(fileID2, 2);
        } else {
            headerTestData[1] = Byte.parseByte(fileID1, 2);
        }
        headerTestData[2] = data1;
        headerTestData[3] = data2;

        testPacket = new DatagramPacket(headerTestData, headerTestData.length);
    }

    @Test
    public void testHandlingOfHeaders() {
        populateHeaderPacket(false);

        PacketManager testManager = new PacketManager();

        testManager.handlePacket(testPacket);
        ReceivedFile filePacket = testManager.getFilePackets(expectedFileID1);

        assertEquals(false, testManager.allPacketsReceived());
        assertEquals(-1, filePacket.getExpectedNumPackets());
        assertEquals(0, filePacket.getNumReceivedPackets());
        assertEquals(expectedHeaderStatus, filePacket.getHeader().getStatus());
        assertEquals(expectedFileID1, filePacket.getHeader().getFileID());

        // Add header for a different file
        populateHeaderPacket(true);

        testManager.handlePacket(testPacket);
        ReceivedFile newFile = testManager.getFilePackets(expectedFileID2);

        assertEquals(false, testManager.allPacketsReceived());
        assertEquals(-1, newFile.getExpectedNumPackets());
        assertEquals(0, newFile.getNumReceivedPackets());
        assertEquals(expectedHeaderStatus, newFile.getHeader().getStatus());
        assertEquals(expectedFileID2, newFile.getHeader().getFileID());
    }

    @Test
    public void testHandlingOfData() {
        populateDataPacket(true, false, false);

        PacketManager testManager = new PacketManager();

        testManager.handlePacket(testPacket);
        ReceivedFile filePacket = testManager.getFilePackets(expectedFileID1);

        assertEquals(false, testManager.allPacketsReceived());
        assertEquals(expectedPacketNum + 1, filePacket.getExpectedNumPackets());
        assertEquals(1, filePacket.getNumReceivedPackets());

        // Add data for a different file
        populateDataPacket(false, false, true);

        testManager.handlePacket(testPacket);
        ReceivedFile newFile = testManager.getFilePackets(expectedFileID2);

        assertEquals(false, testManager.allPacketsReceived());
        assertEquals(-1, newFile.getExpectedNumPackets());
        assertEquals(1, newFile.getNumReceivedPackets());

        // Add more data to the first file
        populateDataPacket(false, true, false);

        testManager.handlePacket(testPacket);
        filePacket = testManager.getFilePackets(expectedFileID1);

        assertEquals(false, testManager.allPacketsReceived());
        assertEquals(expectedPacketNum + 1, filePacket.getExpectedNumPackets());
        assertEquals(2, filePacket.getNumReceivedPackets());
    }

    @Test
    public void testHandlingOfDataAndHeaders() {
        populateHeaderPacket(false);

        PacketManager testManager = new PacketManager();

        testManager.handlePacket(testPacket);

        populateDataPacket(true, true, false);

        testManager.handlePacket(testPacket);
        ReceivedFile filePacket = testManager.getFilePackets(expectedFileID1);

        assertEquals(true, testManager.allPacketsReceived());
        assertEquals(1, filePacket.getExpectedNumPackets());
        assertEquals(1, filePacket.getNumReceivedPackets());
        assertEquals(expectedHeaderStatus, filePacket.getHeader().getStatus());
        assertEquals(expectedFileID1, filePacket.getHeader().getFileID());

        // Add data and header for second file
        populateDataPacket(true, true, true);

        testManager.handlePacket(testPacket);
        ReceivedFile newFile = testManager.getFilePackets(expectedFileID2);

        assertEquals(false, testManager.allPacketsReceived());
        assertEquals(1, newFile.getExpectedNumPackets());
        assertEquals(1, newFile.getNumReceivedPackets());

        populateHeaderPacket(true);

        testManager.handlePacket(testPacket);
        newFile = testManager.getFilePackets(expectedFileID2);

        assertEquals(true, testManager.allPacketsReceived());
        assertEquals(1, newFile.getExpectedNumPackets());
        assertEquals(1, newFile.getNumReceivedPackets());
    }
}
