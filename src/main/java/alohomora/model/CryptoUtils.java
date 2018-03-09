package alohomora.model;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;	
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.bcpg.sig.Features;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;


	public class CryptoUtils
	{
	    public static void main(String args[])
	        throws Exception
	    {
	        char pass[] = {'h', 'e', 'l', 'l', 'o'};
	        PGPKeyRingGenerator krgen = generateKeyRingGenerator
	            ("alice@example.com", pass);

	        // Generate public key ring, dump to file.
	        PGPPublicKeyRing pkr = krgen.generatePublicKeyRing();
	        String publicRoute ="";
	        switch (System.getProperty("os.name").toLowerCase()) {
	        case "windows": publicRoute = "%appdata%/.alohomora";
	        break;
	        case "linux": publicRoute = " ~/.config/alohomora";
	        break;
	        case "mac": publicRoute = "~/Library/Application Support/alohomora";
	        break;
	        default : publicRoute = "%appdata%/.alohomora";
	        }
	        BufferedOutputStream pubout = new BufferedOutputStream
	            (new FileOutputStream("publicKey.pkr"));
	        pkr.encode(pubout);
	        pubout.close();

	        // Generate private key, dump to file.
	        PGPSecretKeyRing skr = krgen.generateSecretKeyRing();
	        String privateRoute ="";
	        switch (System.getProperty("os.name").toLowerCase()) {
	        case "windows": privateRoute = "%appdata%/.alohomora";
	        break;
	        case "linux": privateRoute = " ~/.config/alohomora";
	        break;
	        case "mac": privateRoute = "~/Library/Application Support/alohomora";
	        break;
	        default : privateRoute = "%appdata%/.alohomora";
	        }
	        BufferedOutputStream secout = new BufferedOutputStream
		    (new FileOutputStream(privateRoute + "privateKey.skr"));
	        skr.encode(secout);
	        secout.close();
	    }

	    public final static PGPKeyRingGenerator generateKeyRingGenerator
	        (String id, char[] pass)
	        throws Exception
	    { return generateKeyRingGenerator(id, pass, 0xc0); }


	    public final static PGPKeyRingGenerator generateKeyRingGenerator
	        (String id, char[] pass, int s2kcount)
	        throws Exception
	    {
	        // This object generates individual key-pairs.
	        RSAKeyPairGenerator  kpg = new RSAKeyPairGenerator();

	        // Boilerplate RSA parameters, no need to change anything
	        // except for the RSA key-size (2048). You can use whatever
	        // key-size makes sense for you -- 4096, etc.
	        kpg.init
	            (new RSAKeyGenerationParameters
	             (BigInteger.valueOf(0x10001),
	              new SecureRandom(), 2048, 12));

	        // First create the master (signing) key with the generator.
	        PGPKeyPair rsakp_sign =
	            new BcPGPKeyPair
	            (PGPPublicKey.RSA_SIGN, kpg.generateKeyPair(), new Date());
	        // Then an encryption subkey.
	        PGPKeyPair rsakp_enc =
	            new BcPGPKeyPair
	            (PGPPublicKey.RSA_ENCRYPT, kpg.generateKeyPair(), new Date());

	        // Add a self-signature on the id
	        PGPSignatureSubpacketGenerator signhashgen =
	            new PGPSignatureSubpacketGenerator();
	        
	        // Add signed metadata on the signature.
	        // Declare its purpose
	        signhashgen.setKeyFlags
	            (false, KeyFlags.SIGN_DATA|KeyFlags.CERTIFY_OTHER);
	        // Set preferences for secondary crypto algorithms to use
	        //    when sending messages to this key.
	        signhashgen.setPreferredSymmetricAlgorithms
	            (false, new int[] {
	                SymmetricKeyAlgorithmTags.AES_256,
	                SymmetricKeyAlgorithmTags.AES_192,
	                SymmetricKeyAlgorithmTags.AES_128
	            });
	        signhashgen.setPreferredHashAlgorithms
	            (false, new int[] {
	                HashAlgorithmTags.SHA256,
	                HashAlgorithmTags.SHA1,
	                HashAlgorithmTags.SHA384,
	                HashAlgorithmTags.SHA512,
	                HashAlgorithmTags.SHA224,
	            });
	        //  Request senders add additional checksums to the
	        //    message (useful when verifying unsigned messages.)
	        signhashgen.setFeature
	            (false, Features.FEATURE_MODIFICATION_DETECTION);

	        // Create a signature on the encryption subkey.
	        PGPSignatureSubpacketGenerator enchashgen =
	            new PGPSignatureSubpacketGenerator();
	        // Add metadata to declare its purpose
	        enchashgen.setKeyFlags
	            (false, KeyFlags.ENCRYPT_COMMS|KeyFlags.ENCRYPT_STORAGE);

	        // Objects used to encrypt the secret key.
	        PGPDigestCalculator sha1Calc =
	            new BcPGPDigestCalculatorProvider()
	            .get(HashAlgorithmTags.SHA1);
	        PGPDigestCalculator sha256Calc =
	            new BcPGPDigestCalculatorProvider()
	            .get(HashAlgorithmTags.SHA256);

	        // bcpg 1.48 exposes this API that includes s2kcount. Earlier
	        // versions use a default of 0x60.
	        PBESecretKeyEncryptor pske =
	            (new BcPBESecretKeyEncryptorBuilder
	             (PGPEncryptedData.AES_256, sha256Calc, s2kcount))
	            .build(pass);
	        
	        // Finally, create the keyring itself. The constructor
	        // takes parameters that allow it to generate the self
	        // signature.
	        PGPKeyRingGenerator keyRingGen =
	            new PGPKeyRingGenerator
	            (PGPSignature.POSITIVE_CERTIFICATION, rsakp_sign,
	             id, sha1Calc, signhashgen.generate(), null,
	             new BcPGPContentSignerBuilder
	             (rsakp_sign.getPublicKey().getAlgorithm(),
	              HashAlgorithmTags.SHA1),
	             pske);

	        // Add our encryption subkey, together with its signature.
	        keyRingGen.addSubKey
	            (rsakp_enc, enchashgen.generate(), null);
	        return keyRingGen;
	    }
	    
	    // Return true if the message is signed, false otherwise .
	    public final static boolean signMessage(String message,PGPPublicKeyRing pkr ) {
	    	boolean result = false;
	    	PGPSignatureGenerator signatureGenerator = new PGPSignatureGenerator(
	                (PGPContentSignerBuilder) new JcaPGPContentSignerBuilder(((PGPDigestCalculator) pkr).getAlgorithm(), PGPUtil.SHA512));
	    	//  METTRE LA SIGNATURE SUR LE MESSAGE 
	    	return result;
	    }
	    
	    
	    public final static SecretKey generateKeyAES() throws NoSuchAlgorithmException {	
	    	KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom(new String("").getBytes());
            keygen.init(random);
            SecretKey key = keygen.generateKey();
            return key;
	    }
}
	