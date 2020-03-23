package java.pl.mini.gamemaster;

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
    public int delayDestroyPiece;
    public int delayNextPiecePlace;
    public int delayMove;
    public int delayDiscover;
    public int delayTest;
    public int delayPick;
    public int delayPlace;
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
        delayDestroyPiece = DDP;
        delayNextPiecePlace = DNPP;
        delayMove = DM;
        delayDiscover = DD;
        delayTest = DT;
        delayPick = DPi;
        delayPlace = DPl;
    }

    /**
     * Empty constructor
     */
    public GameMasterConfiguration(){}
}