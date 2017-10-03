package GameObjects;

import java.util.ArrayList;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

public class Brick1 extends Brick {
	
	public Brick1(float pointX, float pointY, float width, float height,boolean exist, World world, FilterData brickFilter) {
		super(pointX, pointY, width, height, exist, world, brickFilter, 165, 208, 251, 1);
		initialize();
	}

	public boolean whenHit(ArrayList<Body> bodyToDestroy) {
		this.exist = false;
		bodyToDestroy.add(this.body);
		return true;
	}
}

