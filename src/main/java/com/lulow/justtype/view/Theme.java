package com.lulow.justtype.view;

import javafx.scene.paint.Color;

public final class Theme {

    public static final Color COLOR_ACCENT  = Color.web("#07DACC");
    public static final Color COLOR_DANGER  = Color.web("#FF4C4C");
    public static final Color COLOR_BASE    = Color.web("#0C0910");
    public static final Color COLOR_WHITE   = Color.WHITE;

    public static final String HEX_BASE = "#0C0910";

    public static final Color GRADIENT_EASY   = Color.web("#130b1c");
    public static final Color GRADIENT_MEDIUM = Color.web("#14081a");
    public static final Color GRADIENT_HARD   = Color.web("#1c0819");
    public static final Color GRADIENT_EXPERT = Color.web("#290b17");

    public static final Color[] CONFETTI_PALETTE = {
            Color.web("#FF4C4C"), Color.web("#07DACC"), Color.web("#FFD700"),
            Color.web("#FF9900"), Color.web("#CC44FF"), Color.web("#44AAFF"),
            Color.web("#FF66AA"), Color.web("#AAFFAA")
    };

    private Theme() {}

    public static String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed()   * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue()  * 255)
        );
    }
}