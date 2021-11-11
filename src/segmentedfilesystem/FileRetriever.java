package segmentedfilesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class FileRetriever {

        InetAddress address;
        int port;

	public FileRetriever(String server, int port) {
                // Store the address and port number of the server
                try {
                        this.address = InetAddress.getByName(server);
                } catch (UnknownHostException e) {
                        System.out.println("Could not recognize address " + server);
                }
                this.port = port;
	}

	public void downloadFiles() {

                // Create the Socket used to communicate with the server
                DatagramSocket socket = null;
                try {
                        socket = new DatagramSocket(port, address);
                } catch (SocketException e) {
                        System.out.println("Could not connect to server ");
                }

                // Create an empty packet and send it to the server
                // This acts as a request that asks the server to start sending it's response packets
                byte[] emptyBuf = new byte[256];
                DatagramPacket requestPacket = new DatagramPacket(emptyBuf, emptyBuf.length, address, port);
                try {
                        socket.send(requestPacket);
                } catch (IOException e) {
                        System.out.println("Error sending request packet");
                }

        // Do all the heavy lifting here.
        // This should
        //   * Download packets in some sort of loop
        //   * Handle the packets as they come in by, e.g.,
        //     handing them to some PacketManager class
        // Your loop will need to be able to ask someone
        // if you've received all the packets, and can thus
        // terminate. You might have a method like
        // PacketManager.allPacketsReceived() that you could
        // call for that, but there are a bunch of possible
        // ways.
	}

}
