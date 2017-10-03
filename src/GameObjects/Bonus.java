package GameObjects;

import java.awt.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import GameUtils.CONSTANT;

public class Bonus extends CircleBody {
	
	public boolean exist=true;

	public int attribute_widen1=1;
	public int attribute_widen2=2;
	public int attribute_narrow1=3;
	public int attribute_narrow2=4;
	public int attribute_fasten=5;
	public int attribute_slow=6;
	public int attribute_penetrate=7;
	public int attribute_fire=8;
	public int attribute_death=9;
	public int attribute_success=10;
	public int attribute;
	boolean attribute_visible=true;
	
	public Bonus(float pointX, float pointY, float radius, World world, FilterData bonusFilter) {
		this.radius=radius;
		this.world=world;
		this.colorR=255;
		this.colorG=0;
		this.colorB=128;

		double a=Math.random();
		if(a<0.1) attribute=attribute_widen1;
		else if(a<0.2) attribute=attribute_widen2;
		else if(a<0.3) attribute=attribute_narrow1;
		else if(a<0.4) attribute=attribute_narrow2;
		else if(a<0.5) attribute=attribute_fasten;
		else if(a<0.6) attribute=attribute_slow;
		else if(a<0.7) attribute=attribute_penetrate;
		else if(a<0.8) attribute=attribute_fire;
		else if(a<0.99) attribute=attribute_death;
		else attribute=attribute_success;

		double b=Math.random();              
		if(b<0.2) attribute_visible=false;

		bDef=new BodyDef();
		bDef.isBullet = true;
		bDef.position.set(pointX, pointY);

		cDef=new CircleDef();
		cDef.radius=radius;
		cDef.density=1.5f;
		cDef.filter=bonusFilter;

		body=world.createBody(bDef);
		body.createShape(cDef);
		body.m_type=Body.e_dynamicType;
		body.setMassFromShapes();
	}

	public void draw(Graphics g){
		if(exist)
		{
			Color c=new Color(colorR,colorG,colorB);
			g.setColor(c);
			int bonus_x=(int)(body.getPosition().x* CONSTANT.RATE+ CONSTANT.SCREEN_WIDTH/2);
			int bonus_y=(int)(-body.getPosition().y* CONSTANT.RATE+ CONSTANT.SCREEN_HEIGHT/2);
			int bonus_r=(int)(CONSTANT.BONUS_RADIUS);			
			Font f=new Font("Arial",Font.BOLD,13);
			g.setFont(f);
			g.setColor(Color.BLACK);
			if(!attribute_visible)
				g.drawImage(CONSTANT.surprise, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			else
			{
				if(attribute==attribute_widen1) 
					g.drawImage(CONSTANT.widen1, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			    if(attribute==attribute_widen2) 
			    	g.drawImage(CONSTANT.widen2, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			    if(attribute==attribute_narrow1)
			    	g.drawImage(CONSTANT.narrow1, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			    if(attribute==attribute_narrow2) 
			    	g.drawImage(CONSTANT.narrow2, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			    if(attribute==attribute_fasten)
			    	g.drawImage(CONSTANT.fasten, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			    if(attribute==attribute_slow) 
			    	g.drawImage(CONSTANT.slow, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			    if(attribute==attribute_penetrate)
			    	g.drawImage(CONSTANT.penetrate, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			    if(attribute==attribute_fire)
			    	g.drawImage(CONSTANT.fire, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			    if(attribute==attribute_death)
			    	g.drawImage(CONSTANT.death, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			    if(attribute==attribute_success) 
			    	g.drawImage(CONSTANT.success, bonus_x-bonus_r/2-8, bonus_y+bonus_r/5*4-10, null);
			}
		}
	}

	public int whenHit(){
		return attribute;
	}
}
