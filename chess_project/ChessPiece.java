
import java.awt.Graphics;
import java.util.ArrayList;


public abstract class ChessPiece 
{
	//黒対白だから
	private boolean isWhite;
	
	private Location loc;
	
	//駒の色と場所を保存しておく
	public ChessPiece(boolean isWhite, Location loc)
	{
		this.isWhite = isWhite;
		this.loc = loc;
	}
	
	//駒を置くメソッド
	public void setLocation(Location loc)
	{
		this.loc = loc;
	}
	//駒の情報を受取るメソッド
	public Location getLocation()
	{
		return loc;
	}
	
	//駒の色を返すメソッド
	public boolean getColor()
	{
		return isWhite;
	}
	
	public abstract void draw(Graphics g);
	
	public abstract ArrayList<Location> getMoves(BoardState board);
	
	public abstract void moveTo(Location moveLoc);
	
	public static ChessPiece clone(ChessPiece dolly)
	{
		if(dolly instanceof King)
		{
			King piece = new King(dolly.getColor(), dolly.getLocation());
			piece.setHasMoved(((King)dolly).getHasMoved());
			piece.setIsChecked(((King)dolly).isChecked());
			return piece;
		}
		else if(dolly instanceof Queen)
			return new Queen(dolly.getColor(), dolly.getLocation());
		else if(dolly instanceof Pawn)
		{
			Pawn piece = new Pawn(dolly.getColor(), dolly.getLocation());
			piece.setDoubleMove(((Pawn)dolly).getDoubleMove());
			return piece;
		}
		else if(dolly instanceof Rook)
		{
			Rook piece = new Rook(dolly.getColor(), dolly.getLocation());
			piece.setHasMoved(((Rook)dolly).getHasMoved());
			return piece;
		}
		else if(dolly instanceof Bishop)
			return new Bishop(dolly.getColor(), dolly.getLocation());
		else if(dolly instanceof Knight)
			return new Knight(dolly.getColor(), dolly.getLocation());
		else
			return null;
	}
	public String toString()
	{
		if(isWhite)
			return "White";
		else
			return "Black";
	}
}
