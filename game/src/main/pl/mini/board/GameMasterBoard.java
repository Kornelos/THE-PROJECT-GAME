package pl.mini.board;

import lombok.Getter;
import lombok.Setter;
import pl.mini.cell.Cell;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.player.PlayerDTO;
import pl.mini.position.Direction;
import pl.mini.position.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class GameMasterBoard extends Board {

    @Getter
    @Setter
    private Set<Position> piecesPosition;

    public GameMasterBoard(int boardWidth, int goalAreaHeight, int taskAreaHeight) {
        super(boardWidth, goalAreaHeight, taskAreaHeight);
    }


    public Position playerMove(PlayerDTO player, Direction direction) {
        Position position = player.getPosition( );
        int x = position.getX( );
        int y = position.getY( );
        position.changePosition(direction);
        position.setX(x);
        position.setY(y);
        return position;
    }


    public CellState takePiece(Position position) {
        int x = position.getX( );
        int y = position.getY( );
        return getCellsGrid( )[ x ][ y ].cellState;
    }

    //generate piece on random position in the TaskArea
    public Position generatePiece(double chance) {
        Random randomX = new Random( );
        Random randomY = new Random( );
        int x = randomX.nextInt(getBoardWidth( ));
        int y = getGoalAreaHeight( ) + randomY.nextInt(getTaskAreaHeight( ));
        return new Position(x, y);
    }


    public void setGoal(Position position) {
        int x = position.getX( );
        int y = position.getY( );
        Cell[][] testCG = getCellsGrid();
        testCG[x][y] = new Cell(CellState.Goal);
        setCellsGrid(testCG);
        // getCellsGrid( )[ x ][ y ].cellState = CellState.Goal;
    }


    public PlacementResult placePiece(PlayerDTO player) {
        Position position = player.getPosition( );
        int x = position.getX( );
        int y = position.getY( );
        if (getCellsGrid( )[ x ][ y ].cellState == CellState.Goal)
            return PlacementResult.Correct;

        return PlacementResult.Pointless;
    }


    public Position placePlayer(PlayerDTO player) {   //#todo other players boundaries
        Random randomX = new Random( );
        Random randomY = new Random( );
        int x = randomX.nextInt(getBoardWidth( ));
        switch (player.getPlayerTeamColor( )) {
            case Red: {
                int y = randomY.nextInt(getGoalAreaHeight( ));
                return new Position(x, y);
            }
            case Blue: {
                int y = getGoalAreaHeight( ) + getTaskAreaHeight( ) + randomY.nextInt(getGoalAreaHeight( ));
                return new Position(x, y);
            }
        }
        return null;
    }

//    #todo ticket how complete goals should be marked
//    public void checkWinCondition(TeamColor teamColor)
//    {
//
//    }


    public List<Field> discover(Position position) {
        //#todo boundary
        int x = position.getX( );
        int y = position.getY( );
        List<Field> list = new ArrayList<>( );
        for (int i = x - 1; i < x + 1; i++) {
            for (int j = y - 1; j < y + 1; j++) {
                Position position1 = new Position(j, i);
                Field field = new Field(position1, getCellsGrid( )[ j ][ i ]);
                list.add(field);
            }
        }
        return list;
    }


    private int manhattanDistanceTwoPoints(Position pointA, Position pointB) {
        return Math.abs(pointA.getX( ) - pointB.getX( )) + Math.abs(pointA.getY( ) - pointB.getY( ));
    }

    public int manhattanDistanceToClosestPiece(Position position) {
        int min = 0;
        int id = 0;
        Position[] positions = piecesPosition.toArray(new Position[piecesPosition.size()]);
        for (int i = 0; i < positions.length; i++) {
            if (manhattanDistanceTwoPoints(position, positions[i]) >= min)
                min = manhattanDistanceTwoPoints(position, positions[i]);
            id = i;
        }
        return manhattanDistanceTwoPoints(position, positions[id]);
    }
}