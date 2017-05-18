package client.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import debug.DebugMessageFactory;

public class OnCloseListener extends WindowAdapter{

	private Game game;
	
	public OnCloseListener(Game game) {
		this.game = game;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		game.serverConnection.closeConnection();
		game.saveGame();
		game.udp_client.logoutPlayer(""+game.player.content.id);
		DebugMessageFactory.printNormalMessage("EXITING.. GAME SAVED!");
		System.exit(0);
	}
	
}
