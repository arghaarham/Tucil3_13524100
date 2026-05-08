package esngalir.heuristic;

import esngalir.model.Board;
import esngalir.model.State;

public interface Heuristic {
    int estimate(State state, Board board);
}
