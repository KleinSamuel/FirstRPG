package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import debug.DebugMessageFactory;

public class UDP_Client {

	private static final long REQUEST_TIMEOUT = 5000;
	
	private int server_port;
	private String server_name;

	private DatagramSocket clientSocket;
	private InetAddress ipAddress;
	
	public UDP_Client(int server_port, String server_name) {
		this.server_port = server_port;
		this.server_name = server_name;
		
		openConnection();
	}
	
	public void openConnection() {
		DebugMessageFactory.printNormalMessage("TRYING TO CONNECT TO SERVER...");
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			DebugMessageFactory.printErrorMessage("COULD NOT OPEN SOCKET!");
			System.exit(1);
		}
		try {
			ipAddress = InetAddress.getByName(server_name);
		} catch (UnknownHostException e) {
			DebugMessageFactory.printErrorMessage("COULD NOT FIND SERVER ["+server_name+"]!");
			System.exit(1);
		}
		DebugMessageFactory.printNormalMessage("CONNECTED TO SERVER!");
	}
	
	public byte[] sendRequest(String s) {
		
		byte[] data = s.getBytes();
		return sendRequest(data);
	}
	
	public byte[] sendRequest(byte[] data) {
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		sendData = data;
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, server_port);

		try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			DebugMessageFactory.printErrorMessage("COULD NOT SEND REQUEST PACKET!");
			System.exit(1);
		}
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		try {
			
			clientSocket.receive(receivePacket);
			
		} catch (IOException e) {
			DebugMessageFactory.printErrorMessage("COULD NOT RECIEVE PACKET!");
			System.exit(1);
		}
		byte[] receivedData = new byte[receivePacket.getLength()];
		System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), receivedData, 0, receivePacket.getLength());
		
		return receivedData;
	}
	
	public int registerPlayer(String request) {
		DebugMessageFactory.printNormalMessage("REGISTER USER WITH ["+request+"]");
		byte[] idArray = sendRequest("register_"+request);
		return Integer.parseInt(new String(idArray));
	}
	
	public void logoutPlayer(String id) {
		DebugMessageFactory.printNormalMessage("LOGGING OUT USER WITH ID ["+id+"]");
		sendRequest("logout_"+id);
	}
	
	public String downloadPlayerData() {
		byte[] data = sendRequest("download_player_data");
		return new String(data, 0, data.length);
	}
	
	public String downloadItemData() {
		byte[] data = sendRequest("download_item_data");
		return new String(data, 0, data.length);
	}
	
	public String removeItem(int i) {
		byte[] data = sendRequest("remove_"+i);
		return new String(data, 0, data.length);
	}
	
	public void waitForMillis(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		UDP_Client cl = new UDP_Client(6066, "localhost");
		
	}
	
}
