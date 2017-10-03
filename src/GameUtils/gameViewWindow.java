import javax.swing.*;

//Frame
public class gameViewWindow extends JFrame{
	gameViewPanel viewPanel;
	Object[] str;
	
	public gameViewWindow(String windowName){
		super(windowName);
		this.setSize((int)CONSTANT.SCREEN_WIDTH+8,(int)CONSTANT.SCREEN_HEIGHT+25);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		
		//弹出选择框，供用户选择游戏关卡
		str=new String[9];
		for(int i=0;i<9;i++)
			str[i]="level"+(i+1);
		int response=JOptionPane.showOptionDialog(null, "please choose game level", "game level",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE, null, str, str[0]);
		
		//若用户关闭选择框，则推出程序
		if(response==-1)
			System.exit(0);;
			
		//根据用户选择启动游戏
		viewPanel=new gameViewPanel(response+1);
		
		this.add(viewPanel);
		this.setVisible(true);
		this.setResizable(false);		
	}
}

