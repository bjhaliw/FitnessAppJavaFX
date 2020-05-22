package model;

import java.util.Scanner;

public class ExerciseListController {
	
	public static void exerciseListOptions(ExerciseList list, Scanner scanner) {
		int selection = 0;

		while (selection != 4) {
			System.out.println();
			System.out.println("***** Exercise List Menu *****");
			System.out.println("1. Create a new exercise\n2. Remove an exercise\n"
					+ "3. View Exercises\n4. Return to main menu");
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
				viewExercisesMenu(list, scanner);
				break;
			case 4:
				System.out.println("Returning to main menu.");
				break;
			default:
				System.out.println("Please enter a valid number.");
				break;
			}
		}
	}
	
	public static void viewExercisesMenu(ExerciseList list, Scanner scanner) {
		int selection = 0;
		
		while (selection != 9) {
			System.out.println();
			System.out.println("***** View Exercises Menu *****");
			System.out.println("1. Abs Exercises\n2. Back Exercises\n3. Biceps Exercises"
					+ "\n4. Cardio Exercises\n5. Chest Exercises\n6. Legs Exercises"
					+ "\n7. Shoulders Exercises\n8. Triceps Exercises\n9. Return to Exercise List Menu");
			System.out.print("Please enter your selection: ");
			
			selection = scanner.nextInt();
			System.out.println();
			
			switch(selection) {			
			case 1:
				System.out.println("//// Abs Exercises ////");
				System.out.println(list.printExerciseList(ExerciseList.ABS));
				break;			
			case 2:
				System.out.println("//// Back Exercises ////");
				System.out.println(list.printExerciseList(ExerciseList.BACK));
				break;				
			case 3:
				System.out.println("//// Biceps Exercises ////");
				System.out.println(list.printExerciseList(ExerciseList.BICEPS));
				break;				
			case 4:
				System.out.println("//// Cardio Exercises ////");
				System.out.println(list.printExerciseList(ExerciseList.CARDIO));
				break;				
			case 5:
				System.out.println("//// Chest Exercises ////");
				System.out.println(list.printExerciseList(ExerciseList.CHEST));
				break;				
			case 6:
				System.out.println("//// Legs Exercises ////");
				System.out.println(list.printExerciseList(ExerciseList.LEGS));
				break;				
			case 7:
				System.out.println("//// Shoulders Exercises ////");
				System.out.println(list.printExerciseList(ExerciseList.SHOULDERS));
				break;			
			case 8:
				System.out.println("//// Triceps Exercises ////");
				System.out.println(list.printExerciseList(ExerciseList.TRICEPS));
				break;
			case 9:
				System.out.println("Now returning to Exercise List Menu");
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
		System.out.println("***** Exercise Creation *****");
		System.out.print("Please enter the name of the new exercise: ");
		name = scanner.nextLine();

		System.out.print("Please enter the type of exercise: ");
		type = scanner.nextLine();

		list.addExercise(new Exercise(name, type));
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
				break;
			default:
				System.out.println("Please enter a valid selection.");
				break;

			}

		}

	}
}
