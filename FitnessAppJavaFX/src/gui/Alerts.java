package gui;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Alerts {

	protected Alert exitProgramAlert() {
		Alert alert = new Alert(AlertType.INFORMATION, "Are you sure that you want to quit?", ButtonType.YES,
				ButtonType.CANCEL);
		alert.setTitle("Closing Program");
		alert.setHeaderText("Information will not be saved automatically!");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.YES) {
			System.exit(0);
		}

		return alert;
	}
	
	protected Alert negativeRepsAlert() {
		Alert alert = new Alert(AlertType.INFORMATION, "You must enter a positive number for the amount of reps done.", ButtonType.YES,
				ButtonType.CANCEL);
		alert.setTitle("Negative Reps");
		alert.setHeaderText("Cannot have a negative number for reps.");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.YES) {
			System.exit(0);
		}

		return alert;
	}
	
	protected Alert negativeWeightAlert() {
		Alert alert = new Alert(AlertType.INFORMATION, "You must enter a positive number for the amount of weight used.", ButtonType.YES,
				ButtonType.CANCEL);
		alert.setTitle("Negative Weight");
		alert.setHeaderText("Cannot have a negative number for weight.");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.YES) {
			System.exit(0);
		}

		return alert;
	}
}
