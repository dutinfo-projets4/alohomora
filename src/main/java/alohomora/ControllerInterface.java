package alohomora;

import alohomora.model.User;
import alohomora.model.retrofitListner.RetrofitListnerUser;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;




public class ControllerInterface {
    private boolean allElementIsActive = true;
    @FXML
    private HBox allElements;

    @FXML
    public void initialize() {
        User.challengeConnect(new RetrofitListnerUser() {
            @Override
            public void onUserLoad(User user) {

            }

            @Override
            public void error(String msg) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.showAndWait();
                    }
                });
            }
        },"",10,"", "");
    }
    @FXML
    public void onClickAllElement(MouseEvent e){


        if(this.allElementIsActive) {
            this.allElements.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), true);
            this.allElementIsActive = false;
        }
        else {
            this.allElements.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), false);
            this.allElementIsActive = true;
        }

    }

}
