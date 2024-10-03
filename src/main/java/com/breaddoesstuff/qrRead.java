package com.breaddoesstuff;

import java.awt.image.BufferedImage;

import com.github.eduramiba.webcamcapture.drivers.NativeDriver;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import com.github.sarxos.webcam.util.jh.JHGrayFilter;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class qrRead {


	public static void read() {

        final int IMAGE_CAPTURE_DELAY = 35; // in ms

        System.out.println("Getting ready to scan QR codes...");

        // Super scuffed but it works ig
        if (appProperties.forceNativeDriver)
        {
            // Set Webcam Driver (native)
            Webcam.setDriver(new NativeDriver());
        }
        else
        {
            // Set Webcam Driver (non-macarm)
            Webcam.setDriver(new WebcamDefaultDriver());
        }

        // Get list of available webcams
        Iterable<Webcam> webcams = Webcam.getWebcams();

        // Check if there are any webcams available
        if (!webcams.iterator().hasNext()) {
            System.out.println("No cameras found.");
            return;
        }

        // Print list of Camera Devices
        System.out.println("\n" + Webcam.getWebcams());

		// Set the Webcam itself
        Webcam webcam = Webcam.getWebcams().get(appProperties.webcamSelectionIndex);
        webcam.setViewSize(WebcamResolution.VGA.getSize());

		// Use the webcam
        System.out.println("Initializing Camera...");
		webcam.open();

		try {
			// Wait a bit before activating camera to make sure it is initialized
            System.out.println("Camera is active!");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        System.out.println("Awaiting QR Codes...");

        // Constantly capturing camera image and decoding any QR codes (if present)
        // Main Scan Loop

        boolean alreadyScanned = false;
        String previousScannedCode = "";

        while (true) {
            if (webcam.isOpen()) {
                try {

                    BufferedImage image = webcam.getImage();
                    BufferedImage grayImage = convertToGrayScale(image);

                    // Get QR code result from image
                    String scannedCode = decodeQRCode(grayImage);

                    // Make sure scanned QR Code is acceptable for the system
                    if (scannedCode.length() == 36) 
                    { 
                        // Case to check if QR Code has already been scanned
                        if (previousScannedCode.equals(scannedCode))
                        {
                            alreadyScanned = true;
                        }
                        else // Different code, reset scan bool
                        {
                            alreadyScanned = false;
                        }
                        
                        // Prevent "already present" spam
                        if (!alreadyScanned)
                        {
                            memberData.setPresent(scannedCode, true);
                            //System.out.println(result);
                        }

                        // Freeze current code in placeholder variable
                        previousScannedCode = scannedCode;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                // Delay between each iteration so the computer doesn't explode
                Thread.sleep(IMAGE_CAPTURE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static BufferedImage convertToGrayScale(BufferedImage image) {
        JHGrayFilter filter = new JHGrayFilter();
        return filter.filter(image, null);
    }

    private static String decodeQRCode(BufferedImage image) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();
        Result decoded;
		
		// Try-catchificationathon
		try {
			decoded = reader.decode(bitmap);
			return decoded.getText();
		} catch (NotFoundException e) {
			//e.printStackTrace();
			//System.out.println("Checking...");
		} catch (ChecksumException e) {
			System.out.println("Uhhh what..?");
			//e.printStackTrace();
		} catch (FormatException e) {
			System.out.println("Funny looking QR code right there...");
			//e.printStackTrace();
		}

        // Return this when no codes are scanned
        return "";
    }

}
