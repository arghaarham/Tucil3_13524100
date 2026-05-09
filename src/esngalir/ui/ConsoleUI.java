package esngalir.ui;

import esngalir.heuristic.CheckpointAware;
import esngalir.heuristic.Euclidean;
import esngalir.heuristic.Heuristic;
import esngalir.heuristic.Manhattan;
import esngalir.io.BoardParser;
import esngalir.io.SolutionWriter;
import esngalir.model.Board;
import esngalir.model.SolverResult;
import esngalir.solver.AStarSolver;
import esngalir.solver.BFSSolver;
import esngalir.solver.DFSSolver;
import esngalir.solver.GBFSSolver;
import esngalir.solver.UCSSolver;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("-----TUCIL3 STIMUY ICE SLIDING PUZZLE SOLVER-----");
        System.out.println("     Arghawisesa Dwinanda Arham  -  13524100     ");
        System.out.println();

        //file input
        System.out.print("Masukan file input: ");
        String filePath = scanner.nextLine().trim();

        //parse board
        Board board;
        try {
            board = BoardParser.parse(filePath);
        } catch (IOException e) {
            System.out.println("Error membaca file: " + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Input tidak valid: " + e.getMessage());
            return;
        }

        //algoritma
        System.out.print("Algoritma apa yang anda pilih? (UCS/GBFS/A*/BFS/DFS): ");
        String algo = scanner.nextLine().trim().toUpperCase();

        //heuristik (cuma buat GBFS, A*, IDA*)
        Heuristic heuristic = null;
        if (algo.equals("GBFS") || algo.equals("A*") || algo.equals("IDA*")) {
            heuristic = chooseHeuristic();
        }

        //solver
        SolverResult result = runSolver(algo, board, heuristic);
        if (result == null) {
            System.out.println("Algoritma tidak dikenal: " + algo);
            return;
        }

        tampilkanHasil(result, board);

        //playback
        System.out.print("Apakah Anda ingin melakukan playback? (Ya/Tidak): ");
        String jawabPlayback = scanner.nextLine().trim();
        if (jawabPlayback.equalsIgnoreCase("Ya")) {
            new Playback(result, board).run();
        }

        //simpan solusi
        System.out.print("Apakah Anda ingin menyimpan solusi? (Ya/Tidak): ");
        String jawabSimpan = scanner.nextLine().trim();
        if (jawabSimpan.equalsIgnoreCase("Ya")) {
            System.out.print("Masukan path file output: ");
            String outputPath = scanner.nextLine().trim();
            try {
                SolutionWriter.write(outputPath, result, board);
                System.out.println("Solusi disimpan pada " + outputPath);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan: " + e.getMessage());
            }
        }
    }

    private Heuristic chooseHeuristic() {
        System.out.println("Heuristic apa yang anda pilih?");
        System.out.println("   H1 - Manhattan Distance");
        System.out.println("   H2 - Checkpoint Aware");
        System.out.println("   H3 - Euclidean Distance");
        System.out.print("   Pilihan: ");
        String pilihan = scanner.nextLine().trim().toUpperCase();

        switch (pilihan) {
            case "H1": return new Manhattan();
            case "H2": return new CheckpointAware();
            case "H3": return new Euclidean();
            default:
                System.out.println("Heuristik tidak dikenal, menggunakan Manhattan.");
                return new Manhattan();
        }
    }

    private SolverResult runSolver(String algo, Board board, Heuristic heuristic) {
        switch (algo) {
            case "UCS":  return new UCSSolver(board).solve();
            case "GBFS": return new GBFSSolver(board, heuristic).solve();
            case "A*":   return new AStarSolver(board, heuristic).solve();
            case "BFS":  return new BFSSolver(board).solve();
            case "DFS":  return new DFSSolver(board).solve();
            default:     return null;
        }
    }

    private void tampilkanHasil(SolverResult result, Board board) {
        System.out.println();
        if (!result.isSolutionFound()) {
            System.out.println("Tidak ada solusi ditemukan.");
            return;
        }

        System.out.println("Solusi Yang Ditemukan : " + result.getMoveAsString());
        System.out.println("Cost dari Solusi      : " + result.getTotalCost());
        System.out.println();

        //visualisasi tiap step
        Playback.printStep(result, board, 0); // initial
        for (int i = 1; i < result.getHistory().size(); i++) {
            Playback.printStep(result, board, i);
        }

        System.out.println("Waktu eksekusi         : " + result.getTime() + " ms");
        System.out.println("Banyak iterasi         : " + result.getTotalIterate() + " iterasi");
    }
}