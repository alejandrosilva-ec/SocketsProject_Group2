package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class UdpPingClient {

	public static void main(String[] args) throws Exception {
		
		//Address of the UDP server
		InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
		int port = 5000;
		
		//Create the UDP socket (used to send and receive datagrams)
		try (DatagramSocket socket = new DatagramSocket()){
			
			//Maximum time to wait for a reply (1 second)
			socket.setSoTimeout(1000);
			
			int sent = 0;
			int received = 0;
			
			long min = Long.MAX_VALUE;
			long max = 0;
			long total = 0;
			
			//Send 10 PINGs messages
			for (int i=1;i<=10;i++) {
				
				//Message to send
				String msg = "PING " + i;
				byte[] data = msg.getBytes(StandardCharsets.UTF_8);
				
				//Packet containing the PING message
				DatagramPacket packet = new DatagramPacket(data, data.length, serverAddr, port);
				
				long start = System.nanoTime(); //Save time before sending
				socket.send(packet); //Send the PING
				sent++;
				
				//Prepare a buffer to receive the response
				byte[] buffer = new byte[1024];
				DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
				
				try {
					//Wait for the PONG response
					socket.receive(resp); 
					long end  = System.nanoTime(); //Save time after receiving
					long rtt = (end - start) / 1_000_000; //Convert to milliseconds
					received++;
					
					//Update RTT statistics
					min = Math.min(min, rtt);
					max = Math.max(max, rtt);
					total += rtt;
					
					//Convert received bytes into text
					String answer = new String(resp.getData(), 0, resp.getLength(), StandardCharsets.UTF_8);
					System.out.println("Received: " + answer + " | RTT = " + rtt + " ms");
					
				}catch (SocketTimeoutException e) {
					//IF No response within the timeout period
					System.out.println("Timeout waiting for PONG " + i);
				}
			}
			
			//Print final statistics
			System.out.println("\n--- Statistics ---");
			System.out.println("Packets sent: " + sent);
			System.out.println("Packets received: " + received);
			System.out.println("Loss: " + ((sent - received) * 100 / sent) + "%");
			
			if(received > 0) {
				System.out.println("Min RTT: " + min + " ms");
				System.out.println("Avg RTT: " + (total / received) + " ms");
				System.out.println("Max RTT: " + max + " ms");
			}
		}
	}
}
