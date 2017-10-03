package GameObjects;

import java.awt.*;
import java.util.*;
import org.jbox2d.dynamics.*;
import GameUtils.CONSTANT;

public abstract class Brick extends RectangleBody {
	public boolean exist;
	int hardValue;
	
	abstract public boolean whenHit(ArrayList<Body> bodyToDestroy); 

	public void draw(Graphics g) {
		if(exist) {
			Color c=new Color(colorR,colorG,colorB);
			g.setColor(c);
			int brick_x=(int)((body.getPosition().x-width/2)* CONSTANT.RATE+ CONSTANT.SCREEN_WIDTH/2)+1;
			int brick_y=(int)((-body.getPosition().y-height/2)* CONSTANT.RATE+ CONSTANT.SCREEN_HEIGHT/2)+1;
			int brick_w=(int)(CONSTANT.BRICK_WIDTH)-1;
			int brick_h=(int)(CONSTANT.BRICK_HEIGHT)-1;			
			g.fillRect(brick_x,brick_y,brick_w,brick_h);
		}
	}
}
