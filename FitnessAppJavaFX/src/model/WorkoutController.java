package model;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class WorkoutController {

	public static void currentWorkoutOptions(Workout workout, ExerciseList list, Scanner scanner,
			WorkoutTracker tracker) {
		int selection = 0;

		while (selection != 8) {
			System.out.println();
			System.out.println("**** Current Workout Menu ****");
			System.out.println("1. Create a new workout\n2. Add an exercise to current workout\n"
					+ "3. Add a set to an exercise\n4. Remove exercise\n5. Remove set\n"
					+ "6. Delete current workout\n7. View current workout\n8. Return to main menu");
			System.out.print("Please enter your selection: ");

			try {
				selection = scanner.nextInt();

				switch (selection) {
				case 1:
					tracker.addWorkout(workout);
					break;
				case 2:
					addExercise(workout, list, scanner);
					break;
				case 3:
					addSet(workout, scanner);
					break;
				case 4:
					removeExercise(workout, scanner);
					break;
				case 5:
					removeSet(workout, scanner);
					break;
				case 6:
					tracker.removeWorkout(workout);
				case 7:
					workout.printExerciseArrayList();
					break;
				case 8:
					System.out.println("Now returning to main menu.");
					break;
				default:
					System.out.println("Please enter a valid selection.");
					break;
				}

				System.out.println();

			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid input (NUMBERS ONLY)");
				scanner.nextLine();
				selection = Integer.MAX_VALUE;
			}
		}
	}

	/**
	 * Used to add an exercise to a workout
	 * 
	 * @param workout
	 * @param list
	 * @param scanner
	 */
	public static void addExercise(Workout workout, ExerciseList list, Scanner scanner) {
		String add = "";
		int selection;
		ArrayList<Exercise> exerciseList = new ArrayList<>();
		System.out.println();
		System.out.println("**** Add Exercise to Workout ****");
		System.out.println("1. Back\n2. Chest\n3. Legs\n4. Shoulders");
		System.out.print("Please select the type of exercise you wish to do or press 0 to return: ");

		try {
			selection = scanner.nextInt();
			scanner.nextLine();

			if (selection == 0) {
				return;
			}

			add = list.printExerciseList(selection);

			if (selection == ExerciseList.BACK) {
				exerciseList = list.backList;

			} else if (selection == ExerciseList.CHEST) {
				exerciseList = list.chestList;

			} else if (selection == ExerciseList.LEGS) {
				exerciseList = list.legsList;

			} else if (selection == ExerciseList.SHOULDERS) {
				exerciseList = list.shouldersList;

			}

			System.out.println(add);
			System.out.println("Please select an exercise: ");

			selection = scanner.nextInt();
			scanner.nextLine();

			workout.addExercise(exerciseList.get(selection - 1));

			System.out.println("Exercise has been successfully added");

		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid input (NUMBERS ONLY)");
			scanner.nextLine();
			selection = Integer.MAX_VALUE;
		}

	}

	/**
	 * Used to add a set to an exercise in a workout
	 * 
	 * @param workout
	 * @param scanner
	 */
	public static void addSet(Workout workout, Scanner scanner) {
		Exercise exercise;
		int selection;
		int reps = 0;
		double weight = 0;

		System.out.println();
		System.out.println("**** Add Set to Exercise ****");
		System.out.println(workout.printExerciseArrayList());
		System.out.print("Select the exercise you wish to add a set to:");

		try {
			selection = scanner.nextInt();

			exercise = workout.exerciseArrayList.get(selection - 1);

			System.out.print("Enter the reps done: ");
			reps = scanner.nextInt();
			scanner.nextLine();

			System.out.print("Enter the weight used: ");
			weight = scanner.nextDouble();
			scanner.nextLine();

			System.out.println(reps);
			System.out.println(weight);

			exercise.addSet(new Set(reps, weight));

			System.out.println("Set has been successfully added!");

		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid input (NUMBERS ONLY)");
			scanner.nextLine();
			selection = Integer.MAX_VALUE;
		}

	}

	/**
	 * Allows the user to remove an exercise entirely from a workout
	 * 
	 * @param workout
	 * @param scanner
	 */
	public static void removeExercise(Workout workout, Scanner scanner) {
		int selection;

		System.out.println();
		System.out.println("**** Remove Exercise from Workout ****");
		System.out.println(workout.printExerciseArrayList());
		System.out.println("Please select the exercise that you wish to remove: ");

		try {
			selection = scanner.nextInt();

			scanner.nextLine();

			workout.removeExercise(workout.exerciseArrayList.remove(selection - 1));

		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid input (NUMBERS ONLY)");
			scanner.nextLine();
			selection = Integer.MAX_VALUE;
		}

	}

	/**
	 * Allows the user to remove a set from an exercise in a workout
	 * 
	 * @param workout
	 * @param scanner
	 */
	public static void removeSet(Workout workout, Scanner scanner) {
		int selection;
		Exercise exercise;

		System.out.println();
		System.out.println("**** Remove Set from Exercise ****");
		System.out.println(workout.printExerciseArrayList());
		System.out.print("Please select the exercise to remove the set from: ");

		try {
			selection = scanner.nextInt();

			exercise = workout.exerciseArrayList.get(selection - 1);

			System.out.println(exercise.printSetList());
			System.out.print("Please select the set that you wish to remove: ");

			selection = scanner.nextInt();

			scanner.nextLine();

			exercise.removeSet(exercise.setList.get(selection - 1));

		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid input (NUMBERS ONLY)");
			scanner.nextLine();
			selection = Integer.MAX_VALUE;
		}
	}
}
