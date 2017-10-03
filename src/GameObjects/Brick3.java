import java.util.ArrayList;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

//第三种砖块类，继承Brick类
public class Brick3 extends Brick{
	
	public Brick3(float pointX, float pointY, float width, float height,boolean exist, World world, FilterData brickFilter){
		this.width=width;
		this.world=world;
		this.height=height;
		this.colorR=51;
		this.colorG=51;
		this.colorB=247;
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
			this.hardValue=3;
		}
	}
	
	//覆写父类抽象函数whenHit，进行碰撞处理
	public boolean whenHit(ArrayList<Body> bodyToDestroy){
		hardValue--;    //硬度值减1
		if(hardValue==2)
		{
			this.colorR=0;          //改变颜色
			this.colorG=128;
			this.colorB=255;
		}
		else if(hardValue==1)
		{
			this.colorR=165;
			this.colorG=208;
			this.colorB=251;
		}
		else if(hardValue==0){
			exist=false;                //若已无硬度，则设置状态位，并删除刚体
			bodyToDestroy.add(this.body);
			return true;
		}
		return false;
	};
	
	
}
