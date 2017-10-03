package GameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import GameObjects.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;
import org.jbox2d.collision.*;
import org.jbox2d.common.*;

//Panel
class gameViewPanel extends JPanel implements Runnable{

	private int choice=0;

	private Thread t;
	private Thread backM;
	private backgroundMusic backMusic;

	private AABB worldAABB;
	private World world;
	private Vec2 gravity;

	private Brick[][] bricks=new Brick[10][16];
	private Ball ball;
	private Bat bat;
	private Ground ground1;
	private Ground ground2;
	private Ground ground3;
	private Ground ground4;

	FilterData brickFilter;
	FilterData ballFilter;
	FilterData batFilter;
	FilterData bonusFilter;
	FilterData groundFilter;

	//ʵʱ�������
	private float mouseX=0,mouseY=0;
	
	//���������
	myMouseMotionListener mouseHandler1;
	myMouseListener mouseHandler2;
	myContactListener contactHandler;
	
	//��ݻٸ����¼
	private ArrayList<Body> bodyToDestroy=new ArrayList<Body>();
		
	//bonusЧ��ָʾ
	private ArrayList<Bonus> bonuses=new ArrayList<Bonus>();
	private int bonusToCreate=0;
	private boolean batToCreate=false;
	private boolean pballToCreate=false;
	private boolean lengthEffect=true;
	private int lengthCounter=0;
	private boolean speedEffect=true;
	private int speedCounter=0;
	private boolean penetrateEffect=true;
	private int penetrateCounter=0;
	private boolean fireEffect=true;
	private int fireCounter=0;

	private Image offScreenImage;

	private int score=0;
	boolean shouldShowScore=false;

	int i=0;
	int j=0;
	int k=0;
	
	long startMili;
	private int time;
	int addScore;

	gameViewPanel(int choice){
		this.choice=choice;
		initiateWorld();
		this.t=new Thread(this);
		this.backMusic=new backgroundMusic(this);
		this.backM=new Thread(this.backMusic);
		this.t.start();
		this.backM.start();
	}

	private void initiateWorld(){
		CONSTANT.initImage(this);
		this.offScreenImage=this.createImage(CONSTANT.SCREEN_WIDTH,CONSTANT.SCREEN_HEIGHT);
		this.worldAABB=new AABB();
		this.worldAABB.lowerBound.set((float)-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE, (float)-CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE);
		this.worldAABB.upperBound.set((float)CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE, (float)CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE);
		this.gravity=new Vec2((float)0, (float)-1);
		this.world = new World(worldAABB, gravity, CONSTANT.doSleep);
		this.world.setGravity(gravity);
		
		createFilter();
		
		float brick_x;
		float brick_y;
		float brick_w;
		float brick_h;
		this.ground1=new Ground(0,-29.999f,80,0.001f,true,world,groundFilter);
		this.ground2=new Ground(0,29.999f,80,0.001f,true,world,groundFilter);
		this.ground3=new Ground(-39.999f,0,0.001f,60,true,world,groundFilter);
		this.ground4=new Ground(39.999f,0,0.001f,60,true,world,groundFilter);
		
		if(choice==9)
		{
			for(int i=0;i<10;i++)
				for(int j=0;j<16;j++){
					brick_x=-CONSTANT.SCREEN_WIDTH / 2 / CONSTANT.RATE + CONSTANT.BRICK_WIDTH / CONSTANT.RATE * j + CONSTANT.BRICK_WIDTH / CONSTANT.RATE / 2;
					brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
					brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
					brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
					if(Math.random()<=0.75)
					{
						if (Math.random()<=0.2) {
							this.bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,this.world,brickFilter);
						} else if (Math.random()<=0.4&&Math.random()>0.2) {
							this.bricks[i][j]=new Brick2(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
						} else if (Math.random()<=0.8&&Math.random()>0.4) {
							this.bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
						} else {
							this.bricks[i][j]=new Brick4(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
						}
					}
					else {
						this.bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,false,world,brickFilter);
					}
				}
		} else if(choice==1) {
			for(int i=0;i<10;i++) {
				for(int j=0;j<16;j++) {
					brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
					brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
					brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
					brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
					this.bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
				}
			}
		} else if(choice==2) {
			for(int i=0;i<10;i++) {
				for(int j=0;j<16;j++)
				{
					brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
					brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
					brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
					brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
					bricks[i][j]=new Brick2(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
				}
			}
		} else if(choice==3) {
			for(int i=0;i<10;i++) {
				for(int j=0;j<16;j++)
				{
					brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
					brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
					brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
					brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
					bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
				}
			}
		} else if(choice==4) {
			for(i=0;i<5;i++) {
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
			for(i=5;i<10;i++) {
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
			for(int i=0;i<10;i++) {
				for(int j=0;j<16;j++) {
					if(!(bricks[i][j] instanceof Brick3)) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
			}
		} else if(choice==5) {
			for(int i=0;i<10;i++) {
				for(int j=0;j<16;j++) {
					if((i==3||i==6)&&j!=0&&j!=15) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick4(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					} else {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
			}
		} else if(choice==6) {
			for(int i=0;i<10;i++) {
				for(int j=0;j<16;j++) {
					if((i%3==0)&&j!=0&&j!=15) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					} else if((i%3==1)&&j!=0&&j!=15) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick2(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					} else {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
			}
		} else if(choice==7) {
			for(int i=0;i<10;i++) {
				for(int j=0;j<16;j++) {
					if((i%5==0)&&j!=0&&j!=15){
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick4(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					} else if((i%5==1)&&j!=0&&j!=15) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					} else if((i%5==2)&&j!=0&&j!=15) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick2(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					} else if((i%5==3)&&j!=0&&j!=15) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					} else {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
			}
		} else {
			for(i=2;i<=7;i++){
				if(i==2) {
					for(j=5;j<=7;j++) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
					for(j=10;j<=12;j++) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
				if(i==3||i==4) {
					for(j=4;j<=8;j++) {
						if(i==4&&j==6) {
							brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
							brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
							brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
							brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
							bricks[i][j]=new Brick4(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
						} else {
							brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
							brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
							brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
							brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
							bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
						}
					}
					for(j=11;j<=11;j++) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
				if(i==5) {
					for(j=4;j<=10;j++) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
				if(i==6) {
					for(j=5;j<=9;j++) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
				if(i==7) {
					for(j=6;j<=8;j++) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick3(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
			}
			for(int i=0;i<10;i++) {
				for(int j=0;j<16;j++) {
					if(!(bricks[i][j] instanceof Brick3)&&!(bricks[i][j] instanceof Brick4)) {
						brick_x=-CONSTANT.SCREEN_WIDTH/2/CONSTANT.RATE+CONSTANT.BRICK_WIDTH/CONSTANT.RATE*j+CONSTANT.BRICK_WIDTH/CONSTANT.RATE/2;
						brick_y=CONSTANT.SCREEN_HEIGHT/2/CONSTANT.RATE-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE*i-CONSTANT.BRICK_HEIGHT/CONSTANT.RATE/2;
						brick_w=CONSTANT.BRICK_WIDTH/CONSTANT.RATE;
						brick_h=CONSTANT.BRICK_HEIGHT/CONSTANT.RATE;
						bricks[i][j]=new Brick1(brick_x,brick_y,brick_w,brick_h,true,world,brickFilter);
					}
				}
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

	public void paint(Graphics g)
	{
		if ((!CONSTANT.win)&&(!CONSTANT.fail)) {
			g.clearRect(0,0,(int)CONSTANT.SCREEN_WIDTH+10,(int)CONSTANT.SCREEN_HEIGHT);		
			for(int i=0;i<10;i++) {
				for(int j=0;j<16;j++) {
					bricks[i][j].draw(g);
				}
			}
			ball.draw(g);
			bat.draw(g);
			for(Bonus bonus: bonuses){
				bonus.draw(g);
			}
			
			if(!CONSTANT.shouldStep)	
				g.drawImage(CONSTANT.click, 200, 450,null);
		} else if (CONSTANT.win) {
			g.clearRect(0,0,(int)CONSTANT.SCREEN_WIDTH+10,(int)CONSTANT.SCREEN_HEIGHT);		
			g.drawImage(CONSTANT.youWin,0,0,null);
			g.setFont(new Font("Arial",Font.BOLD,50));
			if(i==score) g.drawString("time:"+Integer.toString(j)+"s", 295, 320);
			g.drawString("score:"+Integer.toString(i), 300, 380);
		} else {
			g.clearRect(0,0,(int)CONSTANT.SCREEN_WIDTH+10,(int)CONSTANT.SCREEN_HEIGHT);	
			g.drawImage(CONSTANT.gameOver,0,0,null);
			g.setFont(new Font("Arial",Font.BOLD,50));
			if(i==score) g.drawString("time:"+Integer.toString(j)+"s", 295, 320);
			g.drawString("score:"+Integer.toString(i), 300, 380);
		}
	}
	
	public void update(Graphics g) {
		Graphics offG=offScreenImage.getGraphics();
		paint(offG);
		g.drawImage(offScreenImage,0,0,null);
	}


	public void run() {
		while(!CONSTANT.shouldStep)
			repaint();

		while((!CONSTANT.win) && (!CONSTANT.fail)) {
			this.world.step(CONSTANT.TIME_STEP, CONSTANT.ITERA);

			repaint();
			if (this.lengthEffect) {
				this.lengthCounter++;
				if(this.lengthCounter>380) {
					this.lengthEffect=false;
					this.lengthCounter=0;
					this.bodyToDestroy.add(bat.body);
					this.batToCreate=true;
					CONSTANT.BAT_WIDTH=CONSTANT.BAT_WIDTH0;
				}
			}

			for(Body body: this.bodyToDestroy) {
				this.world.destroyBody(body);
			}
			this.bodyToDestroy.clear();

			for(int i=0;i<this.bonusToCreate;i++) {
				this.bonuses.add(new Bonus(((float)(Math.random()*(CONSTANT.SCREEN_WIDTH-40)+20)-CONSTANT.SCREEN_WIDTH/2)/CONSTANT.RATE,(CONSTANT.SCREEN_HEIGHT/2-CONSTANT.BONUS_RADIUS)/CONSTANT.RATE,CONSTANT.BONUS_RADIUS/CONSTANT.RATE,world,bonusFilter));
			}
			this.bonusToCreate=0;

			if(this.batToCreate) {
				this.bat=new Bat(mouseX,-29,CONSTANT.BAT_WIDTH/CONSTANT.RATE,CONSTANT.BAT_HEIGHT/CONSTANT.RATE,world,batFilter);
				this.batToCreate=false;
			}

			if(this.pballToCreate) {
				this.pballToCreate=false;
				for(int i=0;i<10;i++) {
					for(int j=0;j<16;j++) {
						if(this.bricks[i][j].exist&&!(this.bricks[i][j] instanceof Brick4))
							this.bricks[i][j].body.getShapeList().m_isSensor=true;
					}
				}
			}

			if(this.penetrateEffect) {
				this.penetrateCounter++;
				if(this.speedCounter>100) {
					this.penetrateEffect=false;
					this.penetrateCounter=0;
					for(int i=0;i<10;i++) {
						for(int j=0;j<16;j++) {
							if(this.bricks[i][j].exist&&!(this.bricks[i][j] instanceof Brick4))
								this.bricks[i][j].body.getShapeList().m_isSensor=false;
						}
					}
				}
			}

			if(this.fireEffect) {
				this.fireCounter++;
				if(this.fireCounter>380) {
					this.fireEffect=false;
					this.fireCounter=0;
					this.ball.isFireBall=false;
				}
			}

			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		this.backMusic.player.close();
		long endMili=System.currentTimeMillis();
		this.time=(int)(endMili-startMili)/1000;
		i=0; 
		j=0;
		k=0;
		while(i<this.score) {
			repaint();
			k++;
			if(k>=30000) {
				if (i==0) {
					i=this.score/2;
				}
				j++;
				if (j%2000==1999) {
					i++;
					j=0;
				}
			}
		}
		j=0;
		k=0;
		while (j<this.time) {
			repaint();
			k++;
			if (k%8000==7999){
				j++;
				k=0;
			}
		}
	
	}

	class myContactListener implements ContactListener {
		public void add(ContactPoint point) {
			ContactOperate(point.shape1.getBody(),point.shape2.getBody());
		}

		public void persist(ContactPoint point) {}
		public void remove(ContactPoint point) {}
		public void result(ContactResult point) {}		
	}

	class myMouseMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {	
		}
		public void mouseMoved(MouseEvent e) {
			if(CONSTANT.batCanMove) {
				mouseX=(e.getX()-CONSTANT.SCREEN_WIDTH/2)/CONSTANT.RATE;
				mouseY=(-e.getY()+CONSTANT.SCREEN_HEIGHT/2)/CONSTANT.RATE;
				if(mouseX>(CONSTANT.SCREEN_WIDTH/2-CONSTANT.BAT_WIDTH/2)/CONSTANT.RATE)  bat.body.setXForm(new Vec2((CONSTANT.SCREEN_WIDTH/2-CONSTANT.BAT_WIDTH/2)/CONSTANT.RATE,-29), 0);
				else if(mouseX<(-CONSTANT.SCREEN_WIDTH/2+CONSTANT.BAT_WIDTH/2)/CONSTANT.RATE)  bat.body.setXForm(new Vec2((-CONSTANT.SCREEN_WIDTH/2+CONSTANT.BAT_WIDTH/2)/CONSTANT.RATE,-29), 0);
				else  bat.body.setXForm(new Vec2(mouseX,-29), 0);
			}
		}
		
	}

	class myMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			CONSTANT.shouldStep=true;
			CONSTANT.batCanMove=true;
			startMili=System.currentTimeMillis();	
		}
		
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}		
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}	
	}
	

	void ContactOperate(Body b1,Body b2) {
		for(int i=0;i<10;i++) {
			for(int j=0;j<16;j++) {
				if(bricks[i][j].exist) {
					if(b1==bricks[i][j].body||b2==bricks[i][j].body) {
						if(ball.isFireBall) {
							if(i-1>=0&&j-1>=0&&bricks[i-1][j-1].exist)  bricks[i-1][j-1].whenHit(bodyToDestroy);
							if(i-1>=0&&bricks[i-1][j].exist)  bricks[i-1][j].whenHit(bodyToDestroy);
							if(i-1>=0&&j+1<=15&&bricks[i-1][j+1].exist)  bricks[i-1][j+1].whenHit(bodyToDestroy);
							if(j-1>=0&&bricks[i][j-1].exist)  bricks[i][j-1].whenHit(bodyToDestroy);
							if(j+1<=15&&bricks[i][j+1].exist)  bricks[i][j+1].whenHit(bodyToDestroy);
							if(i+1<=9&&j-1>=0&&bricks[i+1][j-1].exist)  bricks[i+1][j-1].whenHit(bodyToDestroy);
							if(i+1<=9&&bricks[i+1][j].exist)  bricks[i+1][j].whenHit(bodyToDestroy);
							if(i+1<=9&&j+1<=15&&bricks[i+1][j+1].exist)  bricks[i+1][j+1].whenHit(bodyToDestroy);
						}
						if(bricks[i][j].whenHit(bodyToDestroy)) {
							if(Math.random()<0.1) {
								bonusToCreate++;
							}
							if(bricks[i][j] instanceof Brick1) {
								score+=100;
							} else if(bricks[i][j] instanceof Brick2) {
								score+=200;
							} else if(bricks[i][j] instanceof Brick3) {
								score+=300;
							}
						}
					}
				}
			}
		}


		if (b1==ball.body&&b2==ground1.body || b2==ball.body&&b1==ground1.body) {
			CONSTANT.fail=true;
			shouldShowScore=true;     
			ball.exist=false;
			bodyToDestroy.add(ball.body);
		}

		for(Bonus bonus: bonuses) {
			if(b1==bonus.body&&b2==ground1.body || b2==bonus.body&&b1==ground1.body) {
				bonus.exist=false;
				bodyToDestroy.add(bonus.body);
			} else if (b1==bonus.body&&b2==bat.body || b2==bonus.body&&b1==bat.body) {
				bonus.exist=false;
				bodyToDestroy.add(bonus.body);
				if(bonus.whenHit()==bonus.attribute_widen1) {
					bat.exist=false;
					bodyToDestroy.add(bat.body);
					CONSTANT.BAT_WIDTH=CONSTANT.BAT_WIDTH1;
					batToCreate=true;
					lengthEffect=true;
					lengthCounter=0;
				} else if(bonus.whenHit()==bonus.attribute_widen2) {
					bat.exist=false;
					bodyToDestroy.add(bat.body);
					CONSTANT.BAT_WIDTH=CONSTANT.BAT_WIDTH2;
					batToCreate=true;
					lengthEffect=true;
					lengthCounter=0;
				} else if(bonus.whenHit()==bonus.attribute_narrow1) {
					bat.exist=false;
					bodyToDestroy.add(bat.body);
					CONSTANT.BAT_WIDTH=CONSTANT.BAT_WIDTH3;
					batToCreate=true;
					lengthEffect=true;
					lengthCounter=0;
				} else if(bonus.whenHit()==bonus.attribute_narrow2) {
					bat.exist=false;
					bodyToDestroy.add(bat.body);
					CONSTANT.BAT_WIDTH=CONSTANT.BAT_WIDTH4;
					batToCreate=true;
					lengthEffect=true;
					lengthCounter=0;
				} else if(bonus.whenHit()==bonus.attribute_fasten) {
					ball.body.setLinearVelocity(new Vec2(ball.body.getLinearVelocity().x*1.5f,ball.body.getLinearVelocity().y*1.5f));
				} else if(bonus.whenHit()==bonus.attribute_slow) {
					ball.body.setLinearVelocity(new Vec2(ball.body.getLinearVelocity().x/1.5f,ball.body.getLinearVelocity().y/1.5f));
				} else if(bonus.whenHit()==bonus.attribute_penetrate) {
					pballToCreate=true;
					penetrateEffect=true;
					penetrateCounter=0;
				} else if(bonus.whenHit()==bonus.attribute_fire) {
					ball.isFireBall=true;
					fireEffect=true;
					fireCounter=0;
				} else if(bonus.whenHit()==bonus.attribute_death) {
					CONSTANT.fail=true;
				} else if(bonus.whenHit()==bonus.attribute_success) {
					CONSTANT.win=true;
				}
			}
		}
	}

	void createFilter() {
		brickFilter=new FilterData();
		brickFilter.categoryBits=2;
		brickFilter.maskBits=4;
		
		ballFilter=new FilterData();
		ballFilter.categoryBits=4;
		ballFilter.maskBits=42;
		
		batFilter=new FilterData();
		batFilter.categoryBits=8;
		batFilter.maskBits=20;
		
		bonusFilter=new FilterData();
		bonusFilter.categoryBits=16;
		bonusFilter.maskBits=40;
		
		groundFilter=new FilterData();
		groundFilter.categoryBits=32;
		groundFilter.maskBits=20;
	}
	
	
	private Image createImage(float sCREEN_WIDTH, float sCREEN_HEIGHT) {
		return null;
	}	
}
