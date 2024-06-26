package com.breaddoesstuff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class appProperties {

    // Generate app.properties file
    // Read properties (true/false)
    // Change vars depending on value
 
    // Location of config file
    private static File configFile = new File("app.properties");

    // Default settings vars
    public static boolean generateMemberData = false;
    public static boolean soundMuted = false;
    public static boolean debugMode = false;
    public static boolean forceNativeDriver = false;
    public static int webcamSelectionIndex = 0;

    public appProperties()
    {

    }

    // Check the config and change bool values accordingly.
    public static void checkConfig()
    {
        // Check computer architecture/os
        checkArch();

        System.out.println("Loading config file...");

        // Check if properties file already exists.
        // If not make one
        if (!configFile.exists())
        {
            System.out.println("Config file not found.");
            System.out.println("Creating new config file...");
            genConfig();
        }

        // Read from the properties file
        // Create a Properties object to store the key-value pairs
        Properties properties = new Properties();

        try {
            // Load the properties file
            properties.load(new FileInputStream(configFile));

            // Access and assign properties
            String key1 = properties.getProperty("memberGenerationMode");
            String key2 = properties.getProperty("muteSound");
            String key3 = properties.getProperty("debug");
            String key4 = properties.getProperty("forceNativeWebcam");
            String key5 = properties.getProperty("webcamSelectionIndex");

            // Change program variables based on config
            generateMemberData = Boolean.parseBoolean(key1);
            soundMuted = Boolean.parseBoolean(key2);
            debugMode = Boolean.parseBoolean(key3);
            
            if (!key4.isBlank()) // Only parse when necessary
            {
                forceNativeDriver = Boolean.parseBoolean(key4);
            }

            if (!key5.isBlank()) // Only parse when necessary
            {
                webcamSelectionIndex = Integer.parseInt(key5);
            }
            
            
            System.out.println("Config loaded sucessfully!");

        } catch (IOException e) {
            System.err.println("Error loading config file: " + e.getMessage());
        }

    }

    // Generates a new config.
    private static void genConfig()
    {
        // Create/Copy new config
        try {
                // Create the config file
                FileWriter writer = new FileWriter(configFile);

                writer.write("# Change to true to enable member generation mode.\n");
                writer.write("# Requires memberInput.csv to be in the same directory as the program.\n");
                writer.write("memberGenerationMode=" + generateMemberData + "\n");
                writer.write("# Change to true to mute program sounds.\n");
                writer.write("muteAudio=" + soundMuted + "\n");
                writer.write("# Change to true to enable debug mode.\n");
                writer.write("debug=" + soundMuted + "\n");
                writer.write("# LEAVE THESE BLANK UNLESS YOU KNOW WHAT YOU'RE DOING!\n");
                writer.write("forceNativeWebcam=\n");
                writer.write("webcamSelectionIndex=\n");

                // Close the writer
                writer.close();

                System.out.println("Config file created: " + configFile);
            } catch (IOException e) {
                System.err.println("Error creating config file: " + e.getMessage());
            }
    }

    // Check computer OS and Arch
    private static void checkArch()
    {
        String osName = System.getProperty("os.name");

        // Force native driver if on Mac
        if (osName.startsWith("Mac")) 
        {
            System.out.println("Using Native Webcam Driver!");
            forceNativeDriver = true;
        }
        else
        {
            // "For the inferior Windows and Linux users" - Said every Apple Sheep ever
            System.out.println("Using Regular Webcam Driver!");
            forceNativeDriver = false;
        }
    }

}
