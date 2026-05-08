package esngalir.ui;

import esngalir.model.Board;
import esngalir.model.MoveResult;
import esngalir.model.State;
import esngalir.model.SolverResult;
import esngalir.model.Tile;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.List;

public class BoardPanel extends Pane {

    // Warna tile
    private static final Color COLOR_WALL       = Color.web("#1a3d0e");
    private static final Color COLOR_PATH       = Color.web("#0a1a04");
    private static final Color COLOR_LAVA       = Color.web("#c0392b");
    private static final Color COLOR_GOAL       = Color.web("#f8c148");
    private static final Color COLOR_CHECKPOINT = Color.web("#6aaf3d");
    private static final Color COLOR_ACTOR      = Color.web("#f8c148");
    private static final Color COLOR_TRAIL      = Color.web("#4a8c2a");
    private static final Color COLOR_BORDER     = Color.web("#2d5a1a");
    private static final Color COLOR_TEXT       = Color.web("#f1f5f9");

    private static final int TILE_SIZE = 64;
    private static final int PADDING   = 16;

    private Canvas       canvas;
    private Board        board;
    private SolverResult result;
    private int          currentStep;

    //animasi sliding
    private double actorPixelRow; //posisi pixel actor (animasi)
    private double actorPixelCol;
    private Timeline slideAnimation;

    public BoardPanel() {
        setStyle("-fx-background-color: #0a1a04;");
    }

    //call pas board/step berubah
    public void setBoard(Board board, SolverResult result, int step) {
        this.board       = board;
        this.result      = result;
        this.currentStep = step;

        //ukuran canvas dengan ukuran papan
        int w = board.getCols() * TILE_SIZE + PADDING * 2;
        int h = board.getRows() * TILE_SIZE + PADDING * 2;

        if (canvas == null) {
            canvas = new Canvas(w, h);
            getChildren().add(canvas);
        } else {
            canvas.setWidth(w);
            canvas.setHeight(h);
        }

        //posisi pixel actor di step ini
        if (result != null && result.isSolutionFound()) {
            State state   = result.getHistory().get(step);
            actorPixelRow = state.getRow() * TILE_SIZE;
            actorPixelCol = state.getCol() * TILE_SIZE;
        } else {
            actorPixelRow = board.getStartRow() * TILE_SIZE;
            actorPixelCol = board.getStartCol() * TILE_SIZE;
        }

        draw();
    }

    //animasi sliding dari step sekarang ke step berikutnya
    public void animateToStep(int targetStep, Runnable onFinished) {
        if (result == null || !result.isSolutionFound()) return;
        if (targetStep < 0 || targetStep >= result.getHistory().size()) return;

        List<State> history = result.getHistory();
        State       target  = history.get(targetStep);

        //titik-titik yang dilalui selama sliding
        double targetPixelRow = target.getRow() * TILE_SIZE;
        double targetPixelCol = target.getCol() * TILE_SIZE;

        if (slideAnimation != null) slideAnimation.stop();

        double dRow     = targetPixelRow - actorPixelRow;
        double dCol     = targetPixelCol - actorPixelCol;
        int    frames   = 12;
        double stepRow  = dRow / frames;
        double stepCol  = dCol / frames;

        final int[] frameCount = {0};

        slideAnimation = new Timeline(new KeyFrame(Duration.millis(40), e -> {
            frameCount[0]++;
            if (frameCount[0] >= frames) {
                actorPixelRow = targetPixelRow;
                actorPixelCol = targetPixelCol;
                currentStep   = targetStep;
                slideAnimation.stop();
                draw();
                if (onFinished != null) onFinished.run();
            } else {
                actorPixelRow += stepRow;
                actorPixelCol += stepCol;
                draw();
            }
        }));
        slideAnimation.setCycleCount(frames + 1);
        slideAnimation.play();
    }

    private void draw() {
        if (board == null || canvas == null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //background
        gc.setFill(Color.web("#0a1a04"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        List<int[]> trail = null;
        if (result != null && result.isSolutionFound() && currentStep > 0) {
            // Trail = semua posisi dari step 0 sampai step sekarang
        }

        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                drawTile(gc, r, c);
            }
        }

        //aktor
        drawActor(gc);
    }

    private void drawTile(GraphicsContext gc, int r, int c) {
        double x = PADDING + c * TILE_SIZE;
        double y = PADDING + r * TILE_SIZE;

        Tile  tile  = board.getTile(r, c);
        Color color = tileColor(tile);

        gc.setFill(color);
        gc.fillRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);

        gc.setStroke(COLOR_BORDER);
        gc.setLineWidth(1);
        gc.strokeRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);

        gc.setFill(COLOR_TEXT);
        gc.setFont(Font.font("Monospace", 13));
        gc.setTextAlign(TextAlignment.CENTER);

        String label = "";
        if (tile == Tile.GOAL) {
            label = "O";
        } else if (tile == Tile.LAVA) {
            label = "L";
        } else if (tile.isCheckPoint()) {
            // Cek apakah checkpoint ini sudah dilewati
            if (result != null && result.isSolutionFound()) {
                State state = result.getHistory().get(currentStep);
                if (tile.getCheckPointNumber() < state.getNextCheckPoint()) {
                    label = "V"; // sudah dilewati
                } else {
                    label = String.valueOf(tile.getSymbol());
                }
            } else {
                label = String.valueOf(tile.getSymbol());
            }
        }

        if (!label.isEmpty()) {
            gc.fillText(label, x + TILE_SIZE / 2.0, y + TILE_SIZE / 2.0 + 5);
        }
    }

    private void drawActor(GraphicsContext gc) {
        double x = PADDING + actorPixelCol + 4;
        double y = PADDING + actorPixelRow + 4;
        double s = TILE_SIZE - 8;

        gc.setFill(COLOR_ACTOR);
        gc.fillRect(x, y, s, s);

        gc.setFill(Color.web("#f8c148", 0.3));
        gc.fillRect(x + s * 0.15, y + s * 0.1, s * 0.35, s * 0.25);

        gc.setFill(COLOR_TEXT);
        gc.setFont(Font.font("Monospace", 14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Z", x + s / 2.0, y + s / 2.0 + 5);
    }

    private Color tileColor(Tile tile) {
        switch (tile) {
            case WALL:  return COLOR_WALL;
            case LAVA:  return COLOR_LAVA;
            case GOAL:  return COLOR_GOAL;
            case START: return COLOR_PATH;
            default:
                if (tile.isCheckPoint()) return COLOR_CHECKPOINT;
                return COLOR_PATH;
        }
    }

    public int getCurrentStep() { return currentStep; }
}