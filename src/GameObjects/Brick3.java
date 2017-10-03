package GameObjects;

import java.util.ArrayList;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

public class Brick3 extends Brick {
	
	public Brick3(float pointX, float pointY, float width, float height,boolean exist, World world, FilterData brickFilter) {
		super(pointX, pointY, width, height, exist, world, brickFilter, 51, 51, 247, 3);
		initialize();
	}

	public boolean whenHit(ArrayList<Body> bodyToDestroy) {
		this.hardValue--;
		if (this.hardValue == 2) {
			this.setColor(0, 128, 255);
		} else if (this.hardValue == 1) {
			this.setColor(165, 208, 251);
		} else if (this.hardValue == 0) {
			this.exist = false;
			bodyToDestroy.add(this.body);
			return true;
		}
		return false;
	}
	
}
