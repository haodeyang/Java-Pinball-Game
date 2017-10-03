import java.io.*;
import java.net.URL;
import java.applet.*;

//碰撞音效类，负责播放碰撞音效
public class bumpMusic implements Runnable{
	File f;
	URL url;
	AudioClip clip;
	
	bumpMusic(gameViewPanel p){
		url=p.getClass().getResource("/music/bumpMusic.mp3");  //得到音乐文件路径
		clip=Applet.newAudioClip(url);
	}
	
	public void run(){
		while(true){
			if(CONSTANT.bump)
			{
				clip.play();
				CONSTANT.bump=false;
			}
		}		
	}
}