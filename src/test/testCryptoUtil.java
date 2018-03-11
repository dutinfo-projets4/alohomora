import alohomora.model.CryptoUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.Security;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class testCryptoUtil {
	private String messageEncrypted;
	private String compar;
	private String messageToEncrypt;
	private String password;
	@Before
	public void init(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		messageToEncrypt = "Success or not";
		password = "leo";
	}
	@Test
	public void cipherAES(){
		this.messageEncrypted = CryptoUtils.encrypt(this.password, messageToEncrypt);
		System.out.println(this.messageEncrypted);
		System.out.println(compar = CryptoUtils.decrypt("leo", this.messageEncrypted));
		assertEquals(compar, messageToEncrypt);
	}

}
