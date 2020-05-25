package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) throws Exception {

		
		ExerciseList list = new ExerciseList();
		WorkoutTracker tracker = new WorkoutTracker();
		Workout workout = new Workout();
		int selection = 0;
		Scanner scanner = new Scanner(System.in);
		/*
		 * final JFileChooser fc = new JFileChooser(); File exerciseListFile = null,
		 * workoutTrackerFile = null;
		 * 
		 * JOptionPane.showMessageDialog(null, "Please select your exercise list");
		 * 
		 * int returnVal = fc.showOpenDialog(null); if (returnVal ==
		 * JFileChooser.APPROVE_OPTION) { exerciseListFile = fc.getSelectedFile();
		 * list.loadExerciseList(exerciseListFile.getAbsolutePath()); }
		 * 
		 * JOptionPane.showMessageDialog(null, "Please select your workout list");
		 * 
		 * returnVal = fc.showOpenDialog(null); if (returnVal ==
		 * JFileChooser.APPROVE_OPTION) { workoutTrackerFile = fc.getSelectedFile();
		 * tracker.loadworkoutList(workoutTrackerFile.getAbsolutePath()); }
		 */

		list.loadExerciseList("C:\\Users\\bjhal\\Desktop\\ExerciseList.txt");
		tracker.loadWorkoutList("C:\\Users\\bjhal\\Desktop\\WorkoutList.txt");
		System.out.println(LocalDateTime.now());
		
		System.out.println(LocalDate.of(2020, 01, 3).atTime(12,2));

		while (selection != 4) {
			
			System.out.println("**** MAIN MENU ****");
			System.out.println("1. Current Workout Options\n2. Exercise List Options\n"
					+ "3. Workout Tracker Options\n4. Exit Application");
			System.out.print("Please make your selection: ");

			try {
				selection = scanner.nextInt();
				
				switch (selection) {

				case 1:
					WorkoutController.currentWorkoutOptions(workout, list, scanner, tracker);
					break;
				case 2:
					ExerciseListController.exerciseListOptions(list, scanner);
					break;
				case 3:
					WorkoutTrackerController.workoutTrackerOptions(tracker, list, scanner);
					break;
				case 4:
					System.out.println("Thank you for using the fitness application!");
					break;
				default:
					System.out.println("Please enter a valid number.");
					break;
				}

				System.out.println();
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid input (NUMBERS ONLY)");
				System.out.println();
				scanner.nextLine();
				selection = Integer.MAX_VALUE;
			}		
		}

		tracker.saveWorkoutList("C:\\Users\\bjhal\\Desktop\\WorkoutList.txt");
		list.saveExerciseList("C:\\Users\\bjhal\\Desktop\\ExerciseList.txt");

		/*
		 * if(exerciseListFile != null) {
		 * list.saveExerciseList(exerciseListFile.getAbsolutePath()); }
		 * 
		 * if(workoutTrackerFile != null) {
		 * tracker.saveworkoutList(workoutTrackerFile.getAbsolutePath()); }
		 */

		scanner.close();
	}

}
