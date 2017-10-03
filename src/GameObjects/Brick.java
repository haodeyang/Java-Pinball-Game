package GameObjects;

import java.awt.*;
import java.util.*;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import GameUtils.CONSTANT;

public abstract class Brick extends RectangleBody {
	public boolean exist;
	int hardValue;

	Brick(float pointX, float pointY, float width, float height, boolean exist, World world, FilterData filter, int colorR, int colorG, int colorB, int hardValue) {
		super(pointX, pointY, width, height, world, colorR, colorG, colorB, filter);
		this.exist = exist;
		this.hardValue = hardValue;
	}

	@Override
	void initialize() {
		if (this.exist) {
			this.bDef = new BodyDef();
			this.bDef.position.set(this.pointX, this.pointY);

			this.pDef = new PolygonDef();
			this.pDef.setAsBox(this.width/2, this.height/2);
			this.pDef.friction = 0;
			this.pDef.density = 0;
			this.pDef.restitution = 1f;
			this.pDef.filter = this.filter;

			this.body = world.createBody(bDef);
			this.body.createShape(pDef);
			this.body.m_type = Body.e_staticType;
		}
	}
	
	abstract public boolean whenHit(ArrayList<Body> bodyToDestroy); 

	public void draw(Graphics g) {
		if (this.exist) {
			Color c = new Color(this.colorR, this.colorG, this.colorB);
			g.setColor(c);
			int brick_x = (int)((body.getPosition().x - width / 2) * CONSTANT.RATE + CONSTANT.SCREEN_WIDTH / 2) + 1;
			int brick_y = (int)((-body.getPosition().y - height / 2) * CONSTANT.RATE + CONSTANT.SCREEN_HEIGHT / 2) + 1;
			int brick_w = (int)(CONSTANT.BRICK_WIDTH) - 1;
			int brick_h = (int)(CONSTANT.BRICK_HEIGHT) - 1;
			g.fillRect(brick_x, brick_y, brick_w, brick_h);
		}
	}
}
