package com.breaddoesstuff;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class qrGen {

    public qrGen()
    {
        
    }

    public static void create(String qrCode, String email) {
        // Set QR Code resolution
        int size = 512;
        String filePath = "generated/" + email + ".png";
        String label = memberData.clearDomain(email);

        try {
            createHandler(qrCode, label, size, filePath);
            System.out.println("QR Code generated successfully!");
        } catch (WriterException | IOException e) {
            System.out.println("Could not generate QR Code: " + e.getMessage());
        }
    }

    private static void createHandler(String qrText, String textBelow, int size, String filePath) throws WriterException, IOException {

        // Create the ByteMatrix for the QR-Code that encodes the given String
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrText, BarcodeFormat.QR_CODE, size, size, hints);

        int sizeFactor = 25;

        // Make the BufferedImage that are to hold the QRCode
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Create a new BufferedImage to hold the QR code image with text below
        BufferedImage combined = new BufferedImage(size, size + sizeFactor, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();

        // Create the background fill
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size + sizeFactor);

        // Draw the QR code image onto the combined image
        g.drawImage(qrImage, 0, 0, null);

        int fontSize = size/28;

        // Create Font Object
        Font font = new Font("Arial", Font.PLAIN, fontSize);

        // Create Font metrics
        FontMetrics metrics = g.getFontMetrics(font);

        // Determine the X coordinate for the text
        int textx = (size - metrics.stringWidth(textBelow)) / 2;
        int texty = size - size/24;

        // Graphics stuff
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Draw text below the QR code image
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString(textBelow, textx, texty);

        File imgFile = new File(filePath);
        if (!imgFile.exists())
        {
            imgFile.mkdirs();
        }

        // Save the combined image with text as PNG
        ImageIO.write(combined, "PNG", imgFile);

        // Cleanup
        g.dispose();
    }

}
