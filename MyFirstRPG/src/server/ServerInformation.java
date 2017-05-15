package server;

public class ServerInformation {

	public String serverName;
	public int port;
	
	private long uptime;
	
	public ServerInformation(String servername, int port) {
		this.serverName = servername;
		this.port = port;
		this.uptime = System.currentTimeMillis();
	}
	
	/**
	 * Get current uptime of server in milliseconds
	 * 
	 * @return long server uptime
	 */
	public long getCurrentUptime() {
		return System.currentTimeMillis()-uptime;
	}
	
}
