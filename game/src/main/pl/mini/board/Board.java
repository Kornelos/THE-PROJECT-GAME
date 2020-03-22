package pl.mini.board;

import lombok.Getter;
import lombok.Setter;

public class Board {
    // #todo: implement Cell class
    // @Getter @Setter private Cell[][] cellsGrid;
    @Getter @Setter private int goalAreaHeight;
    @Getter @Setter private int taskAreaHeight;
    @Getter @Setter private int boardWidth;
    @Getter @Setter private int boardHeight;

    public void Board(int boardWidth,int goalAreaHeight,int taskAreaHeight)
    {
        this.boardWidth  = boardWidth;
        this.goalAreaHeight = goalAreaHeight;
        this.taskAreaHeight = taskAreaHeight;
        this.boardHeight = 2 * goalAreaHeight + taskAreaHeight;
    }

//   #todo implement Field
//   public Field getField(Position position)
//    {
//        return field;
//    }

//    #todo implement Field
//    public void updateField(Field field)
//    {
//
//    }

//    #todo implement Cell
//    private void updateCell(Cell cell, Position position)
//    {
//
//    }

//    #todo implement Cell
//    private Cell getCell(Position position)
//    {
//        return cell;
//    }

    private void initializeCellsGrid()
    {

    }

//    #todo implement Point
//    private int manhattanDistanceTwoPoints(Point pointA, Point pointB)
//    {
//        return dist;
//    }
}