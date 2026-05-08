package esngalir.heuristic;

import esngalir.model.Board;
import esngalir.model.State;

public class Euclidean implements Heuristic{
    @Override
    public int estimate(State state, Board board){
        double dr = state.getRow() - board.getGoalRow();
        double dc = state.getCol() - board.getGoalCol();
        return (int) Math.sqrt((dr * dr) + (dc * dc)); 
    }
}
