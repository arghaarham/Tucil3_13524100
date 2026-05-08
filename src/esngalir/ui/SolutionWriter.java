package esngalir.ui;

import esngalir.model.Board;
import esngalir.model.SolverResult;
import esngalir.model.State;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;

public class SolutionWriter {
    public static void write(String path, SolverResult result, Board board) throws IOException{
        try (BufferedWriter b = new BufferedWriter(new FileWriter(path))){
            if (!result.isSolutionFound()) {
                b.write("Tidak ada solusi ditemukan.");
                b.newLine();
            } else{
                b.write("Move: " + result.getMoveAsString());
                b.newLine();
                b.write("Total Cost: " + result.getTotalCost());
                b.newLine();
                b.write("Total Iterasi: " + result.getTotalIterate());
                b.newLine();
                b.write("Time: " + result.getTime());
                b.newLine();
                b.newLine();
                b.write("-----Langkah Solusi-----");
                b.newLine();
                b.newLine();
                for (int i = 0; i < result.getHistory().size(); i++) {
                    State state = result.getHistory().get(i);
                    if (i == 0) {
                        b.write("Step awal: ");
                    } else{
                        b.write("Step ke-" + i + " (" + state.getMoveDir().name() + "): ");
                    }
                    b.newLine();
                    b.write(boardToString(board, state));
                    b.newLine();
                }
            }
        }
    }

    // RTender board as string
    private static String boardToString(Board board, State state){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                if (i == state.getRow() && j == state.getCol()) {
                    s.append('@'); //karakter saat ini
                } else{
                    s.append(board.getTile(i, j).getSymbol());
                }
            }
            s.append('\n');
        }
        return s.toString();
    }
}
