package esngalir.ui;

import esngalir.model.Board;
import esngalir.model.SolverResult;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ControlPanel extends VBox {

    private final BoardPanel boardPanel;

    private SolverResult result;
    private Board        board;
    private int          currentStep = 0;
    private int          maxStep     = 0;
    private boolean      isPlaying   = false;
    private Timeline     autoPlay;

    private Label  stepLabel;
    private Button btnPrev;
    private Button btnPlay;
    private Button btnNext;
    private Slider speedSlider;

    public ControlPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
        setStyle(
            "-fx-background-color: #122A07;" +
            "-fx-background-radius: 0;" +
            "-fx-padding: 12;"
        );
        setSpacing(8);
        buildUI();
    }

    private void buildUI() {
        stepLabel = new Label("Step 0 / 0");
        stepLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 13;");

        btnPrev = buildButton("<<", "#1a3d0e");
        btnPlay = buildButton("> Play", "#f8c148");
        btnNext = buildButton(">>", "#1a3d0e");

        btnPrev.setOnAction(e -> prevStep());
        btnNext.setOnAction(e -> nextStep());
        btnPlay.setOnAction(e -> togglePlay());

        Label speedLabel = new Label("Kecepatan:");
        speedLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 12;");

        speedSlider = new Slider(1, 10, 5);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(3);
        speedSlider.setPrefWidth(200);
        speedSlider.setStyle("-fx-control-inner-background: #1a3d0e;");

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (isPlaying) {
                stopAutoPlay();
                startAutoPlay();
            }
        });

        HBox controls = new HBox(12, btnPrev, btnPlay, btnNext);
        controls.setAlignment(Pos.CENTER);

        HBox speedBox = new HBox(8, speedLabel, speedSlider);
        speedBox.setAlignment(Pos.CENTER);

        setAlignment(Pos.CENTER);
        getChildren().addAll(stepLabel, controls, speedBox);

        setControlsDisabled(true);
    }

    public void setResult(SolverResult result, Board board) {
        this.result      = result;
        this.board       = board;
        this.currentStep = 0;
        this.maxStep     = result.isSolutionFound() ? result.getHistory().size() - 1 : 0;

        stopAutoPlay();
        isPlaying = false;
        btnPlay.setText("> Play");

        updateStepLabel();
        setControlsDisabled(!result.isSolutionFound());
    }

    public void reset() {
        currentStep = 0;
        maxStep     = 0;
        result      = null;
        stopAutoPlay();
        isPlaying = false;
        btnPlay.setText("> Play");
        updateStepLabel();
        setControlsDisabled(true);
    }

    private void prevStep() {
        if (currentStep > 0) {
            currentStep--;
            boardPanel.animateToStep(currentStep, this::updateStepLabel);
        }
    }

    private void nextStep() {
        if (currentStep < maxStep) {
            currentStep++;
            boardPanel.animateToStep(currentStep, this::updateStepLabel);
        } else if (isPlaying) {
            stopAutoPlay();
            isPlaying = false;
            btnPlay.setText("> Play");
        }
    }

    private void togglePlay() {
        if (isPlaying) {
            stopAutoPlay();
            isPlaying = false;
            btnPlay.setText("> Play");
        } else {
            if (currentStep >= maxStep) {
                currentStep = 0;
                boardPanel.setBoard(board, result, 0);
                updateStepLabel();
            }
            isPlaying = true;
            btnPlay.setText("[] Pause");
            startAutoPlay();
        }
    }

    private void startAutoPlay() {
        // slider 1-10: delay 900ms - 100ms
        double speed    = speedSlider.getValue();
        double delayMs  = 1000 - (speed * 90);

        autoPlay = new Timeline(new KeyFrame(Duration.millis(delayMs), e -> {
            if (currentStep < maxStep) {
                currentStep++;
                boardPanel.animateToStep(currentStep, this::updateStepLabel);
            } else {
                stopAutoPlay();
                isPlaying = false;
                btnPlay.setText("> Play");
            }
        }));
        autoPlay.setCycleCount(Timeline.INDEFINITE);
        autoPlay.play();
    }

    private void stopAutoPlay() {
        if (autoPlay != null) {
            autoPlay.stop();
            autoPlay = null;
        }
    }

    private void updateStepLabel() {
        stepLabel.setText("Step " + currentStep + " / " + maxStep);
    }

    private void setControlsDisabled(boolean disabled) {
        btnPrev.setDisable(disabled);
        btnPlay.setDisable(disabled);
        btnNext.setDisable(disabled);
        speedSlider.setDisable(disabled);
    }

    private Button buildButton(String text, String color) {
        boolean isAccent = color.equals("#f8c148");
        String textColor = isAccent ? "#122A07" : "#ffffff";
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: " + textColor + ";" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 0;" +
            "-fx-cursor: hand;" +
            "-fx-min-width: 80;"
        );
        return btn;
    }
}