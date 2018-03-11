package alohomora.model;


import org.bouncycastle.openpgp.*;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class CryptoUtils {
	/**
	 *  Génère un couple de clef privée et public format PGP
	 * @return PGPKeyPair
	 * @throws NoSuchProviderException
	 */
	public static void  generatePGPKey(char [] passPhrase,String id) throws NoSuchProviderException, PGPException, IOException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		PGPKeyPair rsaKeyPair = null;
		KeyPair keyPair = null;
		//génerer un couple clef publique privée
		try {
			keyPair = generateDSAKeyPair(4096);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		try {
			rsaKeyPair =  new PGPKeyPair(PGPPublicKey.RSA_GENERAL, keyPair, new Date());

		} catch (PGPException e) {
			e.printStackTrace();
		}
		SecureRandom secureRamdom = new SecureRandom();

		PGPKeyRingGenerator keyRingGen = null;
		try {
			keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, rsaKeyPair,id+"<"+id+"@"+getDomain()+">", PGPEncryptedData.AES_256, passPhrase,false, null, null, secureRamdom.getInstanceStrong(), "BC");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		write(Base64.getEncoder().encodeToString(keyRingGen.generateSecretKeyRing().getEncoded()).getBytes(),"secret");
		write(Base64.getEncoder().encodeToString(keyRingGen.generatePublicKeyRing().getEncoded()).getBytes(),"public");
	}

	/**
	 *  Génère un couple de clef privée et public
	 * @return Couple de clef privée clef publique
	 * @throws NoSuchProviderException
	 */
	private static KeyPair generateDSAKeyPair(int sizeKey) throws NoSuchProviderException {
		KeyPair keyPair = null;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA","BC");
			SecureRandom random = SecureRandom.getInstanceStrong();
			keyPairGenerator.initialize(sizeKey, random);
			keyPair =  keyPairGenerator.generateKeyPair();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return keyPair;
	}

	/**
	 * Sauvegarde la clef privée dans un fichier
	 * @param pv clef privée
	 * @param name nom du fichier de sauvegarde
	 * @throws IOException
	 */
	public static void saveSecretKey(PrivateKey pv, String name) throws IOException {
		byte[] key = pv.getEncoded();
		write(key, name);
	}

	/**
	 * Sauvegarde la clef public dans un fichier
	 * @param pk clef privée
	 * @param name nom du fichier de sauvegarde
	 * @throws IOException
	 */
	public static void savePubKey(PublicKey pk, String name) throws IOException {
		byte[] key = pk.getEncoded();
		write(key, name);
	}

	/**
	 * Permet décrire sur la machine (Dans ces fichiers j'ai pas les mots qui me viennent)
	 * @param key
	 * @param name
	 * @throws IOException
	 */
	private static void write(byte[] key,String name) throws IOException {
		FileOutputStream filekey = null;
		try {
			filekey = new FileOutputStream(name);
			filekey.write(key);
			filekey.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Permet dobtenir le domain de l'ordinateur de l'utilisateur
	 *
	 */
	public static String getDomain(){
		String []  split = new String[0];
		try {
			split = InetAddress.getByName(InetAddress.getLocalHost().getHostName()).toString().split("/");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return split[0];
	}
	/**
	 * Permet de signer un message
	 * @param message à signer
	 * @param  pass mot de passe de la clef privée
	 */

	public static String signedMessage(String message, char [] pass){
		String signedMessage = null;
		try{
			PGPSecretKey secretKey = CryptoUtils.readSecretKey("secret");
			PGPPrivateKey pgpPrivKey = secretKey.extractPrivateKey(pass, "BC");
			Signature messageS = Signature.getInstance("SHA256withRSA", "BC");
			messageS.initSign(pgpPrivKey.getKey());
			messageS.update(message.getBytes("UTF-8"));
			byte [] messagesSigned = messageS.sign();
			System.out.print(verifymessageSignature(message,messagesSigned));
			System.out.println(Base64.getEncoder().encodeToString(messagesSigned));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (PGPException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return signedMessage;
	}

	/**
	 * Permet de verifier une signature
	 * @param message message non signer
	 * @param  signature message signer
	 */
	public static boolean verifymessageSignature(String message, byte [] signature ){
		Signature check = null;
		boolean b = false;
		try {
			check = Signature.getInstance("SHA256withRSA", "BC");
			check.initVerify(readPublicKey("public").getKey("BC"));
			check.update(message.getBytes("UTF-8"));
			b = check.verify(signature);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (PGPException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (SignatureException e) {
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return b;
	}
	/**
	 * Permet de chiffrer en AES
	 * @param password Mot de passe de l'utilisateur local
	 * @param value élement non chiffré
	 * @return String la value chiffer
	 */
	public static String encrypt(String password, String value){
		String res = null;
		try {

			Cipher cipher = Cipher.getInstance("AES", "BC");
			 SecretKeySpec key  = generatePrivateKey(password);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			res = Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));



		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Permet déchiffrer AES
	 * @param password Mot de passe de l'utilisateur local
	 * @param value élement chiffré
	 * @return String la value déchiffrer
	 */
	public static String decrypt(String password, String value){
		String res = null;
		try {
			Cipher cipher = Cipher.getInstance("AES", "BC");
			SecretKeySpec key = generatePrivateKey(password);
			cipher.init(Cipher.DECRYPT_MODE, key);
			res = new String(cipher.doFinal(Base64.getDecoder().decode(value)));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return res;

	}

	/**
	 * Genere la clef a partir du mot de passe de l'utilisateur pour chiffrer
	 * @param password mot de passe local de l'utilisateur
	 * @return
	 */
	private static SecretKeySpec generatePrivateKey(String password){
		SecretKeySpec key = null;
		try {
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(),password.getBytes(), 50 , 256);
			 SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
			 key = new SecretKeySpec(keyFactory.generateSecret(pbeKeySpec).getEncoded(), "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return key;
	}

	/**
	 * lire une clef privée
	 * copier du repos github exemple de bounty castle
	 * @param  fileName le nom du fichier ou se trouve la clef privée
	 */

	public static PGPSecretKey readSecretKey(String fileName) throws IOException, PGPException
	{
		InputStream keyIn = new BufferedInputStream(new FileInputStream(fileName));
		PGPSecretKey secKey = readSecretKey(keyIn);
		keyIn.close();
		return secKey;
	}
	private static PGPSecretKey readSecretKey(InputStream input) throws IOException, PGPException
	{
		PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
				PGPUtil.getDecoderStream(input));

		//
		// we just loop through the collection till we find a key suitable for encryption, in the real
		// world you would probably want to be a bit smarter about this.
		//

		Iterator keyRingIter = pgpSec.getKeyRings();
		while (keyRingIter.hasNext())
		{
			PGPSecretKeyRing keyRing = (PGPSecretKeyRing)keyRingIter.next();

			Iterator keyIter = keyRing.getSecretKeys();
			while (keyIter.hasNext())
			{
				PGPSecretKey key = (PGPSecretKey)keyIter.next();

				if (key.isSigningKey())
				{
					return key;
				}
			}
		}

		throw new IllegalArgumentException("Can't find signing key in key ring.");
	}

	/**
	 * lire une clef public
	 * copier du repos github exemple de bounty castle
	 */
	static PGPPublicKey readPublicKey(String fileName) throws IOException, PGPException
	{
		InputStream keyIn = new BufferedInputStream(new FileInputStream(fileName));
		PGPPublicKey pubKey = readPublicKey(keyIn);
		keyIn.close();
		return pubKey;
	}

	/**
	 * A simple routine that opens a key ring file and loads the first available key
	 * suitable for encryption.
	 *
	 * @param input data stream containing the public key data
	 * @return the first public key found.
	 * @throws IOException
	 * @throws PGPException
	 */
	static PGPPublicKey readPublicKey(InputStream input) throws IOException, PGPException
	{
		PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(
				PGPUtil.getDecoderStream(input));

		//
		// we just loop through the collection till we find a key suitable for encryption, in the real
		// world you would probably want to be a bit smarter about this.
		//

		Iterator keyRingIter = pgpPub.getKeyRings();
		while (keyRingIter.hasNext())
		{
			PGPPublicKeyRing keyRing = (PGPPublicKeyRing)keyRingIter.next();

			Iterator keyIter = keyRing.getPublicKeys();
			while (keyIter.hasNext())
			{
				PGPPublicKey key = (PGPPublicKey)keyIter.next();

				if (key.isEncryptionKey())
				{
					return key;
				}
			}
		}

		throw new IllegalArgumentException("Can't find encryption key in key ring.");
	}
}





