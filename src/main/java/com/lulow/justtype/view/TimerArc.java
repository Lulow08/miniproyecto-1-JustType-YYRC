package com.lulow.justtype.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;

/**
 * Renders a circular arc indicator that shrinks as the countdown timer decreases.
 * Switches from the accent color to the danger color when time is critical.
 */
public class TimerArc {

    /** Arc stroke color under normal conditions. */
    private static final Color  COLOR_NORMAL   = Theme.COLOR_ACCENT;

    /** Arc stroke color when time is critical. */
    private static final Color  COLOR_CRITICAL = Theme.COLOR_DANGER;

    /** Stroke width of the arc in pixels. */
    private static final double STROKE_WIDTH   = 2.5;

    /** Radius of the arc circle in pixels. */
    private static final double RADIUS         = 8.0;

    /** Angle length representing a full circle. */
    private static final double FULL_CIRCLE    = 360.0;

    /** The JavaFX arc shape that visually represents the timer. */
    private final Arc   arc;

    /** Container group holding the arc and an invisible bounds circle. */
    private final Group container;

    /** The total seconds the arc was initialized with, used to calculate progress. */
    private int totalSeconds;

    /**
     * Creates and configures the timer arc and its container group.
     */
    public TimerArc() {
        arc = new Arc();
        arc.setRadiusX(RADIUS);
        arc.setRadiusY(RADIUS);
        arc.setStartAngle(90);
        arc.setType(ArcType.OPEN);
        arc.setFill(Color.TRANSPARENT);
        arc.setStroke(COLOR_NORMAL);
        arc.setStrokeWidth(STROKE_WIDTH);

        Circle fixedBounds = new Circle(RADIUS + STROKE_WIDTH);
        fixedBounds.setFill(Color.TRANSPARENT);
        fixedBounds.setMouseTransparent(true);

        container = new Group(fixedBounds, arc);
    }

    /**
     * Returns the container group to be added to the scene graph.
     *
     * @return the group containing the arc
     */
    public Group getContainer() { return container; }

    /**
     * Initializes the arc for a new level, resetting it to a full circle.
     *
     * @param seconds the total time for this level
     */
    public void init(int seconds) {
        totalSeconds = seconds;
        arc.setLength(FULL_CIRCLE);
        arc.setStroke(COLOR_NORMAL);
        arc.setStrokeWidth(STROKE_WIDTH);
    }

    /**
     * Updates the arc length and color based on the remaining time.
     *
     * @param secondsLeft the current number of seconds remaining
     * @param critical    whether the timer is in its critical phase
     */
    public void update(int secondsLeft, boolean critical) {
        double progress = totalSeconds > 0 ? (double) secondsLeft / totalSeconds : 0;
        arc.setLength(FULL_CIRCLE * progress);
        arc.setStroke(critical ? COLOR_CRITICAL : COLOR_NORMAL);
    }
}