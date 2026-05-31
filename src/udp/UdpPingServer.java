package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UdpPingServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
int port = 5000;
		
		//Buffer used to store the incoming message
		byte[] buffer = new byte[1024];

		System.out.println("UDP Ping Server listening on port " + port);
		
		//Create the UDP socket (waits for incoming datagrams)
		try (DatagramSocket socket = new DatagramSocket(port)){
			while(true) {
				
				//Packet that will hold the received data
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				
				//Wait for a datagram to arrive
				socket.receive(packet);
				
				//Convert the received bytes into a UTF-8 string
				String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
				System.out.println("Received: " + msg);
				
				//Build the response by replacing PING with PONG
				String answer = msg.replace("PING", "PONG");
				byte[] outData = answer.getBytes(StandardCharsets.UTF_8);
				
				//Get the client's address and port to send the reply back
				InetAddress clientAddr = packet.getAddress();
				int clientPort = packet.getPort();
				
				//Create the response packet
				DatagramPacket outPacket = new DatagramPacket(outData, outData.length, clientAddr, clientPort);
				
				//Send the PONG message back to the client
				socket.send(outPacket);
				
				System.out.println("Sent: " + answer);
			}
		}catch (Exception e) {
			System.out.println("Server error: " + e.getMessage());
		}
	}

}
