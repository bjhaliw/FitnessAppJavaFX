package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
		VBox top = new VBox(10);
		Label text = new Label();
		text.setText("Fitness Tracker\nBrenton Haliw\nbrenton.haliw@gmail.com\nVersion 1.0\nJuly 21, 2020");
		top.getChildren().addAll(createMenuBar(), mainMenuButtonsHBox());

		this.pane.setTop(top);
		this.pane.setCenter(text);

		if (directoryPath == null) {
			this.alert.directoryPathNotFound();
			launchDirectoryChooser();
			tracker.loadWorkoutList(directoryPath + "/WorkoutList.txt");
			list.loadExerciseList(directoryPath + "/ExerciseList.txt");
		}

		Scene scene = new Scene(this.pane, SCENE_WIDTH, SCENE_HEIGHT);
		stage.setTitle("Fitness Application");
		stage.setScene(scene);
		stage.show();

	}

	public HBox mainMenuButtonsHBox() {
		HBox mainBox = new HBox(50);
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setPadding(new Insets(0, 0, 0, 10));

		Button workoutButton = new Button("Workout Tracker");
		workoutButton.setOnAction(e -> {
			pane.setCenter(workoutMenu());
		});
		Button exerciseListButton = new Button("Exercise List");
		Button viewStatisticsButton = new Button("View Statstics");

		mainBox.getChildren().addAll(workoutButton, exerciseListButton, viewStatisticsButton);

		return mainBox;
	}

	public VBox workoutMenu() {
		VBox mainBox = new VBox(50);
		mainBox.setAlignment(Pos.CENTER);
		HBox tables = new HBox(30);
		tables.setAlignment(Pos.CENTER);

		// Shows workout dates
		TableView<Workout> workoutViewTable = new TableView<>();
		TableColumn<Workout, String> workoutDates = new TableColumn<>("Workout Dates");
		workoutDates.setCellValueFactory(new PropertyValueFactory<>("startTimeString"));
		workoutViewTable.getColumns().add(workoutDates);
		ObservableList<Workout> workoutsList = FXCollections.observableArrayList();

		for (Workout workout : tracker.getWorkoutList()) {
			workoutsList.add(workout);
		}

		workoutViewTable.setItems(workoutsList);

		// Shows Exercises and Sets
		TableView<Exercise> exerciseViewTable = new TableView<>();

		tables.getChildren().addAll(workoutViewTable, exerciseViewTable);
		mainBox.getChildren().addAll(tables);
		return mainBox;
	}

	public MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();

		Menu file = new Menu("File");
		MenuItem save = new MenuItem("Save");
		MenuItem saveAs = new MenuItem("Save As...");
		Menu edit = new Menu("Edit");
		MenuItem exit = new MenuItem("Exit");
		MenuItem editGUI = new Menu("Edit GUI");

		Menu credits = new Menu("Credits");

		menuBar.getMenus().addAll(file, edit, credits);
		file.getItems().addAll(save, saveAs, exit);
		edit.getItems().addAll(editGUI);

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
		chooser.setTitle("Select Fitness Application Directory");
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
