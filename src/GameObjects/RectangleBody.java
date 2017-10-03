package GameObjects;

import java.awt.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

//���������࣬����
public abstract class RectangleBody {
	World world;
	public Body body;   //�����Ӧ�����������������
	BodyDef bDef;       //���嶨��
	PolygonDef pDef;    //��״����
	int colorR;
	int colorG;
	int colorB;
	
	float width;
	float height;
	
	abstract void draw(Graphics g);
}

