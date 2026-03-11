package com.lulow.justtype.view;

import com.lulow.justtype.view.animations.OvershootAnimation;
import com.lulow.justtype.view.animations.PressAnimation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class GameView {

    private static final String COLOR_NEUTRAL  = "-fx-text-fill: #888888;";
    private static final String COLOR_CORRECT  = "-fx-text-fill: #07DACC;";
    private static final String COLOR_WRONG    = "-fx-text-fill: #FF4C4C;";

    private static final String TIMER_NORMAL = "-fx-text-fill: #FBFBFB; -fx-font-family: 'GeistMono NF'; -fx-font-size: 20px;";
    private static final String TIMER_CRITICAL = "-fx-text-fill: #FF4C4C; -fx-font-family: 'GeistMono NF'; -fx-font-size: 20px;";

    private static final String BTN_NORMAL = "-fx-background-color: #FBFBFB; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 0;";
    private static final String BTN_HOVER = "-fx-background-color: #C8C8C8; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-padding: 0;";

    private static final String CHAR_BASE_STYLE = "-fx-font-family: 'Determination'; -fx-font-size: 76px;";
    private static final String CHAR_SMALL_STYLE = "-fx-font-family: 'Determination'; -fx-font-size: 64px;";

    private static final int CRITICAL_TIME_THRESHOLD = 5;

    private final HBox  wordDisplay;
    private final Label levelLabel;
    private final Label timerLabel;

    private Label[] charLabels = new Label[0];
    private String  activeCharStyle = CHAR_BASE_STYLE;
    private PressAnimation inputAnimation;
    private PressAnimation buttonAnimation;

    public GameView(HBox wordDisplay, Label levelLabel, Label timerLabel) {
        this.wordDisplay = wordDisplay;
        this.levelLabel  = levelLabel;
        this.timerLabel  = timerLabel;
    }

    public void setupAnimations(TextField inputField, Button submitButton) {
        inputAnimation = new PressAnimation(inputField, 0.98);
        buttonAnimation = new PressAnimation(submitButton, 0.92);

        submitButton.setStyle(BTN_NORMAL);

        submitButton.setOnMouseEntered(event -> submitButton.setStyle(BTN_HOVER));
        submitButton.setOnMouseExited(event  -> submitButton.setStyle(BTN_NORMAL));

        submitButton.setOnMousePressed(event -> buttonAnimation.play());
        submitButton.setOnMouseReleased(event -> submitButton.setStyle(
                submitButton.isHover() ? BTN_HOVER : BTN_NORMAL
        ));
    }

    public void playInputAnimation() {
        if (inputAnimation != null) inputAnimation.play();
    }

    public void renderWord(char[] chars, int level) {
        activeCharStyle = level > 35 ? CHAR_SMALL_STYLE : CHAR_BASE_STYLE;
        charLabels = new Label[chars.length];
        wordDisplay.getChildren().clear();

        for (int i = 0; i < chars.length; i++) {
            Label lbl = new Label(String.valueOf(chars[i]));
            lbl.setStyle(activeCharStyle + COLOR_NEUTRAL);
            lbl.setAlignment(Pos.CENTER);
            charLabels[i] = lbl;
            wordDisplay.getChildren().add(lbl);
        }

        OvershootAnimation wordAnimation = new OvershootAnimation(wordDisplay, 1.16, 0.96);
        wordAnimation.play();
    }

    public void colorizeChars(String typed, String target) {
        for (int i = 0; i < charLabels.length; i++) {
            if (i < typed.length()) {
                boolean match = typed.charAt(i) == target.charAt(i);
                charLabels[i].setStyle(activeCharStyle + (match ? COLOR_CORRECT : COLOR_WRONG));
            } else {
                charLabels[i].setStyle(activeCharStyle + COLOR_NEUTRAL);
            }
        }
    }

    public void updateHUD(int level, int secondsLeft) {
        levelLabel.setText("#" + level);

        timerLabel.setText(String.valueOf(secondsLeft));
        timerLabel.setStyle(secondsLeft <= CRITICAL_TIME_THRESHOLD ? TIMER_CRITICAL : TIMER_NORMAL);

        OvershootAnimation timeAnimation = new OvershootAnimation(timerLabel, 1.3, 0.94);
        if (secondsLeft <= CRITICAL_TIME_THRESHOLD) { timeAnimation.play(); }
    }
}
