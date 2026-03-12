package com.lulow.justtype.view;

import com.lulow.justtype.view.animations.OvershootAnimation;
import com.lulow.justtype.view.animations.PressAnimation;
import com.lulow.justtype.view.animations.SlideInAnimation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class GameView {

    private static final String CSS_CHAR_NORMAL    = "char-normal";
    private static final String CSS_CHAR_SMALL     = "char-small";
    private static final String CSS_CHAR_NEUTRAL   = "char-neutral";
    private static final String CSS_CHAR_CORRECT   = "char-correct";
    private static final String CSS_CHAR_WRONG     = "char-wrong";
    private static final String CSS_TIMER_NORMAL   = "timer-label";
    private static final String CSS_TIMER_CRITICAL = "timer-label-critical";

    private static final double WORD_TOP_MARGIN_NORMAL = 78;
    private static final double WORD_TOP_MARGIN_SMALL = 92;
    private static final double TIMER_PILL_SLIDE_FROM = -80;
    private static final int CRITICAL_TIME_THRESHOLD = 5;

    private final HBox      wordDisplay;
    private final HBox      timerPill;
    private final Label     levelLabel;
    private final Label     timerLabel;
    private final StackPane timerPane;

    private Label[] charLabels      = new Label[0];

    private PressAnimation inputAnimation;
    private PressAnimation buttonAnimation;

    private final TimerArc           timerArc;
    private final BackgroundGradient backgroundGradient;

    public GameView(HBox wordDisplay, Label levelLabel, Label timerLabel, StackPane timerPane, AnchorPane rootPane, HBox timerPill ) {
        this.wordDisplay = wordDisplay;
        this.timerPill   = timerPill;
        this.levelLabel  = levelLabel;
        this.timerLabel  = timerLabel;
        this.timerPane   = timerPane;

        this.timerArc           = new TimerArc();
        this.backgroundGradient = new BackgroundGradient(rootPane);

        setupTimerArc();
    }

    private void setupTimerArc() {
        timerPane.getChildren().add(0, timerArc.getContainer());
        Node arcNode = timerPane.getChildren().get(0);
        StackPane.setAlignment(arcNode, Pos.CENTER_LEFT);
        StackPane.setMargin(arcNode, new Insets(0,0,0,8));
    }

    public void setupAnimations(TextField inputField, Button submitButton) {
        inputAnimation = new PressAnimation(inputField, 0.96);
        buttonAnimation = new PressAnimation(submitButton, 0.9);

        submitButton.setOnMousePressed(event  -> buttonAnimation.play());
    }

    public void playInputAnimation() { if (inputAnimation != null) inputAnimation.play(); }

    public void playEntranceAnimations() {
        new SlideInAnimation(timerPill, TIMER_PILL_SLIDE_FROM, 1).play();
        new OvershootAnimation(wordDisplay, 1.16, 0.96).play();
        inputAnimation.play();
        buttonAnimation.play();
    }

    public void initTimer(int totalSeconds) { timerArc.init(totalSeconds); }

    public void renderWord(char[] chars, int level) {
        String activeCharStyle = level > 35 ? CSS_CHAR_SMALL : CSS_CHAR_NORMAL;
        AnchorPane.setTopAnchor(wordDisplay, level > 35 ? WORD_TOP_MARGIN_SMALL : WORD_TOP_MARGIN_NORMAL);

        charLabels = new Label[chars.length];
        wordDisplay.getChildren().clear();

        for (int i = 0; i < chars.length; i++) {
            Label charLabel = new Label(String.valueOf(chars[i]));
            charLabel.getStyleClass().addAll(activeCharStyle, CSS_CHAR_NEUTRAL);
            charLabel.setAlignment(Pos.CENTER);
            charLabels[i] = charLabel;
            wordDisplay.getChildren().add(charLabel);
        }
        new OvershootAnimation(wordDisplay, 1.16, 0.96).play();
    }

    public void colorizeChars(String typed, String target) {
        for (int i = 0; i < charLabels.length; i++) {
            String colorClass;
            if (i < typed.length()) {
                colorClass = (typed.charAt(i) == target.charAt(i)) ? CSS_CHAR_CORRECT : CSS_CHAR_WRONG;
            } else { colorClass = CSS_CHAR_NEUTRAL; }

            setCharColor(charLabels[i], colorClass);
        }
    }

    public void updateHUD(int level, int secondsLeft) {
        levelLabel.setText("#" + level);

        timerLabel.setText(String.valueOf(secondsLeft));

        boolean critical = secondsLeft <= CRITICAL_TIME_THRESHOLD;
        timerLabel.getStyleClass().removeAll(CSS_TIMER_NORMAL, CSS_TIMER_CRITICAL);
        timerLabel.getStyleClass().add(critical ? CSS_TIMER_CRITICAL : CSS_TIMER_NORMAL);

        if (critical) { new OvershootAnimation(timerLabel, 1.3, 0.94).play(); }

        timerArc.update(secondsLeft, critical);
        backgroundGradient.updateForLevel(level);
    }

    private void setCharColor(Label charLabel, String colorClass) {
        charLabel.getStyleClass().removeAll(CSS_CHAR_NEUTRAL, CSS_CHAR_CORRECT, CSS_CHAR_WRONG);
        charLabel.getStyleClass().add(colorClass);
    }
}
