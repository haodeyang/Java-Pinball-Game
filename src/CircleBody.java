import java.awt.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

//圆形物体类，抽象
public abstract class CircleBody {
	World world;
	public Body body;    //物体对应的物理世界里的物体
	BodyDef bDef;        //物体定义
	CircleDef cDef;      //形状定义
	int colorR;
	int colorG;
	int colorB;
	
	float radius;
	
	abstract void draw(Graphics g);
}


