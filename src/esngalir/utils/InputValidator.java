package esngalir.utils;

import esngalir.model.Tile;

public class InputValidator {
    public static void validateSize(int rows, int cols){
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException(
                "Ukuran " + rows + "x" + cols + " tidak valid"
            );
        }
    }

    // validate di awal dan yang diinput per barisnya nanti semua ukurannya sama
    public static void validateRowLength(String line, int expectedCols, int rowIdx){
        if (line == null || line.length() != expectedCols) {
            throw new IllegalArgumentException("Panjang baris ke-" + rowIdx + " tidak sesuai. Expected: " + expectedCols + ". Reality: " + (line == null ? "null" : line.length()) + ".");
        }
    }

    public static void validateBoard(Tile[][] grid, int rows, int cols){
        int start = 0;
        int goal = 0;
        int maxCheck = -1;
        boolean[] checkpointFound = new boolean[10]; //checkpoint 0-9

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile tile = grid[i][j];
                if (tile == Tile.START) {
                    start++;
                }
                if (tile == Tile.GOAL) {
                    goal++;
                }
                if (tile.isCheckPoint()) {
                    int temp = tile.getCheckPointNumber();
                    checkpointFound[temp] = true;
                    if (temp > maxCheck) {
                        maxCheck = temp;
                    }
                }
            }
        }

        if (start != 1) {
            throw new IllegalArgumentException("Papan harus memiliki tepat 1 titik start ('Z'). Reality: " + start + ".");
        }
        if (goal != 1) {
            throw new IllegalArgumentException("Papan harus memiliki tepat 1 titik goal ('0'). Reality: " + goal + ".");
        }

        for (int i = 0; i <= maxCheck; i++) {
            if (!checkpointFound[i]) {
                throw new IllegalArgumentException("Checkpoint atau urutan angka yang harus dilalui tidak berurutan (" + i + " tidak ditemukan).");
            }
        }
        validateBorder(grid, rows, cols);
    }

    private static void validateBorder(Tile[][] grid, int rows, int cols){
        for (int i = 0; i < cols; i++) {
            if (grid[0][i] != Tile.WALL || grid[rows-1][i] != Tile.WALL) {
                throw new IllegalArgumentException("Baris atas dan bawah harus dinding semuanya ('X').");
            }
        }
        for (int i = 0; i < rows; i++) {
            if (grid[i][0] != Tile.WALL || grid[i][cols-1] != Tile.WALL) {
                throw new IllegalArgumentException("Kolom kiri dan kanan harus dinding semuanya ('X').");
            }
        }
    }
}
