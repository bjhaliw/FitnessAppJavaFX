package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.Exercise;
import model.ExerciseList;
import model.ReadAndWrite;
import model.Workout;
import model.WorkoutTracker;

public class MainGUI extends Application {

	ExerciseList list;
	WorkoutTracker tracker;
	Workout Workout;
	Alerts alert;
	BorderPane pane;
	Exercise exercise;
	Button homeButton;
	Stage stage;
	
	public static final int SCENE_WIDTH = 1280;
	public static final int SCENE_HEIGHT = 800;
	public static String directoryPath;
		
	public MainGUI() {
		this.list = new ExerciseList();
		this.tracker = new WorkoutTracker();
		this.Workout = new Workout();
		this.alert = new Alerts();
		this.pane = new BorderPane();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {	
		this.stage = primaryStage;
		this.pane.setTop(createMenuBar());
		
		if (directoryPath == null) {
			this.alert.directoryPathNotFound();
			launchDirectoryChooser();
			tracker.loadWorkoutList(directoryPath + "/WorkoutList.txt");
			list.loadExerciseList(directoryPath + "/ExerciseList.txt");
			
			System.out.println(list.getAbsExercises().get(0).getExerciseName());
		}
		
		Scene scene = new Scene(this.pane, SCENE_WIDTH, SCENE_HEIGHT);
		stage.setTitle("Fitness Application");
		stage.setScene(scene);
		stage.show();
			
	}
	
	public MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();

		Menu file = new Menu("File");
		MenuItem save = new MenuItem("Save");
		MenuItem saveAs = new MenuItem("Save As...");
		MenuItem edit = new MenuItem("Edit Application");
		MenuItem exit = new MenuItem("Exit");
		MenuItem editGUI = new Menu("Edit GUI");
		
		Menu credits = new Menu("Credits");

		menuBar.getMenus().addAll(file, credits);
		file.getItems().addAll(save, saveAs, exit);

		save.setOnAction(e -> {
			try {
				if (directoryPath == null) {
					launchDirectoryChooser();
				}
				ReadAndWrite.saveAllInformation(this.tracker, this.list, directoryPath);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		saveAs.setOnAction(e -> {
			launchDirectoryChooser();
			try {
				ReadAndWrite.saveAllInformation(this.tracker, this.list, directoryPath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});


		exit.setOnAction(e -> {
			this.alert.exitProgramAlert();

		});

		return menuBar;
	}
	
	public void launchDirectoryChooser() {
		Stage stage = new Stage();
		
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Select or create a folder");
		stage.setAlwaysOnTop(true);

		File directory = chooser.showDialog(stage);

		if (directory == null) {
			System.out.println("User backed out without selecting a directory");
			return;
		}

		File filePath = new File(directory.getAbsolutePath());
		directoryPath = filePath.getAbsolutePath();
		if (!filePath.exists()) {
			try {
				Files.createDirectories(Paths.get(directoryPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void main(String[] args) {
		Application.launch(args);

	}

}
