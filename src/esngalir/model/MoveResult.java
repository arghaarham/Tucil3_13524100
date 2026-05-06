package esngalir.model;


import java.util.List;

public class MoveResult {
    public enum Status{
        SUCCESS,
        GAME_OVER, //lava, salah urutan, keluar papan
        NO_MOVE //dinding
    }

    private final Status status;
    private final int endRow;
    private final int endCol;
    private final int moveCost;
    private final int nextCheckPoint;
    private final List<int[]> listVisited;

    private MoveResult(Status status, int endRow, int endCol, int moveCost, int nextCheckPoint, List<int[]> listVisited){
        this.status = status;
        this.endRow = endRow;
        this.endCol = endCol;
        this.moveCost = moveCost;
        this.nextCheckPoint = nextCheckPoint;
        this.listVisited = listVisited;
    }

    //biar lebih jelas dan gampang di construct nanti, jadi status otomatis
    public static MoveResult success(int endRow, int endCol, int moveCost, int nextCheckPoint, List<int[]> listVisited){
        return new MoveResult(Status.SUCCESS, endRow, endCol, moveCost, nextCheckPoint, listVisited);
    }

    public static MoveResult gameOver(int endRow, int endCol, int moveCost, int nextCheckPoint, List<int[]> listVisited){
        return new MoveResult(Status.GAME_OVER, endRow, endCol, moveCost, nextCheckPoint, listVisited);
    }

    public static MoveResult noMove(int endRow, int endCol, int moveCost, int nextCheckPoint, List<int[]> listVisited){
        return new MoveResult(Status.NO_MOVE, endRow, endCol, moveCost, nextCheckPoint, listVisited);
    }

    public Status getStatus(){
        return status;
    }
    public int getEndRow(){
        return endRow;
    }
    public int getEndCol(){
        return endCol;
    }
    public int getMoveCost(){
        return moveCost;
    }
    public int getNextCheckPoint(){
        return nextCheckPoint;
    }
    public List<int[]> getListVisited(){
        return listVisited;
    }

    public boolean isSuccess(){
        return this.getStatus() == Status.SUCCESS;
    }
    public boolean isGameOver(){
        return this.getStatus() == Status.GAME_OVER;
    }
    public boolean isNoMove(){
        return this.getStatus() == Status.NO_MOVE;
    }
}
