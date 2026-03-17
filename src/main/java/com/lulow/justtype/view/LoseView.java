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

/**
 * Sets up and animates the loose screen.
 * Injects the word display from the game scene, shows the player's answer
 * and level stats, and plays the loose sound effects after a short delay.
 */
public class LoseView {

    /** Delay in seconds before the lose animations play. */
    private static final double ANIMATION_DELAY_SECONDS = 1.6;

    /** The root pane of the loose scene. */
    private final AnchorPane rootPane;

    /** Label showing the loose screen title. */
    private final Label titleLabel;

    /** Label showing what the player typed. */
    private final Label answerLabel;

    /** Label showing the number of completed levels. */
    private final Label statsLabel;

    /** Label showing navigation hints. */
    private final Label hintLabel;

    /**
     * Creates the loose view with references to its labels and root pane.
     *
     * @param rootPane    the root AnchorPane of the lose scene
     * @param titleLabel  the title label
     * @param answerLabel the label showing the player's answer
     * @param statsLabel  the label showing level stats
     * @param hintLabel   the label showing keyboard hints
     */
    public LoseView(AnchorPane rootPane, Label titleLabel,
                    Label answerLabel, Label statsLabel, Label hintLabel) {
        this.rootPane    = rootPane;
        this.titleLabel  = titleLabel;
        this.answerLabel = answerLabel;
        this.statsLabel  = statsLabel;
        this.hintLabel   = hintLabel;
    }

    /**
     * Populates the loose screen with the game results and plays the animations.
     *
     * @param completedLevels the number of levels the player finished
     * @param playerAnswer    the text the player had typed when the game ended
     * @param wordDisplay     the word display HBox carried over from the game screen
     */
    public void setup(int completedLevels, String playerAnswer, HBox wordDisplay) {
        String answerText = playerAnswer == null || playerAnswer.isBlank()
                ? "You didn't type anything, are u ok?"
                : "Your answer: " + playerAnswer;
        answerLabel.setText(answerText);
        statsLabel.setText("Levels completed: " + completedLevels);

        injectWordDisplay(wordDisplay);
        playAnimations();
        AudioManager.getInstance().playSfx(AudioClips.LOSE_BOOM);
    }

    /**
     * Adds the word display to the root pane centered horizontally.
     *
     * @param wordDisplay the HBox to inject; does nothing if {@code null}
     */
    private void injectWordDisplay(HBox wordDisplay) {
        if (wordDisplay == null) return;
        wordDisplay.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(wordDisplay);
        AnchorPane.setLeftAnchor(wordDisplay,  0.0);
        AnchorPane.setRightAnchor(wordDisplay, 0.0);
    }

    /**
     * Hides all labels, then after a delay plays entrance animations and the whip sound.
     */
    private void playAnimations() {
        titleLabel.setOpacity(0);
        answerLabel.setOpacity(0);
        statsLabel.setOpacity(0);
        hintLabel.setOpacity(0);

        PauseTransition delay = new PauseTransition(Duration.seconds(ANIMATION_DELAY_SECONDS));
        delay.setOnFinished(e -> {
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