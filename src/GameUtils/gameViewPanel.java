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
class gameViewPanel extends JPanel implements Runnable {

	private int choice = 0;

	private Thread t;
	private Thread backM;
	private backgroundMusic backMusic;

	private AABB worldAABB;
	private World world;
	private Vec2 gravity;

	private Brick[][] bricks = new Brick[10][16];
	private Ball ball;
	private Bat bat;
	private Ground ground1;
	private Ground ground2;
	private Ground ground3;
	private Ground ground4;

	private FilterData brickFilter;
	private FilterData ballFilter;
	private FilterData batFilter;
	private FilterData bonusFilter;
	private FilterData groundFilter;

	private float mouseX = 0, mouseY = 0;

	private myMouseMotionListener mouseHandler1;
	private myMouseListener mouseHandler2;
	private myContactListener contactHandler;

	private ArrayList<Body> bodyToDestroy = new ArrayList<Body>();

	private ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
	private int bonusToCreate = 0;
	private boolean batToCreate = false;
	private boolean pballToCreate = false;
	private boolean lengthEffect = true;
	private int lengthCounter = 0;
	private boolean speedEffect = true;
	private int speedCounter = 0;
	private boolean penetrateEffect = true;
	private int penetrateCounter = 0;
	private boolean fireEffect = true;
	private int fireCounter = 0;

	private Image offScreenImage;

	private int score = 0;
	boolean shouldShowScore = false;

	int i = 0;
	int j = 0;
	int k = 0;
	
	long startMili;
	private int time;
	int addScore;

	gameViewPanel(int choice) {
		this.choice = choice;
		initiateWorld();
		this.t = new Thread(this);
		this.backMusic = new backgroundMusic(this);
		this.backM = new Thread(this.backMusic);
		this.t.start();
		this.backM.start();
	}

	private float getStartX(int j) {
		float sw = CONSTANT.SCREEN_WIDTH / 2 / CONSTANT.RATE;
		float bw = CONSTANT.BRICK_WIDTH / CONSTANT.RATE;
		return -1 * sw + bw * j + bw / 2;
	}

	private float getStartY(int i) {
		float sh = CONSTANT.SCREEN_HEIGHT / 2 / CONSTANT.RATE;
		float bh = CONSTANT.BRICK_HEIGHT / CONSTANT.RATE;
		return sh - bh * i - bh / 2;
	}

	private Brick createBrick(int i, int j, int type) {
		float brick_w = CONSTANT.BRICK_WIDTH / CONSTANT.RATE;
		float brick_h = CONSTANT.BRICK_HEIGHT / CONSTANT.RATE;
		float brick_x = getStartX(j);
		float brick_y = getStartY(i);
		if (type == 2) {
			return new Brick2(brick_x, brick_y, brick_w, brick_h, true, this.world, this.brickFilter);
		} else if (type == 3) {
			return new Brick3(brick_x, brick_y, brick_w, brick_h, true, this.world, this.brickFilter);
		} else if (type == 4) {
			return new Brick4(brick_x, brick_y, brick_w, brick_h, true, this.world, this.brickFilter);
		} else {
			return new Brick1(brick_x, brick_y, brick_w, brick_h, true, this.world, this.brickFilter);
		}
	}

	private Brick createBrick(float brick_x, float brick_y, int type) {
		float brick_w = CONSTANT.BRICK_WIDTH / CONSTANT.RATE;
		float brick_h = CONSTANT.BRICK_HEIGHT / CONSTANT.RATE;
		if (type == 2) {
			return new Brick2(brick_x, brick_y, brick_w, brick_h, true, this.world, this.brickFilter);
		} else if (type == 3) {
			return new Brick3(brick_x, brick_y, brick_w, brick_h, true, this.world, this.brickFilter);
		} else if (type == 4) {
			return new Brick4(brick_x, brick_y, brick_w, brick_h, true, this.world, this.brickFilter);
		} else {
			return new Brick1(brick_x, brick_y, brick_w, brick_h, true, this.world, this.brickFilter);
		}
	}

	private void initiateWorld() {
		CONSTANT.initImage(this);
		this.offScreenImage = this.createImage(CONSTANT.SCREEN_WIDTH, CONSTANT.SCREEN_HEIGHT);
		this.worldAABB = new AABB();
		this.worldAABB.lowerBound.set((float)-CONSTANT.SCREEN_WIDTH / 2 / CONSTANT.RATE, (float)-CONSTANT.SCREEN_HEIGHT / 2 / CONSTANT.RATE);
		this.worldAABB.upperBound.set((float)CONSTANT.SCREEN_WIDTH / 2 / CONSTANT.RATE, (float)CONSTANT.SCREEN_HEIGHT / 2 / CONSTANT.RATE);
		this.gravity = new Vec2((float)0, (float)-1);
		this.world = new World(worldAABB, gravity, CONSTANT.doSleep);
		this.world.setGravity(gravity);
		
		createFilter();
		
		float brick_x;
		float brick_y;
		this.ground1 = new Ground(0,-29.999f,80,0.001f,true, world, groundFilter);
		this.ground2 = new Ground(0,29.999f,80,0.001f,true, world, groundFilter);
		this.ground3 = new Ground(-39.999f,0,0.001f,60,true, world, groundFilter);
		this.ground4 = new Ground(39.999f,0,0.001f,60,true, world, groundFilter);

		if(choice == 9) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 16; j++) {
					brick_x = getStartX(j);
					brick_y = getStartY(i);
					if (Math.random()<=0.75) {
						if (Math.random()<=0.2) {
							this.bricks[i][j] = this.createBrick(brick_x, brick_y, 1);
						} else if (Math.random() <= 0.4 && Math.random() > 0.2) {
							this.bricks[i][j] = this.createBrick(brick_x, brick_y, 2);
						} else if (Math.random() <= 0.8 && Math.random() > 0.4) {
							this.bricks[i][j] = this.createBrick(brick_x, brick_y, 3);
						} else {
							this.bricks[i][j] = this.createBrick(brick_x, brick_y, 4);
						}
					} else {
						this.bricks[i][j] = this.createBrick(brick_x, brick_y, 1);
					}
				}
			}
		} else if (choice == 1) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 16; j++) {
					this.bricks[i][j] = this.createBrick(i, j, 1);
				}
			}
		} else if (choice == 2) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 16; j++) {
					this.bricks[i][j] = this.createBrick(i, j, 2);
				}
			}
		} else if (choice == 3) {
			for (int i = 0; i < 10; i++) {
				for(int j = 0; j < 16; j++) {
					this.bricks[i][j] = this.createBrick(i, j, 3);
				}
			}
		} else if ( choice == 4) {
			for (i = 0; i < 5; i++) {
				j = i;
				this.bricks[i][j] = this.createBrick(i, j, 3);
				j = 15 - i;
				this.bricks[i][j] = this.createBrick(i, j, 3);
			}
			for (i = 5; i < 10; i++) {
				j = 9 - i;
				this.bricks[i][j] = this.createBrick(i, j, 3);
				j = i + 6;
				this.bricks[i][j] = this.createBrick(i, j, 3);
			}
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 16; j++) {
					if (!(bricks[i][j] instanceof Brick3)) {
						this.bricks[i][j] = this.createBrick(i, j, 1);
					}
				}
			}
		} else if (choice == 5) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 16; j++) {
					if ((i == 3 || i == 6) && (j != 0) && (j != 15)) {
						this.bricks[i][j] = this.createBrick(i, j, 4);
					} else {
						this.bricks[i][j] = this.createBrick(i, j, 1);
					}
				}
			}
		} else if (choice == 6) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 16; j++) {
					if ((i%3 == 0) && (j != 0) && (j != 15)) {
						this.bricks[i][j] = this.createBrick(i, j, 1);
					} else if((i%3==1)&&j!=0&&j!=15) {
						this.bricks[i][j] = this.createBrick(i, j, 2);
					} else {
						this.bricks[i][j] = this.createBrick(i, j, 3);
					}
				}
			}
		} else if (choice == 7) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 16; j++) {
					if ((i%5 == 0) && (j != 0) && (j != 15)){
						this.bricks[i][j] = this.createBrick(i, j, 4);
					} else if ((i%5 == 1) && (j != 0) && (j != 15)) {
						this.bricks[i][j] = this.createBrick(i, j, 1);
					} else if ((i%5 == 2) && (j != 0) && (j != 15)) {
						this.bricks[i][j] = this.createBrick(i, j, 2);
					} else if ((i%5 == 3) && (j != 0) && (j != 15)) {
						this.bricks[i][j] = this.createBrick(i, j, 3);
					} else {
						this.bricks[i][j] = this.createBrick(i, j, 1);
					}
				}
			}
		} else {
			for (i = 2; i <= 7; i++){
				if (i == 2) {
					for (j = 5; j <= 7; j++) {
						this.bricks[i][j] = this.createBrick(i, j, 3);
					}
					for (j = 10; j <= 12; j++) {
						this.bricks[i][j] = this.createBrick(i, j, 3);
					}
				} else if (i == 3 || i == 4) {
					for (j = 4; j <= 8; j++) {
						if (i == 4 && j == 6) {
							this.bricks[i][j] = this.createBrick(i, j, 4);
						} else {
							this.bricks[i][j] = this.createBrick(i, j, 3);
						}
					}
					for (j = 11; j <= 11; j++) {
						this.bricks[i][j] = this.createBrick(i, j, 3);
					}
				} else if (i == 5) {
					for (j = 4; j <= 10; j++) {
						this.bricks[i][j] = this.createBrick(i, j, 3);
					}
				} else if (i == 6) {
					for (j = 5; j <= 9; j++) {
						this.bricks[i][j] = this.createBrick(i, j, 3);
					}
				} else if (i == 7) {
					for (j = 6; j <= 8; j++) {
						this.bricks[i][j] = this.createBrick(i, j, 3);
					}
				}
			}
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 16; j++) {
					if(!(bricks[i][j] instanceof Brick3) && !(bricks[i][j] instanceof Brick4)) {
						this.bricks[i][j] = this.createBrick(i, j, 1);
					}
				}
			}
		}
		this.ball = new Ball(0, -27, CONSTANT.BALL_RADIUS / CONSTANT.RATE, this.world, this.ballFilter);
		this.bat = new Bat(0, -29, CONSTANT.BAT_WIDTH / CONSTANT.RATE, CONSTANT.BAT_HEIGHT / CONSTANT.RATE, this.world, this.batFilter);
		this.mouseHandler1 = new myMouseMotionListener();
		this.mouseHandler2 = new myMouseListener();
		this.addMouseMotionListener(this.mouseHandler1);
		this.addMouseListener(this.mouseHandler2);
		this.contactHandler = new myContactListener();
		this.world.setContactListener(this.contactHandler);
	}

	public void paint(Graphics g)
	{
		if ((!CONSTANT.win) && (!CONSTANT.fail)) {
			g.clearRect(0, 0, (int)CONSTANT.SCREEN_WIDTH + 10, (int)CONSTANT.SCREEN_HEIGHT);
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 16; j++) {
					this.bricks[i][j].draw(g);
				}
			}
			this.ball.draw(g);
			this.bat.draw(g);
			for (Bonus bonus: this.bonuses) {
				bonus.draw(g);
			}
			
			if(!CONSTANT.shouldStep) {
				g.drawImage(CONSTANT.click, 200, 450,null);
			}
		} else if (CONSTANT.win) {
			g.clearRect(0, 0, (int)CONSTANT.SCREEN_WIDTH + 10, (int)CONSTANT.SCREEN_HEIGHT);
			g.drawImage(CONSTANT.youWin, 0, 0, null);
			g.setFont(new Font("Arial", Font.BOLD, 50));
			if (i == score) {
				g.drawString("time:" + Integer.toString(j) + "s", 295, 320);
			}
			g.drawString("score:" + Integer.toString(i), 300, 380);
		} else {
			g.clearRect(0, 0, (int)CONSTANT.SCREEN_WIDTH + 10, (int)CONSTANT.SCREEN_HEIGHT);
			g.drawImage(CONSTANT.gameOver, 0, 0, null);
			g.setFont(new Font("Arial", Font.BOLD, 50));
			if (i == score) {
				g.drawString("time:" + Integer.toString(j) + "s", 295, 320);
			}
			g.drawString("score:" + Integer.toString(i), 300, 380);
		}
	}
	
	public void update(Graphics g) {
		Graphics offG = offScreenImage.getGraphics();
		this.paint(offG);
		g.drawImage(offScreenImage,0,0,null);
	}


	public void run() {
		while(!CONSTANT.shouldStep) {
			repaint();
		}

		while((!CONSTANT.win) && (!CONSTANT.fail)) {
			this.world.step(CONSTANT.TIME_STEP, CONSTANT.ITERA);

			repaint();
			if (this.lengthEffect) {
				this.lengthCounter++;
				if (this.lengthCounter > 380) {
					this.lengthEffect = false;
					this.lengthCounter = 0;
					this.bodyToDestroy.add(this.bat.body);
					this.batToCreate = true;
					CONSTANT.BAT_WIDTH = CONSTANT.BAT_WIDTH0;
				}
			}

			for (Body body: this.bodyToDestroy) {
				this.world.destroyBody(body);
			}
			this.bodyToDestroy.clear();

			for (int i = 0;i < this.bonusToCreate; i++) {
				this.bonuses.add(new Bonus(((float)(Math.random() * (CONSTANT.SCREEN_WIDTH - 40) + 20) - CONSTANT.SCREEN_WIDTH / 2) / CONSTANT.RATE, (CONSTANT.SCREEN_HEIGHT / 2 - CONSTANT.BONUS_RADIUS) / CONSTANT.RATE, CONSTANT.BONUS_RADIUS / CONSTANT.RATE, this.world, this.bonusFilter));
			}
			this.bonusToCreate = 0;

			if (this.batToCreate) {
				this.bat = new Bat(mouseX, -29, CONSTANT.BAT_WIDTH / CONSTANT.RATE, CONSTANT.BAT_HEIGHT / CONSTANT.RATE, this.world, this.batFilter);
				this.batToCreate = false;
			}

			if (this.pballToCreate) {
				this.pballToCreate = false;
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 16; j++) {
						if (this.bricks[i][j].exist && !(this.bricks[i][j] instanceof Brick4)) {
							this.bricks[i][j].body.getShapeList().m_isSensor = true;
						}
					}
				}
			}

			if (this.penetrateEffect) {
				this.penetrateCounter++;
				if (this.speedCounter > 100) {
					this.penetrateEffect = false;
					this.penetrateCounter = 0;
					for (int i = 0; i < 10; i++) {
						for (int j = 0; j < 16; j++) {
							if(this.bricks[i][j].exist && !(this.bricks[i][j] instanceof Brick4)) {
								this.bricks[i][j].body.getShapeList().m_isSensor = false;
							}
						}
					}
				}
			}

			if (this.fireEffect) {
				this.fireCounter++;
				if (this.fireCounter > 380) {
					this.fireEffect = false;
					this.fireCounter = 0;
					this.ball.isFireBall = false;
				}
			}

			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		this.backMusic.player.close();
		long endMili = System.currentTimeMillis();
		this.time = (int)(endMili - startMili) / 1000;
		i = 0;
		j = 0;
		k = 0;
		while (i < this.score) {
			repaint();
			k++;
			if (k >= 30000) {
				if (i == 0) {
					i = this.score/2;
				}
				j++;
				if (j % 2000 == 1999) {
					i++;
					j = 0;
				}
			}
		}
		j = 0;
		k = 0;
		while (j < this.time) {
			repaint();
			k++;
			if (k % 8000 == 7999){
				j++;
				k = 0;
			}
		}
	
	}

	class myContactListener implements ContactListener {
		public void add(ContactPoint point) {
			ContactOperate(point.shape1.getBody(), point.shape2.getBody());
		}

		public void persist(ContactPoint point) {}
		public void remove(ContactPoint point) {}
		public void result(ContactResult point) {}		
	}

	class myMouseMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {	
		}
		public void mouseMoved(MouseEvent e) {
			if (CONSTANT.batCanMove) {
				mouseX = (e.getX() - CONSTANT.SCREEN_WIDTH / 2) / CONSTANT.RATE;
				mouseY = (-e.getY() + CONSTANT.SCREEN_HEIGHT / 2) / CONSTANT.RATE;
				if (mouseX > (CONSTANT.SCREEN_WIDTH / 2 - CONSTANT.BAT_WIDTH / 2) / CONSTANT.RATE) {
					bat.body.setXForm(new Vec2((CONSTANT.SCREEN_WIDTH / 2 - CONSTANT.BAT_WIDTH / 2) / CONSTANT.RATE, -29), 0);
				} else if (mouseX < (-CONSTANT.SCREEN_WIDTH / 2 + CONSTANT.BAT_WIDTH / 2) / CONSTANT.RATE) {
					bat.body.setXForm(new Vec2((-CONSTANT.SCREEN_WIDTH / 2 + CONSTANT.BAT_WIDTH / 2) / CONSTANT.RATE, -29), 0);
				} else {
					bat.body.setXForm(new Vec2(mouseX, -29), 0);
				}
			}
		}
		
	}

	class myMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			CONSTANT.shouldStep = true;
			CONSTANT.batCanMove = true;
			startMili = System.currentTimeMillis();
		}
		
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}		
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}	
	}
	

	void ContactOperate(Body b1,Body b2) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 16; j++) {
				if (this.bricks[i][j].exist) {
					if (b1 == this.bricks[i][j].body || b2 == this.bricks[i][j].body) {
						if (this.ball.isFireBall) {
							if (i - 1 >= 0 && j - 1 >= 0 && this.bricks[i-1][j-1].exist) {
								this.bricks[i-1][j-1].whenHit(this.bodyToDestroy);
							}
							if (i - 1 >= 0 && this.bricks[i-1][j].exist) {
								this.bricks[i-1][j].whenHit(this.bodyToDestroy);
							}
							if ( i - 1 >= 0 && j + 1 <= 15 && this.bricks[i-1][j+1].exist) {
								this.bricks[i-1][j+1].whenHit(this.bodyToDestroy);
							}
							if ( j - 1 >= 0 && this.bricks[i][j-1].exist) {
								this.bricks[i][j-1].whenHit(this.bodyToDestroy);
							}
							if ( j + 1 <= 15 && this.bricks[i][j+1].exist) {
								this.bricks[i][j+1].whenHit(this.bodyToDestroy);
							}
							if ( i + 1 <= 9 && j - 1 >= 0 && this.bricks[i+1][j-1].exist) {
								this.bricks[i+1][j-1].whenHit(this.bodyToDestroy);
							}
							if ( i + 1 <= 9 && this.bricks[i+1][j].exist) {
								this.bricks[i+1][j].whenHit(this.bodyToDestroy);
							}
							if ( i + 1 <= 9 && j + 1 <= 15 && this.bricks[i+1][j+1].exist) {
								this.bricks[i+1][j+1].whenHit(this.bodyToDestroy);
							}
						}
						if (this.bricks[i][j].whenHit(this.bodyToDestroy)) {
							if (Math.random() < 0.1) {
								this.bonusToCreate++;
							}
							if (bricks[i][j] instanceof Brick1) {
								score += 100;
							} else if(bricks[i][j] instanceof Brick2) {
								score += 200;
							} else if(bricks[i][j] instanceof Brick3) {
								score += 300;
							}
						}
					}
				}
			}
		}


		if (b1 == this.ball.body && b2 == this.ground1.body || b2 == this.ball.body && b1 == this.ground1.body) {
			CONSTANT.fail = true;
			this.shouldShowScore = true;
			this.ball.exist = false;
			this.bodyToDestroy.add(this.ball.body);
		}

		for (Bonus bonus: this.bonuses) {
			if (b1 == bonus.body && b2 == this.ground1.body || b2 == bonus.body && b1 == this.ground1.body) {
				bonus.exist = false;
				this.bodyToDestroy.add(bonus.body);
			} else if (b1 == bonus.body && b2 == this.bat.body || b2 == bonus.body && b1 == this.bat.body) {
				bonus.exist = false;
				this.bodyToDestroy.add(bonus.body);
				if (bonus.whenHit() == Bonus.attribute_widen1) {
					this.bonusBatWidthEffect(CONSTANT.BAT_WIDTH1);
				} else if (bonus.whenHit() == Bonus.attribute_widen2) {
					this.bonusBatWidthEffect(CONSTANT.BAT_WIDTH2);
				} else if (bonus.whenHit() == Bonus.attribute_narrow1) {
					this.bonusBatWidthEffect(CONSTANT.BAT_WIDTH3);
				} else if (bonus.whenHit() == Bonus.attribute_narrow2) {
					this.bonusBatWidthEffect(CONSTANT.BAT_WIDTH4);
				} else if (bonus.whenHit() == Bonus.attribute_fasten) {
					this.ball.body.setLinearVelocity(new Vec2(this.ball.body.getLinearVelocity().x * 1.5f, this.ball.body.getLinearVelocity().y * 1.5f));
				} else if (bonus.whenHit() == Bonus.attribute_slow) {
					this.ball.body.setLinearVelocity(new Vec2(this.ball.body.getLinearVelocity().x / 1.5f, this.ball.body.getLinearVelocity().y / 1.5f));
				} else if (bonus.whenHit() == Bonus.attribute_penetrate) {
					this.pballToCreate = true;
					this.penetrateEffect = true;
					this.penetrateCounter = 0;
				} else if (bonus.whenHit() == Bonus.attribute_fire) {
					this.ball.isFireBall = true;
					this.fireEffect = true;
					this.fireCounter = 0;
				} else if (bonus.whenHit() == Bonus.attribute_death) {
					CONSTANT.fail = true;
				} else if (bonus.whenHit() == Bonus.attribute_success) {
					CONSTANT.win = true;
				}
			}
		}
	}

	private void bonusBatWidthEffect(float BAT_WIDTH) {
		this.bat.exist = false;
		this.bodyToDestroy.add(this.bat.body);
		CONSTANT.BAT_WIDTH = BAT_WIDTH;
		this.batToCreate = true;
		this.lengthEffect = true;
		this.lengthCounter = 0;
	}

	void createFilter() {
		this.brickFilter = new FilterData();
		this.brickFilter.categoryBits = 2;
		this.brickFilter.maskBits = 4;

		this.ballFilter = new FilterData();
		this.ballFilter.categoryBits = 4;
		this.ballFilter.maskBits = 42;

		this.batFilter = new FilterData();
		this.batFilter.categoryBits = 8;
		this.batFilter.maskBits = 20;

		this.bonusFilter = new FilterData();
		this.bonusFilter.categoryBits = 16;
		this.bonusFilter.maskBits = 40;

		this.groundFilter = new FilterData();
		this.groundFilter.categoryBits = 32;
		this.groundFilter.maskBits = 20;
	}
	
	
	private Image createImage(float sCREEN_WIDTH, float sCREEN_HEIGHT) {
		return null;
	}	
}
