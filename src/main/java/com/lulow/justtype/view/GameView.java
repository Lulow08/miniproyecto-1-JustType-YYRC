package com.lulow.justtype.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class GameView {

    private static final String COLOR_NEUTRAL = "-fx-text-fill: #888888;";
    private static final String COLOR_CORRECT  = "-fx-text-fill: #07DACC;";
    private static final String COLOR_WRONG    = "-fx-text-fill: #FF4C4C;";
    private static final String CHAR_BASE_STYLE =
            "-fx-font-family: 'Determination'; -fx-font-size: 80px;";

    private final HBox  wordDisplay;
    private final Label levelLabel;
    private final Label timerLabel;

    private Label[] charLabels = new Label[0];

    public GameView(HBox wordDisplay, Label levelLabel, Label timerLabel) {
        this.wordDisplay = wordDisplay;
        this.levelLabel  = levelLabel;
        this.timerLabel  = timerLabel;
    }

    public void renderWord(char[] chars) {
        charLabels = new Label[chars.length];
        wordDisplay.getChildren().clear();

        for (int i = 0; i < chars.length; i++) {
            Label lbl = new Label(String.valueOf(chars[i]));
            lbl.setStyle(CHAR_BASE_STYLE + COLOR_NEUTRAL);
            lbl.setAlignment(Pos.CENTER);
            charLabels[i] = lbl;
            wordDisplay.getChildren().add(lbl);
        }
    }

    public void colorizeChars(String typed, String target) {
        for (int i = 0; i < charLabels.length; i++) {
            if (i < typed.length()) {
                boolean match = typed.charAt(i) == target.charAt(i);
                charLabels[i].setStyle(CHAR_BASE_STYLE + (match ? COLOR_CORRECT : COLOR_WRONG));
            } else {
                charLabels[i].setStyle(CHAR_BASE_STYLE + COLOR_NEUTRAL);
            }
        }
    }

    public void updateHUD(int level, int time) {
        levelLabel.setText("#" + level);
        timerLabel.setText(String.valueOf(time));
    }
}
