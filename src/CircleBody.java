import java.awt.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

//Բ�������࣬����
public abstract class CircleBody {
	World world;
	public Body body;    //�����Ӧ�����������������
	BodyDef bDef;        //���嶨��
	CircleDef cDef;      //��״����
	int colorR;
	int colorG;
	int colorB;
	
	float radius;
	
	abstract void draw(Graphics g);
}


