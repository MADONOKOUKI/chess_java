import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

public class VerifyRestartgamePopup extends PopupWindow implements ActionListener
{

  private JLabel text;
  private JButton yes, no;
  private GUI parent;

  public VerifyRestartgamePopup(GUI parent)
  {
    super(parent, "Caution");

    this.parent = parent;

    text = new JLabel("相手が新しい試合を求めています。新しいゲームを作成しますか？");
    no = new JButton("いいえ");
    no.addActionListener(this);
    yes = new JButton("はい");
    yes.setActionCommand("-28");
    yes.addActionListener(parent);

    JPanel top = new JPanel();
    top.setBackground(new Color(0,0,0,0));
    top.add(text);
    JPanel bottom = new JPanel();
    bottom.setBackground(new Color(0,0,0,0));
    bottom.add(no);
    bottom.add(yes);
    add(top);
    add(bottom);

    pack();
    resetLocation();
  }

  public void actionPerformed(ActionEvent event)
  {
    parent.setEnabled(false);
    // dispose();
  }
}