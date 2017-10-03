package GameObjects;

import java.awt.*;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import GameUtils.CONSTANT;

public class Bat extends RectangleBody {
	
	public float pointX;
	public float pointY;
	public boolean exist=true;
	
	public Bat(float pointX, float pointY, float width, float height, World world, FilterData batFilter) {
		this.pointX=pointX;
		this.pointY=pointY;
		this.world=world;
		this.width=width;
		this.height=height;
		this.colorR=33;
		this.colorG=33;
		this.colorB=33;

		bDef=new BodyDef();
		bDef.position.set(pointX, pointY);

		pDef=new PolygonDef();
		pDef.setAsBox(width/2, height/2);    
		pDef.friction=0.001f;
		pDef.density=0;
		pDef.restitution=1f;
		pDef.filter=batFilter;

		body=world.createBody(bDef);
		body.createShape(pDef);
	}

	public void draw(Graphics g){
		if(exist) {
			Color c=new Color(colorR,colorG,colorB);
			g.setColor(c);
			g.fillRect((int)(body.getPosition().x* CONSTANT.RATE+ CONSTANT.SCREEN_WIDTH/2- CONSTANT.BAT_WIDTH/2), (int)(CONSTANT.SCREEN_HEIGHT/2-body.getPosition().y* CONSTANT.RATE- CONSTANT.BAT_HEIGHT/2),(int) CONSTANT.BAT_WIDTH,(int) CONSTANT.BAT_HEIGHT);
		}
	}
}

