package com.lulow.justtype.view;

import javafx.scene.paint.Color;

/**
 * Central color palette and hex-conversion utility for the application's visual theme.
 * All colors are defined as constants to ensure consistency across the UI.
 */
public final class Theme {

    /** Primary accent color (teal). */
    public static final Color COLOR_ACCENT  = Color.web("#07DACC");

    /** Danger/error color (red). */
    public static final Color COLOR_DANGER  = Color.web("#FF4C4C");

    /** Base background color (near black). */
    public static final Color COLOR_BASE    = Color.web("#0C0910");

    /** Pure white, used for char labels and effects. */
    public static final Color COLOR_WHITE   = Color.WHITE;

    /** Hex string of the base background color, used for inline CSS gradients. */
    public static final String HEX_BASE = "#0C0910";

    /** Background gradient bottom color for the EASY tier. */
    public static final Color GRADIENT_EASY   = Color.web("#130b1c");

    /** Background gradient bottom color for the MEDIUM tier. */
    public static final Color GRADIENT_MEDIUM = Color.web("#14081a");

    /** Background gradient bottom color for the HARD tier. */
    public static final Color GRADIENT_HARD   = Color.web("#1c0819");

    /** Background gradient bottom color for the EXPERT tier. */
    public static final Color GRADIENT_EXPERT = Color.web("#290b17");

    /** Color palette used for colorful confetti on the win screen. */
    public static final Color[] CONFETTI_PALETTE = {
            Color.web("#FF4C4C"), Color.web("#07DACC"), Color.web("#FFD700"),
            Color.web("#FF9900"), Color.web("#CC44FF"), Color.web("#44AAFF"),
            Color.web("#FF66AA"), Color.web("#AAFFAA")
    };

    /** Prevents instantiation. */
    private Theme() {}

    /**
     * Converts a JavaFX {@link Color} to an uppercase hex string (e.g. {@code "#FF4C4C"}).
     *
     * @param color the color to convert
     * @return a CSS-compatible hex color string
     */
    public static String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed()   * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue()  * 255)
        );
    }
}