package com.breaddoesstuff;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class audioMan {

    public audioMan()
    {
        
    }

    // Plays the inputted sound as long as the sound isn't muted
    public static void playSound(InputStream soundFileResource)
    {
        boolean soundMuted = appProperties.soundMuted;

        if (!soundMuted)
        {
            try {
                // Buffer sound
                InputStream bufferedSoundFileResource = new BufferedInputStream(soundFileResource);
                // Grab buffered sound
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedSoundFileResource);
                Clip clip = AudioSystem.getClip();
                
                // Play the funny sound :)
                clip.open(audioIn);
                clip.start();
    
                // Stop the funny sound :(
                if (clip.isRunning())
                {
                    clip.stop();
                    clip.close();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void correctNotif()
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream sound = classLoader.getResourceAsStream("audio/ta-da.wav");
        playSound(sound);
    }

    public static void incorrectNotif()
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream sound = classLoader.getResourceAsStream("audio/error.wav");
        playSound(sound);
    }

    public static void writeSuccess()
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream sound = classLoader.getResourceAsStream("audio/scribble.wav");
        playSound(sound);
    }
}
