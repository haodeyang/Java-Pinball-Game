import java.awt.Image;
import java.awt.Toolkit;
import java.net.*;

//�����࣬���ø�������
public class CONSTANT {

	public static final float RATE =10; // ��Ļ����ʵ����ı���
	
	public static final boolean DRAW_THREAD_FLAG = true; // �����̹߳�����־λ
	
	public static final float TIME_STEP = 1.0f / 60.0f; // ģ���Ƶ��
	
	public static final int ITERA = 10; // ��������
	
	public static boolean doSleep=true;   //ָʾ��������������������
	
	
	
	public static boolean shouldStep=false;  //ָʾ�û��������꣬���Կ�ʼģ��
	
	public static boolean batCanMove=false;  //ָʾ�û��������꣬���ӿ��Կ�ʼ������ƶ�
	
	
	
		
	public static boolean win=false;     //ָʾ�û�ͨ��
	 
	public static boolean fail=false;    //ָʾ�û�ʧ��
	
	
	
	public static boolean bump=false;    // ָʾ��������ײ����Ҫ������ײ��Ч
	
	
	
	public static float SCREEN_WIDTH=800; // ��Ļ���
	
	public static float SCREEN_HEIGHT=600; // ��Ļ�߶�

	public static float BRICK_WIDTH=50; // ש����
	
	public static float BRICK_HEIGHT=40; // ש��߶�
	
	public static float BALL_RADIUS=10;  // С��뾶
	
	public static float BAT_HEIGHT=20;   //���Ӹ߶�
	
	public static float BAT_WIDTH=120;   //�����ֿ��
		
	public static float BAT_WIDTH0=120;  //�����������
	
	public static float BAT_WIDTH1=180;  //���Ӽӳ����
	
	public static float BAT_WIDTH2=250;  //���ӳ����ӳ����
	
	public static float BAT_WIDTH3=80;   //�������̿��
	
	public static float BAT_WIDTH4=50;   //���ӳ������̿��
	
	public static float BONUS_RADIUS=25;  //bonus�뾶
	
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