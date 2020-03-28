package pl.mini.board;


import lombok.Getter;
import lombok.Setter;
import pl.mini.cell.*;
import pl.mini.position.Position;


public class Board {

    @Getter @Setter private Cell[][] cellsGrid;
    @Getter @Setter private int goalAreaHeight;
    @Getter @Setter private int taskAreaHeight;
    @Getter @Setter private int boardWidth;
    @Getter @Setter private int boardHeight;

    public Board(int boardWidth,int goalAreaHeight,int taskAreaHeight)
    {
        this.boardWidth  = boardWidth;
        this.goalAreaHeight = goalAreaHeight;
        this.taskAreaHeight = taskAreaHeight;
        this.boardHeight = 2 * goalAreaHeight + taskAreaHeight;
    }


   public Field getField(Position position)
    {
        int x = position.getX();
        int y = position.getY();
        Field field = new Field(position, cellsGrid[x][y]);
        return field;
    }

    // #todo ticket
    // #todo update field class to ver. 1.1.7
    // #probably update field color
    public void updateField(Field field)
    {
        Position position = field.getPosition();
        int x = position.getX();
        int y = position.getY();
        field.setCell(cellsGrid[x][y]);
        return;
    }

    // #todo ticket
    // #todo update cell class to ver. 1.1.7
    private void updateCell(Cell cell, Position position)
    {

    }


    private Cell getCell(Position position)
    {
        int x = position.getX();
        int y = position.getY();
        return cellsGrid[x][y];
    }

    private void initializeCellsGrid()
    {
        cellsGrid = new Cell[boardWidth][boardHeight];
    }



}