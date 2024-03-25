package com.breaddoesstuff;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.github.eduramiba.webcamcapture.drivers.NativeDriver;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.util.jh.JHGrayFilter;

public class memberTesterClass {

    public memberTesterClass()
    {

    }

    // Test the camera functionality
    public static void camTest()
    {
        
        // Set Webcam Driver (native)
		Webcam.setDriver(new NativeDriver());
        // Set Webcam Driver (broken)
		//Webcam.setDriver(new WebcamDefaultDriver()());

        // Get list of available webcams
        Iterable<Webcam> webcams = Webcam.getWebcams();

        // Check if there are any webcams available
        if (!webcams.iterator().hasNext()) {
            System.out.println("No cameras found.");
            return;
        }
        
        // Print list of Camera Devices
        System.out.println("\n" + Webcam.getWebcams());

        // Get default webcam
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        // Open webcam, wait, then capture image
        System.out.println("Initializing Camera...");
        webcam.open();
        
        try {
            Thread.sleep(1000 );
            System.out.println("Camera is active!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            ImageIO.write(webcam.getImage(), "PNG", new File("color.png"));
            ImageIO.write(convertToGrayScale(webcam.getImage()), "PNG", new File("bw.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static BufferedImage convertToGrayScale(BufferedImage image) {
        JHGrayFilter filter = new JHGrayFilter();
        return filter.filter(image, null);
    }

    // Tester debug menu
    public static void test()
    {
        // Create Scanner
        Scanner sc = new Scanner(System.in);

        boolean loop = true;

        System.out.println("\n========================================================");
        System.out.println("Welcome to the super duper awesome tester!!!");
        System.out.println("========================================================\n");

        System.out.println("Enter a command");

        while(loop)
        {
            boolean validCommandEntered = false;

            String input = sc.nextLine();
            if (input.equals("print"))
            {
                memberData.printListData(true, true);
                validCommandEntered = true;
            }

            if (input.equals("search"))
            {
                System.out.println("Input term to search:");
                String searchTerm = sc.nextLine();
                memberData.searchMember(searchTerm);
                validCommandEntered = true;
            }

            if (input.equals("attendance"))
            {
                System.out.println("Input secret for attendance:");
                String searchTerm = sc.nextLine();
                memberData.setPresent(searchTerm, false);
                validCommandEntered = true;
            }

            if (input.equals("load"))
            {
                System.out.println("Loading data from memberDataList...");
                memberData.pullData();
                validCommandEntered = true;
            }

            if (input.equals("exit"))
            {
                System.out.println("Exiting Tester!");
                loop = false;
                validCommandEntered = true;
            }

            if (input.equals("help"))
            {
                System.out.println("\nprint - print out the memberList");
                System.out.println("search - search for a member using their email or secret");
                System.out.println("attendance - mark a member present using their secret");
                System.out.println("load - Load data from memberDataList into memberList (if it exists)");
                System.out.println("exit - exits the tester");

                validCommandEntered = true;
            }

            if (!validCommandEntered)
            {
                System.out.println("Invalid Command!");
            }
        }
    }

}
