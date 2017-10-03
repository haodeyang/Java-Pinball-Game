import java.awt.Image;
import java.awt.Toolkit;
import java.net.*;

//常量类，设置各个常量
public class CONSTANT {

	public static final float RATE =10; // 屏幕与现实世界的比例
	
	public static final boolean DRAW_THREAD_FLAG = true; // 绘制线程工作标志位
	
	public static final float TIME_STEP = 1.0f / 60.0f; // 模拟的频率
	
	public static final int ITERA = 10; // 迭代次数
	
	public static boolean doSleep=true;   //指示物理世界允许物体休眠
	
	
	
	public static boolean shouldStep=false;  //指示用户点击了鼠标，可以开始模拟
	
	public static boolean batCanMove=false;  //指示用户点击了鼠标，拍子可以开始随鼠标移动
	
	
	
		
	public static boolean win=false;     //指示用户通关
	 
	public static boolean fail=false;    //指示用户失败
	
	
	
	public static boolean bump=false;    // 指示发生了碰撞并需要播放碰撞音效
	
	
	
	public static float SCREEN_WIDTH=800; // 屏幕宽度
	
	public static float SCREEN_HEIGHT=600; // 屏幕高度

	public static float BRICK_WIDTH=50; // 砖块宽度
	
	public static float BRICK_HEIGHT=40; // 砖块高度
	
	public static float BALL_RADIUS=10;  // 小球半径
	
	public static float BAT_HEIGHT=20;   //拍子高度
	
	public static float BAT_WIDTH=120;   //拍子现宽度
		
	public static float BAT_WIDTH0=120;  //拍子正常宽度
	
	public static float BAT_WIDTH1=180;  //拍子加长宽度
	
	public static float BAT_WIDTH2=250;  //拍子超级加长宽度
	
	public static float BAT_WIDTH3=80;   //拍子缩短宽度
	
	public static float BAT_WIDTH4=50;   //拍子超级缩短宽度
	
	public static float BONUS_RADIUS=25;  //bonus半径
	
	public static Toolkit kit;
	public static URL url;
	
	public static Image surprise=null;
	public static Image widen1=null;
	public static Image widen2=null;
	public static Image narrow1=null;
	public static Image narrow2=null;
	public static Image fasten=null;
	public static Image slow=null;
	public static Image penetrate=null;
	public static Image fire=null;
	public static Image death=null;
	public static Image success=null;
	public static Image gameOver=null;
	public static Image youWin=null;
	public static Image click=null;
	
	public static void initImage(gameViewPanel p){
		kit = Toolkit.getDefaultToolkit();
		
		url=p.getClass().getResource("/img/surprise2.png");
		surprise = kit.getImage(url);
		
		url=p.getClass().getResource("/img/widen1.png");
		widen1 = kit.getImage(url);	
		
		url=p.getClass().getResource("/img/widen2.png");
		widen2 = kit.getImage(url);
		
		url=p.getClass().getResource("/img/narrow1.png");
		narrow1 = kit.getImage(url);
		
		url=p.getClass().getResource("/img/narrow2.png");
		narrow2 = kit.getImage(url);
		
		url=p.getClass().getResource("/img/fasten.png");
		fasten = kit.getImage(url);
		
		url=p.getClass().getResource("/img/slow.png");
		slow = kit.getImage(url);
		
		url=p.getClass().getResource("/img/penetrate.png");
		penetrate = kit.getImage(url);
		
		url=p.getClass().getResource("/img/fire.png");
		fire = kit.getImage(url);
		
		url=p.getClass().getResource("/img/death.png");
		death = kit.getImage(url);
		
		url=p.getClass().getResource("/img/success.png");
		success = kit.getImage(url);
		
		url=p.getClass().getResource("/img/click.png");
		click = kit.getImage(url);
		
		url=p.getClass().getResource("/img/gameOver.jpg");
		gameOver = kit.getImage(url);
		
		url=p.getClass().getResource("/img/youWin.jpg");
		youWin = kit.getImage(url);
	}
	
}