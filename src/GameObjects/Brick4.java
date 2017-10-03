package GameObjects;

import java.util.ArrayList;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

public class Brick4 extends Brick {
	
	public Brick4(float pointX, float pointY, float width, float height,boolean exist, World world, FilterData brickFilter) {
		this.width=width;
		this.world=world;
		this.height=height;
		this.colorR=255;
		this.colorG=128;
		this.colorB=0;
		this.exist=exist;
		if(exist) {
			bDef=new BodyDef();
			bDef.position.set(pointX, pointY);

			pDef=new PolygonDef();
			pDef.setAsBox(width/2, height/2);   
			pDef.friction=0;
			pDef.density=0;
			pDef.restitution=1f;
			pDef.filter=brickFilter;

			body=world.createBody(bDef);
			body.createShape(pDef);
			body.m_type=Body.e_staticType;   	
			this.hardValue=10000;
		}
	}

	public boolean whenHit(ArrayList<Body> bodyToDestroy){
		return false;
	}
	
}
