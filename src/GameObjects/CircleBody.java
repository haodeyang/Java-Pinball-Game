package GameObjects;

import java.awt.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

public abstract class CircleBody {
	World world;
	public Body body;
	BodyDef bDef;
	CircleDef cDef;
	int colorR;
	int colorG;
	int colorB;

	float pointX;
	float pointY;
	float radius;

	FilterData filter;

	CircleBody(float pointX, float pointY, float radius, World world, int colorR, int colorG, int colorB, FilterData filter) {
		this.pointX = pointX;
		this.pointY = pointY;
		this.radius = radius;
		this.world = world;
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
		this.filter = filter;
	}

	abstract void initialize();
	
	abstract void draw(Graphics g);
}


