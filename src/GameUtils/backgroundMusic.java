package GameUtils;

import java.io.*;
import java.net.URL;
import javazoom.jl.player.*;


public class backgroundMusic implements Runnable{
	Player player;
	
	backgroundMusic(gameViewPanel p) {
		URL url=p.getClass().getResource("/music/backgroundMusic.mp3");
		try {
			InputStream fis = url.openStream();
			this.player = new Player(fis);
		} catch (Exception e) {
			System.exit(1);
		}
	}
	
	public void run(){
		try {
			this.player.play();
		} catch (Exception e) {
			System.exit(1);
		}

	}
	
}
