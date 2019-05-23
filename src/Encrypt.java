/**
 * Encrypt Interface:
 *   This is an interface to implement encryption and decryption
 *   Two methods: encrypt and decrypt
 * 
 * @author baoj3101
 */

public interface Encrypt {
    // Given string as input and return encrypted base64 string
    public String encrypt (String data);
    // Given base64 string as input and return decrypted string
    public String decrypt (String data);
}
