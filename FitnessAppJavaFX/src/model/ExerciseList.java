package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
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
public class ExerciseList  {

	private ArrayList<Exercise> absList;
	private ArrayList<Exercise> backList;
	private ArrayList<Exercise> bicepsList;
	private ArrayList<Exercise> cardioList;
	private ArrayList<Exercise> chestList;
	private ArrayList<Exercise> legsList;
	private ArrayList<Exercise> shouldersList;
	private ArrayList<Exercise> tricepsList;

	static final int ABS = 1;
	static final int BACK = 2;
	static final int BICEPS = 3;
	static final int CARDIO = 4;
	static final int CHEST = 5;
	static final int LEGS = 6;
	static final int SHOULDERS = 7;
	static final int TRICEPS = 8;

	

	/**
	 * Default constructor for the ExerciseList class. Initializes all of
	 * the ArrayList objects to track exercises for different muscle groups.
	 */
	public ExerciseList() {
		absList = new ArrayList<>();
		backList = new ArrayList<>();
		bicepsList = new ArrayList<>();
		cardioList = new ArrayList<>();
		chestList = new ArrayList<>();
		legsList = new ArrayList<>();
		shouldersList = new ArrayList<>();
		tricepsList = new ArrayList<>();
	}

	/**
	 * This method adds an exercise to the repository. Checks to make sure that the
	 * same exercise isn't being added multiple times
	 * 
	 * @param exercise
	 * @return specified exercise list
	 */
	public ArrayList<Exercise> addExercise(Exercise exercise) {

		// CHECK ABS LIST
		if (exercise.getExerciseType().equals("Abs")) {
			addExerciseAux(exercise, this.absList);
			return this.absList;
		}
		
		// CHECK BACK LIST
		if (exercise.getExerciseType().equals("Back")) {		
			addExerciseAux(exercise, this.backList);
			return this.backList;
		}
		
		// CHECK BICEPS LIST
		if (exercise.getExerciseType().equals("Biceps")) {
			addExerciseAux(exercise, this.bicepsList);
			return this.bicepsList;
		}
		
		// CHECK CARDIO LIST
		if (exercise.getExerciseType().equals("Cardio")) {
			addExerciseAux(exercise, this.cardioList);
			return this.cardioList;
		}

		// CHECK CHEST LIST
		if (exercise.getExerciseType().equals("Chest")) {
			addExerciseAux(exercise, this.chestList);
			return this.chestList;
		}

		// CHECK LEGS LIST
		if (exercise.getExerciseType().equals("Legs")) {
			addExerciseAux(exercise, this.legsList);
			return this.legsList;
		}

		// CHECK SHOULDERS LIST
		if (exercise.getExerciseType().equals("Shoulders")) {
			addExerciseAux(exercise, this.shouldersList);
			return this.shouldersList;
		}
		
		
		// CHECK TRICEPS LIST
		if (exercise.getExerciseType().equals("Triceps")) {
			addExerciseAux(exercise, this.tricepsList);
			return this.tricepsList;
		}

		return null;

	}
	
	/**
	 * This method checks the specified exercise list to see if an exercise
	 * that is trying to be added already exists. Returns true if exercise
	 * is present and false if exercise of that name is present.
	 * @param exerciseType
	 * @param list
	 */
	private void addExerciseAux(Exercise exercise, ArrayList<Exercise> list) {
		
		for (Exercise current : list) {
			if (current.getExerciseName().equals(exercise.getExerciseName())) {
				System.out.println("Exercise already exists");
				return;
			}
		}
		
		list.add(exercise);
		Collections.sort(list);
		System.out.println("" + exercise.getExerciseName() + " successfully added!");

	}

	/**
	 * Removes the specified exercise from the repository
	 * 
	 * @param exercise
	 * @return specified exercise ArrayList
	 */
	public ArrayList<Exercise> removeExercise(Exercise exercise) {


		if (exercise.getExerciseType().equals("Abs")) {
			removeExerciseAux(exercise, absList);
			return this.absList;
		}
		
		if (exercise.getExerciseType().equals("Back")) {
			removeExerciseAux(exercise, backList);
			return this.backList;
		}
		
		if (exercise.getExerciseType().equals("Biceps")) {
			removeExerciseAux(exercise, bicepsList);
			return this.bicepsList;
		}
		
		if (exercise.getExerciseType().equals("Cardio")) {
			removeExerciseAux(exercise, cardioList);
			return this.cardioList;
		}
		
		if (exercise.getExerciseType().equals("Chest")) {
			removeExerciseAux(exercise, chestList);
			return this.chestList;
		}	
		
		if (exercise.getExerciseType().equals("Legs")) {
			removeExerciseAux(exercise, legsList);
			return this.legsList;
		}
		
		if (exercise.getExerciseType().equals("Shoulders")) {
			removeExerciseAux(exercise, shouldersList);
			return this.shouldersList;
		}

		if (exercise.getExerciseType().equals("Triceps")) {
			removeExerciseAux(exercise, tricepsList);
			return this.tricepsList;
		}

		return null;
	}
	
	private void removeExerciseAux(Exercise exercise, ArrayList<Exercise> list) {
		for (Exercise current : list) {
			if (current.getExerciseName().equals(exercise.getExerciseName())) {
				list.remove(current);
				System.out.println(current.getExerciseName() + " has been removed.");
				return;
			}
		}
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

			writer.println("Exercise Type: Abs");
			saveExerciseListAux(writer, absList);
			
			writer.println("\nExercise Type: Back");
			saveExerciseListAux(writer, backList);
			
			writer.println("\nExercise Type: Biceps");
			saveExerciseListAux(writer, bicepsList);
			
			writer.println("\nExercise Type: Cardio");
			saveExerciseListAux(writer, cardioList);

			writer.println("\nExercise Type: Chest");
			saveExerciseListAux(writer, chestList);

			writer.println("\nExercise Type: Legs");
			saveExerciseListAux(writer, legsList);

			writer.println("\nExercise Type: Shoulders");
			saveExerciseListAux(writer, shouldersList);
			
			writer.println("\nExercise Type: Triceps");
			saveExerciseListAux(writer, tricepsList);

			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("File Name not valid");
			return;
		}

	}
	
	private void saveExerciseListAux(PrintWriter writer, ArrayList<Exercise> list) {
		for (Exercise exercise : list) {
			writer.println("    " + exercise.getExerciseName());
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
				createStandardList(); // File was empty so create a standard exercise List
				scanner.close();
				return;
			}

			String current = "";
			String exerciseType = "";
			
			while(scanner.hasNextLine()) {
				current = scanner.nextLine();
				
				if (current.contains("Exercise Type: ")) {
					exerciseType = current.substring(15);
				} else if (!current.isEmpty()) {
					
					if (exerciseType.equals("Abs")) {
						this.absList.add(new Exercise(current.trim(), exerciseType));
					} else if (exerciseType.equals("Back")) {
						this.backList.add(new Exercise(current.trim(), exerciseType));
					} else if (exerciseType.equals("Biceps")) {
						this.bicepsList.add(new Exercise(current.trim(), exerciseType));
					} else if (exerciseType.equals("Cardio")) {
						this.cardioList.add(new Exercise(current.trim(), exerciseType));
					} else if (exerciseType.equals("Chest")) {
						this.chestList.add(new Exercise(current.trim(), exerciseType));
					} else if (exerciseType.equals("Legs")) {
						this.legsList.add(new Exercise(current.trim(), exerciseType));
					} else if (exerciseType.equals("Shoulders")) {
						this.shouldersList.add(new Exercise(current.trim(), exerciseType));
					} else if (exerciseType.equals("Triceps")) {
						this.tricepsList.add(new Exercise(current.trim(), exerciseType));
					}
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
		this.absList.add(new Exercise("Crunch", "Abs"));
		this.absList.add(new Exercise("Hanging Knee Raise", "Abs"));
		this.absList.add(new Exercise("Russian Twists", "Abs"));
		this.absList.add(new Exercise("Hanging Leg Raise", "Abs"));
		this.absList.add(new Exercise("Sit Up", "Abs"));
		this.absList.add(new Exercise("Plank", "Abs"));
		
		////////////// BACK EXERCISES //////////////
		this.backList.add(new Exercise("Pull Up", "Back"));
		this.backList.add(new Exercise("Chin Up", "Back"));
		this.backList.add(new Exercise("Barbell Row", "Back"));
		this.backList.add(new Exercise("T-Bar Row", "Back"));
		this.backList.add(new Exercise("Straight Arm Cable Pushdown", "Back"));
		this.backList.add(new Exercise("Barbell Deadlift", "Back"));
		this.backList.add(new Exercise("Trapbar Deadlift", "Back"));
		this.backList.add(new Exercise("Lat Pulldown", "Back"));
		this.backList.add(new Exercise("Dumbell Shrug", "Back"));
		this.backList.add(new Exercise("Barbell Shrug", "Back"));
		this.backList.add(new Exercise("Cable Row", "Back"));
		
		////////////// BICEPS EXERCISES //////////////
		this.bicepsList.add(new Exercise("Dumbell Curl", "Biceps"));
		this.bicepsList.add(new Exercise("Cable Curl", "Biceps"));
		this.bicepsList.add(new Exercise("Hammer Curl", "Biceps"));
		this.bicepsList.add(new Exercise("Barbell Curl", "Biceps"));
		
		////////////// CARDIO EXERCISES //////////////
		this.cardioList.add(new Exercise("Treadmill Running", "Cardio"));
		this.cardioList.add(new Exercise("Treadmill Walking", "Cardio"));
		this.cardioList.add(new Exercise("Outside Running", "Cardio"));
		this.cardioList.add(new Exercise("Outside Walking", "Cardio"));
		this.cardioList.add(new Exercise("Elliptical Trainer", "Cardio"));
		this.cardioList.add(new Exercise("Outside Biking", "Cardio"));
		this.cardioList.add(new Exercise("Stationary Biking", "Cardio"));
		this.cardioList.add(new Exercise("Stair Stepper", "Cardio"));

		////////////// CHEST EXERCISES //////////////
		this.chestList.add(new Exercise("Flat Barbell Bench Press", "Chest"));
		this.chestList.add(new Exercise("Flat Dumbell Flies", "Chest"));
		this.chestList.add(new Exercise("Incline Barbell Bench Press", "Chest"));
		this.chestList.add(new Exercise("Flat Cable Flies", "Chest"));
		this.chestList.add(new Exercise("Incline Cable Flies", "Chest"));
		this.chestList.add(new Exercise("High Cable Crossover", "Chest"));

		////////////// LEGS EXERCISES //////////////
		this.legsList.add(new Exercise("Barbell Squat", "Legs"));
		this.legsList.add(new Exercise("Good Mornings", "Legs"));
		this.legsList.add(new Exercise("Dumbell Lunge", "Legs"));
		this.legsList.add(new Exercise("Barbell Calf Raise", "Legs"));
		this.legsList.add(new Exercise("Cable Hip Abduction", "Legs"));
		this.legsList.add(new Exercise("Cable Hip Adduction", "Legs"));
		this.legsList.add(new Exercise("Cable Hip Thrust", "Legs"));
		this.legsList.add(new Exercise("Romanian Deadlift", "Legs"));
		
		////////////// SHOULDERS EXERCISES //////////////
		this.shouldersList.add(new Exercise("Overhead Press", "Shoulders"));
		this.shouldersList.add(new Exercise("Lateral Dumbell Raise", "Shoulders"));
		this.shouldersList.add(new Exercise("Front Dumbell Raise", "Shoulders"));
		this.shouldersList.add(new Exercise("Cable Lateral Raise", "Shoulders"));
		this.shouldersList.add(new Exercise("Dumbell Shoulder Press", "Shoulders"));
		this.shouldersList.add(new Exercise("Cable Face Pull", "Shoulders"));
		
		////////////// TRICEPS EXERCISES //////////////
		this.tricepsList.add(new Exercise("Overhead Cable Extension", "Triceps"));
		this.tricepsList.add(new Exercise("Cable Rope Pushdown", "Triceps"));
		this.tricepsList.add(new Exercise("Dumbell Skullcrushers", "Triceps"));
		
		///// SORTING THE LISTS BY ALPHABETICAL ORDER /////
		Collections.sort(this.absList);	
		Collections.sort(this.backList);
		Collections.sort(this.bicepsList);	
		Collections.sort(this.cardioList);	
		Collections.sort(this.chestList);
		Collections.sort(this.legsList);
		Collections.sort(this.shouldersList);
		Collections.sort(this.tricepsList);	

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
			output = printExerciseListAux(absList);
		} else if (selection == ExerciseList.BACK) {
			output = printExerciseListAux(backList);
		} else if (selection == ExerciseList.BICEPS) {
			output = printExerciseListAux(bicepsList);
		} else if (selection == ExerciseList.CARDIO) {
			output = printExerciseListAux(cardioList);
		} else if (selection == ExerciseList.CHEST) {
			output = printExerciseListAux(chestList);
		} else if (selection == ExerciseList.LEGS) {
			output = printExerciseListAux(legsList);
		} else if (selection == ExerciseList.SHOULDERS) {
			output = printExerciseListAux(shouldersList);
		} else if (selection == ExerciseList.TRICEPS) {
			output = printExerciseListAux(tricepsList);
		}

		return output;
	}
	
	/**
	 * Helper method for the printExerciseList method
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
	
	//////// GETTERS FOR ARRAYLISTS //////// 
	public ArrayList<Exercise> getAbsList() {
		return this.absList;
	}
	
	public ArrayList<Exercise> getBackList() {
		return this.backList;
	}
	
	public ArrayList<Exercise> getBicepsList() {
		return this.bicepsList;
	}
	
	public ArrayList<Exercise> getCardioList() {
		return this.cardioList;
	}
	
	public ArrayList<Exercise> getChestList() {
		return this.chestList;
	}
	
	public ArrayList<Exercise> getLegsList() {
		return this.legsList;
	}
	
	public ArrayList<Exercise> getShouldersList() {
		return this.shouldersList;
	}
	
	public ArrayList<Exercise> getTricepsList() {
		return this.tricepsList;
	}
	
	
	//////// OBSERVABLE LISTS FOR JAVAFX GUI //////// 
	
	/**
	 * Returns an ObservableList object for abs exercises to
	 * be used in JavaFX applications.
	 * @return absList
	 */
	public ObservableList<Exercise> getAbsExercises() {
		return addObservableListAux(absList);
	}

	/**
	 * Returns an ObservableList object for back exercises to
	 * be used in JavaFX applications.
	 * @return backList
	 */
	public ObservableList<Exercise> getBackExercises() {
		return addObservableListAux(backList);
	}

	/**
	 * Returns an ObservableList object for biceps exercises to
	 * be used in JavaFX applications.
	 * @return bicepsList
	 */
	public ObservableList<Exercise> getBicepsExercises() {
		return addObservableListAux(bicepsList);
	}

	/**
	 * Returns an ObservableList object for cardio exercises to
	 * be used in JavaFX applications.
	 * @return cardioList
	 */
	public ObservableList<Exercise> getCardioExercises() {
		return addObservableListAux(cardioList);
	}
	
	/**
	 * Returns an ObservableList object for chest exercises to
	 * be used in JavaFX applications.
	 * @return chestList
	 */
	public ObservableList<Exercise> getChestExercises() {
		return addObservableListAux(chestList);
	}
	
	/**
	 * Returns an ObservableList object for legs exercises to
	 * be used in JavaFX applications.
	 * @return legsList
	 */
	public ObservableList<Exercise> getLegsExercises() {
		return addObservableListAux(legsList);
	}

	/**
	 * Returns an ObservableList object for shoulders exercises to
	 * be used in JavaFX applications.
	 * @return shouldersList
	 */
	public ObservableList<Exercise> getShouldersExercises() {
		return addObservableListAux(shouldersList);
	}
	
	/**
	 * Returns an ObservableList object for triceps exercises to
	 * be used in JavaFX applications.
	 * @return tricepsList
	 */
	public ObservableList<Exercise> getTricepsExercises() {
		return addObservableListAux(tricepsList);
	}
	
	/**
	 * Helper method to add ArrayList objects to an ObservableList object
	 * @param list
	 * @return ObservableList object
	 */
	private ObservableList<Exercise>  addObservableListAux(ArrayList<Exercise> list) {
		ObservableList<Exercise> exercises = FXCollections.observableArrayList();
		for (Exercise exercise : list) {
			exercises.add(exercise);
		}
		
		return exercises;
	}

	/**
	 * Creates and returns an ObservableList object with different types of exercises
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
