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
	
	float radius;
	
	abstract void draw(Graphics g);
}


