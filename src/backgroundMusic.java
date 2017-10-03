import java.io.*;
import java.net.URL;
import java.applet.*;

//背景音乐类，负责播放背景音乐
public class backgroundMusic implements Runnable{         
	File f;
	URL url;
	AudioClip clip;
	
	backgroundMusic(gameViewPanel p){
		url=p.getClass().getResource("/music/backgroundMusic.mp3");   //获得音乐文件路径
		clip=Applet.newAudioClip(url);
	}
	
	public void run(){
			clip.play();
	}
	
}
