package esngalir.solver;

import esngalir.model.Board;
import esngalir.model.State;
import esngalir.heuristic.Heuristic;

public class GBFSSolver extends Solver {
    private final Heuristic heuristic;

    public GBFSSolver(Board board, Heuristic heuristic){
        super(board);
        this.heuristic = heuristic;
    }

    @Override
    protected State withFCost(State state){
        int h = heuristic.estimate(state, board);
        return new State(state.getRow(), state.getCol(), state.getNextCheckPoint(), state.getGCost(), h, state.getParent(), state.getMoveDir());
    }
}
