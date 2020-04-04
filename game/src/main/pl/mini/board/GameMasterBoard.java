package pl.mini.board;

import lombok.Getter;
import lombok.Setter;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.player.PlayerDTO;
import pl.mini.position.Direction;
import pl.mini.position.Position;

import java.util.*;


public class GameMasterBoard extends Board {

    @Getter
    @Setter
    private Set<Position> piecesPosition = new HashSet<>();


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
        getCellsGrid( )[ x ][ y ].cellState = CellState.Goal;
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
        for (int i = y - 1; i < y + 1; i++) {
            for (int j = x - 1; j < x + 1; j++) {
                int localX = j;
                int localY = i;
                if(localX < 0)
                    localX = 0;
                if(localY < 0)
                    localY = 0;
                if(localX > getBoardWidth() - 1)
                    localX = getBoardWidth() - 1;
                if(localY > getBoardHeight() - 1)
                    localY = getBoardHeight() - 1;
                Position position1 = new Position(localX, localY);
                Field field = new Field(position1, getCellsGrid( )[ position1.getX() ][ position1.getY() ]);
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
        try {
            Position[] positions = piecesPosition.toArray(new Position[piecesPosition.size()]);


            for (int i = 0; i < positions.length; i++) {
                if (manhattanDistanceTwoPoints(position, positions[i]) >= min)
                    min = manhattanDistanceTwoPoints(position, positions[i]);
                id = i;
            }
            return manhattanDistanceTwoPoints(position, positions[id]);
        }
        catch(NullPointerException e)
        {
            return -1;
        }
    }
}