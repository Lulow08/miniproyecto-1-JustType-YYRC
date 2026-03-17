package com.lulow.justtype.view;

import com.lulow.justtype.model.AudioClips;
import com.lulow.justtype.model.AudioManager;
import com.lulow.justtype.view.animations.FadeInAnimation;
import com.lulow.justtype.view.animations.OvershootAnimation;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class LoseView {

    private static final double ANIMATION_DELAY_SECONDS = 1.6;

    private final AnchorPane rootPane;
    private final Label      titleLabel;
    private final Label      answerLabel;
    private final Label      statsLabel;
    private final Label      hintLabel;

    public LoseView(AnchorPane rootPane, Label titleLabel,
                    Label answerLabel, Label statsLabel, Label hintLabel) {
        this.rootPane    = rootPane;
        this.titleLabel  = titleLabel;
        this.answerLabel = answerLabel;
        this.statsLabel  = statsLabel;
        this.hintLabel   = hintLabel;
    }

    public void setup(int completedLevels, String playerAnswer, HBox wordDisplay) {
        String answerText = playerAnswer == null || playerAnswer.isBlank()
                ? "You didn't type nothing, are u ok?"
                : "Your answer: " + playerAnswer;
        answerLabel.setText(answerText);
        statsLabel.setText("Levels completed: " + completedLevels);

        injectWordDisplay(wordDisplay);
        playAnimations();
        AudioManager.getInstance().playSfx(AudioClips.LOSE_BOOM);
    }

    private void injectWordDisplay(HBox wordDisplay) {
        if (wordDisplay == null) return;
        wordDisplay.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(wordDisplay);
        AnchorPane.setLeftAnchor(wordDisplay,  0.0);
        AnchorPane.setRightAnchor(wordDisplay, 0.0);
    }

    private void playAnimations() {
        titleLabel.setOpacity(0);
        answerLabel.setOpacity(0);
        statsLabel.setOpacity(0);
        hintLabel.setOpacity(0);

        PauseTransition delay = new PauseTransition(Duration.seconds(ANIMATION_DELAY_SECONDS));
        delay.setOnFinished(event -> {
            titleLabel.setOpacity(1.0);
            new OvershootAnimation(titleLabel, 3.0, 0.88).play();
            new FadeInAnimation(answerLabel).play();
            new FadeInAnimation(statsLabel).play();
            new FadeInAnimation(hintLabel).play();

            AudioManager.getInstance().playSfx(AudioClips.LOSE_WHIP);
        });
        delay.play();
    }
}