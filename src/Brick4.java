import java.util.ArrayList;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

//第四种砖块类，继承Brick类
public class Brick4 extends Brick{
	
	public Brick4(float pointX, float pointY, float width, float height,boolean exist, World world, FilterData brickFilter){
		this.width=width;
		this.world=world;
		this.height=height;
		this.colorR=255;
		this.colorG=128;
		this.colorB=0;
		this.exist=exist;
		if(exist)
		{
			//设置刚体定义
			bDef=new BodyDef();
			bDef.position.set(pointX, pointY);
			
			//设置形状定义
			pDef=new PolygonDef();
			pDef.setAsBox(width/2, height/2);   
			pDef.friction=0;
			pDef.density=0;
			pDef.restitution=1f;
			pDef.filter=brickFilter;
			
			//创建刚体
			body=world.createBody(bDef);
			body.createShape(pDef);
			body.m_type=Body.e_staticType;   	
			this.hardValue=10000;
		}
	}
	
	//覆写父类抽象函数whenHit，进行碰撞处理
	public boolean whenHit(ArrayList<Body> bodyToDestroy){
		return false;
	}
	
}
