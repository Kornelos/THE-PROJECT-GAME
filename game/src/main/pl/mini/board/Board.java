package pl.mini.board;


import lombok.Getter;
import lombok.Setter;
import pl.mini.cell.Cell;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.position.Position;


public class Board {

    @Getter
    @Setter
    private Cell[][] cellsGrid;
    @Getter
    @Setter
    private int goalAreaHeight;
    @Getter
    @Setter
    private int taskAreaHeight;
    @Getter
    @Setter
    private int boardWidth;
    @Getter
    @Setter
    private int boardHeight;

    public Board(int boardWidth, int goalAreaHeight, int taskAreaHeight) {
        this.boardWidth = boardWidth;
        this.goalAreaHeight = goalAreaHeight;
        this.taskAreaHeight = taskAreaHeight;
        this.boardHeight = 2 * goalAreaHeight + taskAreaHeight;
        initializeCellsGrid();
    }


    public Field getField(Position position) {
        int x = position.getX( );
        int y = position.getY( );
        Field field = new Field(position, cellsGrid[ x ][ y ]);
        return field;
    }


    public void updateField(Field field)
    {
        updateCell(field.getCell(), field.getPosition());
    }



    private void updateCell(Cell cell, Position position)
    {
        cellsGrid[position.getX()][position.getY()] = cell;
    }


    private Cell getCell(Position position) {
        int x = position.getX( );
        int y = position.getY( );
        return cellsGrid[ x ][ y ];
    }

    private void initializeCellsGrid() {
        cellsGrid = new Cell[boardWidth][boardHeight];

        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                if(j > getGoalAreaHeight() - 1 || j < getBoardHeight() - getGoalAreaHeight() - 1)
                cellsGrid[i][j] = new Cell(CellState.Empty);
                else
                    cellsGrid[i][j] = new Cell(CellState.Unknown);
            }
        }
    }


}