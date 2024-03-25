package com.breaddoesstuff;

import java.util.ArrayList;

public class mainScannerApp 
{

    // Make public so every class can access the list
    public static ArrayList<memberID> memberList = new ArrayList<memberID>();

    public static void main( String[] args )
    {

        // Config Checker
        appProperties.checkConfig();
        
        // Settings bools that can be changed within a config file
        boolean generateMemberData = appProperties.generateMemberData;
        boolean debugMode = appProperties.debugMode;

        // Check if in member generating mode
        if (generateMemberData)
        {
            System.out.println("Entering member data generation mode...");
            memberData.genDataInput(); // Send to selector
            System.out.println("Exiting program...");
            System.exit(0); // End when complete
        }

        // Debug stuffs
        if (debugMode)
        {
            System.out.println("Entering debug mode...");
            memberTesterClass.camTest();
            memberTesterClass.test();
            System.out.println("Exiting program...");
            System.exit(0); // End when complete
        }

        // Clear list just to be safe
        memberList.clear();

        // Import member data into list
        // Input data will be from a .csv file with data fields for Secret and Email
        // CORE FUNCTIONALITY OF PROGRAM RELIES ON THIS
        memberData.pullData();

        // Print loaded members
        memberData.printListData(false, false);

        // Start the QR code reading process
        qrRead.read();

    }    
}
