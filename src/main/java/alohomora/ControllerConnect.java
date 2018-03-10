package alohomora;
import alohomora.model.Challenge;
import alohomora.model.CryptoUtils;
import alohomora.model.User;
import javafx.fxml.FXML;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPSecretKey;

import java.io.IOException;
import java.security.*;

public class ControllerConnect {
	private User user;

	@FXML
	public void initialize() {
		char [] pass = {'t','e'};
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		try {
			try {
				CryptoUtils.generatePGPKey(pass,"e");
				CryptoUtils.signedMessage("test", pass);
			} catch (PGPException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			}
			} catch (IOException e) {
			e.printStackTrace();
			}
	}


}

