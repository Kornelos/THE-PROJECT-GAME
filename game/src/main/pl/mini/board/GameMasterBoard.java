package pl.mini.board;

import lombok.Getter;
import lombok.Setter;

import pl.mini.player.*;
import pl.mini.position.*;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;


import java.util.*;


public class GameMasterBoard extends Board {

     @Getter
     @Setter private Set<Position> piecesPosition;

    public GameMasterBoard(int boardWidth, int goalAreaHeight, int taskAreaHeight) {
        super(boardWidth, goalAreaHeight, taskAreaHeight);
    }


    public Position playerMove(PlayerDTO player, Direction direction)
    {
        Position position = player.getPosition();
        int x = position.getX();
        int y = position.getY();
        position.changePosition(direction);
        position.setX(x);
        position.setY(y);
        return position;
    }


    public CellState takePiece(Position position)
    {
        int x = position.getX();
        int y = position.getY();
        return getCellsGrid()[x][y].cellState;
    }

    //generate piece on random position in the TaskArea
    public Position generatePiece(double chance)
    {
        Random randomX = new Random();
        Random randomY = new Random();
        int x = randomX.nextInt(getBoardWidth());
        int y = getGoalAreaHeight() + randomY.nextInt(getTaskAreaHeight());
        return new Position(x,y);
    }


    public void setGoal(Position position)
    {
        int x = position.getX();
        int y = position.getY();
        getCellsGrid()[x][y].cellState = CellState.Goal;
    }


    public PlacementResult placePiece(PlayerDTO player)
    {
        Position position = player.getPosition();
        int x = position.getX();
        int y = position.getY();
        if (getCellsGrid()[x][y].cellState == CellState.Goal)
            return PlacementResult.Correct;

        return PlacementResult.Pointless;
    }

//  #todo IMPLEMENT FIELD COLOR
    public Position placePlayer(PlayerDTO player)
    {   Position position = new Position(0,0);
        return position;
    }

//    #todo IMPLEMENT FIELD COLOR
//    public void checkWinCondition(TeamColor teamColor)
//    {
//
//    }


    public List<Field> discover(Position position)
    {
        //#todo boundary
        int x = position.getX();
        int y = position.getY();
        List<Field> list = new ArrayList<>();
        for(int i = x - 1; i < x + 1; i++)
        {
            for(int j = y - 1; j < y + 1; j++)
            {
                Position position1 = new Position (j, i);
                Field field = new Field(position1, getCellsGrid()[j][i]);
                list.add(field);
            }
        }
        return list;
    }

//    point?

//    private int manhattanDistanceTwoPoints(Point pointA, Point pointB)
//    {
//        return dist;
//    }

}