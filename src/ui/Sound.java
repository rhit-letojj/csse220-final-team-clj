package ui;

import java.net.URL;
import javax.sound.sampled.*;

public class Sound {
    private Clip clip;
    private static boolean muted = false;
    private float lastDb = 0f;

    public Sound(String resourceName) {
        try {
            URL url = Sound.class.getResource(resourceName);
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            throw new RuntimeException("Sound load failed: " + resourceName, e);
        }
    }

    public void play() {
        if (clip == null) return;
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        applyVolume();
        clip.start();
    }

    public void loop() {
        if (clip == null) return;
        clip.setFramePosition(0);
        applyVolume();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }


    public void stop() {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
    }

    public void setVolume(float db) {
        lastDb = db;
        applyVolume();
    }

    private void applyVolume() {
        if (clip == null) return;
        FloatControl c = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        c.setValue(muted ? -80f : lastDb);
    }
    
    public static void setMuted(boolean v) {
        muted = v;
    }

    public static boolean isMuted() {
        return muted;
    }

}
