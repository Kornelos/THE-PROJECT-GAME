package pl.mini.gamemaster;

public class GameMasterClientRunner {
     public static void main(String[] args)
    {
        try{
            GameMasterClient gmClient = new GameMasterClient();
            gmClient.start(13000);
        }
        catch (InterruptedException e){
        e.printStackTrace();
    }
    }
}
