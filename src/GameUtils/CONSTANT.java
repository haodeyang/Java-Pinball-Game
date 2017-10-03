package GameUtils;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.*;

public class CONSTANT {

	public static final float RATE =10;
	
	public static final boolean DRAW_THREAD_FLAG = true;
	
	public static final float TIME_STEP = 1.0f / 60.0f;
	
	public static final int ITERA = 10;
	
	public static boolean doSleep = true;
	
	
	
	public static boolean shouldStep = false;
	
	public static boolean batCanMove = false;
	
	
	
		
	public static boolean win = false;
	 
	public static boolean fail = false;

	
	public static float SCREEN_WIDTH = 800;
	
	public static float SCREEN_HEIGHT = 600;

	public static float BRICK_WIDTH = 50;
	
	public static float BRICK_HEIGHT = 40;
	
	public static float BALL_RADIUS = 10;
	
	public static float BAT_HEIGHT = 20;
	
	public static float BAT_WIDTH = 120;
		
	public static float BAT_WIDTH0 = 120;
	
	public static float BAT_WIDTH1 = 180;
	
	public static float BAT_WIDTH2 = 250;
	
	public static float BAT_WIDTH3 = 80;
	
	public static float BAT_WIDTH4 = 50;
	
	public static float BONUS_RADIUS = 25;
	
	public static Toolkit kit;
	public static URL url;
	
	public static Image surprise = null;
	public static Image widen1 = null;
	public static Image widen2 = null;
	public static Image narrow1 = null;
	public static Image narrow2 = null;
	public static Image fasten = null;
	public static Image slow = null;
	public static Image penetrate = null;
	public static Image fire = null;
	public static Image death = null;
	public static Image success = null;
	public static Image gameOver = null;
	public static Image youWin = null;
	public static Image click = null;
	
	public static void initImage(gameViewPanel p){
		kit = Toolkit.getDefaultToolkit();
		
		url = p.getClass().getResource("/img/surprise2.png");
		surprise = kit.getImage(url);
		
		url = p.getClass().getResource("/img/widen1.png");
		widen1 = kit.getImage(url);	
		
		url = p.getClass().getResource("/img/widen2.png");
		widen2 = kit.getImage(url);
		
		url = p.getClass().getResource("/img/narrow1.png");
		narrow1 = kit.getImage(url);
		
		url = p.getClass().getResource("/img/narrow2.png");
		narrow2 = kit.getImage(url);
		
		url = p.getClass().getResource("/img/fasten.png");
		fasten = kit.getImage(url);
		
		url = p.getClass().getResource("/img/slow.png");
		slow = kit.getImage(url);
		
		url = p.getClass().getResource("/img/penetrate.png");
		penetrate = kit.getImage(url);
		
		url = p.getClass().getResource("/img/fire.png");
		fire = kit.getImage(url);
		
		url = p.getClass().getResource("/img/death.png");
		death = kit.getImage(url);
		
		url = p.getClass().getResource("/img/success.png");
		success = kit.getImage(url);
		
		url = p.getClass().getResource("/img/click.png");
		click = kit.getImage(url);
		
		url = p.getClass().getResource("/img/gameOver.jpg");
		gameOver = kit.getImage(url);
		
		url = p.getClass().getResource("/img/youWin.jpg");
		youWin = kit.getImage(url);
	}
	
}