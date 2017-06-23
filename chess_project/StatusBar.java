
import java.awt.*;
import javax.swing.*;


public class StatusBar extends JLabel 
{
	public StatusBar(Dimension size)
	{
		super(" ", JLabel.CENTER);
		setToolTipText("Dynamic Status Bar");
		setPreferredSize(size);
		setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
		setOpaque(true);
		setBackground(Color.WHITE);
		setFont(new Font("Courier", getFont().getStyle(), 20));
	}
	public void update(String text, boolean alert)
	{
		if(alert)
		{
			setBackground(Color.PINK);
			setForeground(Color.GREEN);
		}
		else
		{
			setBackground(Color.WHITE);
			setForeground(Color.BLACK);
		}
		setText(text);
	}
}
