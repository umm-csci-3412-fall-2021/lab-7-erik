package segmentedfilesystem;

import static org.junit.Assert.*;

import java.net.DatagramPacket;

import org.junit.Test;

public class ReceivedFileTests {
    
    DatagramPacket testPacket;
    byte[] dataTestData = new byte[6];
    byte[] headerTestData = new byte[4];

    String lastStatus = "00000111";
    int expectedLastStatus = 7;

    String notLastStatus = "00000101";
    int expectedNotLastStatus = 5;

    String headerStatus = "00000110";
    int expectedHeaderStatus = 6;

    String fileID = "00001010";
    int expectedFileID = 10;

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
    private void populateDataPacket(boolean isLast, boolean onlyPacket) {

        if(isLast) {
            dataTestData[0] = Byte.parseByte(lastStatus, 2);
        } else {
            dataTestData[0] = Byte.parseByte(notLastStatus, 2);
        }

        dataTestData[1] = Byte.parseByte(fileID, 2);

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
    private void populateHeaderPacket() {

        headerTestData[0] = Byte.parseByte(headerStatus, 2);
        headerTestData[1] = Byte.parseByte(fileID, 2);
        headerTestData[2] = data1;
        headerTestData[3] = data2;

        testPacket = new DatagramPacket(headerTestData, headerTestData.length);
    }

    @Test
    public void testConstructionFromHeader() {
        populateHeaderPacket();

        HeaderPacket createdPacket = new HeaderPacket(testPacket);
        ReceivedFile createdFile = new ReceivedFile(createdPacket);

        HeaderPacket fileHeader = createdFile.getHeader();
        assertEquals(expectedHeaderStatus, fileHeader.getStatus());
        assertEquals(expectedFileID, fileHeader.getFileID());
        assertEquals(data1, fileHeader.getData()[0]);
        assertEquals(data2, fileHeader.getData()[1]);

        assertEquals(false, createdFile.hasAllPackets());
        assertEquals(0, createdFile.getNumReceivedPackets());
        assertEquals(-1, createdFile.getExpectedNumPackets());
    }

    @Test
    public void testConstructionFromData() {
        populateDataPacket(false, false);

        DataPacket createdPacket = new DataPacket(testPacket);
        ReceivedFile createdFile = new ReceivedFile(createdPacket);

        assertEquals(false, createdFile.hasAllPackets());
        assertEquals(1, createdFile.getNumReceivedPackets());
        assertEquals(-1, createdFile.getExpectedNumPackets());
    }

    @Test
    public void testExpectedNumPackets() {
        populateDataPacket(true, false);

        DataPacket createdPacket = new DataPacket(testPacket);
        ReceivedFile createdFile = new ReceivedFile(createdPacket);

        assertEquals(false, createdFile.hasAllPackets());
        assertEquals(1, createdFile.getNumReceivedPackets());
        assertEquals(expectedPacketNum + 1, createdFile.getExpectedNumPackets());

        populateDataPacket(false, true);

        DataPacket newPacket = new DataPacket(testPacket);
        createdFile.addDataPacket(newPacket);

        assertEquals(false, createdFile.hasAllPackets());
        assertEquals(2, createdFile.getNumReceivedPackets());
        assertEquals(expectedPacketNum + 1, createdFile.getExpectedNumPackets());
    }

    @Test
    public void testReceivedAllDataPackets() {
        populateDataPacket(true, true);

        DataPacket createdPacket = new DataPacket(testPacket);
        ReceivedFile createdFile = new ReceivedFile(createdPacket);

        assertEquals(false, createdFile.hasAllPackets());
        assertEquals(1, createdFile.getNumReceivedPackets());
        assertEquals(1, createdFile.getExpectedNumPackets());
    }

    @Test
    public void testReceivedAllPackets() {
        populateDataPacket(true, true);

        DataPacket createdPacket = new DataPacket(testPacket);
        ReceivedFile createdFile = new ReceivedFile(createdPacket);

        populateHeaderPacket();

        HeaderPacket createdHeader = new HeaderPacket(testPacket);
        createdFile.addHeader(createdHeader);

        assertEquals(true, createdFile.hasAllPackets());
        assertEquals(1, createdFile.getNumReceivedPackets());
        assertEquals(1, createdFile.getExpectedNumPackets());
    }
}
