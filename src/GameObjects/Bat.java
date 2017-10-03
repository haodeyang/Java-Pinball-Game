package GameObjects;

import java.awt.*;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import GameUtils.CONSTANT;

public class Bat extends RectangleBody {

	public boolean exist = true;
	
	public Bat(float pointX, float pointY, float width, float height, World world, FilterData batFilter) {
		super(pointX, pointY, width, height, world, 33, 33, 33, batFilter);
		initialize();
	}

	@Override
	void initialize() {
		this.bDef = new BodyDef();
		this.bDef.position.set(this.pointX, this.pointY);

		this.pDef = new PolygonDef();
		this.pDef.setAsBox(width/2, height/2);
		this.pDef.friction = 0.001f;
		this.pDef.density = 0;
		this.pDef.restitution = 1f;
		this.pDef.filter = this.filter;

		this.body=world.createBody(bDef);
		this.body.createShape(pDef);
	}

	public void draw(Graphics g){
		if(this.exist) {
			Color c = new Color(this.colorR, this.colorG, this.colorB);
			g.setColor(c);
			g.fillRect((int)(this.body.getPosition().x * CONSTANT.RATE + CONSTANT.SCREEN_WIDTH / 2 - CONSTANT.BAT_WIDTH / 2), (int)(CONSTANT.SCREEN_HEIGHT / 2 - this.body.getPosition().y * CONSTANT.RATE - CONSTANT.BAT_HEIGHT / 2), (int) CONSTANT.BAT_WIDTH, (int) CONSTANT.BAT_HEIGHT);
		}
	}
}

