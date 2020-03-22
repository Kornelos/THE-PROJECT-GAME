package pl.mini.gamemaster;

import java.awt.Point;

public class GameMasterConfiguration {
    public double shamProbability;
    public int maxTeamSize;
    public int maxPieces;
    // current (?)
    public Point[] predefinedGoalPositions;
    public int boardWidth;
    public int boardTaskHeight;
    public int boardGoalHeight;
    public int DelayDestroyPiece;
    public int DelayNextPiecePlace;
    public int DelayMove;
    public int DelayDiscover;
    public int DelayTest;
    public int DelayPick;
    public int DelayPlace;
    /**
    * Constructor
    */
    public GameMasterConfiguration(double sP, int mTS, int mP, Point[] pGP, int bW, int bTH, int bGH, int DDP,
                                   int DNPP, int DM, int DD, int DT, int DPi, int DPl){
        shamProbability = sP;
        maxTeamSize = mTS;
        maxPieces = mP;
        predefinedGoalPositions = pGP;
        boardWidth = bW;
        boardTaskHeight = bTH;
        boardGoalHeight = bGH;
        DelayDestroyPiece = DDP;
        DelayNextPiecePlace = DNPP;
        DelayMove = DM;
        DelayDiscover = DD;
        DelayTest = DT;
        DelayPick = DPi;
        DelayPlace = DPl;
    }

    /**
     * Empty constructor
     */
    public GameMasterConfiguration(){}
}