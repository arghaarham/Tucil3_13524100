package esngalir.model;

import java.util.Objects;

public class State { //kondisi "pion"
    private final int row;
    private final int col;
    private final int nextCheckPoint;
    private final int gCost; //total cost dari start ke state skrg
    private final int fCost; //nilai prioritas buat priority queue
    private final State parent; //buat bikin path
    private final Direction moveDir;

    public State(int row, int col, int nextCheckPoint, int gCost, int fCost, State parent, Direction moveDir){
        this.row = row;
        this.col = col;
        this.nextCheckPoint = nextCheckPoint;
        this.gCost = gCost;
        this.fCost = fCost;
        this.parent = parent;
        this.moveDir = moveDir;
    }

    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public int getNextCheckPoint(){
        return nextCheckPoint;
    }
    public int getGCost(){
        return gCost;
    }
    public int getFCost(){
        return fCost;
    }
    public State getParent(){
        return parent;
    }
    public Direction getMoveDir(){
        return moveDir;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) { //persis
            return true;
        }
        if (!(o instanceof State)) {
            return false;
        }
        State other = (State) o;
        return row == other.getRow() && col == other.getCol() && nextCheckPoint == other.getNextCheckPoint();
    }

    @Override
    public int hashCode(){
        return Objects.hash(row, col, nextCheckPoint);
    }

    @Override
    public String toString(){
        return String.format("State(%d,%d | cp=%d | g=%d | f=%d)", row, col, nextCheckPoint, gCost, fCost);
    }
}
