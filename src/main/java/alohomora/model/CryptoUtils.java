package alohomora.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils {
    public static void generateKeyPair() throws IOException {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(4096);
            KeyPair keys = keyGen.generateKeyPair();

            byte[] privateKey = keys.getPrivate().getEncoded();
            FileOutputStream pvkeyw = null;
            try {
                pvkeyw = new FileOutputStream("private");
                pvkeyw.write(privateKey);
                pvkeyw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            byte[] publicKey = keys.getPrivate().getEncoded();
            FileOutputStream pbkeyw = null;
            try {
                pbkeyw = new FileOutputStream("public");
                pbkeyw.write(publicKey);
                pbkeyw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
