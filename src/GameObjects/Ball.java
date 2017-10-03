package GameObjects;

import java.awt.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.common.*;
import GameUtils.CONSTANT;


public class Ball extends CircleBody {
	
	public boolean exist=true;
	public boolean isFireBall=false;
	
	public Ball(float pointX, float pointY, float radius, World world, FilterData ballFilter) {
		this.radius=radius;
		this.world=world;
		this.colorR=33;
		this.colorG=33;
		this.colorB=33;

		bDef=new BodyDef();
		bDef.isBullet = true;
		bDef.position.set(pointX, pointY);

		cDef=new CircleDef();
		cDef.radius=radius;
		cDef.friction=0.1f;
		cDef.density=0.1f;
		cDef.restitution=1f;
		cDef.filter=ballFilter;

		body=world.createBody(bDef);
		body.createShape(cDef);
		body.m_type=Body.e_dynamicType;
		body.setLinearVelocity(new Vec2(-8,50));
		body.setMassFromShapes();
	}

	@Override
	public void draw(Graphics g) {
		if(exist) {
			Color c=new Color(colorR,colorG,colorB);
			g.setColor(c);
			int ball_x=(int)(body.getPosition().x* CONSTANT.RATE+ CONSTANT.SCREEN_WIDTH/2);
			int ball_y=(int)(-body.getPosition().y* CONSTANT.RATE+ CONSTANT.SCREEN_HEIGHT/2);
			int ball_r=(int)(CONSTANT.BALL_RADIUS);			
			g.fillArc(ball_x-ball_r,ball_y-ball_r,(int)ball_r*2,(int)ball_r*2,0,360);
		}
	}
}