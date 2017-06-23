
import java.awt.*;
import javax.swing.*;

public class GameOverPopup extends PopupWindow
{
	
	private JLabel text;
	private JButton newGame, exit;
	
	public GameOverPopup(GUI parent, String title)
	{
		super(parent, title);
		
		text = new JLabel("The game ended in a "+title+"!");
		newGame = new JButton("ゲームを終了する");
		newGame.setActionCommand("-12");
		
		newGame.addActionListener(parent);
	
		
		JPanel top = new JPanel();
		top.setBackground(new Color(0,0,0,0));
		top.add(text);
		JPanel bottom = new JPanel();
		bottom.setBackground(new Color(0,0,0,0));
		bottom.add(newGame);
		add(top);
		add(bottom);
		
		pack();
		resetLocation();
	}
}
