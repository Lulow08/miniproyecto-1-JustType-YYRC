package com.lulow.justtype.model;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton manager for playing background music and sound effects.
 * Music is handled via {@link MediaPlayer}; short SFX via {@link AudioClip}.
 */
public final class AudioManager {

    /** Single instance of this manager. */
    private static AudioManager instance;

    /** Player for background music tracks. */
    private MediaPlayer musicPlayer;

    /** Cache of loaded sound effect clips. */
    private final Map<String, AudioClip> clips = new HashMap<>();

    /** Private constructor to enforce singleton. */
    private AudioManager() {}

    /**
     * Returns the singleton instance, creating it if necessary.
     *
     * @return the shared {@code AudioManager} instance
     */
    public static AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    /**
     * Plays a music track, stopping any currently playing track first.
     *
     * @param filename the file name under {@code /sfx/}
     * @param loop     whether the track should loop indefinitely
     */
    public void playMusic(String filename, boolean loop) {
        stopMusic();

        URL resource = getClass().getResource("/sfx/" + filename);
        if (resource == null) return;

        Media media = new Media(resource.toExternalForm());
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
        musicPlayer.play();
    }

    /**
     * Plays a short sound effect. Clips are cached after the first load.
     *
     * @param filename the file name under {@code /sfx/}
     */
    public void playSfx(String filename) {
        AudioClip clip = clips.get(filename);

        if (clip == null) {
            URL resource = getClass().getResource("/sfx/" + filename);
            if (resource == null) return;
            clip = new AudioClip(resource.toExternalForm());
            clips.put(filename, clip);
        }

        clip.play();
    }

    /**
     * Stops and disposes the current music player, if any.
     */
    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }

    /**
     * Stops all currently playing sound effects.
     */
    public void stopAllSfx() {
        clips.values().forEach(AudioClip::stop);
    }

    /**
     * Stops all audio: music and sound effects.
     */
    public void stopAll() {
        stopMusic();
        stopAllSfx();
    }
}