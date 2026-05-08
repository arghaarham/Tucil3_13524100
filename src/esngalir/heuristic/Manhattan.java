package esngalir.heuristic;

import esngalir.model.Board;
import esngalir.model.State;


//jumlah langkah row + col
public class Manhattan implements Heuristic{
    @Override
    public int estimate(State state, Board board){
        int dr = Math.abs(state.getRow() - board.getGoalRow());
        int dc = Math.abs(state.getCol() - board.getGoalCol());

        return dr + dc;
    }
}