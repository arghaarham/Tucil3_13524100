package esngalir.heuristic;

import esngalir.model.Board;
import esngalir.model.State;
import esngalir.model.Tile;

//manhattan tapi lewat checkpoint dulu
public class CheckpointAware implements Heuristic{
    @Override
    public int estimate(State state, Board board){
        int nextCheckp = state.getNextCheckPoint();
        int total = board.getTotalCheckPoint();

        if (nextCheckp == total) { //semua checkpoint udah lewat maka langsung estimate ke goal
            return manhattan(state.getRow(), state.getCol(), board.getGoalRow(), board.getGoalCol());
        }

        int[] checkpPos = findCheckpoint(board, nextCheckp); //cari next posisi checkp di grid

        //estimate posisi skrg -> checkp next -> goal
        int toCheckp = manhattan(state.getRow(), state.getCol(), checkpPos[0], checkpPos[1]);
        int toGoal = manhattan(checkpPos[0], checkpPos[1], board.getGoalRow(), board.getGoalCol());

        return toCheckp + toGoal;
    }

    private int manhattan(int r1, int c1, int r2, int c2){
        return Math.abs(r1-r2) + Math.abs(c1-c2);
    }

    private int[] findCheckpoint(Board board, int checkpNum){
        Tile target = Tile.convertFromChar((char) ('0' + checkpNum));
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                if (board.getTile(i, j) == target) {
                    return new int[]{i, j};
                }
            }
        }
        
        throw new IllegalStateException("Checkpoint " + checkpNum + " tidak ditemukan.");
    }
}
