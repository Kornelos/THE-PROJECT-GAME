package pl.mini.communication;

public class GameMasterClientRunner {
    public static void main(String[] args)
    {
        try{
            GameMasterClient gmClient = new GameMasterClient();
            GameMasterClient.start(2137);
        }
        catch (InterruptedException e){
        e.printStackTrace();
    }
    }
}