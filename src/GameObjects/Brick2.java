package GameObjects;

import java.util.ArrayList;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

public class Brick2 extends Brick {
	
	public Brick2(float pointX, float pointY, float width, float height,boolean exist, World world, FilterData brickFilter) {
		super(pointX, pointY, width, height, exist, world, brickFilter, 0, 128, 255, 2);
		initialize();
	}

	public boolean whenHit(ArrayList<Body> bodyToDestroy) {
		this.hardValue--;
		this.setColor(165, 208, 251);

		if (this.hardValue == 0) {
			this.exist = false;
			bodyToDestroy.add(this.body);
			return true;
		}
		return false;
	}

}

