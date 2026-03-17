package com.lulow.justtype.controller;

import com.lulow.justtype.model.AudioClips;
import com.lulow.justtype.model.AudioManager;
import com.lulow.justtype.view.MenuView;
import com.lulow.justtype.view.MenuView.MenuSelection;
import com.lulow.justtype.view.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the main menu screen.
 * Manages keyboard and mouse navigation between "New Game" and "Exit",
 * and delegates visual state changes to {@link MenuView}.
 */
public class MenuController {

    private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

    @FXML private AnchorPane rootPane;
    @FXML private HBox       titleDisplay;
    @FXML private Label      newGameLabel;
    @FXML private Label      exitLabel;
    @FXML private Label      newGameIndicator;
    @FXML private Label      exitIndicator;

    /** The view that manages visual state of menu items and animations. */
    private MenuView menuView;

    /** The currently highlighted menu option. */
    private MenuSelection currentSelection = MenuSelection.NEW_GAME;

    /**
     * Initializes the controller after FXML injection.
     * Sets up the menu view, selection state, mouse events, music, and keyboard handler.
     */
    @FXML
    public void initialize() {
        menuView = new MenuView(rootPane, titleDisplay,
                newGameLabel, exitLabel, newGameIndicator, exitIndicator);

        menuView.setSelection(currentSelection);
        menuView.start();
        setupMouseEvents();

        AudioManager.getInstance().playMusic(AudioClips.MENU_MUSIC, true);

        rootPane.setOnKeyPressed(event -> handleKey(event.getCode()));
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) rootPane.requestFocus();
        });
    }

    /**
     * Wires mouse click, enter, and exit handlers to the menu item labels.
     * Clicking a label selects and immediately confirms it.
     */
    private void setupMouseEvents() {
        newGameLabel.setOnMouseClicked(event -> {
            currentSelection = MenuSelection.NEW_GAME;
            menuView.setSelection(currentSelection);
            confirm();
        });
        newGameLabel.setOnMouseEntered(event -> {
            if (currentSelection != MenuSelection.NEW_GAME) newGameLabel.setOpacity(0.7);
        });
        newGameLabel.setOnMouseExited(event -> newGameLabel.setOpacity(1.0));

        exitLabel.setOnMouseClicked(event -> {
            currentSelection = MenuSelection.EXIT;
            menuView.setSelection(currentSelection);
            confirm();
        });
        exitLabel.setOnMouseEntered(event -> {
            if (currentSelection != MenuSelection.EXIT) exitLabel.setOpacity(0.7);
        });
        exitLabel.setOnMouseExited(event -> exitLabel.setOpacity(1.0));
    }

    /**
     * Handles keyboard input for menu navigation.
     * UP/DOWN toggles the selection; ENTER confirms it.
     *
     * @param code the key code of the pressed key
     */
    private void handleKey(KeyCode code) {
        switch (code) {
            case UP, DOWN -> toggleSelection();
            case ENTER    -> confirm();
        }
    }

    /**
     * Switches the current selection between NEW_GAME and EXIT
     * and updates the view accordingly.
     */
    private void toggleSelection() {
        currentSelection = (currentSelection == MenuSelection.NEW_GAME)
                ? MenuSelection.EXIT
                : MenuSelection.NEW_GAME;
        menuView.setSelection(currentSelection);
    }

    /**
     * Confirms the current selection. Stops the beat animation,
     * then either starts the game or exits the application.
     */
    private void confirm() {
        menuView.stop();
        if (currentSelection == MenuSelection.NEW_GAME) {
            goToGame();
        } else {
            Platform.exit();
        }
    }

    /**
     * Navigates to the game screen.
     */
    private void goToGame() {
        try {
            SceneManager.getInstance().switchScene("game-view.fxml");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load game screen", exception);
        }
    }
}