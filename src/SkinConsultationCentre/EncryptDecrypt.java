package SkinConsultationCentre;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EncryptDecrypt {

    public void imgEncrypt(String imagePath, LocalDateTime dateTime, String pId) throws IOException {

        // Selecting an Image for operation

        try {

            String subfolder = "Encryption" + File.separator + "img";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");
            String fileName = dateTime.format(formatter) + pId + "-encryptedImg.jpg";
            File newFile = new File(subfolder, fileName);
            FileOutputStream outStream = new FileOutputStream(newFile);
            FileInputStream file = new FileInputStream(imagePath);
            byte[] k = "CooL2116NiTh5252".getBytes();
            SecretKeySpec key = new SecretKeySpec(k, "AES");
            Cipher enc = Cipher.getInstance("AES");
            enc.init(Cipher.ENCRYPT_MODE, key);
            CipherOutputStream cos = new CipherOutputStream(outStream, enc);
            byte[] buf = new byte[1024];
            int read;
            while ((read = file.read(buf)) != -1) {
                cos.write(buf, 0, read);
            }
            file.close();
            outStream.flush();
            cos.close();
            JOptionPane.showMessageDialog(null, "The notes and images are encrypted Successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void imgDecrypt(LocalDateTime localDateTime, String pId) throws IOException {

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");

            String subfolder = "Encryption" + File.separator + "img";
            String imgName = localDateTime.format(formatter) + pId + "-encryptedImg.jpg";
            File readFile = new File(subfolder, imgName);
            FileInputStream file = new FileInputStream(readFile);

            String fileName = localDateTime.format(formatter) + pId + "-DecryptedImg.jpg";
            File decFile = new File(subfolder, fileName);
            FileOutputStream outStream = new FileOutputStream(decFile);

            byte[] k = "CooL2116NiTh5252".getBytes();
            SecretKeySpec key = new SecretKeySpec(k, "AES");
            Cipher enc = Cipher.getInstance("AES");
            enc.init(Cipher.DECRYPT_MODE, key);
            CipherOutputStream cos = new CipherOutputStream(outStream, enc);
            byte[] buf = new byte[1024];
            int read;
            while ((read = file.read(buf)) != -1) {
                cos.write(buf, 0, read);
            }
            file.close();
            outStream.flush();
            cos.close();
            JOptionPane.showMessageDialog(null, "The image was decrypted successfully");

        } catch (HeadlessException | IOException | InvalidKeyException | NoSuchAlgorithmException |
                 NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtEncrypt(String text, LocalDateTime localDateTime, String pId) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        try {

            String subfolder = "Encryption" + File.separator + "txt";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");
            String fileName = localDateTime.format(formatter) + pId + "-encryptedText.txt";
            File file = new File(subfolder, fileName);
            FileOutputStream outStream = new FileOutputStream(file);
            byte[] key = "CooL2116NiTh5252".getBytes(StandardCharsets.UTF_8);
            SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
            CipherOutputStream cos = new CipherOutputStream(outStream, cipher);
            cos.write(text.getBytes(StandardCharsets.UTF_8));
            outStream.flush();
            JOptionPane.showMessageDialog(null, "The text was encrypted successfully");
            cos.close();
            //System.out.println("File created at: " + file.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public String txtDecrypt(LocalDateTime localDateTime, String pId) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");

            String subfolder = "Encryption" + File.separator + "txt";
            String fileName = localDateTime.format(formatter) + pId + "-encryptedText.txt";
            File file = new File(subfolder, fileName);
            byte[] key = "CooL2116NiTh5252".getBytes(StandardCharsets.UTF_8);
            SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec);

            FileInputStream inStream = new FileInputStream(file);
            CipherInputStream cis = new CipherInputStream(inStream, cipher);
            byte[] decrypted = cis.readAllBytes();
            inStream.close();
            cis.close();
            JOptionPane.showMessageDialog(null, "The text was decrypted successfully");
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (HeadlessException | IOException | InvalidKeyException | NoSuchAlgorithmException |
                 NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}