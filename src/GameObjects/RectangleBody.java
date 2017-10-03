package GameObjects;

import java.awt.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

public abstract class RectangleBody {
	World world;
	public Body body;
	BodyDef bDef;
	PolygonDef pDef;
	int colorR;
	int colorG;
	int colorB;

	float pointX;
	float pointY;
	float width;
	float height;

	FilterData filter;

	RectangleBody(float pointX, float pointY, float width, float height, World world, int colorR, int colorG, int colorB, FilterData filter) {
		this.pointX = pointX;
		this.pointY = pointY;
		this.world = world;
		this.width = width;
		this.height = height;
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
		this.filter = filter;
	}

	void setColor(int colorR, int colorG, int colorB) {
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
	}

	abstract void initialize();
	
	abstract void draw(Graphics g);
}

