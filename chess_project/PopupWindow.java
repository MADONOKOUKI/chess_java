
import java.awt.*;

import javax.swing.*;

public class PopupWindow extends JDialog
{
	
	private Point parentLoc;
	private GUI parent;
	public PopupWindow(GUI parent, String title)
	{
		super(parent,title);
		this.parent = parent;
		setSize(300,100);
		if(parent.isVisible())
		{
			parentLoc = parent.getLocationOnScreen();
			setLocation((int)parentLoc.getX()+(parent.getWidth()/2-getSize().width/2), (int)parentLoc.getY()+(parent.getHeight()/2-getSize().height/2));
		}
		setResizable(false);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	public void resetLocation()
	{
		if(parent.isVisible())
			setLocation((int)parentLoc.getX()+(parent.getWidth()/2-getSize().width/2), (int)parentLoc.getY()+(parent.getHeight()/2-getSize().height/2));
	}
}
