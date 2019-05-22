/**
 * Encrypt Interface: two methods encrypt and decrypt
 * @author baoj3101
 */

public interface Encrypt {
    public byte[] encrypt (String data);
    public String decrypt (byte[] data);
}
