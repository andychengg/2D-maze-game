package dev.project276.display;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Class for audio-related actions.
 */
public class AudioPlayer {
    Clip clip;
    public void setFile(String soundFileName) {
        try {
            // obtain the sound file from the project resource folder as AudioInputStream and open it
            AudioInputStream sound = AudioSystem.getAudioInputStream(AudioPlayer.class.getResource(soundFileName));
            clip = AudioSystem.getClip();
            clip.open(sound);

            // Set the audio volume
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-6.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // loop to loop the music continuously
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // to play the music
    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    // to stop the music
    public void stop() {
        clip.stop();
        clip.close();
    }
}


