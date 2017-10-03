import java.util.ArrayList;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

//�ڶ���ש���࣬�̳�Brick��
public class Brick2 extends Brick{
	
	public Brick2(float pointX, float pointY, float width, float height,boolean exist, World world, FilterData brickFilter){
		this.width=width;
		this.world=world;
		this.height=height;
		this.colorR=0;
		this.colorG=128;
		this.colorB=255;
		this.exist=exist;
		if(exist)
		{
			//���嶨��
			bDef=new BodyDef();
			bDef.position.set(pointX, pointY);
			
			//��״����
			pDef=new PolygonDef();
			pDef.setAsBox(width/2, height/2);   
			pDef.friction=0;
			pDef.density=0;
			pDef.restitution=1f;
			pDef.filter=brickFilter;
			
			//��������
			body=world.createBody(bDef);
			body.createShape(pDef);
			body.m_type=Body.e_staticType;   	
			this.hardValue=2;
		}
	}
	
	//��д���������whenHit��������ײ����
	public boolean whenHit(ArrayList<Body> bodyToDestroy){
		hardValue--;   //Ӳ��ֵ��1
		this.colorR=165;
		this.colorG=208;
		this.colorB=251;
		if(hardValue==0){   //������Ӳ�ȣ�������״̬λ����ɾ������
			exist=false;
			bodyToDestroy.add(this.body);
			return true;
		}
		return false;
	};

}

