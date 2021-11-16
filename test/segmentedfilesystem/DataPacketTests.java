package segmentedfilesystem;

import static org.junit.Assert.*;

import java.net.DatagramPacket;

import org.junit.Test;

/**
 * This is just a stub test file. You should rename it to
 * something meaningful in your context and populate it with
 * useful tests.
 */
public class DataPacketTests {

    DatagramPacket testPacket;
    byte[] testData1 = new byte[6];

    String status = "00000111";
    int expectedStatus = 7;

    String fileID = "00001010";
    int expectedFileID = 10;

    String firstPacketNum = "00001100";
    String secondPacketNum = "00100010";

    int expectedPacketNum = 12 * 256 + 34;

    byte data1 = 5;
    byte data2 = 9;

    private void populateDataPacket() {
        testData1[0] = Byte.parseByte(status, 2);
        testData1[1] = Byte.parseByte(fileID, 2);
        testData1[2] = Byte.parseByte(firstPacketNum, 2);
        testData1[3] = Byte.parseByte(secondPacketNum, 2);
        testData1[4] = data1;
        testData1[5] = data2;

        testPacket = new DatagramPacket(testData1, testData1.length);
    }

    @Test
    public void testDataPacketConstruction() {
        populateDataPacket();
        
        DataPacket createdPacket = new DataPacket(testPacket);

        assertEquals(expectedStatus, createdPacket.getStatus());
        assertEquals(expectedFileID, createdPacket.getFileID());
        assertEquals(expectedPacketNum, createdPacket.getPacketNum());
        assertEquals(true, createdPacket.isLast());
    }

}
