package model;

import java.util.Scanner;

public class ExerciseListController {
	
	public static void exerciseListOptions(ExerciseList list, Scanner scanner) {
		int selection = 0;

		while (selection != 7) {
			System.out.println();
			System.out.println("**** Exercise List Menu ****");
			System.out.println("1. Create a new exercise\n2. Remove an exercise\n"
					+ "3. View Back exercises\n4. View Chest exercises\n5. View Legs exercises\n"
					+ "6. View Shoulders exercises\n7. Return to main menu");
			System.out.print("Enter your selection: ");

			selection = scanner.nextInt();

			switch (selection) {

			case 1: // Create new exercise
				createExercise(list, scanner);
				break;
			case 2: // Remove exercise
				deleteExerciseFromList(list, scanner);
				break;
			case 3: // View Back exercises
				System.out.println(list.printExerciseList(ExerciseList.BACK));
				break;
			case 4: // View Chest exercises
				System.out.println(list.printExerciseList(ExerciseList.CHEST));
				break;
			case 5: // View Legs exercises
				System.out.println(list.printExerciseList(ExerciseList.LEGS));
				break;
			case 6: // View Shoulders exercises
				System.out.println(list.printExerciseList(ExerciseList.SHOULDERS));
				break;
			case 7:
				System.out.println("Returning to main menu.");
				break;
			default:
				System.out.println("Please enter a valid number.");
				break;
			}
		}
	}
	
	public static void createExercise(ExerciseList list, Scanner scanner) {
		String name, type;
		scanner.nextLine();
		
		System.out.println();
		System.out.println("***** Exercise Creation ****");
		System.out.print("Please enter the name of the new exercise: ");
		name = scanner.nextLine();

		System.out.print("Please enter the type of exercise: ");
		type = scanner.nextLine();

		list.addExercise(new Exercise(name, type));

		System.out.println(name + " has been successfully created!");

	}
	
	public static void deleteExerciseFromList(ExerciseList list, Scanner scanner) {
		int selection = 0;
		int exerciseSelection = 0;

		while (selection != 5) {
			System.out.println();
			System.out.println("**** Exercise Deletion ****");
			System.out.println("Please select the type of exercise:");
			System.out.println("1. Back\n2. Chest\n3. Legs\n4. Shoulders\n5. Back to exercise list menu");
			System.out.print("Enter your selection: ");
			selection = scanner.nextInt();

			switch (selection) {
			case 1:
				System.out.println(list.printExerciseList(ExerciseList.BACK));
				System.out.print("Select exercise to be removed: ");
				exerciseSelection = scanner.nextInt();
				list.removeExercise(list.getBackExercises().get(exerciseSelection - 1));
				break;
			case 2:
				System.out.println(list.printExerciseList(ExerciseList.CHEST));
				System.out.print("Select exercise to be removed: ");
				exerciseSelection = scanner.nextInt();
				list.removeExercise(list.getChestExercises().get(exerciseSelection - 1));
				break;
			case 3:
				System.out.println(list.printExerciseList(ExerciseList.LEGS));
				System.out.print("Select exercise to be removed: ");
				exerciseSelection = scanner.nextInt();
				list.removeExercise(list.getLegsExercises().get(exerciseSelection - 1));
				break;
			case 4:
				System.out.println(list.printExerciseList(ExerciseList.SHOULDERS));
				System.out.print("Select exercise to be removed: ");
				exerciseSelection = scanner.nextInt();
				list.removeExercise(list.getShouldersExercises().get(exerciseSelection - 1));
				break;
			case 5:
				System.out.println("Returning to exercise list menu.");
			default:
				System.out.println("Please enter a valid selection.");

			}

		}

	}
}
