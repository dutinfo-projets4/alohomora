package alohomora.model;


import org.bouncycastle.openpgp.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.*;
import java.util.Base64;
import java.util.Date;

	public class CryptoUtils {
		/**
		 *  Génère un couple de clef privée et public format PGP
		 * @return PGPKeyPair
		 * @throws NoSuchProviderException
		 */
		public static void  generatePGPKey(char [] passPhrase,String id) throws NoSuchProviderException, PGPException, IOException {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			PGPKeyPair dsaKeyPair = null;
			KeyPair keyPair = null;
			//génerer un couple clef publique privée
			try {
				 keyPair = generateDSAKeyPair(4096);
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			}
			try {
				 dsaKeyPair =  new PGPKeyPair(PGPPublicKey.RSA_SIGN, keyPair, new Date());

			} catch (PGPException e) {
				e.printStackTrace();
			}
			SecureRandom secureRamdom = new SecureRandom();

			PGPKeyRingGenerator keyRingGen = null;
			try {
				keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, dsaKeyPair,id+"<"+id+"@"+getDomain()+">", PGPEncryptedData.AES_256, passPhrase,true, null, null, secureRamdom.getInstanceStrong(), "BC");
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
		 * Permet de signer un String
		 */

		

}

