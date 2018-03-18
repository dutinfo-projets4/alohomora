package alohomora;
import alohomora.crypto.RSAObject;
import alohomora.model.Data;
import alohomora.model.User;
import javafx.fxml.FXML;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class ControllerConnect {

	private RSAObject obj;
	private User user;

	@FXML
	public void initialize() {
		this.user = new User(0, "Toto", "toto@toto.fr", false, "", new Data());
		this.loadKeys();

		try {
			String msg = "Toto va a la plage";
			String signature = this.obj.sign(msg);

			FileOutputStream fos = new FileOutputStream("msg.sha256");
			fos.write(signature.getBytes());
			fos.close();

			fos = new FileOutputStream("msg.txt");
			fos.write(msg.getBytes());
			fos.close();

			fos = new FileOutputStream("key.pub");
			fos.write(this.obj.formatSSL().getBytes());
			fos.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.obj.kill();
	}

	private void loadKeys(){
		try {
			if (RSAObject.exists(this.user.getUsername().toLowerCase())) {
				try {
					this.obj = new RSAObject(this.user.getUsername());
				} catch (InvalidKeySpecException e) {
					e.printStackTrace();
				}
			}
			else
				this.obj = new RSAObject(this.user.getUsername(), 4096);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}

