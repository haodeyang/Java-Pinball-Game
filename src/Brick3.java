import java.util.ArrayList;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;

//������ש���࣬�̳�Brick��
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
			//���ø��嶨��
			bDef=new BodyDef();
			bDef.position.set(pointX, pointY);
			
			//������״����
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
			this.hardValue=3;
		}
	}
	
	//��д���������whenHit��������ײ����
	public boolean whenHit(ArrayList<Body> bodyToDestroy){
		hardValue--;    //Ӳ��ֵ��1
		if(hardValue==2)
		{
			this.colorR=0;          //�ı���ɫ
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
			exist=false;                //������Ӳ�ȣ�������״̬λ����ɾ������
			bodyToDestroy.add(this.body);
			return true;
		}
		return false;
	};
	
	
}