package gui;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.Exercise;
import model.ExerciseList;
import model.Workout;
import model.WorkoutTracker;
import model.Set;

public class GUIDriver extends Application {

	ExerciseList list;
	WorkoutTracker tracker;
	Workout Workout;
	Alerts alert;
	BorderPane pane;
	Exercise exercise;
	Button homeButton;
	Stage window;

	public GUIDriver() {
		this.list = new ExerciseList();
		this.tracker = new WorkoutTracker();
		this.Workout = new Workout();
		this.alert = new Alerts();
		this.pane = new BorderPane();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		int sceneWidth = 800, sceneHeight = 650;

		this.window = primaryStage;

		this.list.loadExerciseList("C:\\Users\\bjhal\\Desktop\\ExerciseList.txt");
		this.tracker.loadWorkoutList("C:\\Users\\bjhal\\Desktop\\WorkoutList.txt");

		this.pane.backgroundProperty()
				.set(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

		this.homeButton = createHomeButton();

		VBox homeBox = new VBox(10);
		homeBox.setPadding(new Insets(0, 0, 10, 0));
		homeBox.setAlignment(Pos.CENTER);
		homeBox.getChildren().add(this.homeButton);

		this.pane.setTop(createMenuBar());
		this.pane.setCenter(createInitialSelectionButtons());
		this.pane.setBottom(homeBox);

		Scene scene = new Scene(this.pane, sceneWidth, sceneHeight);
		window.setTitle("Fitness Application");
		window.setScene(scene);
		window.show();

	}

	public Button createHomeButton() {
		this.homeButton = new Button("Home");
		this.homeButton.setAlignment(Pos.CENTER);

		this.homeButton.backgroundProperty()
				.set(new Background(new BackgroundFill(Color.GOLD, new CornerRadii(10), Insets.EMPTY)));
		this.homeButton.setFont(Font.font("Calibri", FontWeight.BOLD, 36));

		this.homeButton.setOnAction(e -> {
			this.pane.setCenter(createInitialSelectionButtons());
		});

		return this.homeButton;
	}

	public MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();

		Menu file = new Menu("File");
		MenuItem save = new MenuItem("Save");
		MenuItem edit = new MenuItem("Edit Application");
		MenuItem exit = new MenuItem("Exit");
		Menu credits = new Menu("Credits");

		menuBar.getMenus().addAll(file, credits);
		file.getItems().addAll(save, edit, exit);

		save.setOnAction(e -> {
			try {
				this.tracker.saveWorkoutList("C:\\Users\\bjhal\\Desktop\\WorkoutList.txt");
				this.list.saveExerciseList("C:\\Users\\bjhal\\Desktop\\ExerciseList.txt");
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		});

		edit.setOnAction(e -> {
			editScreen();
		});

		exit.setOnAction(e -> {
			this.alert.exitProgramAlert();

		});

		return menuBar;
	}

	/**
	 * Allows the user to modify the colors of the buttons and background
	 */
	public void editScreen() {
		Color backgroundColor = (Color) this.pane.getBackground().getFills()
				.get(this.pane.getBackground().getFills().size() - 1).getFill();
		int backgroundRedNum = (int) (backgroundColor.getRed() * 255);
		int backgroundGreenNum = (int) (backgroundColor.getGreen() * 255);
		int backgroundBlueNum = (int) (backgroundColor.getBlue() * 255);

		VBox mainBox = new VBox(10);
		mainBox.setAlignment(Pos.CENTER);

		VBox backgroundRed = new VBox(10);
		VBox backgroundGreen = new VBox(10);
		VBox backgroundBlue = new VBox(10);
		HBox backgroundRedBox = new HBox(10);
		HBox backgroundGreenBox = new HBox(10);
		HBox backgroundBlueBox = new HBox(10);
		VBox title = new VBox(20);

		HBox backgroundSliders = new HBox(10);
		HBox buttonSliders = new HBox(10);

		Slider backgroundRedSlider = new Slider(0, 255, 5);
		Slider backgroundGreenSlider = new Slider(0, 255, 5);
		Slider backgroundBlueSlider = new Slider(0, 255, 5);
		backgroundRedSlider.setShowTickLabels(true);
		backgroundRedSlider.setShowTickMarks(true);
		backgroundGreenSlider.setShowTickLabels(true);
		backgroundGreenSlider.setShowTickMarks(true);
		backgroundBlueSlider.setShowTickLabels(true);
		backgroundBlueSlider.setShowTickMarks(true);
		backgroundRedSlider.setOrientation(Orientation.VERTICAL);
		backgroundBlueSlider.setOrientation(Orientation.VERTICAL);
		backgroundGreenSlider.setOrientation(Orientation.VERTICAL);

		Label header = new Label("Application Edit Screen");
		header.setFont(Font.font("Calibri", FontWeight.BOLD, 45));

		Label description = new Label(
				"Use this menu to make edits to the app. You can change colors and the order of buttons.");
		description.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));

		title.getChildren().addAll(header, description);
		title.setAlignment(Pos.CENTER);

		TextField redText = new TextField("" + backgroundRedNum);
		TextField greenText = new TextField("" + backgroundGreenNum);
		TextField blueText = new TextField("" + backgroundBlueNum);

		backgroundRedSlider.setValue(backgroundRedNum);
		backgroundGreenSlider.setValue(backgroundGreenNum);
		backgroundBlueSlider.setValue(backgroundBlueNum);

		backgroundRedSlider.valueProperty().addListener(e -> {
			int num = (int) backgroundRedSlider.getValue();
			String text = Integer.toString(num);
			redText.setText(text);

			this.pane.setBackground(new Background(new BackgroundFill(Color.rgb(Integer.parseInt(redText.getText()),
					Integer.parseInt(greenText.getText()), Integer.parseInt(blueText.getText())), CornerRadii.EMPTY,
					Insets.EMPTY)));

		});

		backgroundGreenSlider.valueProperty().addListener(e -> {
			int num = (int) backgroundGreenSlider.getValue();
			String text = Integer.toString(num);
			greenText.setText(text);

			this.pane.setBackground(new Background(new BackgroundFill(Color.rgb(Integer.parseInt(redText.getText()),
					Integer.parseInt(greenText.getText()), Integer.parseInt(blueText.getText())), CornerRadii.EMPTY,
					Insets.EMPTY)));

		});

		backgroundBlueSlider.valueProperty().addListener(e -> {
			int num = (int) backgroundBlueSlider.getValue();
			String text = Integer.toString(num);
			blueText.setText(text);

			this.pane.setBackground(new Background(new BackgroundFill(Color.rgb(Integer.parseInt(redText.getText()),
					Integer.parseInt(greenText.getText()), Integer.parseInt(blueText.getText())), CornerRadii.EMPTY,
					Insets.EMPTY)));
		});

		redText.setOnAction(e -> {
			backgroundRedSlider.setValue(Double.parseDouble(redText.getText()));
		});

		greenText.setOnAction(e -> {
			backgroundGreenSlider.setValue(Double.parseDouble(greenText.getText()));
		});

		blueText.setOnAction(e -> {
			backgroundBlueSlider.setValue(Double.parseDouble(blueText.getText()));

		});

		Label redLabel = new Label("Red");
		Label greenLabel = new Label("Green");
		Label blueLabel = new Label("Blue");

		redLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		greenLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		blueLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

		backgroundRed.getChildren().addAll(redLabel, backgroundRedSlider);
		backgroundRedBox.getChildren().addAll(backgroundRed, redText);

		backgroundGreen.getChildren().addAll(greenLabel, backgroundGreenSlider);
		backgroundRedBox.getChildren().addAll(backgroundGreen, greenText);

		backgroundBlue.getChildren().addAll(blueLabel, backgroundBlueSlider);
		backgroundBlueBox.getChildren().addAll(backgroundBlue, blueText);

		backgroundSliders.getChildren().addAll(backgroundRedBox, backgroundGreenBox, backgroundBlueBox);
		backgroundSliders.setAlignment(Pos.CENTER);

		Label backgroundSliderText = new Label("Background Color");
		backgroundSliderText.setFont(Font.font("Calibri", FontWeight.BOLD, 25));

		/************ Button Sliders *******************/

		Label buttonSliderText = new Label("Button Color");
		buttonSliderText.setFont(Font.font("Calibri", FontWeight.BOLD, 25));

		Slider buttonRedSlider = new Slider(0, 255, 5);
		Slider buttonGreenSlider = new Slider(0, 255, 5);
		Slider buttonBlueSlider = new Slider(0, 255, 5);
		buttonRedSlider.setShowTickLabels(true);
		buttonRedSlider.setShowTickMarks(true);
		buttonGreenSlider.setShowTickLabels(true);
		buttonGreenSlider.setShowTickMarks(true);
		buttonBlueSlider.setShowTickLabels(true);
		buttonBlueSlider.setShowTickMarks(true);
		buttonRedSlider.setOrientation(Orientation.VERTICAL);
		buttonBlueSlider.setOrientation(Orientation.VERTICAL);
		buttonGreenSlider.setOrientation(Orientation.VERTICAL);

		Color buttonColor = (Color) this.homeButton.getBackground().getFills()
				.get(this.homeButton.getBackground().getFills().size() - 1).getFill();
		int buttonRedNum = (int) (buttonColor.getRed() * 255);
		int buttonGreenNum = (int) (buttonColor.getGreen() * 255);
		int buttonBlueNum = (int) (buttonColor.getBlue() * 255);

		VBox buttonRed = new VBox(10);
		VBox buttonGreen = new VBox(10);
		VBox buttonBlue = new VBox(10);
		HBox buttonRedBox = new HBox(10);
		HBox buttonGreenBox = new HBox(10);
		HBox buttonBlueBox = new HBox(10);

		TextField buttonRedText = new TextField("" + buttonRedNum);
		TextField buttonGreenText = new TextField("" + buttonGreenNum);
		TextField buttonBlueText = new TextField("" + buttonBlueNum);

		buttonRedSlider.setValue(buttonRedNum);
		buttonGreenSlider.setValue(buttonGreenNum);
		buttonBlueSlider.setValue(buttonBlueNum);

		buttonRedSlider.valueProperty().addListener(e -> {
			int num = (int) buttonRedSlider.getValue();
			String text = Integer.toString(num);
			buttonRedText.setText(text);

			this.homeButton.setBackground(
					new Background(new BackgroundFill(Color.rgb(Integer.parseInt(buttonRedText.getText()),
							Integer.parseInt(buttonGreenText.getText()), Integer.parseInt(buttonBlueText.getText())),
							new CornerRadii(10), Insets.EMPTY)));

		});

		buttonGreenSlider.valueProperty().addListener(e -> {
			int num = (int) buttonGreenSlider.getValue();
			String text = Integer.toString(num);
			buttonGreenText.setText(text);

			this.homeButton.setBackground(
					new Background(new BackgroundFill(Color.rgb(Integer.parseInt(buttonRedText.getText()),
							Integer.parseInt(buttonGreenText.getText()), Integer.parseInt(buttonBlueText.getText())),
							new CornerRadii(10), Insets.EMPTY)));

		});

		buttonBlueSlider.valueProperty().addListener(e -> {
			int num = (int) buttonBlueSlider.getValue();
			String text = Integer.toString(num);
			buttonBlueText.setText(text);

			this.homeButton.setBackground(
					new Background(new BackgroundFill(Color.rgb(Integer.parseInt(buttonRedText.getText()),
							Integer.parseInt(buttonGreenText.getText()), Integer.parseInt(buttonBlueText.getText())),
							new CornerRadii(10), Insets.EMPTY)));
		});

		buttonRedText.setOnAction(e -> {
			buttonRedSlider.setValue(Double.parseDouble(buttonRedText.getText()));
		});

		buttonGreenText.setOnAction(e -> {
			buttonGreenSlider.setValue(Double.parseDouble(buttonGreenText.getText()));
		});

		buttonBlueText.setOnAction(e -> {
			buttonBlueSlider.setValue(Double.parseDouble(buttonBlueText.getText()));

		});
		Label buttonRedLabel = new Label("Red");
		Label buttonGreenLabel = new Label("Green");
		Label buttonBlueLabel = new Label("Blue");

		buttonRedLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		buttonGreenLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		buttonBlueLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

		buttonRed.getChildren().addAll(buttonRedLabel, buttonRedSlider);
		buttonGreen.getChildren().addAll(buttonGreenLabel, buttonGreenSlider);
		buttonBlue.getChildren().addAll(buttonBlueLabel, buttonBlueSlider);

		buttonRedBox.getChildren().addAll(buttonRed, buttonRedText);
		buttonGreenBox.getChildren().addAll(buttonGreen, buttonGreenText);
		buttonBlueBox.getChildren().addAll(buttonBlue, buttonBlueText);

		buttonSliders.getChildren().addAll(buttonRedBox, buttonGreenBox, buttonBlueBox);
		buttonSliders.setAlignment(Pos.CENTER);

		mainBox.getChildren().addAll(title, backgroundSliderText, backgroundSliders, buttonSliderText, buttonSliders);

		this.pane.setCenter(mainBox);
	}

	public VBox createInitialSelectionButtons() {

		VBox initialButtons = new VBox(50);

		Button newWorkout = new Button("Create a New Workout");
		newWorkout.backgroundProperty()
				.set(new Background(new BackgroundFill(this.homeButton.getBackground().getFills().get(0).getFill(),
						new CornerRadii(10), Insets.EMPTY)));
		newWorkout.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
		newWorkout.setOnAction(e -> {
			createNewWorkout();
		});

		Button viewWorkouts = new Button("View a Past Workout");
		viewWorkouts.backgroundProperty()
				.set(new Background(new BackgroundFill(this.homeButton.getBackground().getFills().get(0).getFill(),
						new CornerRadii(10), Insets.EMPTY)));
		viewWorkouts.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
		viewWorkouts.setOnAction(e -> {
			viewWorkouts();
		});

		Button stats = new Button("View Exercise Statistics");
		stats.backgroundProperty()
				.set(new Background(new BackgroundFill(this.homeButton.getBackground().getFills().get(0).getFill(),
						new CornerRadii(10), Insets.EMPTY)));
		stats.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
		stats.setOnAction(e -> {
			displayStats();
		});

		Button newExercise = new Button("Create a New Exercise");
		newExercise.backgroundProperty()
				.set(new Background(new BackgroundFill(this.homeButton.getBackground().getFills().get(0).getFill(),
						new CornerRadii(10), Insets.EMPTY)));
		newExercise.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
		newExercise.setOnAction(e -> {
			this.pane.setCenter(viewExercises());
		});

		initialButtons.getChildren().addAll(newWorkout, viewWorkouts, stats, newExercise);

		initialButtons.setAlignment(Pos.CENTER);

		return initialButtons;
	}

	public void createNewWorkout() {
		VBox box = viewExercises();
		box.getChildren().remove(1); // Removes add/remove/edit/labels

		HBox exerciseButtons = new HBox(10);
		exerciseButtons.setAlignment(Pos.CENTER);
		Button addExercise = new Button("Add to Workout");
		Button removeExercise = new Button("Remove from Workout");
		exerciseButtons.getChildren().addAll(addExercise, removeExercise);

		TableView<Exercise> Workout = new TableView<>();
		Workout.setStyle("-fx-table-cell-border-color: transparent;");
		Workout.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<Exercise, String> names = new TableColumn<>("Workout Exercises");
		names.setCellValueFactory(new PropertyValueFactory<>("exerciseName"));

		addExercise.setOnAction(e -> {

		});

		box.getChildren().addAll(exerciseButtons);
		this.pane.setCenter(box);

	}

	public void viewWorkouts() {

	}

	public VBox viewExercises() {
		VBox box = new VBox(10);
		HBox tables = new HBox();
		HBox bottom = new HBox(10);
		box.getChildren().addAll(tables, bottom);
		tables.setAlignment(Pos.CENTER);

		TableView<Exercise> type = new TableView<>();
		type.setStyle("-fx-table-cell-border-color: transparent;");
		type.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TableView<Exercise> back = new TableView<>();
		back.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		back.setStyle("-fx-table-cell-border-color: transparent;");
		TableView<Exercise> chest = new TableView<>();
		chest.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		chest.setStyle("-fx-table-cell-border-color: transparent;");
		TableView<Exercise> legs = new TableView<>();
		legs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		legs.setStyle("-fx-table-cell-border-color: transparent;");
		TableView<Exercise> shoulders = new TableView<>();
		shoulders.setStyle("-fx-table-cell-border-color: transparent;");
		shoulders.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		back.setVisible(false);
		back.managedProperty().bind(back.visibleProperty());
		chest.setVisible(false);
		chest.managedProperty().bind(chest.visibleProperty());
		legs.setVisible(false);
		legs.managedProperty().bind(legs.visibleProperty());
		shoulders.setVisible(false);
		legs.managedProperty().bind(legs.visibleProperty());

		tables.getChildren().addAll(type, back, chest, legs, shoulders);

		TableColumn<Exercise, String> exerciseTypes = new TableColumn<>("Exercise Types");
		TableColumn<Exercise, String> chestNames = new TableColumn<>("Exercise Names");
		TableColumn<Exercise, String> backNames = new TableColumn<>("Exercise Names");
		TableColumn<Exercise, String> legsNames = new TableColumn<>("Exercise Names");
		TableColumn<Exercise, String> shouldersNames = new TableColumn<>("Exercise Names");

		exerciseTypes.setCellValueFactory(new PropertyValueFactory<>("exerciseType"));
		chestNames.setCellValueFactory(new PropertyValueFactory<>("exerciseName"));
		backNames.setCellValueFactory(new PropertyValueFactory<>("exerciseName"));
		shouldersNames.setCellValueFactory(new PropertyValueFactory<>("exerciseName"));
		legsNames.setCellValueFactory(new PropertyValueFactory<>("exerciseName"));

		type.getColumns().add(exerciseTypes);
		back.getColumns().add(backNames);
		chest.getColumns().add(chestNames);
		legs.getColumns().add(legsNames);
		shoulders.getColumns().add(shouldersNames);

		back.setItems(this.list.getBackExercises());
		chest.setItems(this.list.getChestExercises());
		legs.setItems(this.list.getLegsExercises());
		shoulders.setItems(this.list.getShouldersExercises());
		type.setItems(this.list.getTypeExercises());

		TextField exerciseName = new TextField();
		TextField exerciseType = new TextField();
		Button add = new Button("Add");
		Button delete = new Button("Delete");
		Button edit = new Button("Edit");
		exerciseName.setPromptText("Name");
		exerciseType.setPromptText("Type");

		bottom.getChildren().addAll(exerciseName, exerciseType, add, delete, edit);

		type.setOnMouseClicked(e -> {
			if (type.getSelectionModel().getSelectedItem() != null) {
				Exercise exercise = (Exercise) type.getSelectionModel().getSelectedItem();

				exerciseType.setText(exercise.getExerciseType());

				if (exercise.getExerciseType().equals("Chest")) {
					chest.setVisible(true);
					chest.setPrefSize(type.getWidth(), type.getHeight());
					back.setVisible(false);
					back.setPrefSize(1, 1);
					shoulders.setVisible(false);
					shoulders.setPrefSize(1, 1);
					legs.setVisible(false);
					legs.setPrefSize(1, 1);
				}

				if (exercise.getExerciseType().equals("Back")) {
					back.setVisible(true);
					back.setPrefSize(type.getWidth(), type.getHeight());
					chest.setVisible(false);
					chest.setPrefSize(1, 1);
					shoulders.setVisible(false);
					shoulders.setPrefSize(1, 1);
					legs.setVisible(false);
					legs.setPrefSize(1, 1);
				}

				if (exercise.getExerciseType().equals("Legs")) {
					legs.setVisible(true);
					legs.setPrefSize(type.getWidth(), type.getHeight());
					back.setVisible(false);
					back.setPrefSize(1, 1);
					chest.setVisible(false);
					chest.setPrefSize(1, 1);
					shoulders.setVisible(false);
					shoulders.setPrefSize(1, 1);
				}

				if (exercise.getExerciseType().equals("Shoulders")) {
					shoulders.setVisible(true);
					shoulders.setPrefSize(type.getWidth(), type.getHeight());
					back.setVisible(false);
					back.setPrefSize(1, 1);
					chest.setVisible(false);
					chest.setPrefSize(1, 1);
					legs.setVisible(false);
					legs.setPrefSize(1, 1);
				}

			}
		});

		chest.setOnMouseClicked(e -> {
			if (chest.getSelectionModel().getSelectedItem() != null) {
				Exercise exercise = (Exercise) chest.getSelectionModel().getSelectedItem();

				exerciseName.setText(exercise.getExerciseName());
			}
		});

		back.setOnMouseClicked(e -> {
			if (back.getSelectionModel().getSelectedItem() != null) {
				Exercise exercise = (Exercise) back.getSelectionModel().getSelectedItem();

				exerciseName.setText(exercise.getExerciseName());
			}
		});

		legs.setOnMouseClicked(e -> {
			if (legs.getSelectionModel().getSelectedItem() != null) {
				Exercise exercise = (Exercise) legs.getSelectionModel().getSelectedItem();

				exerciseName.setText(exercise.getExerciseName());
			}
		});

		shoulders.setOnMouseClicked(e -> {
			if (shoulders.getSelectionModel().getSelectedItem() != null) {
				Exercise exercise = (Exercise) shoulders.getSelectionModel().getSelectedItem();

				exerciseName.setText(exercise.getExerciseName());
			}
		});

		add.setOnAction(e -> {
			if (!exerciseName.getText().equals("") && !exerciseType.getText().equals("")) {
				list.addExercise(new Exercise(exerciseName.getText(), exerciseType.getText()));
			}

			if (exerciseType.getText().equals("Chest")) {
				chest.getItems().add(new Exercise(exerciseName.getText(), exerciseType.getText()));
			}

			if (exerciseType.getText().equals("Back")) {
				back.getItems().add(new Exercise(exerciseName.getText(), exerciseType.getText()));
			}

			if (exerciseType.getText().equals("Legs")) {
				legs.getItems().add(new Exercise(exerciseName.getText(), exerciseType.getText()));
			}

			if (exerciseType.getText().equals("Shoulders")) {
				shoulders.getItems().add(new Exercise(exerciseName.getText(), exerciseType.getText()));
			}

			exerciseName.clear();
		});

		delete.setOnAction(e -> {
			if (!exerciseName.getText().equals("") && !exerciseType.getText().equals("")) {
				list.removeExercise(new Exercise(exerciseName.getText(), exerciseType.getText()));
			}

			ObservableList<Exercise> selected, exercises;

			if (exerciseType.getText().equals("Chest")) {
				selected = chest.getSelectionModel().getSelectedItems();
				exercises = chest.getItems();

				selected.forEach(exercises::remove);
			}

			if (exerciseType.getText().equals("Back")) {
				selected = back.getSelectionModel().getSelectedItems();
				exercises = back.getItems();

				selected.forEach(exercises::remove);
			}

			if (exerciseType.getText().equals("Legs")) {
				selected = legs.getSelectionModel().getSelectedItems();
				exercises = legs.getItems();

				selected.forEach(exercises::remove);
			}

			if (exerciseType.getText().equals("Shoulders")) {
				selected = shoulders.getSelectionModel().getSelectedItems();
				exercises = shoulders.getItems();

				selected.forEach(exercises::remove);
			}

			exerciseName.clear();
		});

		bottom.setAlignment(Pos.CENTER);

		return box;
	}

	public void displayStats() {

		VBox box = new VBox(10);
		HBox exercises = new HBox();
		exercises.setAlignment(Pos.CENTER);
		box.setAlignment(Pos.CENTER);

		MenuItem back = new MenuItem("Back");
		MenuItem chest = new MenuItem("Chest");
		MenuItem legs = new MenuItem("Legs");
		MenuItem shoulders = new MenuItem("Shoulders");
		MenuButton exerciseTypes = new MenuButton("Type");
		MenuButton exerciseNames = new MenuButton("Name");
		exerciseTypes.setPrefWidth(100);
		exerciseTypes.getItems().addAll(back, chest, legs, shoulders);

		box.getChildren().add(exercises);

		back.setOnAction(e -> {
			exerciseTypes.setText(back.getText());

			exerciseNames.getItems().clear();

			for (Exercise exercise : list.getBackList()) {
				exerciseNames.getItems().add(new MenuItem(exercise.getExerciseName()));
			}

			for (MenuItem item : exerciseNames.getItems()) {
				item.setOnAction(f -> {
					exerciseNames.setText(item.getText());
					if (box.getChildren().size() == 2) {
						box.getChildren().remove(1);
					}
					box.getChildren().add(createGraph(item.getText()));

				});

			}
		});

		chest.setOnAction(e -> {
			exerciseTypes.setText(chest.getText());

			exerciseNames.getItems().clear();

			for (Exercise exercise : list.getChestList()) {
				exerciseNames.getItems().add(new MenuItem(exercise.getExerciseName()));
			}

			for (MenuItem item : exerciseNames.getItems()) {
				item.setOnAction(f -> {
					exerciseNames.setText(item.getText());
					if (box.getChildren().size() == 2) {
						box.getChildren().remove(1);
					}
					box.getChildren().add(createGraph(item.getText()));

				});

			}
		});

		legs.setOnAction(e -> {
			exerciseTypes.setText(legs.getText());

			exerciseNames.getItems().clear();

			for (Exercise exercise : list.getLegsList()) {
				exerciseNames.getItems().add(new MenuItem(exercise.getExerciseName()));
			}

			for (MenuItem item : exerciseNames.getItems()) {
				item.setOnAction(f -> {
					exerciseNames.setText(item.getText());
					if (box.getChildren().size() == 2) {
						box.getChildren().remove(1);
					}
					box.getChildren().add(createGraph(item.getText()));

				});

			}
		});

		shoulders.setOnAction(e -> {
			exerciseTypes.setText(shoulders.getText());

			exerciseNames.getItems().clear();

			for (Exercise exercise : list.getShouldersList()) {
				exerciseNames.getItems().add(new MenuItem(exercise.getExerciseName()));
			}

			for (MenuItem item : exerciseNames.getItems()) {
				item.setOnAction(f -> {
					exerciseNames.setText(item.getText());
					if (box.getChildren().size() == 2) {
						box.getChildren().remove(1);
					}
					box.getChildren().add(createGraph(item.getText()));

				});

			}
		});

		exercises.getChildren().addAll(exerciseTypes, exerciseNames);

		this.pane.setCenter(box);

	}

	public LineChart<String, Number> createGraph(String name) {
		double maxWeight = 0;
		double totalMax = 0;
		double minWeight = 1000;

		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("Max Weight Per Workout");

		for (Workout Workout : this.tracker.getWorkoutList()) {
			for (Exercise current : Workout.getExerciseArrayList()) {
				if (current.getExerciseName().equals(name)) {
					for (Set set : current.getSetList()) {
						if (set.getWeight() < minWeight) {
							minWeight = set.getWeight();
						}

						if (set.getWeight() > maxWeight) {
							maxWeight = set.getWeight();
						}

						if (set.getWeight() > totalMax) {
							totalMax = set.getWeight();
						}
					}

					series.getData()
							.add(new XYChart.Data(Workout.getStartTime().toString().substring(0, 10), maxWeight));

					maxWeight = 0;
				}
			}
		}

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis(minWeight - 10, totalMax + 10, 5);
		xAxis.setLabel("Date");
		yAxis.setLabel("Weight (lbs");

		final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle(name);

		lineChart.getData().add(series);

		return lineChart;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
