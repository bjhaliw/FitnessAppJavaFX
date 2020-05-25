package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * This class allows the user to keep track of all the workouts that they have
 * completed over a length of time.
 * 
 * @author Brenton Haliw
 *
 */
public class WorkoutTracker {

	ArrayList<Workout> workoutList;
	static boolean currentWorkoutAdded = false;

	/**
	 * Default constructor for the workoutTracker class
	 */
	public WorkoutTracker() {
		workoutList = new ArrayList<>();
	}

	/**
	 * Returns the ArrayList containing workouts
	 * 
	 * @return workoutList
	 */
	public ArrayList<Workout> getWorkoutList() {
		return this.workoutList;
	}

	/**
	 * Adds a workout to the ArrayList containing workouts. Double checks
	 * the workout tracker to ensure that a workout of the same start time
	 * isn't trying to be added multiple times.
	 * 
	 * @param workout
	 * @return workoutList
	 */
	public ArrayList<Workout> addWorkout(Workout workout) {

		for (Workout workoutObject : this.workoutList) {
			if (workoutObject.getStartTime().isEqual(workout.getStartTime())) {
				System.out.println("The workout with Start Time: " + workout.getStartTime().toString()
						+ " has already been added to the tracker.");

				return this.workoutList;
			}
		}

		boolean result = this.workoutList.add(workout);

		if (result) {
			System.out.println("Workout has been added to the tracker");
		} else {
			System.out.println("Adding workout to tracker has failed");
		}

		return this.workoutList;
	}

	/**
	 * Removes the specified workout from the ArrayList containing workouts
	 * 
	 * @param workout
	 * @return workoutList
	 */
	public ArrayList<Workout> removeWorkout(Workout workout) {
		boolean result = this.workoutList.remove(workout);
		
		if (result == false) {
			System.out.println("Failed to remove workout.");
		} else {
			System.out.println("Workout has been removed.");
		}

		return this.workoutList;
	}

	/**
	 * This method is responsible for taking the information that is present in the
	 * ArrayList containing workouts and then saving it to a document to be used for
	 * future use. This way, data that is created by the user will not be lost upon
	 * shutdown of the program.
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void saveWorkoutList(String fileName) throws FileNotFoundException, UnsupportedEncodingException {

		PrintWriter writer = new PrintWriter(fileName, "UTF-8");

		for (Workout workout : this.workoutList) {
			writer.println("Start Time:" + workout.startTime);
			for (Exercise exercise : workout.exerciseArrayList) {
				writer.println("    " + exercise);
				for (Set set : exercise.setList) {
					writer.println("        " + set);
				}
			}
			
			writer.println(); // Adds a blank space in between each workout

		}

		writer.close();

	}

	/**
	 * This method will attempt to open up the given file and load the information
	 * from it into the workoutList.
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void loadWorkoutList(String fileName) throws FileNotFoundException {

		File file = new File(fileName);
		Scanner scanner = new Scanner(file);
		String current;
		int workoutNum = -1;
		int exerciseNum = -1;

		// If document is empty, return back to method call
		if (!scanner.hasNextLine()) {
			scanner.close();
			return;
		}

		// Reads the text file until the end of the document
		while (scanner.hasNextLine()) {

			current = scanner.nextLine();

			// Checks to see if a new workout has been started
			if (current.contains("Start Time:")) {
				workoutNum++;
				exerciseNum = -1;
				current = current.substring(11);
				this.workoutList.add(new Workout());
				this.workoutList.get(workoutNum).startTime = LocalDateTime.parse(current);
			}

			// Checks to see if a new Exercise has been started for the workout
			if (current.contains("Exercise name: ")) {
				int name = current.indexOf("Exercise name: ");
				int type = current.indexOf("Exercise Type: ");
				exerciseNum++;

				this.workoutList.get(workoutNum).addExercise(new Exercise(current.substring(name + 15, type - 1),
						current.substring(type + 15, current.length())));

			}

			// Checks to see if a new Set was started for the exercise
			if (current.contains("Reps: ")) {
				int reps = current.indexOf("Reps:");
				int weight = current.indexOf("Weight:");

				this.workoutList.get(workoutNum).getExercise(exerciseNum)
						.addSet(new Set(Double.parseDouble(current.substring(reps + 6, weight - 1)),
								Double.parseDouble(current.substring(weight + 7, current.length()))));

			}

		}

		scanner.close();
	}
}
