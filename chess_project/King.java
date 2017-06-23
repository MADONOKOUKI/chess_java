
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.*;

public class King extends ChessPiece
{
private boolean isChecked;
	private boolean hasMoved;
	
	public King(boolean isWhite, Location loc)
	{
		super(isWhite, loc);
		
		hasMoved=false;
		
		isChecked=false;		
	}
	
	public void draw(Graphics g)
	{
		final int x = 20;
		final int y = 40;
		final int width = 40;
		if(getColor())
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", g.getFont().getStyle(), width));
			g.fillOval(10, 10, width, width);
			g.setColor(Color.BLACK);
		}
		else
		{
			g.setColor(Color.BLACK);
			//フォントの指定を行っている。
			g.setFont(new Font("Courier", g.getFont().getStyle(), width));
			g.fillOval(10, 10, width, width);
			g.setColor(Color.WHITE);
		}
		//今セットされている色を使って駒にStringの文字が書かれる
		g.drawString("+", x, y);
	}
	
	public boolean getHasMoved()
	{
		return hasMoved;
	}
	
	public void setHasMoved(boolean input)
	{
		hasMoved=input;
	}
	
	public boolean isChecked()
	{
		return isChecked;
	}
	
	public void setIsChecked(boolean check)
	{
		isChecked=check;
	}
	
	public void updateIsChecked(BoardState board)
	{
		ArrayList<Location> possibleChecks = getPossibleChecks(board);
		
		for(int a=0; a<possibleChecks.size(); a++)
			if(possibleChecks.get(a).equals(getLocation()))
			{
				isChecked = true;
				break;
			}
			else
				isChecked=false;
	}
	
	public ArrayList<Location> getPossibleChecks(BoardState board)
	{
		ArrayList<Location> checks = new ArrayList<Location>();
		for(int y=0; y<board.getState().length; y++)
			for(int x=0; x<board.getState().length; x++)
				//なにか駒が置いてあって置いてある色と自分の持ち色が違う場合
				if(board.getState()[y][x]!=null && board.getState()[y][x].getColor()!=getColor())
				{
					ArrayList<Location> tempChecks = board.getState()[y][x].getMoves(board);
					if(board.getState()[y][x] instanceof King)
					{
						for(int a=0; a<tempChecks.size(); a++)
							//今の場所から動ける可能性のある場所までの距離が2の場合対象から外す
							if(Math.abs((board.getState()[y][x].getLocation().getCol())-(tempChecks.get(a).getCol()))==2)
							{
								tempChecks.remove(a);
								a--;
							}
					}
					for(int a=0; a<tempChecks.size(); a++)
						checks.add(tempChecks.get(a));
				}
		return checks;
	}
	
	//動ける場所を返すメソッド
	public ArrayList<Location> getMoves(BoardState board)
	{
		ArrayList<Location> possibleMoves = new ArrayList<Location>();
		int y = getLocation().getRow();
		int x = getLocation().getCol();
		
		Location[] locs = new Location[10];
		
		locs[0] = new Location(y-1,x-1);
		locs[1] = new Location(y-1,x);
		locs[2] = new Location(y-1,x+1);
		locs[3] = new Location(y,x-1);
		locs[4] = new Location(y,x+1);
		locs[5] = new Location(y+1,x+1);
		locs[6] = new Location(y+1,x);
		locs[7] = new Location(y+1,x-1);
		locs[8] = new Location(y,x-2);
		locs[9] = new Location(y,x+2);
		//動ける場所を見つける。
		//①　・で何も置いていないか
		//②色が違う場合
		for(Location z: locs)
			if(board.isValid(z) && (board.isEmpty(z) || board.isPieceWhite(z)!=getColor()))
				possibleMoves.add(z);
		
		return possibleMoves;
	}

	//移動させて動いたということをboolenで入れておく
	public void moveTo(Location moveLoc)
	{
		//この場所を代入する(親クラスのメソッド)
		setLocation(moveLoc);
		hasMoved=true;
	}
	
	//super.toString()で黒か白かが書いてある
	public String toString()
	{
		return super.toString()+" King";
	}
}
