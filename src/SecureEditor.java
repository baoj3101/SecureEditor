
/**
 * Secure Editor:
 *   This is a text editor with encryption and decryption
 *   When a file is saved, it will be encrypted
 *   When a file is loaded, it will be decrypted
 *
 * @author baoj3101
 */
import java.util.Base64;
import java.util.Random;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SecureEditor extends BaseEditor implements Encrypt {

    // encryption variables

    // By default, AES supports a 128 Bit key. 
    // To use longer key size, need higher version of JDK > JDK8
    private static final int keySize = 128;                // 128-bit AES
    private static final String key = "x,.@$klk;a,cjk09";  // 16 char key for AES

    //    private static final int keySize = 256;          // 256-bit AES
    //    private static final String key = "x,.@$klk;a,cjk09{}-==oiurcsgq!*&"; // 32 char key for AES
    
    // tool menu
    JMenuItem showD, showE;

    // buffer to store editor content
    private String contents;

    // constructor
    public SecureEditor() {
        super();
        setTitle("New File");
        Security.setProperty("crypto.policy", "unlimited");
        
        // add tool menu: to show encrypted or decrypted content
        JMenu toolMenu = new JMenu("View");
        showE = new JMenuItem("Show Encrypted");     // "Show Encrypted"
        showE.setEnabled(true);
        showE.addActionListener(new ShowEListener());
        showD = new JMenuItem("Show Decrypted");     // "Show Decrypted"
        showD.setEnabled(false);
        showD.addActionListener(new ShowDListener());
        toolMenu.add(showD);
        toolMenu.add(showE);
        menuBar.add(toolMenu, 1);
    }

    public String getDocument() {
        return textPane.getText();
    }

    // Open file and load into text pane
    public void OpenFile() {
        StringBuilder lines = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Decode and decrypt 
        String encodedText = lines.toString();
        String clearText = decrypt(encodedText);

        textPane.setText(clearText);
        setTitle(file.getName());
    }

    // Save content from text pane to file
    public void SaveFile() {
        try {
            FileWriter fileWriter = new FileWriter(file);

            // Get text and encrypt and encode to based64
            String clearText = getDocument();
            String encodedText = encrypt(clearText);

            fileWriter.write(encodedText);
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
    // Event Handlers
    //==========================================================================    
    // Show Decrypted Handler
    protected class ShowDListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            textPane.setText(contents);
            contents = null;
            showE.setEnabled(true);
            showD.setEnabled(false);
        }
    }

    protected class ShowEListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // store text pane sting to contents
            contents = textPane.getText();
            textPane.setText(encrypt(contents));
            showE.setEnabled(false);
            showD.setEnabled(true);
        }
    }

    //==========================================================================
    // Encryption Methods
    //==========================================================================    
    // Shared method to go through data to encrypt/decrypt block by block
    private byte[] cipherDoFinal(Cipher c, byte[] data, int blockSize) {
        int pos = 0;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        while (pos < data.length) {
            if (data.length - pos < blockSize) {     // end of array
                blockSize = data.length - pos;
            }
            try {
                outStream.write(c.doFinal(data, pos, blockSize));
            } catch (Exception e) {
                e.printStackTrace();
            }
            pos += blockSize;
        }
        // return byte array
        return outStream.toByteArray();
    }

    // Methods from java.security.SecureRandom are used here
    // Given byte array as input and return encrypted byte array
    public String encrypt(String data) {
        String ret = null;
        try {
            // initialize Cipher to AES algorithm
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            System.out.println(aesKey);
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, aesKey);

            // encryption
            int blockSize = keySize / 8 - 1;                  // block size based on key size
            byte[] enc = cipherDoFinal(c, data.getBytes(), blockSize);

            // base64 encoding to convert byte array to string
            ret = new String(Base64.getEncoder().encode(enc));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    // Given byte array as input and return decrypted byte array
    public String decrypt(String data) {
        String ret = null;
        try {
            // initialize Cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            System.out.println(aesKey);
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, aesKey);

            // based64 decoding to convert string to byte array
            byte[] decoded = Base64.getDecoder().decode(data.getBytes());
            // decryption
            int blockSize = keySize / 8;                     // block size based on key size
            byte[] dec = cipherDoFinal(c, decoded, blockSize);

            // return as String
            ret = new String(dec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
