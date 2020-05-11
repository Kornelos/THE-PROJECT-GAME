package pl.mini.communication;

public class ServerRunner {

    public static void main(String[] args) {
        try {
            CommunicationServer communicationServer = new CommunicationServer();
            communicationServer.start(2137);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

