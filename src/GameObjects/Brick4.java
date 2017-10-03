package GameObjects;

import java.util.ArrayList;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

public class Brick4 extends Brick {
	
	public Brick4(float pointX, float pointY, float width, float height,boolean exist, World world, FilterData brickFilter) {
		super(pointX, pointY, width, height, exist, world, brickFilter, 255, 128, 0, 10000);
		initialize();
	}

	public boolean whenHit(ArrayList<Body> bodyToDestroy) {
		return false;
	}
}
