# Portable Attendance System

## What is this program and how does it work?
This program is a simple sign-in system that involes using QR Codes that contain a UUID that corresponds to an email address (or any other data) in a local database.

I made this to be able to easily track the attendance of a user by outputting the time and user's email into a .csv file to be easily imported into Excel or Google Sheets.

## Instructions
1. Fill a .csv list with user emails. Name the file `memberInput.csv` and place it in the same directory that the program is running in. Make sure duplicate entries are removed and cleaned up.

2. Run the jar inside of a Terminal window to generate an `app.properties` config file. Enter member generation mode by editing the value of `memberGenerationMode` to true.

3. Run the program jar again to enter memberDataGenerationMode. Enter the option to generate memberData from the `memberInput.csv`.

4. A `generated` folder and `memberDataList.csv` should have been generated in the same directory as the program. The `generated` folder contains the QR Codes associated with the UUID of each generated member while `memberDataList.csv` contains the UUIDs associated to each member email.

5. Run the program again setting `memberGenerationMode` to false and the camera should initate. Simply scan one of the generated QR codes and a new `Attendance.csv` file should be created with the local date included in the name.

## Known Issues
* The program does not handle checking for valid emails & duplicate entries. Make sure to sanatize your input file beforehand.

## To-Do
* Sanatize input data.
* Read from attendance file after program restarts to prevent duplicate entries.
* Implement a GUI with a camera buffer preview, options panel, etc.
* Manual attendance input field while main program is running.