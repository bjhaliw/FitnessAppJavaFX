package model;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;

import org.apache.commons.codec.DecoderException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DriverWriter {

	public static void main(String[] args) throws IOException, DecoderException {
		WorkoutTracker tracker = new WorkoutTracker();
		tracker.loadWorkoutList("C:\\Users\\bjhal\\Desktop\\WorkoutList.txt");
		Application.launch(ReadAndWrite.class, args);

		FileOutputStream fos = new FileOutputStream(ReadAndWrite.directoryPath + "/WorkoutTracker.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		ReadAndWrite.saveWorkoutExcel(tracker, workbook);
		workbook.write(fos);
		System.out.println("Success!");

		workbook.close();
	}
}
