package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class creates multiple ArrayLists for the different types of body parts
 * that can be utilized during a workout session. The user will be able to look
 * at and select the specific exercise that they want from these lists. The user
 * can also add their own exercises into these lists, and can remove exercises
 * as well should the need arise.
 * 
 * @author Brenton Haliw
 *
 */
public class ExerciseList {

	private HashMap<String, Exercise> exerciseList;

	public static final int ABS = 1;
	public static final int BACK = 2;
	public static final int BICEPS = 3;
	public static final int CARDIO = 4;
	public static final int CHEST = 5;
	public static final int LEGS = 6;
	public static final int SHOULDERS = 7;
	public static final int TRICEPS = 8;

	/**
	 * Default constructor for the ExerciseList class. Initializes HashMap.
	 */
	public ExerciseList() {
		this.exerciseList = new HashMap<>();
	}

	/**
	 * This method adds an exercise to the repository. Checks to make sure that the
	 * same exercise isn't being added multiple times.
	 * 
	 * @param exercise
	 */
	public void addExercise(Exercise exercise) {
		if (this.exerciseList.containsKey(exercise.getExerciseName())) {
			System.out.println("Exercise already exists in the database.");
		} else {
			this.exerciseList.put(exercise.getExerciseName(), exercise);
			System.out.println(exercise.getExerciseName() + " has been added to the database.");
		}
	}

	/**
	 * Removes the specified exercise from the repository.
	 * 
	 * @param exercise
	 */
	public void removeExercise(Exercise exercise) {
		if (this.exerciseList.containsKey(exercise.getExerciseName())) {
			this.exerciseList.remove(exercise.getExerciseName());
			System.out.println(exercise.getExerciseName() + " has been removed from the database.");
		} else {
			System.out.println("Exercise does not exist in the database.");
		}
	}

	/**
	 * Saves the exercise list to the specified document so that it can be used at a
	 * later time by the user. Will save as plain text to a text document.
	 * 
	 * @param fileName
	 */
	public void saveExerciseList(String fileName) {

		try {
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");

			writer.println("Exercise Type: Abs");
			saveExerciseListAux(writer, "Abs");

			writer.println("\nExercise Type: Back");
			saveExerciseListAux(writer, "Back");

			writer.println("\nExercise Type: Biceps");
			saveExerciseListAux(writer, "Biceps");

			writer.println("\nExercise Type: Cardio");
			saveExerciseListAux(writer, "Cardio");

			writer.println("\nExercise Type: Chest");
			saveExerciseListAux(writer, "Chest");

			writer.println("\nExercise Type: Legs");
			saveExerciseListAux(writer, "Legs");

			writer.println("\nExercise Type: Shoulders");
			saveExerciseListAux(writer, "Shoulders");

			writer.println("\nExercise Type: Triceps");
			saveExerciseListAux(writer, "Triceps");

			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("File Name not valid");
			return;
		}

	}

	private void saveExerciseListAux(PrintWriter writer, String exerciseType) {
		ArrayList<Exercise> list = hashmapToArrayList(exerciseType);
		
		for (Exercise exercise : list) {
			writer.println("    " + exercise.getExerciseName());
		}
	}

	/**
	 * Loads the exercise list so that it can be used by the user. Loads from a
	 * plain text document.
	 * 
	 * @param fileName
	 */
	public void loadExerciseList(String fileName) {

		File file = new File(fileName);
		Scanner scanner;

		try {
			scanner = new Scanner(file);

			if (scanner.hasNextLine() == false) {
				createStandardList(); // File was empty so create a standard exercise List
				scanner.close();
				return;
			}

			String current = "";
			String exerciseType = "";

			while (scanner.hasNextLine()) {
				current = scanner.nextLine();

				if (current.contains("Exercise Type: ")) {
					exerciseType = current.substring(15);
				} else if (!current.isEmpty()) {			
					this.exerciseList.put(current.trim(), new Exercise(current.trim(), exerciseType));
				}
			}

			scanner.close();

		} catch (FileNotFoundException e) {
			System.out.println("Was not able to read the file containing the Exercise List");
			System.out.println("The standard Exercise List will be generated instead");
			createStandardList();
		}

	}

	/**
	 * Creates a standard list for the ExerciseList object in case one doesn't
	 * already exist.
	 */
	public void createStandardList() {

		////////////// ABS EXERCISES //////////////
		this.exerciseList.put("Crunch", new Exercise("Crunch", "Abs"));
		this.exerciseList.put("Hanging Knee Raise", new Exercise("Hanging Knee Raise", "Abs"));
		this.exerciseList.put("Russian Twists", new Exercise("Russian Twists", "Abs"));
		this.exerciseList.put("Hanging Leg Raise", new Exercise("Hanging Leg Raise", "Abs"));
		this.exerciseList.put("Sit Up", new Exercise("Sit Up", "Abs"));
		this.exerciseList.put("Plank", new Exercise("Plank", "Abs"));

		////////////// BACK EXERCISES //////////////
		this.exerciseList.put("Pull Up", new Exercise("Pull Up", "Back"));
		this.exerciseList.put("Chin Up", new Exercise("Chin Up", "Back"));
		this.exerciseList.put("Barbell Row", new Exercise("Barbell Row", "Back"));
		this.exerciseList.put("T-Bar Row", new Exercise("T-Bar Row", "Back"));
		this.exerciseList.put("Straight Arm Cable Pushdown", new Exercise("Straight Arm Cable Pushdown", "Back"));
		this.exerciseList.put("Barbell Deadlift", new Exercise("Barbell Deadlift", "Back"));
		this.exerciseList.put("Trapbar Deadlift", new Exercise("Trapbar Deadlift", "Back"));
		this.exerciseList.put("Lat Pulldown", new Exercise("Lat Pulldown", "Back"));
		this.exerciseList.put("Dumbell Shrug", new Exercise("Dumbell Shrug", "Back"));
		this.exerciseList.put("Barbell Shrug", new Exercise("Barbell Shrug", "Back"));
		this.exerciseList.put("Cable Row", new Exercise("Cable Row", "Back"));

		////////////// BICEPS EXERCISES //////////////
		this.exerciseList.put("Dumbell Curl", new Exercise("Dumbell Curl", "Biceps"));
		this.exerciseList.put("Cable Curl", new Exercise("Cable Curl", "Biceps"));
		this.exerciseList.put("Hammer Curl", new Exercise("Hammer Curl", "Biceps"));
		this.exerciseList.put("Barbell Curl", new Exercise("Barbell Curl", "Biceps"));

		////////////// CARDIO EXERCISES //////////////
		this.exerciseList.put("Treadmill Running", new Exercise("Treadmill Running", "Cardio"));
		this.exerciseList.put("Treadmill Walking", new Exercise("Treadmill Walking", "Cardio"));
		this.exerciseList.put("Outside Running", new Exercise("Outside Running", "Cardio"));
		this.exerciseList.put("Outside Walking", new Exercise("Outside Walking", "Cardio"));
		this.exerciseList.put("Elliptical Trainer", new Exercise("Elliptical Trainer", "Cardio"));
		this.exerciseList.put("Outside Biking", new Exercise("Outside Biking", "Cardio"));
		this.exerciseList.put("Stationary Biking", new Exercise("Stationary Biking", "Cardio"));
		this.exerciseList.put("Stair Stepper", new Exercise("Stair Stepper", "Cardio"));

		////////////// CHEST EXERCISES //////////////
		this.exerciseList.put("Flat Barbell Bench Press", new Exercise("Flat Barbell Bench Press", "Chest"));
		this.exerciseList.put("Flat Dumbell Flies", new Exercise("Flat Dumbell Flies", "Chest"));
		this.exerciseList.put("Incline Barbell Bench Press", new Exercise("Incline Barbell Bench Press", "Chest"));
		this.exerciseList.put("Flat Cable Flies", new Exercise("Flat Cable Flies", "Chest"));
		this.exerciseList.put("Incline Cable Flies", new Exercise("Incline Cable Flies", "Chest"));
		this.exerciseList.put("High Cable Crossover", new Exercise("High Cable Crossover", "Chest"));

		////////////// LEGS EXERCISES //////////////
		this.exerciseList.put("Barbell Squat", new Exercise("Barbell Squat", "Legs"));
		this.exerciseList.put("Good Mornings", new Exercise("Good Mornings", "Legs"));
		this.exerciseList.put("Dumbell Lunge", new Exercise("Dumbell Lunge", "Legs"));
		this.exerciseList.put("Barbell Calf Raise", new Exercise("Barbell Calf Raise", "Legs"));
		this.exerciseList.put("Cable Hip Abduction", new Exercise("Cable Hip Abduction", "Legs"));
		this.exerciseList.put("Cable Hip Adduction", new Exercise("Cable Hip Adduction", "Legs"));
		this.exerciseList.put("Cable Hip Thrust", new Exercise("Cable Hip Thrust", "Legs"));
		this.exerciseList.put("Romanian Deadlift", new Exercise("Romanian Deadlift", "Legs"));

		////////////// SHOULDERS EXERCISES //////////////
		this.exerciseList.put("Overhead Press", new Exercise("Overhead Press", "Shoulders"));
		this.exerciseList.put("Lateral Dumbell Raise", new Exercise("Lateral Dumbell Raise", "Shoulders"));
		this.exerciseList.put("Front Dumbell Raise", new Exercise("Front Dumbell Raise", "Shoulders"));
		this.exerciseList.put("Cable Lateral Raise", new Exercise("Cable Lateral Raise", "Shoulders"));
		this.exerciseList.put("Dumbell Shoulder Press", new Exercise("Dumbell Shoulder Press", "Shoulders"));
		this.exerciseList.put("Cable Face Pull", new Exercise("Cable Face Pull", "Shoulders"));

		////////////// TRICEPS EXERCISES //////////////
		this.exerciseList.put("Overhead Cable Extension", new Exercise("Overhead Cable Extension", "Triceps"));
		this.exerciseList.put("Cable Rope Pushdown", new Exercise("Cable Rope Pushdown", "Triceps"));
		this.exerciseList.put("Dumbell Skullcrushers", new Exercise("Dumbell Skullcrushers", "Triceps"));
	}

	/**
	 * Prints out all the items in every Exercise ArrayList
	 * 
	 * @param selection
	 * @return output
	 */
	public String printExerciseList(int selection) {
		String output = "";

		if (selection == ExerciseList.ABS) {
			output = printExerciseListAux(getAbsList());
		} else if (selection == ExerciseList.BACK) {
			output = printExerciseListAux(getBackList());
		} else if (selection == ExerciseList.BICEPS) {
			output = printExerciseListAux(getBicepsList());
		} else if (selection == ExerciseList.CARDIO) {
			output = printExerciseListAux(getCardioList());
		} else if (selection == ExerciseList.CHEST) {
			output = printExerciseListAux(getChestList());
		} else if (selection == ExerciseList.LEGS) {
			output = printExerciseListAux(getLegsList());
		} else if (selection == ExerciseList.SHOULDERS) {
			output = printExerciseListAux(getShouldersList());
		} else if (selection == ExerciseList.TRICEPS) {
			output = printExerciseListAux(getTricepsList());
		}

		return output;
	}

	/**
	 * Helper method for the printExerciseList method
	 * 
	 * @param list
	 * @return output
	 */
	private String printExerciseListAux(ArrayList<Exercise> list) {
		String output = "";
		int i = 1;

		for (Exercise exercise : list) {
			if (i == list.size()) {
				output += i + ": " + exercise.getExerciseName();
			} else {
				output += i + ": " + exercise.getExerciseName() + "\n";
				i++;
			}
		}

		return output;
	}
	
	public ArrayList<Exercise> hashmapToArrayList(String exerciseType) {
		ArrayList<Exercise> list = new ArrayList<>();
		
		for (Exercise exercise : exerciseList.values()) {
			if (exercise.getExerciseType().equals(exerciseType)) {
				list.add(exercise);
			}
		}
		
		Collections.sort(list);
		
		return list;
	}

	//////// GETTERS FOR ARRAYLISTS ////////
	public ArrayList<Exercise> getAbsList() {
		return hashmapToArrayList("Abs");
	}

	public ArrayList<Exercise> getBackList() {
		return hashmapToArrayList("Back");
	}

	public ArrayList<Exercise> getBicepsList() {
		return hashmapToArrayList("Biceps");
	}

	public ArrayList<Exercise> getCardioList() {
		return hashmapToArrayList("Cardio");
	}

	public ArrayList<Exercise> getChestList() {
		return hashmapToArrayList("Chest");
	}

	public ArrayList<Exercise> getLegsList() {
		return hashmapToArrayList("Legs");
	}

	public ArrayList<Exercise> getShouldersList() {
		return hashmapToArrayList("Shoulders");
	}

	public ArrayList<Exercise> getTricepsList() {
		return hashmapToArrayList("Triceps");
	}

	//////// OBSERVABLE LISTS FOR JAVAFX GUI ////////

	/**
	 * Returns an ObservableList object for abs exercises to be used in JavaFX
	 * applications.
	 * 
	 * @return absList
	 */
	public ObservableList<Exercise> getAbsExercises() {
		return addObservableListAux(getAbsList());
	}

	/**
	 * Returns an ObservableList object for back exercises to be used in JavaFX
	 * applications.
	 * 
	 * @return backList
	 */
	public ObservableList<Exercise> getBackExercises() {
		return addObservableListAux(getBackList());
	}

	/**
	 * Returns an ObservableList object for biceps exercises to be used in JavaFX
	 * applications.
	 * 
	 * @return bicepsList
	 */
	public ObservableList<Exercise> getBicepsExercises() {
		return addObservableListAux(getBicepsList());
	}

	/**
	 * Returns an ObservableList object for cardio exercises to be used in JavaFX
	 * applications.
	 * 
	 * @return cardioList
	 */
	public ObservableList<Exercise> getCardioExercises() {
		return addObservableListAux(getCardioList());
	}

	/**
	 * Returns an ObservableList object for chest exercises to be used in JavaFX
	 * applications.
	 * 
	 * @return chestList
	 */
	public ObservableList<Exercise> getChestExercises() {
		return addObservableListAux(getChestList());
	}

	/**
	 * Returns an ObservableList object for legs exercises to be used in JavaFX
	 * applications.
	 * 
	 * @return legsList
	 */
	public ObservableList<Exercise> getLegsExercises() {
		return addObservableListAux(getLegsList());
	}

	/**
	 * Returns an ObservableList object for shoulders exercises to be used in JavaFX
	 * applications.
	 * 
	 * @return shouldersList
	 */
	public ObservableList<Exercise> getShouldersExercises() {
		return addObservableListAux(getShouldersList());
	}

	/**
	 * Returns an ObservableList object for triceps exercises to be used in JavaFX
	 * applications.
	 * 
	 * @return tricepsList
	 */
	public ObservableList<Exercise> getTricepsExercises() {
		return addObservableListAux(getTricepsList());
	}

	/**
	 * Helper method to add ArrayList objects to an ObservableList object
	 * 
	 * @param list
	 * @return ObservableList object
	 */
	private ObservableList<Exercise> addObservableListAux(ArrayList<Exercise> list) {
		ObservableList<Exercise> exercises = FXCollections.observableArrayList();
		for (Exercise exercise : list) {
			exercises.add(exercise);
		}

		return exercises;
	}

	/**
	 * Creates and returns an ObservableList object with different types of
	 * exercises
	 * 
	 * @return ObservableList object
	 */
	public ObservableList<Exercise> getTypeExercises() {
		ObservableList<Exercise> exercises = FXCollections.observableArrayList();

		exercises.add(new Exercise("", "Abs"));
		exercises.add(new Exercise("", "Back"));
		exercises.add(new Exercise("", "Biceps"));
		exercises.add(new Exercise("", "Cardio"));
		exercises.add(new Exercise("", "Chest"));
		exercises.add(new Exercise("", "Legs"));
		exercises.add(new Exercise("", "Shoulders"));
		exercises.add(new Exercise("", "Triceps"));

		return exercises;
	}

}
