package esngalir.solver;

import esngalir.model.Board;
import esngalir.model.State;

public class UCSSolver extends Solver{
    public UCSSolver(Board board){
        super(board);
    }

    @Override
    protected State withFCost(State state){
        return new State(state.getRow(), state.getCol(), state.getNextCheckPoint(), state.getGCost(), state.getGCost(), state.getParent(), state.getMoveDir());
        // fcost == gcost karena gaada heuristic
    }
}
