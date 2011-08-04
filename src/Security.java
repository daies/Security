/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author haiwei
 */
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Security {

    private static final int FBENCRYPT_KEY_SIZE = 16;
    private static final String AES_STR = "AES/ECB/NoPadding";
//    private static final String AES_STR = "AES/ECB/PKCS5Padding";

    private static String processKey(String key) {
        if (key == null) {
            key = "                ";
        } else if (key.length() < FBENCRYPT_KEY_SIZE) {

            while (key.length() < FBENCRYPT_KEY_SIZE) {
                key = key.concat(" ");
            }
        } else if (key.length() > FBENCRYPT_KEY_SIZE) {
            key = key.substring(0, FBENCRYPT_KEY_SIZE);
        }
        return key;
    }

    private static String processStringEncrypt(String str) {
        int len = str.length();
        int num = FBENCRYPT_KEY_SIZE - (len % FBENCRYPT_KEY_SIZE);

        for (int i = 0; i < num; ++i) {
            str = str.concat("`");

        }
        return str;
    }

    private static String processStringDecrypt(String str) {
        int offset = str.indexOf("`");
        if (offset != -1) {
            str = str.substring(0, offset);
        }
        return str;
    }

    public static String encrypt(String input, String key) {

        input = processStringEncrypt(input);
        key = processKey(key);

        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_STR);
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(Base64.encodeBase64(crypted));
    }

    public static String decrypt(String input, String key) {
        key = processKey(key);
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_STR);
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decodeBase64(input.getBytes()));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        String returnStr = new String(output);

        return processStringDecrypt(returnStr);
    }

    public static void main(String[] args) {
        String key = "key";
        String data = "123456789abcdefg123456789abcdefg123------------------------------------------------------------------007";
        String enMsg = Security.encrypt(data, key);
        System.out.println("enMsg = " + enMsg);
        String deMsg = Security.decrypt(Security.encrypt(data, key), key);
        System.out.println("deMsg = " + deMsg);

    }
}
