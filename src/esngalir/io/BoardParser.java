package esngalir.io;

import esngalir.model.Board;
import esngalir.model.Tile;
import esngalir.utils.InputValidator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class BoardParser {
    public static Board parse(String filePath) throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            //baris pertama NxM
            Scanner sc = new Scanner(br.readLine());
            int rows = sc.nextInt();
            int cols = sc.nextInt();
            InputValidator.validateSize(rows, cols); //validate ukuran papan

            //tile
            Tile[][] grid = new Tile[rows][cols];
            for (int i = 0; i < rows; i++) {
                String line = br.readLine();
                InputValidator.validateRowLength(line, cols, i);

                for (int j = 0; j < cols; j++) {
                    grid[i][j] = Tile.convertFromChar(line.charAt(j));
                }
            }

            //cost
            int[][] cost = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                Scanner costt = new Scanner(br.readLine());
                for (int j = 0; j < cols; j++) {
                    cost[i][j] = costt.nextInt();
                }
            }

            //validate papan overall
            InputValidator.validateBoard(grid, rows, cols);
            
            return new Board(rows, cols, grid, cost);
        }
    }
}
