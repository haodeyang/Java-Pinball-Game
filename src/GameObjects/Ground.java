package GameObjects;

import java.util.ArrayList;

import GameObjects.Brick;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;


public class Ground extends Brick {
	
	public Ground(float pointX, float pointY, float width, float height,boolean exist, World world, FilterData groundFilter) {
		super(pointX, pointY, width, height, exist, world, groundFilter, 255, 128, 0, 10000);
		initialize();
	}
	
	public boolean whenHit(ArrayList<Body> bodyToDestroy){
		return false;
	}
	
}
