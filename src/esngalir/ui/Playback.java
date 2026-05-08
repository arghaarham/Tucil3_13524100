package esngalir.ui;

import esngalir.model.Board;
import esngalir.model.Direction;
import esngalir.model.State;
import esngalir.model.SolverResult;

import java.util.List;
import java.util.Scanner;

public class Playback {

    private final SolverResult result;
    private final Board        board;
    private final Scanner      scanner = new Scanner(System.in);

    public Playback(SolverResult result, Board board) {
        this.result = result;
        this.board  = board;
    }

    public void run() {
        if (!result.isSolutionFound()) {
            System.out.println("Tidak ada solusi untuk di-playback.");
            return;
        }

        List<State> history  = result.getHistory();
        int         maxStep  = history.size() - 1;
        int         step     = 0;

        System.out.println("-----Playback Mode-----");
        System.out.println("Arrow kanan (d) : maju 1 step");
        System.out.println("Arrow kiri  (a) : mundur 1 step");
        System.out.println("ESC         (e) : lompat ke step tertentu");
        System.out.println("Quit        (q) : keluar playback");
        System.out.println();

        printStep(result, board, step);

        while (true) {
            System.out.print("Input: ");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "d": //maju
                    if (step < maxStep) {
                        step++;
                        printStep(result, board, step);
                    } else {
                        System.out.println("Sudah di step terakhir.");
                    }
                    break;

                case "a": //mundur
                    if (step > 0) {
                        step--;
                        printStep(result, board, step);
                    } else {
                        System.out.println("Sudah di step pertama.");
                    }
                    break;

                case "e": //lompat ke step tertentu
                    System.out.print(">> Pada step berapa anda ingin melakukan playback (0-"
                        + maxStep + "): ");
                    try {
                        int target = Integer.parseInt(scanner.nextLine().trim());
                        if (target >= 0 && target <= maxStep) {
                            step = target;
                            printStep(result, board, step);
                        } else {
                            System.out.println("Step tidak valid, range: 0-" + maxStep);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input harus angka.");
                    }
                    break;

                case "q": // keluar
                    System.out.println("Keluar dari playback.");
                    return;

                default:
                    System.out.println("Input tidak dikenal. (d/a/e/q)");
            }
        }
    }

    public static void printStep(SolverResult result, Board board, int step) {
        List<State> history = result.getHistory();
        State       state   = history.get(step);

        if (step == 0) {
            System.out.println("Initial");
        } else {
            Direction dir = state.getMoveDir();
            System.out.println("Step " + step + " : " + dir.getSymbol());
        }

        //papan
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                if (r == state.getRow() && c == state.getCol()) {
                    System.out.print('Z'); //posisi karakter
                } else {
                    System.out.print(board.getTile(r, c).getSymbol());
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}