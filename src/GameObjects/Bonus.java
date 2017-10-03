package GameObjects;

import java.awt.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import GameUtils.CONSTANT;

public class Bonus extends CircleBody {

	static public int attribute_widen1 = 1;
	static public int attribute_widen2 = 2;
	static public int attribute_narrow1 = 3;
	static public int attribute_narrow2 = 4;
	static public int attribute_fasten = 5;
	static public int attribute_slow = 6;
	static public int attribute_penetrate = 7;
	static public int attribute_fire = 8;
	static public int attribute_death = 9;
	static public int attribute_success = 10;

	private int attribute;
	private boolean attribute_visible = true;

	public boolean exist = true;


	public Bonus(float pointX, float pointY, float radius, World world, FilterData bonusFilter) {
		super(pointX, pointY, radius, world, 255, 0, 128, bonusFilter);

		double a = Math.random();
		if (a < 0.1) {
			this.attribute = attribute_widen1;
		} else if (a < 0.2) {
			this.attribute = attribute_widen2;
		} else if (a < 0.3) {
			this.attribute = attribute_narrow1;
		} else if (a < 0.4) {
			this.attribute = attribute_narrow2;
		} else if (a < 0.5) {
			this.attribute = attribute_fasten;
		} else if (a < 0.6) {
			this.attribute = attribute_slow;
		} else if (a < 0.7) {
			this.attribute = attribute_penetrate;
		} else if (a < 0.8) {
			this.attribute = attribute_fire;
		} else if (a < 0.99) {
			this.attribute = attribute_death;
		} else {
			this.attribute = attribute_success;
		}

		double b = Math.random();
		if (b < 0.2) {
			this.attribute_visible = false;
		}

		initialize();
	}

	@Override
	void initialize() {
		this.bDef = new BodyDef();
		this.bDef.isBullet = true;
		this.bDef.position.set(this.pointX, this.pointY);

		this.cDef = new CircleDef();
		this.cDef.radius = this.radius;
		this.cDef.density = 1.5f;
		this.cDef.filter = this.filter;

		this.body = world.createBody(this.bDef);
		this.body.createShape(this.cDef);
		this.body.m_type = Body.e_dynamicType;
		this.body.setMassFromShapes();
	}

	public void draw(Graphics g) {
		if (this.exist) {
			Color c = new Color(this.colorR, this.colorG, this.colorB);
			g.setColor(c);
			int bonus_x = (int)(body.getPosition().x * CONSTANT.RATE + CONSTANT.SCREEN_WIDTH / 2);
			int bonus_y = (int)(-body.getPosition().y * CONSTANT.RATE + CONSTANT.SCREEN_HEIGHT / 2);
			int bonus_r = (int)(CONSTANT.BONUS_RADIUS);
			Font f = new Font("Arial", Font.BOLD,13);
			g.setFont(f);
			g.setColor(Color.BLACK);
			if (!attribute_visible) {
				g.drawImage(CONSTANT.surprise, bonus_x - bonus_r / 2 - 8, bonus_y + bonus_r / 5 * 4 - 10, null);
			} else {
				if (this.attribute == attribute_widen1) {
					g.drawImage(CONSTANT.widen1, bonus_x - bonus_r / 2 - 8, bonus_y + bonus_r / 5 * 4 - 10, null);
				} else if (this.attribute == attribute_widen2) {
					g.drawImage(CONSTANT.widen2, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
				} else if (this.attribute == attribute_narrow1) {
					g.drawImage(CONSTANT.narrow1, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
				} else if (this.attribute == attribute_narrow2) {
					g.drawImage(CONSTANT.narrow2, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
				} else if (this.attribute == attribute_fasten) {
					g.drawImage(CONSTANT.fasten, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
				} else if (this.attribute == attribute_slow) {
					g.drawImage(CONSTANT.slow, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
				} else if (this.attribute == attribute_penetrate) {
					g.drawImage(CONSTANT.penetrate, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
				} else if (this.attribute == attribute_fire) {
					g.drawImage(CONSTANT.fire, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
				} else if (this.attribute == attribute_death) {
					g.drawImage(CONSTANT.death, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
				} else if (this.attribute == attribute_success) {
					g.drawImage(CONSTANT.success, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
				}
			}
		}
	}

	public int whenHit(){
		return this.attribute;
	}
}
