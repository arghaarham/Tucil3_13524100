package esngalir.solver;

import esngalir.model.Board;
import esngalir.model.Direction;
import esngalir.model.MoveResult;
import esngalir.model.State;
import esngalir.model.SolverResult;
import esngalir.utils.Timer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Deque;
import java.util.Set;

// BONUS!!!! bismillah
public class DFSSolver {
    private final Board board;

    public DFSSolver(Board board){
        this.board = board;
    }

    public SolverResult solve(){
        Timer timer = new Timer();
        timer.start();

        Deque<State> stack = new ArrayDeque<>();
        Set<State> visited = new HashSet<>();
        int iter = 0;
        State initial = board.getInitialState();
        stack.push(initial);
        visited.add(initial);

        while (!stack.isEmpty()) {
            State current = stack.pop();
            iter++;
            if (board.isGoal(current)) {
                timer.stop();
                return buildResult(current, iter, timer.getTime());
            }
            for (Direction dir : Direction.values()) {
                MoveResult moveResult = board.move(current, dir);

                if (!moveResult.isSuccess()) {
                    continue;
                }

                State next = new State(moveResult.getEndRow(), moveResult.getEndCol(), moveResult.getNextCheckPoint(), current.getGCost() + moveResult.getMoveCost(), 0, current, dir);

                if (!visited.contains(next)) {
                    visited.add(next);
                    stack.push(next);
                }
            }
        }
        timer.stop();
        return SolverResult.noSolution(iter, timer.getTime());
    }
    
    private SolverResult buildResult(State goal, int iter, long time){
        List<Direction> move = new ArrayList<>();
        List<State> stateHistroy = new ArrayList<>();
        int totalCost = goal.getGCost();

        State current = goal;
        while (current.getParent() != null) {
            move.add(current.getMoveDir());
            stateHistroy.add(current);
            current = current.getParent();
        }
        stateHistroy.add(current);
        
        Collections.reverse(move);
        Collections.reverse(stateHistroy);
        return new SolverResult(move, totalCost, iter, time, stateHistroy, true);
    }
}
