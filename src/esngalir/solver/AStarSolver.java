package esngalir.solver;

import esngalir.model.Board;
import esngalir.model.State;
import esngalir.heuristic.Heuristic;

public class AStarSolver extends Solver {
    private final Heuristic heuristic;

    public AStarSolver(Board board, Heuristic heuristic){
        super(board);
        this.heuristic = heuristic;
    }

    @Override
    protected State withFCost(State state){
        int g = state.getGCost();
        int h = heuristic.estimate(state, board);
        return new State(state.getRow(), state.getCol(), state.getNextCheckPoint(), g, g + h, state.getParent(), state.getMoveDir());
    }
}
