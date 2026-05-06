package esngalir.model;


import java.util.List;

public class SolverResult {
    private final List<Direction> moves; //urutan gerak
    private final int totalCost;
    private final int totalIterate; //banyak state yg diekspansi
    private final long time;
    private final List<State> history; //state tiap step buat playback tar
    private final boolean found;

    public SolverResult(List<Direction> moves, int totalCost, int totalIterate, long time, List<State> history, boolean found){
        this.moves = moves;
        this.totalCost = totalCost;
        this.totalIterate = totalIterate;
        this.time = time;
        this.history = history;
        this.found = found;
    }

    public static SolverResult noSolution(int totalIterate, long time){
        return new SolverResult(List.of(), 0, totalIterate, time, List.of(), false);
    }

    public String getMoveAsString(){ //dari list direction ke string
        StringBuilder s = new StringBuilder();
        for (Direction d : moves) {
            s.append(d.getSymbol());
        }
        return s.toString();
    }

    public List<Direction> getMoves(){
        return moves;
    }
    public int getTotalCost(){
        return totalCost;
    }
    public int getTotalIterate(){
        return totalIterate;
    }
    public long getTime(){
        return time;
    }
    public List<State> getHistory(){
        return history;
    }
    public boolean isSolutionFound(){
        return found;
    }

}
