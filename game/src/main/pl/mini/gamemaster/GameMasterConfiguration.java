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
    public GameMasterConfiguration(double shamProbability, int maxTeamSize, int maxPieces, Point[] predefinedGoalPositions,
                                   int boardWidth, int boardTaskHeight, int boardGoalHeight, int delayDestroyPiece,
                                   int delayNextPiecePlace, int delayMove, int delayDiscover, int delayTest,
                                   int delayPick, int delayPlace){
        this.shamProbability = shamProbability;
        this.maxTeamSize = maxTeamSize;
        this.maxPieces = maxPieces;
        this.predefinedGoalPositions = predefinedGoalPositions;
        this.boardWidth = boardWidth;
        this.boardTaskHeight = boardTaskHeight;
        this.boardGoalHeight = boardGoalHeight;
        this.delayDestroyPiece = delayDestroyPiece;
        this.delayNextPiecePlace = delayNextPiecePlace;
        this.delayMove = delayMove;
        this.delayDiscover = delayDiscover;
        this.delayTest = delayTest;
        this.delayPick = delayPick;
        this.delayPlace = delayPlace;
    }

    /**
     * Empty constructor
     */
    public GameMasterConfiguration(){}
}