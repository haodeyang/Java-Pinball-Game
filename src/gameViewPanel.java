import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;
import org.jbox2d.collision.*;
import org.jbox2d.common.*;

//Panel
public class gameViewPanel extends JPanel implements Runnable{
	
	//地图选择
	int choice=0;
	
	//线程
	Thread t;
	Thread backM;
	backgroundMusic backMusic;
	Thread bumpM;
	
	//物理世界
	AABB worldAABB;
	World world;
	Vec2 gravity;
	
	//物理世界中所有刚体
	Brick[][] bricks=new Brick[10][16];
	Ball ball;
	Bat bat;
	Ground ground1;
	Ground ground2;
	Ground ground3;
	Ground ground4;
	
	//各类碰撞过滤器
	FilterData brickFilter;
	FilterData ballFilter;
	FilterData batFilter;
	FilterData bonusFilter;
	FilterData groundFilter;

	//实时鼠标坐标
	float mouseX=0,mouseY=0;
	
	//各类监听器
	myMouseMotionListener mouseHandler1;
	myMouseListener mouseHandler2;
	myContactListener contactHandler;
	
	//需摧毁刚体记录
	ArrayList<Body> bodyToDestroy=new ArrayList<Body>();
		
	//bonus效果指示
	ArrayList<Bonus> bonuses=new ArrayList<Bonus>();
	int bonusToCreate=0;
	boolean batToCreate=false;
	boolean pballToCreate=false;
	boolean lengthEffect=true;
	int lengthCounter=0;	
	boolean speedEffect=true;
	int speedCounter=0;
	boolean penetrateEffect=true;
	int penetrateCounter=0;
	boolean fireEffect=true;
	int fireCounter=0;
	
	//双缓冲
	Image offScreenImage;   
	
	//计分器
	int score=0;   
	boolean shouldShowScore=false;   
	
	//临时变量
	int i=0;                         
	int j=0;
	int k=0;
	
	long startMili;
	int time;
	int addScore;
	
	//创建游戏界面
	public gameViewPanel(int choice){
		this.choice=choice;
		initiateWorld();
		t=new Thread(this);
		backMusic=new backgroundMusic(this);
		backM=new Thread(backMusic);
		bumpM=new Thread(new bumpMusic(this));
		bumpM.start();
		t.start();
		backM.start();
	}
	
	//初始化整个物理世界
	void initiateWorld(){
		CONSTANT.initImage(this);
		offScreenImage=this.createImage(CONSTANT.SCREEN_WIDTH,CONSTANT.SCREEN_HEIGHT);
		worldAABB=new AABB();
		worldAABB.lowerBound.set((float)-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE, (float)-CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE);
		worldAABB.upperBound.set((float)CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE, (float)CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE);
		gravity=new Vec2((float)0, (float)-1);
		world = new World(worldAABB, gravity, CONSTANT.doSleep);
		world.setGravity(gravity);
		
		createFilter();
		
		float brick_x;
		float brick_y;
		float brick_w;
		float brick_h;
		ground1=new Ground(0,-29.999f,80,0.001f,true,world,groundFilter);
		ground2=new Ground(0,29.999f,80,0.001f,true,world,groundFilter);
		ground3=new Ground(-39.999f,0,0.001f,60,true,world,groundFilter);
		ground4=new Ground(39.999f,0,0.001f,60,true,world,groundFilter);
		
		if(choice==9)
		{
			for(int i=0;i<10;i++)
				for(int j=0;j<16;j++){
					brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
					brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
					brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
					brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
					if(Math.random()<=0.75)
					{
						if(Math.random()<=0.2)  
							bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
						else if(Math.random()<=0.4&&Math.random()>0.2)  
							bricks[i][j]=new Brick2(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
						else if(Math.random()<=0.8&&Math.random()>0.4)
							bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
						else
							bricks[i][j]=new Brick4(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
					else
					{
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,false,world,brickFilter);
					}
				}
		}
		else if(choice==1)
		{
			for(int i=0;i<10;i++)
				for(int j=0;j<16;j++)
					{
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
		}
		else if(choice==2)
		{
			for(int i=0;i<10;i++)
				for(int j=0;j<16;j++)
					{
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick2(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
		}
		else if(choice==3)
		{
			for(int i=0;i<10;i++)
				for(int j=0;j<16;j++)
					{
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
		}
		else if(choice==4)
		{
			for(i=0;i<5;i++){
				j=i;
				brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
				brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
				brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
				brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
				bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
				j=15-i;
				brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
				brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
				brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
				brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
				bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
			}
			for(i=5;i<10;i++){
				j=9-i;
				brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
				brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
				brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
				brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
				bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
				j=i+6;
				brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
				brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
				brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
				brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
				bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
			}
			for(int i=0;i<10;i++)
				for(int j=0;j<16;j++)
					if(!(bricks[i][j] instanceof Brick3)){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
		}
		else if(choice==5)
		{
			for(int i=0;i<10;i++){
				for(int j=0;j<16;j++){
					if((i==3||i==6)&&j!=0&&j!=15){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick4(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
					else{
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
			}
		}
		else if(choice==6)
		{
			for(int i=0;i<10;i++){
				for(int j=0;j<16;j++){
					if((i%3==0)&&j!=0&&j!=15){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
					else if((i%3==1)&&j!=0&&j!=15){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick2(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
					else{
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
			}
		}
		else if(choice==7)
		{
			for(int i=0;i<10;i++){
				for(int j=0;j<16;j++){
					if((i%5==0)&&j!=0&&j!=15){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick4(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
					else if((i%5==1)&&j!=0&&j!=15){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
					else if((i%5==2)&&j!=0&&j!=15){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick2(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
					else if((i%5==3)&&j!=0&&j!=15){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
					else{
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
			}
		}
		else
		{
			for(i=2;i<=7;i++){
				if(i==2){
					for(j=5;j<=7;j++){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
					for(j=10;j<=12;j++){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
				if(i==3||i==4){
					for(j=4;j<=8;j++){
						if(i==4&&j==6)
						{
							brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
							brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
							brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
							brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
							bricks[i][j]=new Brick4(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
						}
						else
						{
							brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
							brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
							brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
							brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
							bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
						}
					}
					for(j=11;j<=11;j++){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
				if(i==5){
					for(j=4;j<=10;j++){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
				if(i==6){
					for(j=5;j<=9;j++){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
				if(i==7){
					for(j=6;j<=8;j++){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
			}
			for(int i=0;i<10;i++)
				for(int j=0;j<16;j++)
					if(!(bricks[i][j] instanceof Brick3)&&!(bricks[i][j] instanceof Brick4)){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
		}
		ball=new Ball(0,-27,CONSTANT.BALL_RADIUS/CONSTANT.RATE,world,ballFilter);
		bat=new Bat(0,-29,CONSTANT.BAT_WIDTH/CONSTANT.RATE,CONSTANT.BAT_HEIGHT/CONSTANT.RATE,world,batFilter);
		mouseHandler1=new myMouseMotionListener();
		mouseHandler2=new myMouseListener();
		this.addMouseMotionListener(mouseHandler1);
		this.addMouseListener(mouseHandler2);
		contactHandler=new myContactListener();
		world.setContactListener(contactHandler);		
	}
	
	//绘制游戏界面
	public void paint(Graphics g)
	{	
		
		if((!CONSTANT.win)&&(!CONSTANT.fail))
		{
			g.clearRect(0,0,(int)CONSTANT.SCREEN_WIDTH+10,(int)CONSTANT.SCREEN_HEIGHT);		
			for(int i=0;i<10;i++)
				for(int j=0;j<16;j++){
					bricks[i][j].draw(g);
				}
			ball.draw(g);
			bat.draw(g);
			for(Bonus bonus: bonuses){
				bonus.draw(g);
			}
			
			if(!CONSTANT.shouldStep)	
				g.drawImage(CONSTANT.click, 200, 450,null);
			return;
		}
		else if(CONSTANT.win)                         
		{
			g.clearRect(0,0,(int)CONSTANT.SCREEN_WIDTH+10,(int)CONSTANT.SCREEN_HEIGHT);		
			g.drawImage(CONSTANT.youWin,0,0,null);
			g.setFont(new Font("Arial",Font.BOLD,50));
			if(i==score) g.drawString("time:"+Integer.toString(j)+"s", 295, 320);
			g.drawString("score:"+Integer.toString(i), 300, 380);
		}
		else                                          
		{
			g.clearRect(0,0,(int)CONSTANT.SCREEN_WIDTH+10,(int)CONSTANT.SCREEN_HEIGHT);	
			g.drawImage(CONSTANT.gameOver,0,0,null);
			g.setFont(new Font("Arial",Font.BOLD,50));
			if(i==score) g.drawString("time:"+Integer.toString(j)+"s", 295, 320);
			g.drawString("score:"+Integer.toString(i), 300, 380);
		}
	}
	
	public void update(Graphics g){
		Graphics offG=offScreenImage.getGraphics();
		paint(offG);
		g.drawImage(offScreenImage,0,0,null);
	}



	//线程运行
	public void run()
	{
		while(!CONSTANT.shouldStep)
			repaint();
		
		
		while((!CONSTANT.win) && (!CONSTANT.fail)) 
		{
				world.step(CONSTANT.TIME_STEP, CONSTANT.ITERA);
			
				repaint();
				if(lengthEffect)
				{
					lengthCounter++;
					if(lengthCounter>380)
					{
						lengthEffect=false;
					    lengthCounter=0;
					    bodyToDestroy.add(bat.body);
					    batToCreate=true;
					    CONSTANT.BAT_WIDTH=CONSTANT.BAT_WIDTH0;
					    
					}
				}
				
				for(Body body: bodyToDestroy){
					world.destroyBody(body);
				}
				bodyToDestroy.clear();
				
				for(int i=0;i<bonusToCreate;i++){
					bonuses.add(new Bonus(((float)(Math.random()*(CONSTANT.SCREEN_WIDTH-40)+20)-CONSTANT.SCREEN_WIDTH/2)/CONSTANT.RATE,(CONSTANT.SCREEN_HEIGHT/2-CONSTANT.BONUS_RADIUS)/CONSTANT.RATE,CONSTANT.BONUS_RADIUS/CONSTANT.RATE,world,bonusFilter));
				}
				bonusToCreate=0;
				
				if(batToCreate){
					bat=new Bat(mouseX,-29,CONSTANT.BAT_WIDTH/CONSTANT.RATE,CONSTANT.BAT_HEIGHT/CONSTANT.RATE,world,batFilter);
					batToCreate=false;
				}
				
				if(pballToCreate){
					pballToCreate=false;
					for(int i=0;i<10;i++)
						for(int j=0;j<16;j++){
							if(bricks[i][j].exist&&!(bricks[i][j] instanceof Brick4))
								bricks[i][j].body.getShapeList().m_isSensor=true;
						}
				}
				
				
				
				
				if(penetrateEffect)
				{
					penetrateCounter++;
					if(speedCounter>100)
					{
						penetrateEffect=false;
						penetrateCounter=0;
						for(int i=0;i<10;i++)
							for(int j=0;j<16;j++){
								if(bricks[i][j].exist&&!(bricks[i][j] instanceof Brick4))
									bricks[i][j].body.getShapeList().m_isSensor=false;
							}
					}
				}
				
				if(fireEffect)
				{
					fireCounter++;
					if(fireCounter>380)
					{
						fireEffect=false;
					    fireCounter=0;
					    ball.isFireBall=false;
					}
				}
				
				
				try 
				{
					Thread.sleep(15); 
				}
				catch (InterruptedException e) 
				{ 
					e.printStackTrace(); 
				}
		}
		
		backMusic.clip.stop();
		long endMili=System.currentTimeMillis();
		time=(int)(endMili-startMili)/1000;
		i=0; 
		j=0;
		k=0;
		while(i<score){                                 
			repaint();
			k++;
			if(k>=30000){
				if(i==0) i=score/2;
				j++;
				if(j%2000==1999){
					i++;
					j=0;
				}
			}
		}
		j=0;
		k=0;
		while(j<time){
			repaint();
			k++;
			if(k%8000==7999){
				j++;
				k=0;
			}
		}
	
	}
	
	//侦听碰撞事件并进行碰撞处理
	class myContactListener implements ContactListener{
		public void add(ContactPoint point) {
			ContactOperate(point.shape1.getBody(),point.shape2.getBody());
		}

		public void persist(ContactPoint point) {}
		public void remove(ContactPoint point) {}
		public void result(ContactResult point) {}		
	}
	
	//侦听鼠标移动事件，更新拍子位置
	class myMouseMotionListener implements MouseMotionListener{
		public void mouseDragged(MouseEvent e) {	
		}
		public void mouseMoved(MouseEvent e) {
			if(CONSTANT.batCanMove){
				mouseX=(e.getX()-CONSTANT.SCREEN_WIDTH/2)/CONSTANT.RATE;
				mouseY=(-e.getY()+CONSTANT.SCREEN_HEIGHT/2)/CONSTANT.RATE;
				if(mouseX>(CONSTANT.SCREEN_WIDTH/2-CONSTANT.BAT_WIDTH/2)/CONSTANT.RATE)  bat.body.setXForm(new Vec2((CONSTANT.SCREEN_WIDTH/2-CONSTANT.BAT_WIDTH/2)/CONSTANT.RATE,-29), 0);
				else if(mouseX<(-CONSTANT.SCREEN_WIDTH/2+CONSTANT.BAT_WIDTH/2)/CONSTANT.RATE)  bat.body.setXForm(new Vec2((-CONSTANT.SCREEN_WIDTH/2+CONSTANT.BAT_WIDTH/2)/CONSTANT.RATE,-29), 0);
				else  bat.body.setXForm(new Vec2(mouseX,-29), 0);
			}
		}
		
	}
	
	//侦听鼠标点击事件，指示物理引擎开始工作
	class myMouseListener implements MouseListener{
		@SuppressWarnings("unused")
		public void mouseClicked(MouseEvent e) {      //当鼠标点击时，物理引擎开始工作，拍子可移动,开始计时
			CONSTANT.shouldStep=true;
			CONSTANT.batCanMove=true;
			startMili=System.currentTimeMillis();	
		}
		
		public void mouseEntered(MouseEvent arg0) {	}
		public void mouseExited(MouseEvent arg0) {}		
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}	
	}
	
		
	//碰撞处理
	void ContactOperate(Body b1,Body b2){		
		//检测是否为 小球与砖块碰撞
		for(int i=0;i<10;i++)
			for(int j=0;j<16;j++){
				if(bricks[i][j].exist){
					if(b1==bricks[i][j].body||b2==bricks[i][j].body)
					{	
						CONSTANT.bump=true;
						if(ball.isFireBall){   //如果小球是火球 ,撞到一个砖块则附近九宫格内的砖块都算撞到一次
							if(i-1>=0&&j-1>=0&&bricks[i-1][j-1].exist)  bricks[i-1][j-1].whenHit(bodyToDestroy);
							if(i-1>=0&&bricks[i-1][j].exist)  bricks[i-1][j].whenHit(bodyToDestroy);
							if(i-1>=0&&j+1<=15&&bricks[i-1][j+1].exist)  bricks[i-1][j+1].whenHit(bodyToDestroy);
							if(j-1>=0&&bricks[i][j-1].exist)  bricks[i][j-1].whenHit(bodyToDestroy);
							if(j+1<=15&&bricks[i][j+1].exist)  bricks[i][j+1].whenHit(bodyToDestroy);
							if(i+1<=9&&j-1>=0&&bricks[i+1][j-1].exist)  bricks[i+1][j-1].whenHit(bodyToDestroy);
							if(i+1<=9&&bricks[i+1][j].exist)  bricks[i+1][j].whenHit(bodyToDestroy);
							if(i+1<=9&&j+1<=15&&bricks[i+1][j+1].exist)  bricks[i+1][j+1].whenHit(bodyToDestroy);
						}
						if(bricks[i][j].whenHit(bodyToDestroy))
						{
							if(Math.random()<0.1)
								bonusToCreate++;
							if(bricks[i][j] instanceof Brick1) score+=100;
							else if(bricks[i][j] instanceof Brick2) score+=200;
							else if(bricks[i][j] instanceof Brick3) score+=300;
						}
						//在whenHit里面不能直接destroybody，因为这时world忙自己的迭代，不会理你的。。
						//这时应把需要destroy的body用arraylist记录下来，到run里面每次step完后再一个个destroy
					}
					
				}
			}
		
		//检测是否为 小球与拍子碰撞
		if(b1==ball.body&&b2==bat.body || b2==ball.body&&b1==bat.body) 
			CONSTANT.bump=true;
	
		//检测是否为 小球与地板碰撞
		if(b1==ball.body&&b2==ground1.body || b2==ball.body&&b1==ground1.body) 
		{
			CONSTANT.fail=true;
			shouldShowScore=true;     
			ball.exist=false;
			bodyToDestroy.add(ball.body);
		}
		
		//检测是否为 bonus发生碰撞
		for(Bonus bonus: bonuses)
		{
			//bonus与地板碰撞
			if(b1==bonus.body&&b2==ground1.body || b2==bonus.body&&b1==ground1.body) 
			{
				bonus.exist=false;
				bodyToDestroy.add(bonus.body);
			}
			//bonus与拍子碰撞
			else if(b1==bonus.body&&b2==bat.body || b2==bonus.body&&b1==bat.body)  //bonus撞拍子
			{
				bonus.exist=false;
				bodyToDestroy.add(bonus.body);
				//若bonus为加长拍子
				if(bonus.whenHit()==bonus.attribute_widen1)  
				{
					bat.exist=false;
					bodyToDestroy.add(bat.body);
					CONSTANT.BAT_WIDTH=CONSTANT.BAT_WIDTH1;
					batToCreate=true;
					lengthEffect=true;
					lengthCounter=0;
				}
				//若bonus为超级加长拍子
				else if(bonus.whenHit()==bonus.attribute_widen2) 
				{
					bat.exist=false;
					bodyToDestroy.add(bat.body);
					CONSTANT.BAT_WIDTH=CONSTANT.BAT_WIDTH2;
					batToCreate=true;
					lengthEffect=true;
					lengthCounter=0;
				}
				//若bonus为缩短拍子
				else if(bonus.whenHit()==bonus.attribute_narrow1)  //缩短拍子
				{
					bat.exist=false;
					bodyToDestroy.add(bat.body);
					CONSTANT.BAT_WIDTH=CONSTANT.BAT_WIDTH3;
					batToCreate=true;
					lengthEffect=true;
					lengthCounter=0;
				}
				//若bonus为超级缩短拍子
				else if(bonus.whenHit()==bonus.attribute_narrow2) //缩短拍子
				{
					bat.exist=false;
					bodyToDestroy.add(bat.body);
					CONSTANT.BAT_WIDTH=CONSTANT.BAT_WIDTH4;
					batToCreate=true;
					lengthEffect=true;
					lengthCounter=0;
				}
				//若bonus为加快球速
				else if(bonus.whenHit()==bonus.attribute_fasten)
				{
					ball.body.setLinearVelocity(new Vec2(ball.body.getLinearVelocity().x*1.5f,ball.body.getLinearVelocity().y*1.5f));
				}
				//若bonus为减小球速
				else if(bonus.whenHit()==bonus.attribute_slow)   //减慢球速
				{
					ball.body.setLinearVelocity(new Vec2(ball.body.getLinearVelocity().x/1.5f,ball.body.getLinearVelocity().y/1.5f));
				}
				//若bonus为 变穿透球
				else if(bonus.whenHit()==bonus.attribute_penetrate)
				{
					pballToCreate=true;
					penetrateEffect=true;
					penetrateCounter=0;
				}
				//若bonus为 变火球
				else if(bonus.whenHit()==bonus.attribute_fire)    //设置小球为火球
				{
					ball.isFireBall=true;
					fireEffect=true;
					fireCounter=0;
				}
				//若bonus为直接死亡
				else if(bonus.whenHit()==bonus.attribute_death)  //游戏结束
				{
					CONSTANT.fail=true;
				}
				//若bonus为直接通关
				else if(bonus.whenHit()==bonus.attribute_success)                                          //游戏获胜
				{
					CONSTANT.win=true;
				}
			}
		}
	}
	
	//设置各种刚体的碰撞过滤器
	void createFilter(){
		brickFilter=new FilterData();
		brickFilter.categoryBits=2;			brickFilter.maskBits=4;
		
		ballFilter=new FilterData();
		ballFilter.categoryBits=4;			ballFilter.maskBits=42;
		
		batFilter=new FilterData();
		batFilter.categoryBits=8;			batFilter.maskBits=20;
		
		bonusFilter=new FilterData();
		bonusFilter.categoryBits=16;			bonusFilter.maskBits=40;
		
		groundFilter=new FilterData();
		groundFilter.categoryBits=32;			groundFilter.maskBits=20;
	}
	
	
	private Image createImage(float sCREEN_WIDTH, float sCREEN_HEIGHT) {
		return null;
	}	
}
