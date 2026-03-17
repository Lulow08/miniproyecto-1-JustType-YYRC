package com.lulow.justtype.model;

/**
 * Constants for all audio file names used in the game.
 * All files are expected to be located under the {@code /sfx/} resource folder.
 */
public final class AudioClips {

    /** Background music for the main menu. */
    public static final String MENU_MUSIC    = "menu-music.mp3";

    /** Background music during gameplay. */
    public static final String GAME_MUSIC    = "game-music.mp3";

    /** Music played on the win screen. */
    public static final String WIN_MUSIC     = "win-music.mp3";

    /** Sound effect for a correct answer. */
    public static final String CRUNCH        = "crunch.mp3";

    /** Sound effect for each key press in the input field. */
    public static final String KEY_PRESS     = "key-press.mp3";

    /** Beep played when the timer reaches critical seconds. */
    public static final String CRITICAL_BEEP = "critical-beep.mp3";

    /** Boom sound on the lose screen. */
    public static final String LOSE_BOOM     = "lose-boom.mp3";

    /** Whip sound on the lose screen. */
    public static final String LOSE_WHIP     = "lose-whip.mp3";

    /** Sound effect for a wrong answer. */
    public static final String ERROR         = "error.mp3";

    /** Pop sound when confetti fires. */
    public static final String CONFETTI_POP  = "confetti-pop.mp3";

    /** Prevents instantiation. */
    private AudioClips() {}
}