package esngalir.solver;

import esngalir.model.Board;
import esngalir.model.Direction;
import esngalir.model.MoveResult;
import esngalir.model.State;
import esngalir.model.SolverResult;
import esngalir.utils.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Comparator;

public abstract class Solver {
    protected Board board;

    public Solver(Board board){
        this.board = board;
    }

    public SolverResult solve(){
        Timer timer = new Timer();
        timer.start();

        PriorityQueue<State> openSet = new PriorityQueue<>(new Comparator<State>() {
            @Override
            public int compare(State a, State b){
                return Integer.compare(a.getFCost(), b.getFCost());
            }
        });
        Set<State> visited = new HashSet<>();
        int iter = 0;

        State initialState = board.getInitialState();
        openSet.add(withFCost(initialState));

        while (!openSet.isEmpty()){
            State current = openSet.poll();
            iter++;

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            if (board.isGoal(current)) {
                timer.stop();
                return buildResult(current, iter, timer.getTime());
            }

            for (Direction dir : Direction.values()) {
                MoveResult moveResult = board.move(current, dir);

                if (!moveResult.isSuccess()) {
                    continue;
                }

                State next = new State(moveResult.getEndRow(), moveResult.getEndCol(), moveResult.getNextCheckPoint(), current.getGCost() + moveResult.getMoveCost(), 0, current, dir); //fcostnya 0 temp aja
                if (!visited.contains(next)) {
                    openSet.add(withFCost(next));
                }
            }
        }

        timer.stop();
        return SolverResult.noSolution(iter, timer.getTime());

    }

    //UCS: fcost = gcost
    //GBFS: fcost = h(state)
    //AStar: fcost = gcost+h(state)
    protected abstract State withFCost(State state); //implement di subclass

    //build path dari goal ke start (dari chain parent)
    private SolverResult buildResult(State goal, int iter, long time){
        List<Direction> moves = new ArrayList<>();
        List<State> stateHistory = new ArrayList<>();
        int totalCost = goal.getGCost();

        //follow chain parent goal-start
        State current = goal;
        while (current.getParent() != null) {
            moves.add(current.getMoveDir());
            stateHistory.add(current);
            current = current.getParent();
        }
        stateHistory.add(current);

        Collections.reverse(moves);
        Collections.reverse(stateHistory);

        return new SolverResult(moves, totalCost, iter, time, stateHistory, true);
    }
}
