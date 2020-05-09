package pl.mini.board;

import lombok.Getter;
import lombok.Setter;
import pl.mini.cell.Cell;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.player.PlayerDTO;
import pl.mini.position.Direction;
import pl.mini.position.Position;
import pl.mini.team.TeamColor;

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
        int x_old = position.getX();
        int y_old = position.getY();
        int x = position.getX( );
        int y = position.getY( );
        switch (direction) {
            case Up:
                {
                y -= 1;
                if(y < 0)
                    y = 0;
                break;
                }
            case Down:
                {
                y += 1;
                if(y > getBoardHeight() - 1)
                    y = getBoardHeight() - 1;
                break;
                }
            case Left:
                {
                x -= 1;
                if(x < 0)
                    x = 0;
                break;
                }
            case Right:
                {
                x += 1;
                if(x > getBoardWidth() - 1)
                    x = getBoardWidth() - 1;
                break;
                }
        }

        if (!getCellsGrid()[x][y].playerGuid.equals(""))
            return new Position(x_old, y_old);

        if(x != x_old || y != y_old) {
            getCellsGrid()[x][y].playerGuid = player.getPlayerUuid().toString();
            getCellsGrid()[x_old][y_old].playerGuid = "";
        }

        position.setX(x);
        position.setY(y);

        return position;
    }


    public CellState takePiece(Position position) {
        int x = position.getX();
        int y = position.getY();
        CellState cs = getCellsGrid()[x][y].cellState;
        getCellsGrid()[x][y].cellState = CellState.Empty;
        piecesPosition.remove(position);
        for(int i = getGoalAreaHeight(); i < getGoalAreaHeight() + getTaskAreaHeight(); i++)
        {
            for(int j = 0; j < getBoardWidth(); j++)
            {
                Position pos = new Position(j, i);
                getCellsGrid()[j][i].distance = manhattanDistanceToClosestPiece(pos);
            }
        }
        return cs;
    }

    //generate piece on random position in the TaskArea
    public Position generatePiece() {
        Random randomX = new Random();
        Random randomY = new Random();
        int x = randomX.nextInt(getBoardWidth());
        int y = getGoalAreaHeight() + randomY.nextInt(getTaskAreaHeight());
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


    public PlacementResult placePiece(PlayerDTO player, double probab) {
        Position position = player.getPosition( );
        int x = position.getX( );
        int y = position.getY( );
        Random rand = new Random();
        double random = rand.nextDouble();
        if (random <= probab)
            return PlacementResult.Sham;
        if (getCellsGrid( )[ x ][ y ].cellState == CellState.Goal)
        {
            getCellsGrid()[x][y].cellState = CellState.Valid;
            return PlacementResult.Correct;
        }

        getCellsGrid()[x][y].cellState = CellState.Empty;
        return PlacementResult.Pointless;
    }


    public Position placePlayer(PlayerDTO player) {
        Random randomX = new Random();
        Random randomY = new Random();
        Cell[][] cll = this.getCellsGrid();
        int x = randomX.nextInt(getBoardWidth());
        int y;
        switch (player.getPlayerTeamColor()) {
            case Red: {
                y = randomY.nextInt(getGoalAreaHeight());
                break;
            }
            case Blue: {
                y = getGoalAreaHeight( ) + getTaskAreaHeight( ) + randomY.nextInt(getGoalAreaHeight( ));
                break;
            }
            default:
                return null;
        }

        cll[x][y].playerGuid = player.getPlayerUuid().toString();
        this.setCellsGrid(cll);
        return new Position(x, y);
    }


    public boolean checkWinCondition(TeamColor teamColor)
    {
        int goalcounter = 0;
        switch(teamColor)
        {
        case Red:
            {
            for(int i = 0; i < getGoalAreaHeight(); i++)
            {
                for (int j = 0; j < getBoardWidth(); j++)
                {
                    if(getCellsGrid()[j][i].cellState == CellState.Goal)
                        goalcounter++;
                }
            }
                return goalcounter == 0;
            }
        case Blue:
            {
                for(int i = getBoardHeight() - getGoalAreaHeight(); i < getBoardHeight(); i++)
                {
                    for (int j = 0; j < getBoardWidth(); j++)
                    {
                        if(getCellsGrid()[j][i].cellState == CellState.Goal)
                            goalcounter++;
                    }
                }
                return goalcounter == 0;
            }

        }
        return false;
    }


    public List<Field> discover(Position position) {
        int x = position.getX( );
        int y = position.getY( );
        List<Field> list = new ArrayList<>( );
        for (int i = y - 1; i <= y + 1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                int localX = j;
                int localY = i;
                if(localX < 0 || localY < 0 || localX > getBoardWidth() - 1 || localY > getBoardHeight() - 1)
                {
                 //   list.add(null);
                }

                else {
                    Position position1 = new Position(localX, localY);
                    Field field = new Field(position1, getCellsGrid()[position1.getX()][position1.getY()]);
                    list.add(field);
                }
            }
        }
        return list;
    }


    private int manhattanDistanceTwoPoints(Position pointA, Position pointB) {
        return Math.abs(pointA.getX( ) - pointB.getX( )) + Math.abs(pointA.getY( ) - pointB.getY( ));
    }

    public int manhattanDistanceToClosestPiece(Position position) {
        int min = Integer.MAX_VALUE;
        int id = 0;
        try {
            Position[] positions = piecesPosition.toArray(new Position[piecesPosition.size()]);


            for (int i = 0; i < positions.length; i++) {
                if (manhattanDistanceTwoPoints(position, positions[i]) < min) {
                    min = manhattanDistanceTwoPoints(position, positions[i]);
                    id = i;
                }
            }
            //System.out.println("Closest piece position: " + positions[id].toString());
            return min;
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public int manhattanDistanceToClosestUnknown(Position position, TeamColor teamColor) {
        int min = Integer.MAX_VALUE;
        int mDist;

        try {
            Cell[][] cll = this.getCellsGrid();

            if(teamColor == TeamColor.Red)
            {
                for (int i = 0; i < this.getBoardWidth(); i++)
                    for(int j = 0; j < this.getGoalAreaHeight(); j++) {
                        if (cll[i][j].cellState == CellState.Unknown || cll[i][j].cellState == CellState.Goal) {
                            mDist = manhattanDistanceTwoPoints(position, new Position(i, j));
                            if (mDist < min)
                                min = mDist;
                        }
                    }
            }
            else if(teamColor == TeamColor.Blue)
            {
                for (int i = 0; i < this.getBoardWidth(); i++)
                    for(int j = this.getGoalAreaHeight() + this.getTaskAreaHeight();
                        j < this.getBoardHeight(); j++) {
                        if (cll[i][j].cellState == CellState.Unknown || cll[i][j].cellState == CellState.Goal) {
                            mDist = manhattanDistanceTwoPoints(position, new Position(i, j));
                            if (mDist < min)
                                min = mDist;
                        }
                    }
            }

            if (min != Integer.MAX_VALUE)
                return min;
            else
                return -1;
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public Position findPlayerPositionByGuid(String guid) {
        for (int i = 0; i < getGoalAreaHeight(); i++)
            for (int j = 0; j < getBoardWidth(); j++)
                if (getCellsGrid()[j][i].playerGuid.equals(guid))
                    return new Position(j, i);

        // if not found
        return null;
    }
}