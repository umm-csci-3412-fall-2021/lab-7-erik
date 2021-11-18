package segmentedfilesystem;

import static org.junit.Assert.*;

import java.net.DatagramPacket;

import org.junit.Test;

public class HeaderPacketTests {
    
    DatagramPacket testPacket;
    byte[] testData = new byte[4];
    
    String status = "00000110";
    int expectedStatus = 6;
    
    String fileID = "00001010";
    int expectedFileID = 10;

    byte data1 = 5;
    byte data2 = 9;

    private void populateHeaderPacket() {

        testData[0] = Byte.parseByte(status, 2);
        testData[1] = Byte.parseByte(fileID, 2);
        testData[2] = data1;
        testData[3] = data2;

        testPacket = new DatagramPacket(testData, testData.length);
    }

    @Test
    public void testHeaderPacketConstruction() {
        populateHeaderPacket();

        HeaderPacket createdPacket = new HeaderPacket(testPacket);

        assertEquals(expectedStatus, createdPacket.getStatus());
        assertEquals(expectedFileID, createdPacket.getFileID());
        assertEquals(data1, createdPacket.getData()[0]);
        assertEquals(data2, createdPacket.getData()[1]);
        
    }
}
