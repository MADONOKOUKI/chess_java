
import java.util.ArrayList;
import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class GUIRunner 
{
	
	private Location selectedPiece,promotedPiece;
	static private Location firstloc,movedPiece;
	private BoardState board;
	private GUI gui;
	private boolean isWhiteTurn;
	private boolean highlight;
	private boolean perpetualFlip;
	private ArrayList<Integer> undoMoves;
	private ArrayList<Integer> wasPieceTaken;
	private ArrayList<Location> moves;
	private boolean mycolor;
	public ChessClient client;
	private int flg=0;
	private int pawn_state = 0;
	public GUIRunner(boolean iswhite, ChessClient client)
	{
		//チェスボードの作成
		board = new BoardState();
		gui = new GUI(board, this);
		gui.setVisible(true);
		//基本的に駒が動ける場所はハイライトする設定にしておく
		highlight = true;
		perpetualFlip=false;
		//後でこの色で動かせるか動かせられないか決めたいため
		mycolor=iswhite;
		if(iswhite == false ){
			gui.flipBoard();
			gui.enableSide(isWhiteTurn);
			if(selectedPiece!=null)
			{
				gui.selected(selectedPiece);
				processMoves();
			}
		}
		//自分のターンを代入する
		isWhiteTurn=iswhite;
		moves = new ArrayList<Location>();
		this.client=client;
	}
	
	public void setVisible()
	{
		gui.setVisible(true);
		newGame();
//		sounds.newgame();
	}
	
	public void newGame()
	{
		board.resetBoardState();
		selectedPiece = null;
		isWhiteTurn = false;
		board.setTurn(isWhiteTurn);
		gui.updateBoard(board);
		gui.enableSide(isWhiteTurn);
		undoMoves = new ArrayList<Integer>();
		wasPieceTaken = new ArrayList<Integer>();
		gui.newGame();
	}
	
	public boolean getTurn()
	{
		return isWhiteTurn;
	}
	
	public ArrayList<Location> getMoves(Location piece)
	{
		ArrayList<Location> allMoves = board.getState()[piece.getRow()][piece.getCol()].getMoves(board);
		for(int a=0; a<allMoves.size(); a++)
			if(!isLegal(piece, allMoves.get(a), 0))
			{
				allMoves.remove(a);
				a--;
			}
		return allMoves;
	}
	
	public void checkGameOver()
	{
		ArrayList<Location> moves = new ArrayList<Location>();
		ArrayList<Location> temp = new ArrayList<Location>();
		for(int y=0; y<board.getState().length; y++)
			for(int x=0; x<board.getState().length; x++)
			{
				if(board.getState()[y][x]!=null && board.getState()[y][x].getColor()==isWhiteTurn)
				{
					temp = getMoves(new Location(y,x));
					for(Location z : temp)
						moves.add(z);
					if(moves.size()>0)
						break;
				}
				//実際いらないかもしれない
				if(moves.size()>0)
					break;
			}
		//動ける場所が取れない場合
		if(moves.size()==0)
		{
			boolean isCheck = true;
			for(int y=0; y<board.getState().length; y++)
				for(int x=0; x<board.getState().length; x++)
					if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof King && board.getState()[y][x].getColor()==isWhiteTurn)
					{
						isCheck = ((King)board.getState()[y][x]).isChecked();
						break;
					}
			if(isCheck)
			{
				gui.updateStatusBar("ゲーム終了", true);
				if(isWhiteTurn)
					//黒の価値
					gui.gameOver(-1);
				else
					//白の価値
					gui.gameOver(1);
//				sounds.victory();
			}
			else
			{
				//引き分けの場合
				gui.updateStatusBar("引き分け", true);
				gui.gameOver(0);
//				sounds.draw();
			}
		}

		boolean isWhiteKnight = false;
		boolean isWhiteBishop = false;
		boolean isBlackKnight = false;
		boolean isBlackBishop = false;
		boolean isDraw = true;
		for(int y=0; y<board.getState().length; y++)
			for(int x=0; x<board.getState().length; x++)
			{
				if(board.getState()[y][x]!=null && (board.getState()[y][x] instanceof Queen || board.getState()[y][x] instanceof Rook || board.getState()[y][x] instanceof Pawn))
				{
					isDraw=false;
					break;
				}
				else if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof Bishop && board.getState()[y][x].getColor()==isWhiteTurn)
					isWhiteBishop=true;
				else if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof Knight && board.getState()[y][x].getColor()==isWhiteTurn)
					isWhiteKnight = true;
				else if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof Bishop && board.getState()[y][x].getColor()!=isWhiteTurn)
					isBlackBishop=true;
				else if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof Knight && board.getState()[y][x].getColor()!=isWhiteTurn)
					isBlackKnight = true;
			}
		if(isDraw)
			if(isWhiteBishop && isWhiteKnight || isBlackBishop && isBlackKnight || isBlackBishop && isWhiteBishop || isBlackKnight && isWhiteKnight)
				isDraw = false;
		if(isDraw)
		{
			gui.updateStatusBar("Draw!", true);
			gui.gameOver(0);
		}
	}
	
	//駒を強いのに帰るためのメソッド
	public void checkPromotion(Location loc)
	{
		if(board.getState()[loc.getRow()][loc.getCol()] instanceof Pawn && (loc.getRow()==0 || loc.getRow()==7))
		{
			System.out.println("set Reverse");
			// isWhiteTurn = !isWhiteTurn;
			if(mycolor!=isWhiteTurn)
				{gui.promotion();
				}
			//
			promotedPiece = loc;
		}
	}
	
	public boolean isLegal(Location start, Location end, int restraint)
	{
		boolean isLegal = true;
		boolean complete = true;
		int queue;
		
		if(restraint!=1 && board.getState()[start.getRow()][start.getCol()] instanceof King)
		{
			if(start.getRow()==end.getRow() && Math.abs(start.getCol()-end.getCol())==2)
			{
				((King)board.getState()[start.getRow()][start.getCol()]).updateIsChecked(board);
				if(((King)board.getState()[start.getRow()][start.getCol()]).getHasMoved() || ((King)board.getState()[start.getRow()][start.getCol()]).isChecked())
				{
					isLegal = false;
					complete = false;
				}
				else if(end.getCol()==2 && board.isEmpty(new Location(start.getRow(),start.getCol()-1)) && board.isEmpty(new Location(start.getRow(),start.getCol()-2)) && board.isEmpty(new Location(start.getRow(),start.getCol()-3)) && !board.isEmpty(new Location(start.getRow(),start.getCol()-4)) && board.getState()[start.getRow()][start.getCol()-4] instanceof Rook && !((Rook)board.getState()[start.getRow()][start.getCol()-4]).getHasMoved())
				{
					if(isLegal(start, new Location(start.getRow(), start.getCol()-1), 1) && isLegal(start, end, 1))
					{
						isLegal = true;
						complete = false;
					}
					else
					{
						isLegal = false;
						complete = false;
					}
				}
				else if(end.getCol()==6 && board.isEmpty(new Location(start.getRow(),start.getCol()+1)) && board.isEmpty(new Location(start.getRow(),start.getCol()+2)) && !board.isEmpty(new Location(start.getRow(),start.getCol()+3)) && board.getState()[start.getRow()][start.getCol()+3] instanceof Rook && !((Rook)board.getState()[start.getRow()][start.getCol()+3]).getHasMoved())
				{
					if(isLegal(start, new Location(start.getRow(), start.getCol()+1), 1) && isLegal(start, end, 1))
					{
						isLegal = true;
						complete = false;
					}
					else
					{
						isLegal = false;
						complete = false;
					}
				}
				else
				{
					isLegal = false;
					complete = false;
				}
			}
		}
		
		if(complete)
		{
			queue = board.saveState();
			board.moveFrom_To(start, end);
			for(int y=0; y<board.getState().length; y++)
				for(int x=0; x<board.getState().length; x++)
					if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof King && board.getState()[y][x].getColor()==isWhiteTurn)
					{
						((King)board.getState()[y][x]).updateIsChecked(board);
						isLegal = !((King)board.getState()[y][x]).isChecked();
						break;
					}
			board.undoMove(queue);
		}
		
		return isLegal;
	}
	
	public void movePiece(Location loc)
	{
		undoMoves.add(board.saveState());
		if(board.getState()[loc.getRow()][loc.getCol()]!=null)
		{
			wasPieceTaken.add(1);
//			sounds.taken();
		}
		else
		{
			wasPieceTaken.add(0);
//			sounds.move();
		}
		board.moveFrom_To(selectedPiece, loc);
		gui.updatePGN(selectedPiece, loc);
		System.out.println("move Piece");
		isWhiteTurn = !isWhiteTurn;
		board.setTurn(isWhiteTurn);
		selectedPiece=null;
		gui.updateBoard(board);
		gui.enableSide(isWhiteTurn);
		for(int y=0; y<board.getState().length; y++)
			for(int x=0; x<board.getState().length; x++)
				if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof King && board.getState()[y][x].getColor()==isWhiteTurn)
				{
					((King)board.getState()[y][x]).updateIsChecked(board);
					if(((King)board.getState()[y][x]).isChecked())
//						sounds.check();
					break;
				}
		if(perpetualFlip)
		{
			gui.flipBoard();
			if(gui.getFlipped()==isWhiteTurn)
				gui.flipBoard();
			//ターンを与える
			gui.enableSide(isWhiteTurn);
			if(selectedPiece!=null)
			{
				gui.selected(selectedPiece);
				processMoves();
			}
		}
	}
	
	public void processMoves()
	{
		boolean isCheck = true;
		//今動的に選択された駒の動ける場所を獲得する
		moves = getMoves(selectedPiece);
		//①もうチェック・メイトしているかどうか
		//②ただ単に動かせる駒がないだけ
		if(moves.size()==0)
		{
			for(int y=0; y<board.getState().length; y++)
				for(int x=0; x<board.getState().length; x++)
					if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof King && board.getState()[y][x].getColor()==isWhiteTurn)
					{
						isCheck = ((King)board.getState()[y][x]).isChecked();
						break;
					}
			if(isCheck){
				gui.updateStatusBar("チェックメイト", true);
				checkGameOver();
			}else{
				gui.updateStatusBar("駒を動かすことが出来ません", true);
			}
		}
		else
		//highlightがtrueであれば光らせる
			for(Location z : moves)
				gui.enable(z, highlight);
	}
	
	public void processOne(Location loc,boolean yourturn)
	{
		//駒が存在した場合
		gui.setVisible(true);
		System.out.println("porcessOne");
		if(board.getState()[loc.getRow()][loc.getCol()]!=null)
		{
			//駒の色とターンが一致していた場合
			 if((board.getState()[loc.getRow()][loc.getCol()].getColor() == mycolor) && yourturn)
			{
					checkPromotion(loc);				
					checkGameOver();
					System.out.print("1");
					gui.enableSide(isWhiteTurn);
					selectedPiece = loc;
					//
					gui.resetBackground();
					//境界の作成を行う
					gui.resetBorders();
					gui.selected(selectedPiece);
					processMoves();
					firstloc =loc;
					System.out.println("This piece can move"+firstloc.getRow()+""+firstloc.getCol());
			} else if(!yourturn){
				System.out.print("2");
				if(firstloc==null){
					checkPromotion(loc);
					checkGameOver();
					gui.enableSide(isWhiteTurn);
					selectedPiece = loc;
					//
					gui.resetBackground();
					//境界の作成を行う
					gui.resetBorders();
					gui.selected(selectedPiece);
					processMoves();
					firstloc =loc;
					System.out.println("This piece can move"+firstloc.getRow()+""+firstloc.getCol());
				}else{
					movePiece(loc);
					movedPiece = loc;
					// board.resetOtherPawns(loc);
					// checkPromotion(loc);
					checkGameOver();
					System.out.println("駒が取られる場面");

				}
			} else if(board.getState()[loc.getRow()][loc.getCol()].getColor() != mycolor && selectedPiece==null){
					gui.updateStatusBar("This is not your Turn",true);
			}
			else
			{
				System.out.print("3");
				// gui.updateStatusBar("This is not your turn", true);
				// if(mycolor==yourturn)
				// {
				movePiece(loc);
				movedPiece = loc;
				board.resetOtherPawns(loc);
				checkPromotion(loc);
				checkGameOver();
				System.out.println("firstplace"+firstloc.getRow()+""+firstloc.getCol());
				client.send(firstloc,movedPiece,pawn_state);
				pawn_state=0;
				firstloc=null;
			}
		}
		else
		{
			if(selectedPiece != null){
			System.out.print("4");
			movePiece(loc);
			movedPiece = loc;
			board.resetOtherPawns(loc);
			checkPromotion(loc);
			checkGameOver();
			System.out.println("start moving piece");
			if(yourturn){
				client.send(firstloc,movedPiece,pawn_state);
				pawn_state=0;
				selectedPiece=null;
				firstloc=null;
			}
		}
			//ここでダーン終了
		}
		// return yourturn;
	}
	
	//コマンドに応じた走査が実行される
	public void processTwo(int command)
	{
		if(command == 0){
			System.out.println("0");
		}
		else if(command>=-4)
		{
			if(command==-1){
				if(promotedPiece != null) board.addQueen(promotedPiece);
				else board.addQueen(movedPiece);
				pawn_state = 1;
				System.out.println("1");
			}else if(command==-2){
				if(promotedPiece != null) board.addRook(promotedPiece);
				else board.addRook(movedPiece);
				pawn_state = 2;
				System.out.println("2");
			}else if(command==-3){
				if(promotedPiece != null) board.addBishop(promotedPiece);
				else board.addBishop(movedPiece);
				pawn_state = 3;
				System.out.println("3");
			}else if(command==-4){
				if(promotedPiece != null) board.addKnight(promotedPiece);
				else board.addKnight(movedPiece);
				pawn_state = 4;
				System.out.println("4");
			}
			for(int y=0; y<board.getState().length; y++)
				for(int x=0; x<board.getState().length; x++)
					if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof King && board.getState()[y][x].getColor()==isWhiteTurn)
					{
						break;
					}
			
			if(mycolor != isWhiteTurn) {
				client.sendPromotion(command);
				gui.endPromotion();
			}
			gui.updateBoard(board);
		}
		else if(command==-10)
		{
			gui.verifySurrender();
		}
		else if(command==-11)
		{
			gui.verifyExit();
		}
		else if(command==-12)
		{
			try
	        {
				Thread.sleep(1000);
	        }
			catch(Exception e)
	        {
	            e.printStackTrace();
	        }
			gui.dispose();
		}
		else if(command==-13)
		{
			perpetualFlip=!perpetualFlip;
		}
		else if(command==-14)
		{
			try {
				Desktop.getDesktop().browse(new URI("http://chess.plala.jp/p1-3.html"));
			} catch (IOException e) {
				//Prints this throwable and its backtrace to the standard error stream.
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

		}
		else if(command==-20)
		{
			gui.flipBoard();
			gui.enableSide(isWhiteTurn);
			if(selectedPiece!=null)
			{
				gui.selected(selectedPiece);
				processMoves();
			}
		}
		else if(command==-21)
		{
			//既に1回以上駒を動かしていたら
			if(undoMoves.size() > 0)
			{
				//saveされたボードの駒を今の状態に代入する
				board.undoMove(undoMoves.get(undoMoves.size()-1));
				undoMoves.remove(undoMoves.size()-1);
				//ターンを戻す
				isWhiteTurn = !isWhiteTurn;
				//でboardにセットする
				board.setTurn(isWhiteTurn);
				//選択していない状態に戻す
				selectedPiece=null;
				gui.updateBoard(board);
				gui.enableSide(isWhiteTurn);
				//取られたピースのリストを元に戻す
				wasPieceTaken.remove(wasPieceTaken.size()-1);
				gui.backPGN();
				if(perpetualFlip)
					gui.flipBoard();
//				sounds.undo();
			}
			else
				gui.updateStatusBar("1つ前の状態に戻すことが出来ないです", true);
		}
		else if(command==-22)
		{
			highlight = !highlight;
			if(!highlight)
				gui.resetBackground();
			else if(selectedPiece!=null)
				processMoves();
			gui.updateStatusBar("光っている箇所が動かせる範囲です", true);
		}
		else if(command==-25)
		{
			gui.updateStatusBar("ゲーム終了", true);
			client.send(new Location(1,1),new Location(1,1),1);
				if(isWhiteTurn)
				{
					//黒の勝ち
					gui.gameOver(-1);
				}
				else
				{
					//白の勝ち
					gui.gameOver(1);
//				sounds.victory();
				}
		}
		else if(command==-26)
		{
			client.send(new Location(10,1),new Location(10,1),pawn_state);		
		}
		else if(command==101)
		{
			if(flg==0){
				flg++;
				//acceptするかどうかを決定する
			} else {
				flg=0;
				gui.verifyRestartgame();
			}
		}
		else if(command==-28)
		{
			//commandを送信する
			client.send(new Location(8,2),new Location(8,2),0);
			//新しくゲーム再開
			// newGame();
		}
		else if(command==102)
		{
			//新しくゲーム再開
			if(flg==0){
				flg++;
				//acceptするかどうかを決定する
			} else {
				flg=0;
				newGame();
			}
		}
	}
	public void change_Iswhite(){
		isWhiteTurn = !isWhiteTurn;
	}

}
