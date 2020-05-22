package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

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
public class ExerciseList  {

	ArrayList<Exercise> backList;
	ArrayList<Exercise> shouldersList;
	ArrayList<Exercise> chestList;
	ArrayList<Exercise> legsList;
	
	static final int BACK = 1;
	static final int CHEST = 2;
	static final int LEGS = 3;
	static final int SHOULDERS = 4;

	public ExerciseList() {
		backList = new ArrayList<Exercise>();
		shouldersList = new ArrayList<>();
		chestList = new ArrayList<>();
		legsList = new ArrayList<>();

	}

	/**
	 * This method adds an exercise to the repository. Checks to make sure that the
	 * same exercise isn't being added multiple times
	 * 
	 * @param exercise
	 * @return specified exercise list
	 */
	public ArrayList<Exercise> addExercise(Exercise exercise) {

		// Checks to see if the backList contains the exercise already
		if (exercise.getExerciseType().equals("Back")) {
			for (Exercise current : this.backList) {
				if (current.getExerciseName().equals(exercise.getExerciseName())) {
					System.out.println("Exercise already exists");
					return this.backList;
				}
			}

			this.backList.add(exercise);
			return this.backList;

		}

		// Checks to see if the chestList contains the exercise already
		if (exercise.getExerciseType().equals("Chest")) {
			for (Exercise current : this.chestList) {
				if (current.getExerciseName().equals(exercise.getExerciseName())) {
					System.out.println("Exercise already exists");
					return this.chestList;
				}
			}

			this.chestList.add(exercise);
			return this.chestList;

		}

		// Checks to see if the legsList contains the exercise already
		if (exercise.getExerciseType().equals("Legs")) {
			for (Exercise current : this.legsList) {
				if (current.getExerciseName().equals(exercise.getExerciseName())) {
					System.out.println("Exercise already exists");
					return this.legsList;
				}
			}

			this.legsList.add(exercise);
			return this.legsList;

		}

		// Checks to see if the shouldersList contains the exercise already
		if (exercise.getExerciseType().equals("Shoulders")) {
			for (Exercise current : this.shouldersList) {
				if (current.getExerciseName().equals(exercise.getExerciseName())) {
					System.out.println("Exercise already exists");
					return this.shouldersList;
				}
			}

			this.shouldersList.add(exercise);
			return this.shouldersList;

		}

		return null;

	}

	/**
	 * Removes the specified exercise from the repository
	 * 
	 * @param exercise
	 * @return specified exercise ArrayList
	 */
	public ArrayList<Exercise> removeExercise(Exercise exercise) {

		if (exercise.getExerciseType().equals("Shoulders")) {
			for (Exercise current : this.shouldersList) {
				if (current.getExerciseName().equals(exercise.getExerciseName())) {
					this.shouldersList.remove(current);
					System.out.println(current.getExerciseName() + " has been removed.");
					return this.shouldersList;
				}
			}
		}

		if (exercise.getExerciseType().equals("Legs")) {
			for (Exercise current : this.legsList) {
				if (current.getExerciseName().equals(exercise.getExerciseName())) {
					this.legsList.remove(current);
					System.out.println(current.getExerciseName() + " has been removed.");
					return this.legsList;
				}
			}
		}

		if (exercise.getExerciseType().equals("Chest")) {
			for (Exercise current : this.chestList) {
				if (current.getExerciseName().equals(exercise.getExerciseName())) {
					this.chestList.remove(current);
					System.out.println(current.getExerciseName() + " has been removed.");
					return this.chestList;
				}
			}
		}

		if (exercise.getExerciseType().equals("Back")) {
			for (Exercise current : this.backList) {
				if (current.getExerciseName().equals(exercise.getExerciseName())) {
					this.backList.remove(current);
					System.out.println(current.getExerciseName() + " has been removed.");
					return this.backList;
				}
			}
		}

		return null;
	}

	/**
	 * Saves the exercise list to the specified document so that it can be used at a
	 * later time by the user.
	 * 
	 * @param fileName
	 */
	public void saveExerciseList(String fileName) {

		try {
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");

			writer.println("Back Exercises:");
			for (Exercise exercise : this.backList) {
				writer.println("    " + exercise.exerciseName);
			}

			writer.println("\nChest Exercises:");

			for (Exercise exercise : this.chestList) {
				writer.println("    " + exercise.exerciseName);
			}

			writer.println("\nLeg Exercises:");

			for (Exercise exercise : this.legsList) {
				writer.println("    " + exercise.exerciseName);
			}

			writer.println("\nShoulder Exercises:");

			for (Exercise exercise : this.shouldersList) {
				writer.println("    " + exercise.exerciseName);
			}

			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("File Name not valid");
			return;
		}

	}

	/**
	 * Loads the exercise list so that it can be used by the user.
	 * 
	 * @param fileName
	 */
	public void loadExerciseList(String fileName) {

		File file = new File(fileName);
		Scanner scanner;

		try {
			scanner = new Scanner(file);

			if (scanner.hasNextLine() == false) {
				createStandardList();
				scanner.close();
				return;
			}

			String current = scanner.nextLine();

			while (!current.isEmpty()) {
				current = scanner.nextLine();
				if (!current.equals("Chest Exercises:") && !current.isEmpty()) {
					this.backList.add(new Exercise(current.trim(), "Back"));
				}
			}
			
			current = scanner.nextLine();

			while (!current.isEmpty()) {
				current = scanner.nextLine();
				if (!current.equals("Leg Exercises:")  && !current.isEmpty()) {
					this.chestList.add(new Exercise(current.trim(), "Chest"));
				}
			}
			
			current = scanner.nextLine();

			while (!current.isEmpty()) {
				current = scanner.nextLine();
				if (!current.equals("Shoulder Exercises:") && !current.isEmpty()) {
					this.legsList.add(new Exercise(current.trim(), "Legs"));
				}
			}
			
			current = scanner.nextLine();

			while (scanner.hasNextLine()) {
				current = scanner.nextLine();
				this.shouldersList.add(new Exercise(current.trim(), "Shoulders"));

			}
			
			scanner.close();

		} catch (FileNotFoundException e) {
			createStandardList();
		}

	}

	/**
	 * Creates a standard list for the ExerciseList object in case one doesn't
	 * already exist.
	 */
	public void createStandardList() {

		this.backList.add(new Exercise("Pull Up", "Back"));
		this.backList.add(new Exercise("Barbell Row", "Back"));
		this.backList.add(new Exercise("T-Bar Row", "Back"));
		this.backList.add(new Exercise("Straight Arm Cable Pushdown", "Back"));
		this.backList.add(new Exercise("This is the standard list", "Back"));

		this.chestList.add(new Exercise("Flat Barbell Bench Press", "Chest"));
		this.chestList.add(new Exercise("Flat Dumbell Flies", "Chest"));
		this.chestList.add(new Exercise("Incline Barbell Bench Press", "Chest"));
		this.chestList.add(new Exercise("Flat Cable Flies", "Chest"));

		this.legsList.add(new Exercise("Barbell Squat", "Legs"));
		this.legsList.add(new Exercise("Good Mornings", "Legs"));
		this.legsList.add(new Exercise("Dumbell Lunget", "Legs"));
		this.legsList.add(new Exercise("Barbell Calf Raise", "Legs"));

		this.shouldersList.add(new Exercise("Overhead Press", "Shoulders"));
		this.shouldersList.add(new Exercise("Lateral Dumbell Raise", "Shoulders"));
		this.shouldersList.add(new Exercise("Front Dumbell Raise", "Shoulders"));

	}

	/**
	 * Prints out all the items in every Exercise ArrayList
	 * 
	 * @param selection
	 * @return output
	 */
	public String printExerciseList(int selection) {
		String output = "";
		int i = 1;

		if (selection == ExerciseList.BACK) {
			for (Exercise exercise : this.backList) {
				output += i + ": " + exercise.exerciseName + "\n";
				i++;
			}
		} else if (selection == ExerciseList.CHEST) {
			for (Exercise exercise : this.chestList) {
				output += i + ": " + exercise.exerciseName + "\n";
				i++;
			}
		} else if (selection == ExerciseList.LEGS) {
			for (Exercise exercise : this.legsList) {
				output += i + ": " + exercise.exerciseName + "\n";
				i++;
			}
		} else if (selection == ExerciseList.SHOULDERS) {
			for (Exercise exercise : this.shouldersList) {
				if (i == this.shouldersList.size()) {
					output += i + ": " + exercise.exerciseName;
				} else {
					output += i + ": " + exercise.exerciseName + "\n";
					i++;
				}
			}
		}

		return output;
	}
	
	public int compareTo(Exercise first, Exercise second) {
		return first.exerciseName.compareTo(second.exerciseName);
	}
	
	public ArrayList<Exercise> getBackList() {
		return this.backList;
	}
	
	public ArrayList<Exercise> getChestList() {
		return this.chestList;
	}
	
	public ArrayList<Exercise> getShouldersList() {
		return this.shouldersList;
	}
	
	public ArrayList<Exercise> getLegsList() {
		return this.legsList;
	}
	
	public ObservableList<Exercise> getBackExercises() {
		ObservableList<Exercise> exercises = FXCollections.observableArrayList();

		for (Exercise exercise : this.backList) {
			exercises.add(exercise);
		}

		return exercises;
	}

	public ObservableList<Exercise> getChestExercises() {
		ObservableList<Exercise> exercises = FXCollections.observableArrayList();

		for (Exercise exercise : this.chestList) {
			exercises.add(exercise);
		}

		return exercises;
	}

	public ObservableList<Exercise> getShouldersExercises() {
		ObservableList<Exercise> exercises = FXCollections.observableArrayList();

		for (Exercise exercise : this.shouldersList) {
			exercises.add(exercise);
		}

		return exercises;
	}

	public ObservableList<Exercise> getLegsExercises() {
		ObservableList<Exercise> exercises = FXCollections.observableArrayList();

		for (Exercise exercise : this.legsList) {
			exercises.add(exercise);
		}

		return exercises;
	}

	public ObservableList<Exercise> getTypeExercises() {
		ObservableList<Exercise> exercises = FXCollections.observableArrayList();

		exercises.add(new Exercise("", "Back"));
		exercises.add(new Exercise("", "Legs"));
		exercises.add(new Exercise("", "Shoulders"));
		exercises.add(new Exercise("", "Chest"));

		return exercises;
	}



}
