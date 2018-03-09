package alohomora;
import alohomora.model.Challenge;
import alohomora.model.CryptoUtils;
import alohomora.model.User;
import javafx.fxml.FXML;
import org.bouncycastle.openpgp.PGPException;

import java.io.IOException;
import java.security.NoSuchProviderException;

public class ControllerConnect {
	private User user;

	@FXML
	public void initialize() {
		char [] pass = {'t','e'};
			try {
				try {
					CryptoUtils.generatePGPKey(pass, "leo");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (PGPException e) {
				e.printStackTrace();
			}

	}


}

