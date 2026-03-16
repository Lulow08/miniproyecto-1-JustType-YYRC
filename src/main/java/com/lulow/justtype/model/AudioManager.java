package com.lulow.justtype.model;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class AudioManager {

    private static AudioManager instance;

    private MediaPlayer musicPlayer;
    private final Map<String, AudioClip> clips = new HashMap<>();

    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    public void playMusic(String filename, boolean loop) {

        stopMusic();

        URL resource = getClass().getResource("/sfx/" + filename);
        if (resource == null) return;

        Media media = new Media(resource.toExternalForm());
        musicPlayer = new MediaPlayer(media);

        musicPlayer.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
        musicPlayer.play();
    }

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

    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }

    public void stopAllSfx() {
        clips.values().forEach(AudioClip::stop);
    }

    public void stopAll() {
        stopMusic();
        stopAllSfx();
    }
}