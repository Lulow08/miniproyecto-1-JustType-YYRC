package com.lulow.justtype.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class TimerArc {

    private static final Color  COLOR_NORMAL   = Theme.COLOR_ACCENT;
    private static final Color  COLOR_CRITICAL = Theme.COLOR_DANGER;
    private static final double STROKE_WIDTH   = 2.5;
    private static final double RADIUS         = 8.0;
    private static final double FULL_CIRCLE    = 360.0;

    private final Arc arc;
    private int totalSeconds;

    public TimerArc() {
        arc = new Arc();
        arc.setRadiusX(RADIUS);
        arc.setRadiusY(RADIUS);
        arc.setStartAngle(90);
        arc.setType(ArcType.OPEN);
        arc.setFill(Color.TRANSPARENT);
        arc.setStroke(COLOR_NORMAL);
        arc.setStrokeWidth(STROKE_WIDTH);
    }

    public Arc getArc() { return arc; }

    public void init(int seconds) {
        totalSeconds = seconds;
        arc.setLength(FULL_CIRCLE);
        arc.setStroke(COLOR_NORMAL);
        arc.setStrokeWidth(STROKE_WIDTH);
    }

    public void update(int secondsLeft, boolean critical) {
        double progress = totalSeconds > 0 ? (double) secondsLeft / totalSeconds : 0;
        arc.setLength(FULL_CIRCLE * progress);
        arc.setStroke(critical ? COLOR_CRITICAL : COLOR_NORMAL);
    }
}