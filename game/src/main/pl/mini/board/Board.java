package pl.mini.board;

import lombok.Getter;
import lombok.Setter;

public class Board {
    //IMPLEMENT CELL CLASS
    //@Getter @Setter private Cell[][] cellsGrid;
    @Getter @Setter private int goalAreaHeight;
    @Getter @Setter private int taskAreaHeight;
    @Getter @Setter private int boardWidth;
    @Getter @Setter private int boardHeight;



    public void Board(int boardWidth,int goalAreaHeight,int taskAreaHeight)
    {
        this.boardWidth  = boardWidth;
        this.goalAreaHeight = goalAreaHeight;
        this.taskAreaHeight = taskAreaHeight;
        this.boardHeight = 2*goalAreaHeight + taskAreaHeight;
    }

   /* IMPLEMENT FIELD CLASS
   public Field getField(Position position)
    {
        return field;
    }
    */

    /* IMPLEMENT FIELD CLASS
    public void updateField(Field field)
    {

    }
    */

    /* IMPLEMENT POSITION CLASS
    private void updateCell(Cell cell, Position position)
    {

    }
    */

    /* IMPLEMENT CELL CLASS
    private Cell getCell(Position position)
    {
        return cell;
    }
    */

    private void initializeCellsGrid()
    {

    }

    /* IMPLEMENT POINT CLASS
    private int manhattanDistanceTwoPoints(Point pointA, Point pointB)
    {
        return dist;
    }
    */
}