package model;

import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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

		list.loadExerciseList("C:\\Users\\bjhal\\OneDrive\\Desktop\\ExerciseList.txt");
		tracker.loadWorkoutList("C:\\Users\\bjhal\\OneDrive\\Desktop\\workoutList.txt");

		while (selection != 4) {
			
			System.out.println("**** MAIN MENU ****");
			System.out.println("1. Current Workout Options\n2. Exercise List Options\n"
					+ "3. Workout Tracker Options\n4. Exit Application");
			System.out.print("Please make your selection: ");

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
		}

		tracker.saveWorkoutList("C:\\Users\\bjhal\\OneDrive\\Desktop\\workoutList.txt");
		list.saveExerciseList("C:\\Users\\bjhal\\OneDrive\\Desktop\\ExerciseList.txt");

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
