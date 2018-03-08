package alohomora;

import alohomora.model.Challenge;
import alohomora.model.CryptoUtils;
import alohomora.model.User;
import javafx.fxml.FXML;

import java.io.IOException;

public class ControllerConnect {
    private User user;
    @FXML
    public void initialize() {
        try {
            CryptoUtils.generateKeyPair();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

