/**
 * Encrypt Interface:
 *   This is an interface to implement encryption and decryption
 *   Two methods: encrypt and decrypt
 * 
 * @author baoj3101
 */

public interface Encrypt {
    // Given byte array as input and return encrypted byte array
    public byte[] encrypt (byte[] data);
    // Given byte array as input and return decrypted byte array
    public byte[] decrypt (byte[] data);
}
