package model;

import java.util.Scanner;

public class WorkoutTrackerController {

	public static void workoutTrackerOptions(WorkoutTracker tracker, ExerciseList list, Scanner scanner) {
		int selection = 0;

		while (selection != 4) {
			System.out.println();
			System.out.println("**** Workout Tracker Menu ****");
			System.out.println("1. View past workouts\n2. Edit a past workout\n" 
								+ "3. View statistics\n4. Return to main menu");
			System.out.print("Enter your selection: ");
			selection = scanner.nextInt();

			switch (selection) {
			case 1:
				viewWorkouts(tracker, scanner);
				break;
			case 2:
				editWorkout(tracker, list, scanner);
				break;
			case 3:
				viewStatisticsOptions(tracker, list, scanner);
				break;
			case 4:
				System.out.println("Returning to main menu");
				break;
			default:
				System.out.println("Please enter valid selection");
				break;
			}
			
			System.out.println();
		}
	}

	/**
	 * Allows for the retrieval of a all the exercises within a certain workout
	 * 
	 * @param tracker
	 * @param scanner
	 * @return workout
	 */
	public static Workout viewWorkouts(WorkoutTracker tracker, Scanner scanner) {
		Workout workout;
		int selection;
		int i = 1;
		int j = 1;
		
		System.out.println();
		System.out.println("**** Past Workouts ****");
		for (Workout current : tracker.workoutList) {
			System.out.println(i + ": " + current.getStartTime());
			i++;
		}
		i = 1;

		System.out.print("Please select the workout or 0 to return to previous menu: ");

		selection = scanner.nextInt();
		scanner.nextLine();

		if (selection == 0) {
			return null;
		}
		workout = tracker.workoutList.get(selection - 1);

		System.out.println();
		System.out.println("Workout Details from: " + workout.getStartTime().toString());
		for (Exercise exercise : workout.exerciseArrayList) {
			System.out.println(i + ": " + exercise);

			for (Set set : exercise.setList) {
				System.out.println("    " + j + ": " + set);
				j++;
			}
			j = 1;
			i++;
		}

		return workout;

	}

	/**
	 * Allows the user to edit a workout's exercise or set. Can remove or add sets
	 * 
	 * @param tracker
	 * @param list
	 * @param scanner
	 */
	public static void editWorkout(WorkoutTracker tracker, ExerciseList list, Scanner scanner) {
		Workout workout;
		int selection;

		workout = viewWorkouts(tracker, scanner);
		
		if (workout == null) { // User backed out without selection a workout
			return; 
		}
		
		System.out.println("0. Return to Workout Tracker Menu\n1. Add Exercise\n2. Remove Exercise\n"
				+ "3. Add Set\n4. Remove Set\n5. Select another workout");
		System.out.print("Please select from one of the previous options: ");

		selection = scanner.nextInt();

		scanner.nextLine();
		
		if (selection == 0) {
			return; // Back to tracker menu
		}

		if (selection == 1) {
			WorkoutController.addExercise(workout, list, scanner);
		} else if (selection == 2) {
			WorkoutController.removeExercise(workout, scanner);
		} else if (selection == 3) {
			WorkoutController.addSet(workout, scanner);
		} else if (selection == 4) {
			WorkoutController.removeSet(workout, scanner);
		} else if (selection == 5) { // Restart the method (picked the wrong option?)
			WorkoutTrackerController.editWorkout(tracker, list, scanner);
		}
	}

	public static void viewStatisticsOptions(WorkoutTracker tracker, ExerciseList list, Scanner scanner) {
		int selection = 0;

		while (selection != 3) {
			System.out.println();
			System.out.println("**** View Workout Statistics Menu ****");
			System.out.println("1. View Max Weight\n2. View Max Reps\n3. Return to tracker menu");
			System.out.print("Enter your selection: ");

			selection = scanner.nextInt();
			
			switch (selection) {
			case 1:
				viewStatisticsAux(tracker, list, scanner, selection);
				break;
			case 2:
				viewStatisticsAux(tracker, list, scanner, selection);
				break;
			case 3:
				System.out.println("Returning to tracker menu");
				break;
			default:
				System.out.println("Please enter valid selection");
				break;
			}
		}
	}

	public static void viewStatisticsAux(WorkoutTracker tracker, ExerciseList list, Scanner scanner, int numSelection) {
		int selection = 0, exerciseSelection = 0;
		String name;

		while (selection != 5) {
			System.out.println();
			System.out.println("**** View Specific Workout Statistics ****");
			System.out.println("1. Back exercises\n2. Chest exercises\n"
					+ "3. Legs exercises\n4. Shoulders exercises\n5. Back to statistics menu");

			System.out.print("Please make your selection: ");
			selection = scanner.nextInt();

			switch (selection) {
			case 1:
				System.out.println(list.printExerciseList(ExerciseList.BACK));
				System.out.print("Select exercise to get max weight: ");

				exerciseSelection = scanner.nextInt();
				name = list.getBackExercises().get(exerciseSelection - 1).getExerciseName();
				
				if(numSelection == 1) {
					viewMaxWeightAux(tracker, name);
				} else if (numSelection == 2) {
					viewMaxRepsAux(tracker, name);
				}

				break;
			case 2:
				System.out.println(list.printExerciseList(ExerciseList.CHEST));
				System.out.print("Select exercise to get max weight: ");

				exerciseSelection = scanner.nextInt();
				name = list.getChestExercises().get(exerciseSelection - 1).getExerciseName();
				
				if(numSelection == 1) {
					viewMaxWeightAux(tracker, name);
				} else if (numSelection == 2) {
					viewMaxRepsAux(tracker, name);
				}

				break;
			case 3:
				System.out.println(list.printExerciseList(ExerciseList.LEGS));
				System.out.print("Select exercise to get max weight: ");

				exerciseSelection = scanner.nextInt();
				name = list.getLegsExercises().get(exerciseSelection - 1).getExerciseName();
				
				if(numSelection == 1) {
					viewMaxWeightAux(tracker, name);
				} else if (numSelection == 2) {
					viewMaxRepsAux(tracker, name);
				}

				break;
			case 4:
				System.out.println(list.printExerciseList(ExerciseList.SHOULDERS));
				System.out.print("Select exercise to get max weight: ");
				exerciseSelection = scanner.nextInt();
				name = list.getChestExercises().get(exerciseSelection - 1).getExerciseName();
				
				if(numSelection == 1) {
					viewMaxWeightAux(tracker, name);
				} else if (numSelection == 2) {
					viewMaxRepsAux(tracker, name);
				}
				
				break;
			case 5:
				System.out.println("Returning to statistics menu.");
			default:
				System.out.println("Please enter a valid selection.");

			}
		}

	}

	/**
	 * This helper method is responsible for looking through the workout tracker for
	 * a particular exercise and then returning the maximum weight for that exercise
	 * that the user has lifted.
	 * 
	 * @param tracker
	 * @param name
	 * @return
	 */
	public static void viewMaxWeightAux(WorkoutTracker tracker, String name) {
		double maxWeight = Integer.MIN_VALUE; // Minimum weight so user weight is always higher

		for (Workout workout : tracker.getWorkoutList()) { // Looking through the workouts
			for (Exercise current : workout.getExerciseArrayList()) { // Looking through the exercises for the workouts
				if (current.getExerciseName().equals(name)) { // Looking to see if the workout has that exercise
					for (Set set : current.getSetList()) { // Looking through the sets for the particular exercise
						if (set.getWeight() > maxWeight) { // Checking if the current is higher than the max
							maxWeight = set.getWeight(); // Updating the new maximum weight
						}
					}
				}
			}
		}

		if (maxWeight == Integer.MIN_VALUE) {
			System.out.println(name + " was not found");
		} else {
			System.out.println("The maximum weight for " + name + " is: " + maxWeight);
		}
		
		System.out.println();
	}

	public static void viewMaxRepsAux(WorkoutTracker tracker, String name) {		
		double maxReps = Integer.MIN_VALUE; // Minimum reps so user reps are always higher

		for (Workout workout : tracker.getWorkoutList()) { // Looking through the workouts
			for (Exercise current : workout.getExerciseArrayList()) { // Looking through the exercises inside of the workout
				if (current.getExerciseName().equals(name)) { // Looking to see if the workout has that exercise
					for (Set set : current.getSetList()) { // Looking through the sets for the particular exercise
						if (set.getReps() > maxReps) { // Checking if the current is higher than the max
							maxReps = set.getReps(); // Updating the new maximum reps
						}
					}
				}
			}
		}

		if (maxReps == Integer.MIN_VALUE) {
			System.out.println(name + " was not found");
		} else {
			System.out.println("The maximum reps for " + name + " is: " + maxReps);
		}
	}

}
