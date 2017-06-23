import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.SwingUtilities;
public class ChessServer{
  public static int PORT;
  ServerSocket s1;
  ServerSocket s2;
  Socket socket1;
  Socket socket2;
  BufferedReader in1;
  BufferedReader in2;
  PrintWriter out1;
  PrintWriter out2;
  String str1;
  String str2;
  Boolean player1iswhite=true;
    ChessServer()throws IOException{
      // 　クライアントが指定したポート番号に送られたコネクション設定要求を受け取る事を意味している
      s1 = new ServerSocket(2222);
      s2 = new ServerSocket(3333);
      System.out.println("Started: " +s1);
      System.out.println("Started: " +s2);
      // チェスゲーム開始
      // GUIRunnerを利用する

      try{
        socket1 = s1.accept();
        System.out.println("player1 is login");
        System.out.println("Connection accepted: " + socket1);
        socket2 = s2.accept();
        System.out.println("player2 is login");
        System.out.println("Connection accepted: " + socket2);
        try{
          //
          in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
          in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
          out1 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream())),true);
          out2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream())),true);
          System.out.println("connection Suceeded");
          out1.println(!player1iswhite);
          out2.println(player1iswhite);
          int flg = 0;
          System.out.println("start Thread");
          ChessServer2 cs = new ChessServer2(in2,out1);
          Thread thread = new Thread(cs);
          thread.start();
          while(true){
              str1 = in1.readLine();
              if(str1.equals("END")) break;
              System.out.println("receive : "+str1);
              out2.println(str1);
          }
        } finally {
          System.out.println("closing...");
          socket1.close();
          socket2.close();
        }
      } finally{
        s1.close();
        s2.close();
      }
    }

    Boolean blackorwhite(int p){
      if(p == 2222) return player1iswhite;
      else return !player1iswhite;

    }
    public void run(){
    	try{
    	while(true){
    			System.out.println("thread is working");
              str2 = in2.readLine();
              if(str2.equals("END")) break;
              System.out.println("receive : "+str2);
              out1.println(str2);
              System.out.println("next1");
              // out2.println("success to send");
    	}
    	}catch(IOException e){

    	}finally{
    		try{
    	  socket1.close();
          socket2.close();
      		}catch(IOException e){
      		}
    	}

    }
}
class ChessServer2 implements Runnable{
  public static int PORT;
  ServerSocket s1;
  ServerSocket s2;
  Socket socket1;
  Socket socket2;
  BufferedReader in1;
  BufferedReader in2;
  PrintWriter out1;
  PrintWriter out2;
  String str1;
  String str2;
  Boolean player1iswhite=true;
  	ChessServer2(BufferedReader in2,PrintWriter out1){
  		this.in2  = in2;
  		this.out1 = out1;
  	}
    public void run(){
    	try{
    	while(true){
    			System.out.println("thread is working");
              str2 = in2.readLine();
              if(str2.equals("END")) break;
              System.out.println("receive : "+str2);
              out1.println(str2);
              System.out.println("next2");
    	}
    	}catch(IOException e){

    	}finally{
    	}

    }
}