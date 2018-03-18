package alohomora.crypto;

import alohomora.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Alohomora Password Manager
 * Copyright (C) 2018 Team Alohomora
 * Léo BERGEROT, Sylvain COMBRAQUE, Sarah LAMOTTE, Nathan JANCZEWSKI
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 **/
public class RSAObject {

	private File pubKeyFile, priKeyFile;
	private KeyPair keys;

	/**
	 * RSAObject is a singleton that allows to sign messages
	 * @param keyname
	 * @throws IOException
	 */
	public RSAObject(String keyname) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		this.pubKeyFile = new File(Configuration.KEYS_FOLDER, keyname.toLowerCase() + ".pub");
		this.priKeyFile = new File(Configuration.KEYS_FOLDER, keyname.toLowerCase() + ".key");

		if (!pubKeyFile.exists() || !priKeyFile.exists()) {
			throw new FileNotFoundException();
		}

		byte[] priKeyBytes = Files.readAllBytes(priKeyFile.toPath());
		byte[] pubKeyBytes = Files.readAllBytes(pubKeyFile.toPath());

		PKCS8EncodedKeySpec priKS = new PKCS8EncodedKeySpec(priKeyBytes);
		X509EncodedKeySpec  pubKS = new X509EncodedKeySpec(pubKeyBytes);

		KeyFactory kf  = KeyFactory.getInstance("RSA");
		PrivateKey prk = kf.generatePrivate(priKS);
		PublicKey  puk = kf.generatePublic(pubKS);

		this.keys = new KeyPair(puk, prk);
	}

	/**
	 * Generates some new keys and saves them
	 * @param keyname Name for the key
	 * @param size    Bit size of the key (Probably 4096)
	 */
	public RSAObject(String keyname, int size) throws NoSuchAlgorithmException, IOException {
		this.pubKeyFile = new File(Configuration.KEYS_FOLDER, keyname.toLowerCase() + ".pub");
		this.priKeyFile = new File(Configuration.KEYS_FOLDER, keyname.toLowerCase() + ".key");

		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(size);

		this.keys = kpg.generateKeyPair();
		this.save();
	}

	/**
	 * Signs the message with the private key
	 * Let's the server check that we are the one who sent it
	 */
	public byte[] sign(String message) throws IllegalArgumentException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		if (message.isEmpty()) throw new IllegalArgumentException();

		Signature sign = Signature.getInstance("SHA512withRSA");
		sign.initSign(this.keys.getPrivate());

		sign.update(message.getBytes());

		return sign.sign();

	}

	/**
	 * Returns the public key that will be sent to the server
	 */
	public String formatServer(){
		return Base64.getEncoder().encodeToString(this.keys.getPublic().getEncoded());
	}

	/**
	 * Returns the OpenSSL valid public key
	 */
	public String formatSSL(){
		return "-----BEGIN PUBLIC KEY-----\n" + this.formatServer() + "\n-----END PUBLIC KEY-----\n";
	}

	/**
	 * Saves the keys to the config folder
	 * @throws FileAlreadyExistsException, IOException
	 */
	public void save() throws FileAlreadyExistsException, IOException {
		if (this.pubKeyFile.exists())
			throw new FileAlreadyExistsException(this.pubKeyFile.getName());
		if (this.priKeyFile.exists())
			throw new FileAlreadyExistsException(this.priKeyFile.getName());

		FileOutputStream fos = new FileOutputStream(this.priKeyFile);
		fos.write(this.keys.getPrivate().getEncoded());
		fos.close();

		fos = new FileOutputStream(this.pubKeyFile);
		fos.write(this.keys.getPublic().getEncoded());
		fos.close();

	}

	 /**
	 * This empty the object when not needed (e.g. when the user locks the software)
	 * Safety feature that NEEDS to be called everytime the object is no longer used
	 */
	public void kill(){
		// @TODO Simply setting the variables to null is not enough.
		// Need to do some work around the garbage collector probably
	}

	/**
	 * Returns whether the key for the keyname already exists
	 */
	public static boolean exists(String keyname){
		return new File(Configuration.KEYS_FOLDER, keyname.toLowerCase() + ".pub").exists() && new File(Configuration.KEYS_FOLDER, keyname.toLowerCase() + ".key").exists();
	}

}
