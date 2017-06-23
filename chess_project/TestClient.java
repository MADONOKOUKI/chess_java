import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.SwingUtilities;

public class TestClient{
  public static void main(String args[]){
    try{
      ChessClient  client = new ChessClient(Integer.parseInt(args[0]));
      boolean youarewhite = client.areyouwhite();
      if(youarewhite){
        System.out.println("you are white");
    	}else{
    		System.out.println("you are black");
    	}

        GUIRunner run = new GUIRunner(youarewhite,client);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            run.setVisible();
        }
       });
        while(true){
          Location fst_place = client.receive();
          if(fst_place.getRow()==9){
          	run.processTwo(-1*fst_place.getCol());
          }else{
          	 run.processOne(fst_place,false);
          }
          run.checkGameOver();
          System.out.println(fst_place);
        }
    }catch(IOException e){
      System.out.println("error at TestClient");
    }
  }
}
