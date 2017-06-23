
import java.awt.*;

import javax.swing.*;

public class ToolBar extends JPanel
{

	public ToolBar(GUI listener, Dimension size)
	{
		setPreferredSize(size);
		setOpaque(true);
		setBackground(new Color(51,102,255));

		JPanel title = new JPanel();
		title.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		title.setLayout(new BorderLayout());
		title.setPreferredSize(new Dimension(size.width-5, 60));
		title.setOpaque(true);
		title.setBackground(new Color(46,184,0));

		JLabel title1 = new JLabel("MENU", JLabel.CENTER);
		title1.setPreferredSize(new Dimension(size.width-5, 30));
		title1.setFont(new Font("Courier", title1.getFont().getStyle(), 20));
		title1.setOpaque(true);
		title1.setBackground(new Color(46,184,0));

		JButton highlight = new JButton("HIGHLIGHT");
		highlight.setPreferredSize(new Dimension(size.width-5, 60));
		highlight.setBorder(BorderFactory.createRaisedBevelBorder());
		highlight.setBackground(Color.WHITE);
		highlight.setToolTipText("Toggle Move Highlighting");

		JButton surrenderGameButton = new JButton("SURRENDER");
		surrenderGameButton.setPreferredSize(new Dimension(size.width-5, 60));
		surrenderGameButton.setBorder(BorderFactory.createRaisedBevelBorder());
		surrenderGameButton.setBackground(Color.LIGHT_GRAY);
		surrenderGameButton.setToolTipText("SURRENDER");

		JButton exitButton = new JButton("EXIT");
		exitButton.setPreferredSize(new Dimension(size.width-5, 60));
		exitButton.setBorder(BorderFactory.createRaisedBevelBorder());
		exitButton.setBackground(Color.WHITE);
		exitButton.setToolTipText("Exit G Chess");

		highlight.setActionCommand("-22");
		surrenderGameButton.setActionCommand("-10");
		exitButton.setActionCommand("-11");

		highlight.addActionListener(listener);
		surrenderGameButton.addActionListener(listener);
		exitButton.addActionListener(listener);

		add(title);
		add(highlight);
		add(surrenderGameButton);
		add(exitButton);
	}
}
