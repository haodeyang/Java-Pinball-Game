package GameObjects;

import java.awt.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.common.*;
import GameUtils.CONSTANT;


public class Ball extends CircleBody {
	
	public boolean exist = true;
	public boolean isFireBall = false;
	
	public Ball(float pointX, float pointY, float radius, World world, FilterData ballFilter) {
		super(pointX, pointY, radius, world, 33, 33, 33, ballFilter);
		initialize();
	}

	@Override
	void initialize() {
		this.bDef = new BodyDef();
		this.bDef.isBullet = true;
		this.bDef.position.set(this.pointX, this.pointY);

		this.cDef = new CircleDef();
		this.cDef.radius = this.radius;
		this.cDef.friction = 0.1f;
		this.cDef.density = 0.1f;
		this.cDef.restitution = 1f;
		this.cDef.filter = this.filter;

		this.body = world.createBody(this.bDef);
		this.body.createShape(this.cDef);
		this.body.m_type = Body.e_dynamicType;
		this.body.setLinearVelocity(new Vec2(-8,50));
		this.body.setMassFromShapes();
	}

	@Override
	public void draw(Graphics g) {
		if(exist) {
			Color c = new Color(colorR,colorG,colorB);
			g.setColor(c);
			int ball_x = (int)(body.getPosition().x* CONSTANT.RATE+ CONSTANT.SCREEN_WIDTH/2);
			int ball_y = (int)(-body.getPosition().y* CONSTANT.RATE+ CONSTANT.SCREEN_HEIGHT/2);
			int ball_r = (int)(CONSTANT.BALL_RADIUS);
			g.fillArc(ball_x-ball_r,ball_y-ball_r,(int)ball_r*2,(int)ball_r*2,0,360);
		}
	}
}