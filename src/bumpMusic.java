import java.io.*;
import java.net.URL;
import java.applet.*;

//��ײ��Ч�࣬���𲥷���ײ��Ч
public class bumpMusic implements Runnable{
	File f;
	URL url;
	AudioClip clip;
	
	bumpMusic(gameViewPanel p){
		url=p.getClass().getResource("/music/bumpMusic.mp3");  //�õ������ļ�·��
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