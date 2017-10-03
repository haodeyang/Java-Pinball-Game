import java.io.*;
import java.net.URL;
import java.applet.*;


public class backgroundMusic implements Runnable{         
	File f;
	URL url;
	AudioClip clip;
	
	backgroundMusic(gameViewPanel p){
		url=p.getClass().getResource("/music/backgroundMusic.mp3");
		clip=Applet.newAudioClip(url);
	}
	
	public void run(){
			clip.play();
	}
	
}
