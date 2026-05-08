package esngalir.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int rows;
    private final int cols;
    private final Tile[][] grid;
    private final int[][] cost; //cost tiap tile
    private final int startRow;
    private final int startCol;
    private final int goalRow;
    private final int goalCol;
    private final int totalCheckPoint; //berapa banyak checkpoint (0..n-1)

    public Board(int rows, int cols, Tile[][] grid, int[][] cost){
        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.cost = cost;
        
        int tempRow = -1;
        int tempCol = -1;
        int tempGRow = -1;
        int tempGCol = -1;
        int tempCheckPoint = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == Tile.START) {
                    tempRow = i;
                    tempCol = j;
                }
                if (grid[i][j] == Tile.GOAL) {
                    tempGRow = i;
                    tempGCol = j;
                }
                if (grid[i][j].isCheckPoint()) {
                    tempCheckPoint++;
                }
            }
        }

        this.startRow = tempRow;
        this.startCol = tempCol;
        this.goalRow = tempGRow;
        this.goalCol = tempGCol;
        this.totalCheckPoint = tempCheckPoint;
    }

    private boolean inGrid(int row, int col){ //helper cek koordinat di papan apa ga
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean isGoal(State state){ //cek state udah goal apa belom
        return state.getRow() == goalRow && state.getCol() == goalCol && state.getNextCheckPoint() == totalCheckPoint;
    }

    public State getInitialState(){
        return new State(startRow, startCol, 0, 0, 0, null, null);
    }

    public MoveResult move(State state, Direction dir){ //gerakan sliding ke dir
        int row = state.getRow();
        int col = state.getCol();
        int checkPoint = state.getNextCheckPoint();
        int dRow = dir.getDeltaRow();
        int dCol = dir.getDeltaCol();

        int nextRow = row + dRow;
        int nextCol = col + dCol;

        if (!inGrid(nextRow, nextCol) || grid[nextRow][nextCol] == Tile.WALL) {
            return MoveResult.noMove();
        }
        
        List<int[]> visitedList = new ArrayList<>();
        int totalMoveCost = 0;

        while (true){
            int tempRow = row + dRow;
            int tempCol = col + dCol;

            if (!inGrid(tempRow, tempCol)) {
                return MoveResult.gameOver();
            }
            if (grid[tempRow][tempCol] == Tile.WALL) {
                break;
            }
            row = tempRow;
            col = tempCol;
            Tile tempTile = grid[row][col];

            if (tempTile == Tile.LAVA) {
                return MoveResult.gameOver();
            }

            if (tempTile.isCheckPoint()){
                int checkPointNumber = tempTile.getCheckPointNumber();
                if (checkPointNumber != checkPoint) {
                    return MoveResult.gameOver();
                }
                checkPoint++;
            }
            visitedList.add(new int[]{row, col});
            totalMoveCost += cost[row][col];
        }
        if (visitedList.isEmpty()) {
            return MoveResult.noMove();
        }
        return MoveResult.success(row, col, totalMoveCost, checkPoint, visitedList);
    }

    public int getRows(){
        return rows;
    }
    public int getCols(){
        return cols;
    }
    public Tile[][] getGrid(){
        return grid;
    }
    public int[][] getCost(){
        return cost;
    }
    public int getStartRow(){
        return startRow;
    }
    public int getStartCol(){
        return startCol;
    }
    public int getGoalRow(){
        return goalRow;
    }
    public int getGoalCol(){
        return goalCol;
    }
    public int getTotalCheckPoint(){
        return totalCheckPoint;
    }
    public Tile getTile(int row, int col){
        return grid[row][col];
    }
    public int getCost(int row, int col){
        return cost[row][col];
    }
}
