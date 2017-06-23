
import javax.swing.*;

public class Menu extends JMenuBar
{
	
	public Menu(GUI parent)
	{
		//File Menu
		JMenu file = new JMenu("ファイル");
		JMenuItem exit = new JMenuItem("タブを閉じる");
		exit.setActionCommand("-11");
		exit.addActionListener(parent);
		file.add(exit);

		//Game Menu
		JMenu game = new JMenu("ゲームについて");
		JMenuItem newGame = new JMenuItem("降参する");
		newGame.setActionCommand("-10");
		newGame.addActionListener(parent);
		game.add(newGame);

		//Options Menu
		JMenu options = new JMenu("設定");
		JCheckBoxMenuItem flip = new JCheckBoxMenuItem("駒をターンでひっくり返す");
		flip.setActionCommand("-13");
		flip.addActionListener(parent);
		options.add(flip);
		
		//Help Menu
		JMenu help = new JMenu("ルール");
		JMenuItem rules = new JMenuItem("確認する");
		rules.setActionCommand("-14");
		rules.addActionListener(parent);
		help.add(rules);
		
		add(file);
		add(game);
		add(options);
		add(help);
	}
}
