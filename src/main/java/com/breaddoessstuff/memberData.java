package com.breaddoessstuff;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import java.util.Date;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class memberData {

    static LocalDate currentDate1 = LocalDate.now(); // Simple date
    static Date currentDate2 = new Date(); // Complex date w/ time

    // Paths to the CSV files
    static String csvInputPath = "memberInput.csv";
    static String csvOutputPath = "memberDataList.csv";
    static String attendanceDataOutputPath = "Attendance - " + currentDate1 + " .csv";

    public memberData()
    {
        
    }

    // Let the user choose if they want to add a new member or mass generate from csv
    public static void genDataInput()
    {
        Scanner sc = new Scanner(System.in);

        int selectedChoice;
        boolean choosing = true;
        String email;

        System.out.println("Enter one of the following options:");
        System.out.println("1): Mass Generate New Members from memberInput.csv");
        System.out.println("2): Add New Members Individually");
        System.out.println("3): Regenerate QR Codes");


        while (choosing == true)
        {
            selectedChoice = sc.nextInt();

            switch(selectedChoice)
            {
                case 1:
                genData();
                choosing = false;
                break;
    
                case 2:
                while (true)
                {
                    System.out.println("Enter the email of the member you would like to add");
                    email = sc.next();
                    genDataPartial(email);
                }
                case 3:
                ArrayList<memberID> a = mainScannerApp.memberList;
                // Load member data from csv into memberList
                pullData();

                System.out.println("Regenerating member QR Codes...");

                // Generates QR codes for every loaded member
                for (int i=0; i < a.size(); i++)
                {
                    qrGen.create(a.get(i).getSecret(), clearFunnies(a.get(i).getEmail()));
                }
                System.out.println("All Done!");
                choosing = false;
                break;
               

                default:
                System.err.println("Invalid option!");
                break;
            }
        }
        

    }

    // Method to append new member to existing memberDataList.csv
    public static void genDataPartial(String email)
    {
        UUID uuid = new UUID(0, 0);

        try {
            // Check if the file already exists
            if (!Files.exists(Paths.get(csvOutputPath)))
            {
                System.out.println("Cannot append to memberDataList.csv if it doesn't exist!");
                return;
            }

            // Create a FileWriter object
            FileWriter fileWriter = new FileWriter(csvOutputPath, true);

            // Create a CSVPrinter object
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

            String tempID = uuid.randomUUID().toString();

            System.out.println("Attemping to append new member...");

            csvPrinter.printRecord(tempID, clearFunnies(email));

            // Flush and close the CSVPrinter
            csvPrinter.flush();
            csvPrinter.close();

            System.out.println("Appended new member sucessfully!");
            System.out.println("Generating QR Code...");

            qrGen.create(tempID, clearFunnies(email));

            System.out.println("All Done!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
       

    }


    // Load member csv list
    // Generate random uuids
    // Generate qr codes for every random value
    // Assign member emails to random values
    // Save secret and member emails as csv
    // Save QR Codes with email as filename
    public static void genData()
    {

        ArrayList<memberID> a = mainScannerApp.memberList;

        UUID uuid = new UUID(0, 0);

        try {
            // Check if input csv file exists before reading
            if (!Files.exists(Paths.get(csvInputPath))) 
           {
                System.out.println("memberInput.csv not found! Ensure it is in the same directory as the jar file.");
                System.exit(0);;
           }

            // Create a reader for the CSV file
            Reader reader = new FileReader(csvInputPath);

            // Parse the CSV file using Commons CSV
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            // Loop through each record in the CSV file
            for (CSVRecord csvRecord : csvParser) {

                // Access the first column of each record
                String emailValues = csvRecord.get(0);

                // Add column data to memberList
                a.add(new memberID(uuid.randomUUID().toString(), emailValues, false));

            }

            // Close the CSV parser
            csvParser.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        // Move on to outputting member data to file from arraylist

         try {
            // Check if the file already exists
           if (Files.exists(Paths.get(csvOutputPath))) 
           {
                System.out.println("Cannot create new memberDataList! File already exists!");
                return;
            }

            // Create a FileWriter object
            FileWriter fileWriter = new FileWriter(csvOutputPath);

            // Create a CSVPrinter object
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

            System.out.println("Writing memberList to memberDataList...");

            // Write each element of the ArrayList as a separate record
            for (int i=0; i < a.size(); i++)
            {
                csvPrinter.printRecord(a.get(i).getSecret(), clearFunnies(a.get(i).getEmail()));
            }

            // Flush and close the CSVPrinter
            csvPrinter.flush();
            csvPrinter.close();

            System.out.println("Member data written to CSV file successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Generating member QR Codes...");

        // Generates QR codes for every member
        for (int i=0; i < a.size(); i++)
        {
            qrGen.create(a.get(i).getSecret(), clearFunnies(a.get(i).getEmail()));
        }

        System.out.println("Member data generation completed sucessfully!");

    }

    // Pull member data from generated csv into memberList
    public static void pullData()
    {

        ArrayList<memberID> a = mainScannerApp.memberList;

        try {
            // Check if memberDataList file exists before reading
            if (!Files.exists(Paths.get(csvOutputPath))) 
           {
                System.out.println("memberDataList.csv not found! Ensure it is in the same directory as the jar file.");
                System.exit(0);;
           }

            // Create a reader for the CSV file
            Reader reader = new FileReader(csvOutputPath);

            // Parse the CSV file using Commons CSV
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            // Loop through each record in the CSV file
            for (CSVRecord csvRecord : csvParser) {
                // Access the first column of each record
                String secretValues = csvRecord.get(0);
                String emailValues = csvRecord.get(1);

                a.add(new memberID(secretValues, emailValues, false));


            }

            // Close the CSV parser
            csvParser.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Data extraction successful!");

    }

    // Takes email string and current date and appends to attendance csv file
    private static void outputPresence(String email)
    {

        try {

            // Create a FileWriter object
            FileWriter fileWriter = new FileWriter(attendanceDataOutputPath, true);

            // Create a CSVPrinter object
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

            currentDate2.setTime(System.currentTimeMillis());
            csvPrinter.printRecord(currentDate2, email);

            // Flush and close the CSVPrinter
            csvPrinter.flush();
            csvPrinter.close();

            audioMan.writeSuccess(); // Play notif
            System.out.println("Data written to CSV file successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Searches for secret in memberList
    // Sets member's presence true if secret exists
    public static void setPresent(String secret, boolean scanMode)
    {
        ArrayList<memberID> a = mainScannerApp.memberList;

        boolean memberFound = false;

        // Search list for member
        for (int i=0; i < a.size(); i++)
        {
            if (a.get(i).getSecret().equals(secret))
            {
                memberFound = true;
                String userEmail = a.get(i).getEmail();
                if (a.get(i).getPresence())
                {
                    System.out.printf("%s is already present!\n", clearDomain(userEmail));
                }
                else if (!a.get(i).getPresence())
                {
                    // Set attandance true
                    audioMan.correctNotif(); // Play notif
                    System.out.printf("%s has been marked present!\n", clearDomain(userEmail));
                    a.get(i).setPresence(true);
                    outputPresence(a.get(i).getEmail());
                }
            }
        }

        if (memberFound == false && !scanMode)
        {
            System.out.println("Member does not exist!");
            audioMan.incorrectNotif(); // Play notif
        }

    }

    // Searches for secret in memberList
    // Prints member information associated to secret
    public static void searchMember(String secret)
    {

        ArrayList<memberID> a = mainScannerApp.memberList;

        boolean memberFound = false;

        // Search list for member
        for (int i=0; i < a.size(); i++)
        {
            if (a.get(i).getSecret().equals(secret) || a.get(i).getEmail().equals(secret))
            {
                System.out.printf("Email: %s | Present?: %b\n", 
                a.get(i).getEmail(), a.get(i).getPresence());
                memberFound = true;
                audioMan.correctNotif(); // Play notif
            }
        }

        if (memberFound == false)
        {
            System.out.println("Member does not exist!");
            audioMan.incorrectNotif(); // Play notif
        }

    }

    // Prints current ArrayList data
    public static void printListData(boolean showSecret, boolean showData)
    {
        ArrayList<memberID> a = mainScannerApp.memberList;
        
        int memberCount = a.size();

        if (showSecret && showData)
        {
            for (int i=0; i < a.size(); i++)
            {
                System.out.printf("Secret: %s | Email: %s | Present?: %b\n", 
                a.get(i).getSecret(), a.get(i).getEmail(), a.get(i).getPresence());
            }
        }
        else if (showData)
        {
            for (int i=0; i < a.size(); i++)
            {
                System.out.printf("Email: %s | Present?: %b\n", 
                a.get(i).getEmail(), a.get(i).getPresence());
            }
        }
        System.out.printf("%d Total Members.\n", memberCount);
    }

    // Will get rid of the email domain
    public static String clearDomain(String email)
    {
        // Make sure that the inputted email is actually an email
        if (!email.contains("@"))
        {
            return email;
        }

        // Find the index of '@'
        int atIndex = email.indexOf('@');
        
        // Extract the substring before '@'
        String username = email.substring(0, atIndex);

        return username;
    }

    // I hate U+FEFF
    public static String clearFunnies(String funny)
    {
		String noFunny = funny.replaceAll("[\uFEFF-\uFFFF]", ""); 

        return noFunny.trim();
    }
    

}
