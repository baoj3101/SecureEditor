
/**
 * Secure Editor:
 *   This is a text editor with encryption and decryption
 *   When a file is saved, it will be encrypted using RSA
 *   When a file is loaded, it will be decrypted using RSA
 *
 * @author baoj3101
 */
import java.util.Base64;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.security.*;
import javax.crypto.Cipher;

public class SecureEditor extends BaseEditor implements Encrypt {

    // RSA variables
    private static final int keySize = 2048;         // 2048-bit RSA
    private static final int keySeed = 20190523;     // fixed random seed to ensure repeatibilty
    private static KeyPair rsaKey;                   // public/private key pair

    // constructor
    public SecureEditor() {
        super();
        setTitle("New File");

        // generate RSA key pair
        System.out.println("Debug: generating RSA key pair ......");
        genRSAKey();              // generate RSA key using fixed random seed
        
        // print for debugging
        System.out.println("Debug: public key generated");
        System.out.println(rsaKey.getPublic());
    }

    // generate RSA key pair
    private void genRSAKey() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(keySeed);             // fix random seed for repeatibility
            KeyPairGenerator genKey = KeyPairGenerator.getInstance("RSA");
            genKey.initialize(keySize, random);
            rsaKey = genKey.genKeyPair();        // generat RSA key pair
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
            // initialize Cipher
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, rsaKey.getPrivate());  // encrypt using private key

            // encryption
            int blockSize = keySize / 8 - 11;                  // block size based on key size
            byte[] enc = cipherDoFinal (c, data.getBytes(), blockSize);
            
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
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, rsaKey.getPublic());  // decrypt using public key
            
            // based64 decoding to convert string to byte array
            byte[] decoded = Base64.getDecoder().decode(data.getBytes());
            // decryption
            int blockSize = keySize / 8;                     // block size based on key size
            byte[] dec = cipherDoFinal (c, decoded, blockSize);
            
            // return as String
            ret = new String(dec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
