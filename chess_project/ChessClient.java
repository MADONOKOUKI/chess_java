import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class ChessClient{
  boolean youareblack;
  BufferedReader in;
  PrintWriter out;
  Scanner scanner = new Scanner(System.in);
  String str;
  Socket socket;
  private int pawn_state=0;

  ChessClient(int port) throws IOException{

    System.out.println("サーバーと接続しています。");
    System.out.println("connecting...");
    // System.out.println(InetAddress.getLocalHost());
    //IPアドレスに変換する
    InetAddress addr = InetAddress.getByName("localhost");
    System.out.println("addr = " + addr);
    socket = new Socket(addr,port);
    try{
      System.out.println("socket = " + socket);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
      str = in.readLine();
      if(str.equals("true")) youareblack=true;
      else youareblack=false;
      System.out.println("connection success");
    } catch(IOException e){
      System.out.println("error at Client");
      socket.close();
    }
  }

  void send(Location selected ,Location moved,int pawn_state){//送信
    String echo;
    System.out.println("start sending");
    //ここから作業を開始する
      int fst_deta = selected.getRow()*10+selected.getCol();
      System.out.println(fst_deta);
      out.println(fst_deta);
      System.out.println("send :"+fst_deta);
      int snd_deta = moved.getRow()*10+moved.getCol();
      out.println(snd_deta);
      System.out.println("send :"+snd_deta);
      this.pawn_state = pawn_state;
  }

  Location receive(){//受信
    int row=0,col=0;
    int num;
    System.out.println("start receiving");
    try{
      str = in.readLine();
      System.out.println("receive :"+str);
      num=Integer.parseInt(str);
      row = num/10;
      col = num%10;
      System.out.println("convert to :"+row + ""+col);
    }catch(IOException e){
      System.out.println("error");
    }
     return new Location(row,col);
  }
  
  void end(){
    System.out.println("closing...");
    try{
      socket.close();
    }catch(IOException e){

    }
  }

  Boolean areyouwhite(){
    System.out.println("check black or white");
    return youareblack;
  }

 
	void sendPromotion(int number){//送信
    	String echo;
    	int com;
    	System.out.println("start send promotion");
      System.out.println(number);
      com=90-number;
      out.println(com);
      System.out.println("send :"+com);
	}
}