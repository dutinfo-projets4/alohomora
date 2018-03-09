package alohomora;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;

public class ControllerInterface {
	private boolean allElementIsActive = true;
	@FXML
	private HBox allElements;

	@FXML
	public void initialize() {

	}

	@FXML
	public void onClickAllElement(MouseEvent e) {


		if (this.allElementIsActive) {
			this.allElements.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), true);
			this.allElementIsActive = false;
		} else {
			this.allElements.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), false);
			this.allElementIsActive = true;
		}

	}

}
