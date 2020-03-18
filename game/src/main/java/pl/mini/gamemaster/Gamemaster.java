package pl.mini.gamemaster;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.lang.Object;
import java.net.InetAddress;

public class Gamemaster {
    private int portNumber;
    private InetAddress ipAddress;
    private final List<UUID> teamRedGuids;
    private final List<UUID> teamBlueGuids;
    /*
    @Getter
    @Setter
    private GameMasterBoard board;
    */
    @Getter
    @Setter
    private GameMasterStatus status;

    //private GameMasterConfiguration configuration;

    public Gamemaster()
    {

    }
    /*
    public GameMasterConfiguration loadConfigurationFromJson(String path)
    {

    }
    */
    public void saveConfigurationToJson(String path)
    {

    }

    private void putNewPiece()
    {

    }

    private void printBoard()
    {

    }

    public void messageHandler(String message)
    {

    }
}