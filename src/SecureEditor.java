
/**
 * Secure Editor:
 *   This is a text editor with encryption and decryption
 *   When a file is saved, it will be encrypted using RSA
 *   When a file is loaded, it will be decrypted using RSA
 *
 * @author baoj3101
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class SecureEditor extends BaseEditor implements Encrypt {
    // RSA encryption/decryption variables

    // constructor
    public SecureEditor() {
        super();
        setTitle("New File");
    }

    public String getDocument() {
        return textPane.getText();
    }

    // Open file and load into text pane
    public void OpenFile() {
        StringBuilder lines = new StringBuilder();
        try ( BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.append(line).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // To be implemented: decryption
        
        textPane.setText(lines.toString());
        setTitle(file.getName());
    }

    // Save content from text pane to file
    public void SaveFile() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            // To be implemented: encryption
            fileWriter.write(getDocument());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // abstract methods
    public void setTitle(String str) {
        frame.setTitle("Secure Editor: " + str);
    }

    // main method for testing
    public static void main(String[] args) {
        BaseEditor e = new SecureEditor();
        e.show();
    }
    
    //==========================================================================
    // RSA Encryption Methods
    //
    // RSA Algorithm
    // 1. Choose two very large random prime numbers p and q
    // 2. Compute modulus n = pq
    // 3. Compute phi = (p-1)(q-1)
    // 4. Choose an integer e (public key) such that 1 < e < phi and GCD(e, totient) = 1 (greatest common denomintor)
    // 5. Choose an integer d (private key) such that d * e = 1 + k (phi).
    // 6. Encryption: c = m^e mod n where m is the data to be encrypted
    // 7. Decryption: m = c^d mod n where c is the encrypted data
    //==========================================================================
    // Methods from java.security.SecureRandom are used here
    
    // Given byte array as input and return encrypted byte array
    public byte[] encrypt (byte[] data) {
        return null;
    }
    // Given byte array as input and return decrypted byte array
    public byte[] decrypt (byte[] data) {
        return null;
    }
}
