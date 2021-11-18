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
        private final int BUF_LENGTH = 1028;

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

                // Create a PacketManager to work with the packets
                PacketManager packetManager = new PacketManager();

                // Get first packet, so we don't immediately assume we're done
                getNewPacket(socket, packetManager);

                // Until all packets have been received...
                while(!packetManager.allPacketsReceived()) {

                        getNewPacket(socket, packetManager);

                }

                // TODO: download received files
	}

        private void getNewPacket(DatagramSocket socket, PacketManager packetManager) {
                // Construct a new DatagramPacket to hold the data
                byte[] buf = new byte[BUF_LENGTH];
                DatagramPacket packet = new DatagramPacket(buf, BUF_LENGTH);

                // Receive the data from the server
                try {
                        socket.receive(packet);
                } catch (IOException e) {
                        System.out.println("Error receiving packet from server");
                }

                // Let the PacketManager deal with it
                packetManager.handlePacket(packet);
        }

}
