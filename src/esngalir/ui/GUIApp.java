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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class GUIApp extends Application {

    private BoardPanel   boardPanel;
    private ControlPanel controlPanel;

    private Board       board;
    private SolverResult result;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EsNgalir: Ice Sliding Puzzle Solver");

        //layout utama
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a1a04;");
        root.setPadding(new Insets(16));

        //panel kiri (input control)
        VBox leftPanel = buildLeftPanel(primaryStage);
        leftPanel.setPrefWidth(260);

        //panel tengah (board game)
        boardPanel = new BoardPanel();

        //panel bawah (playback control)
        controlPanel = new ControlPanel(boardPanel);

        root.setLeft(leftPanel);
        root.setCenter(boardPanel);
        root.setBottom(controlPanel);

        BorderPane.setMargin(leftPanel, new Insets(0, 16, 0, 0));
        BorderPane.setMargin(controlPanel, new Insets(16, 0, 0, 0));

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox buildLeftPanel(Stage stage) {
        VBox panel = new VBox(12);
        panel.setStyle(
            "-fx-background-color: #122A07;" +
            "-fx-background-radius: 0;" +
            "-fx-padding: 16;"
        );

        Label title = new Label("EsNgalir! Ice Sliding Puzzle");
        title.setStyle("-fx-text-fill: #f8c148; -fx-font-size: 18; -fx-font-weight: bold;");

        Label subtitle = new Label("Tucil 3 Strategi Algoritma\nArghawisessa Dwinanda Arham - 13524100");
        subtitle.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 11;");
        subtitle.setWrapText(true);

        Label fileLabel = new Label("File Input:");
        fileLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 12;");

        Label fileNameLabel = new Label("Belum dipilih");
        fileNameLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 11;");
        fileNameLabel.setWrapText(true);

        Button btnFile = buildButton("Pilih File (.txt)", "#f8c148", "#122A07");
        btnFile.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Pilih File Test Case");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                try {
                    board = BoardParser.parse(file.getAbsolutePath());
                    boardPanel.setBoard(board, null, 0);
                    fileNameLabel.setText(file.getName());
                    result = null;
                    controlPanel.reset();
                } catch (IOException | IllegalArgumentException ex) {
                    showError("Error membaca file", ex.getMessage());
                }
            }
        });

        //algoritma
        Label algoLabel = new Label("Algoritma:");
        algoLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 12;");

        ComboBox<String> algoBox = new ComboBox<>();
        algoBox.getItems().addAll("UCS", "GBFS", "A*", "BFS", "DFS");
        algoBox.setValue("A*");
        algoBox.setMaxWidth(Double.MAX_VALUE);
        algoBox.setStyle("-fx-background-color: #1a3d0e; -fx-background-radius: 0;");
        algoBox.setButtonCell(new ListCell<String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold;");
            }
        });
        algoBox.setCellFactory(lv -> new ListCell<String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-text-fill: #ffffff; -fx-background-color: #1a3d0e; -fx-padding: 4 8;");
            }
        });

        //heuristik
        Label heurLabel = new Label("Heuristik:");
        heurLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 12;");

        ComboBox<String> heurBox = new ComboBox<>();
        heurBox.getItems().addAll("H1 - Manhattan", "H2 - Checkpoint Aware", "H3 - Euclidean");
        heurBox.setValue("H2 - Checkpoint Aware");
        heurBox.setMaxWidth(Double.MAX_VALUE);
        heurBox.setStyle("-fx-background-color: #1a3d0e; -fx-background-radius: 0;");
        heurBox.setButtonCell(new ListCell<String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold;");
            }
        });
        heurBox.setCellFactory(lv -> new ListCell<String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-text-fill: #e8e8e8; -fx-background-color: #1a3d0e; -fx-padding: 4 8;");
            }
        });

        //disable heuristik if UCS/BFS/DFS
        algoBox.setOnAction(e -> {
            String algo = algoBox.getValue();
            boolean needsHeur = algo.equals("GBFS") || algo.equals("A*");
            heurBox.setDisable(!needsHeur);
        });
        heurBox.setDisable(false);

        //solve
        Button btnSolve = buildButton("Solve!", "#f8c148", "#122A07");
        btnSolve.setMaxWidth(Double.MAX_VALUE);
        btnSolve.setOnAction(e -> {
            if (board == null) {
                showError("Error", "Pilih file input terlebih dahulu.");
                return;
            }
            String algo = algoBox.getValue();
            Heuristic heuristic = parseHeuristic(heurBox.getValue());
            result = jalankanSolver(algo, board, heuristic);

            if (result == null) return;

            //hasil di control panel
            controlPanel.setResult(result, board);

            //step awal
            boardPanel.setBoard(board, result, 0);
        });

        //hasil
        Label infoLabel = new Label("");
        infoLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 11;");
        infoLabel.setWrapText(true);

        //info after solve
        btnSolve.setOnAction(e -> {
            if (board == null) {
                showError("Error", "Pilih file input terlebih dahulu.");
                return;
            }
            String algo     = algoBox.getValue();
            Heuristic heur  = parseHeuristic(heurBox.getValue());
            result          = jalankanSolver(algo, board, heur);
            if (result == null) return;

            controlPanel.setResult(result, board);
            boardPanel.setBoard(board, result, 0);

            if (result.isSolutionFound()) {
                infoLabel.setText(
                    "Gerakan : " + result.getMoveAsString() + "\n" +
                    "Cost    : " + result.getTotalCost() + "\n" +
                    "Iterasi : " + result.getTotalIterate() + "\n" +
                    "Waktu   : " + result.getTime() + " ms"
                );
            } else {
                infoLabel.setText("Tidak ada solusi ditemukan.");
            }
        });

        //simpan solusi
        Button btnSave = buildButton("Simpan Solusi", "#f8c148", "#122A07");
        btnSave.setMaxWidth(Double.MAX_VALUE);
        btnSave.setOnAction(e -> {
            if (result == null) {
                showError("Error", "Jalankan solver terlebih dahulu.");
                return;
            }
            FileChooser fc = new FileChooser();
            fc.setTitle("Simpan Solusi");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            File file = fc.showSaveDialog(stage);
            if (file != null) {
                try {
                    SolutionWriter.write(file.getAbsolutePath(), result, board);
                } catch (IOException ex) {
                    showError("Gagal menyimpan", ex.getMessage());
                }
            }
        });

        panel.getChildren().addAll(
            title, subtitle,
            fileLabel, btnFile, fileNameLabel,
            algoLabel, algoBox,
            heurLabel, heurBox,
            btnSolve,
            infoLabel,
            btnSave
        );

        return panel;
    }

    private Button buildButton(String text, String bgColor, String textColor) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-text-fill: " + textColor + ";" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 0;" +
            "-fx-cursor: hand;"
        );
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private Heuristic parseHeuristic(String val) {
        if (val.startsWith("H2")) return new CheckpointAware();
        if (val.startsWith("H3")) return new Euclidean();
        return new Manhattan();
    }

    private SolverResult jalankanSolver(String algo, Board board, Heuristic heuristic) {
        try {
            switch (algo) {
                case "UCS":  return new UCSSolver(board).solve();
                case "GBFS": return new GBFSSolver(board, heuristic).solve();
                case "A*":   return new AStarSolver(board, heuristic).solve();
                case "BFS":  return new BFSSolver(board).solve();
                case "DFS":  return new DFSSolver(board).solve();
                default:     return null;
            }
        } catch (Exception e) {
            showError("Error saat solve", e.getMessage());
            return null;
        }
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    //entry point dari Main.java
    public static void launch(String[] args) {
        Application.launch(GUIApp.class, args);
    }
}