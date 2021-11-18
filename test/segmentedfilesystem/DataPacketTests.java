package segmentedfilesystem;

import static org.junit.Assert.*;

import java.net.DatagramPacket;

import org.junit.Test;

/**
 * Tests for DataPacket.java
 */
public class DataPacketTests {

    DatagramPacket testPacket;
    byte[] testData1 = new byte[6];

    String lastStatus = "00000111";
    int expectedLastStatus = 7;

    String notLastStatus = "00000101";
    int expectedNotLastStatus = 5;

    String fileID = "00001010";
    int expectedFileID = 10;

    String firstPacketNum = "00001100";
    String secondPacketNum = "00100010";

    int expectedPacketNum = 12 * 256 + 34;

    byte data1 = 5;
    byte data2 = 9;

    private void populateDataPacket(boolean isLast) {

        if(isLast) {
            testData1[0] = Byte.parseByte(lastStatus, 2);
        } else {
            testData1[0] = Byte.parseByte(notLastStatus, 2);
        }

        testData1[1] = Byte.parseByte(fileID, 2);
        testData1[2] = Byte.parseByte(firstPacketNum, 2);
        testData1[3] = Byte.parseByte(secondPacketNum, 2);
        testData1[4] = data1;
        testData1[5] = data2;

        testPacket = new DatagramPacket(testData1, testData1.length);
    }

    @Test
    public void testDataPacketConstruction() {
        populateDataPacket(true);
        
        DataPacket createdPacket = new DataPacket(testPacket);

        assertEquals(expectedLastStatus, createdPacket.getStatus());
        assertEquals(expectedFileID, createdPacket.getFileID());
        assertEquals(expectedPacketNum, createdPacket.getPacketNum());
        assertEquals(data1, createdPacket.getData()[0]);
        assertEquals(data2, createdPacket.getData()[1]);
    }

    @Test
    public void testStatusCheckForLast() {
        populateDataPacket(true);

        DataPacket createdPacket = new DataPacket(testPacket);

        
        assertEquals(expectedLastStatus, createdPacket.getStatus());
        assertEquals(true, createdPacket.isLast());
    }

    @Test
    public void testStatusCheckNotLast() {
        populateDataPacket(false);

        DataPacket createdPacket = new DataPacket(testPacket);

        
        assertEquals(expectedNotLastStatus, createdPacket.getStatus());
        assertEquals(false, createdPacket.isLast());
    }

}
