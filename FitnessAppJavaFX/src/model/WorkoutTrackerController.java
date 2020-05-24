package model;

import java.awt.Dimension;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


public class WorkoutTrackerController {

	public static final int MAX_WEIGHT_STATS = 1;
	public static final int MAX_REPS_STATS = 2;
	public static final int ONE_REP_MAX = 3;
	public static final int TOTAL_VOLUME = 4;
	
	public static void workoutTrackerOptions(WorkoutTracker tracker, ExerciseList list, Scanner scanner) {
		int selection = 0;

		while (selection != 4) {
			System.out.println();
			System.out.println("**** Workout Tracker Menu ****");
			System.out.println(
					"1. View past workouts\n2. Edit a past workout\n" + "3. View statistics\n4. Return to main menu");
			System.out.print("Enter your selection: ");

			try {
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

			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid input (NUMBERS ONLY)");
				scanner.nextLine();
				selection = Integer.MAX_VALUE;
			}
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
		Workout workout = null;
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

		try {
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

		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid input (NUMBERS ONLY)");
			scanner.nextLine();
			selection = Integer.MAX_VALUE;
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

		try {
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

		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid input (NUMBERS ONLY)");
			scanner.nextLine();
			selection = Integer.MAX_VALUE;
		}
	}

	public static void viewStatisticsOptions(WorkoutTracker tracker, ExerciseList list, Scanner scanner) {
		int selection = 0;

		while (selection != 3) {
			System.out.println();
			System.out.println("**** View Workout Statistics Menu ****");
			System.out.println("1. View Max Weight\n2. View Max Reps\n3. Return to tracker menu");
			System.out.print("Enter your selection: ");

			try {
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

			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid input (NUMBERS ONLY)");
				scanner.nextLine();
				selection = Integer.MAX_VALUE;
			}
		}
	}

	private static void viewStatisticsAux(WorkoutTracker tracker, ExerciseList list, Scanner scanner, int numSelection) {
		int selection = 0, exerciseSelection = 0;
		String name;

		while (selection != 5) {
			System.out.println();
			System.out.println("**** View Specific Workout Statistics ****");
			System.out.println("1. Back exercises\n2. Chest exercises\n"
					+ "3. Legs exercises\n4. Shoulders exercises\n5. Back to statistics menu");

			System.out.print("Please make your selection: ");

			try {
				selection = scanner.nextInt();

				switch (selection) {
				case 1:
					System.out.println(list.printExerciseList(ExerciseList.BACK));
					System.out.print("Select exercise to get max weight: ");

					exerciseSelection = scanner.nextInt();
					name = list.getBackExercises().get(exerciseSelection - 1).getExerciseName();

					if (numSelection == 1) {
						viewMaxWeightAux(scanner, tracker, name);
					} else if (numSelection == 2) {
						viewMaxRepsAux(scanner, tracker, name);
					}

					break;
				case 2:
					System.out.println(list.printExerciseList(ExerciseList.CHEST));
					System.out.print("Select exercise to get max weight: ");

					exerciseSelection = scanner.nextInt();
					name = list.getChestExercises().get(exerciseSelection - 1).getExerciseName();

					if (numSelection == 1) {
						viewMaxWeightAux(scanner, tracker, name);
					} else if (numSelection == 2) {
						viewMaxRepsAux(scanner, tracker, name);
					}

					break;
				case 3:
					System.out.println(list.printExerciseList(ExerciseList.LEGS));
					System.out.print("Select exercise to get max weight: ");

					exerciseSelection = scanner.nextInt();
					name = list.getLegsExercises().get(exerciseSelection - 1).getExerciseName();

					if (numSelection == 1) {
						viewMaxWeightAux(scanner, tracker, name);
					} else if (numSelection == 2) {
						viewMaxRepsAux(scanner, tracker, name);
					}

					break;
				case 4:
					System.out.println(list.printExerciseList(ExerciseList.SHOULDERS));
					System.out.print("Select exercise to get max weight: ");
					exerciseSelection = scanner.nextInt();
					name = list.getChestExercises().get(exerciseSelection - 1).getExerciseName();

					if (numSelection == 1) {
						viewMaxWeightAux(scanner, tracker, name);
					} else if (numSelection == 2) {
						viewMaxRepsAux(scanner, tracker, name);
					}

					break;
				case 5:
					System.out.println("Returning to statistics menu.");
				default:
					System.out.println("Please enter a valid selection.");

				}

			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid input (NUMBERS ONLY)");
				scanner.nextLine();
				selection = Integer.MAX_VALUE;
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
	private static void viewMaxWeightAux(Scanner scanner, WorkoutTracker tracker, String name) {
		double maxWeight = Integer.MIN_VALUE; // Minimum weight so user weight is always higher
		int selection = Integer.MAX_VALUE;

		for (Workout workout : tracker.getWorkoutList()) { // Looking through the workouts
			for (Exercise current : workout.getExerciseArrayList()) { // Looking through the exercises for the workouts
				if (current.getExerciseName().equals(name)) { // Looking to see if the workout has that exercise
					if (current.getMaxWeight() > maxWeight) {
						maxWeight = current.getMaxWeight();
					}
				}
			}
		}

		if (maxWeight == Integer.MIN_VALUE) {
			System.out.println(name + " was not found");
			return;
		} else {
			System.out.println("The maximum weight for " + name + " is: " + maxWeight);
			System.out.println("Would you like to see a graph of max weight for each workout for this exercise?");
			System.out.print("Type 1 to see a graph or 2 to return to menu: ");
		}
		
		try {
			selection = scanner.nextInt();

			if (selection == 1) {
				showStatisticsGraph(tracker, name, WorkoutTrackerController.MAX_WEIGHT_STATS);				
			} else if (selection == 2) {
				return;
			}

		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid input (NUMBERS ONLY)");
			scanner.nextLine();
			selection = Integer.MAX_VALUE;
		}

		System.out.println();
	}

	private static void viewMaxRepsAux(Scanner scanner, WorkoutTracker tracker, String name) {
		double maxReps = Integer.MIN_VALUE; // Minimum reps so user reps are always higher
		int selection = 0;

		for (Workout workout : tracker.getWorkoutList()) { // Looking through the workouts
			for (Exercise current : workout.getExerciseArrayList()) { // Looking through the exercises inside of the workout
				if (current.getExerciseName().equals(name)) { // Looking to see if the workout has that exercise
					if (current.getMaxWeight() > maxReps) {
						maxReps = current.getMaxReps();
					}
				}
			}
		}

		if (maxReps == Integer.MIN_VALUE) {
			System.out.println(name + " was not found");
			return;
		} else {
			System.out.println("The maximum reps for " + name + " is: " + maxReps);
			System.out.println("Would you like to see a graph of max reps for each workout for this exercise?");
			System.out.print("Type 1 to see a graph or 2 to return to menu: ");

			try {
				selection = scanner.nextInt();

				if (selection == 1) {
					showStatisticsGraph(tracker, name, WorkoutTrackerController.MAX_REPS_STATS);				
				} else if (selection == 2) {
					return;
				}

			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid input (NUMBERS ONLY)");
				scanner.nextLine();
				selection = Integer.MAX_VALUE;
			}
		}
	}
	
	/**
	 * This method allows the user to view a graph of their current progress with a specified
	 * exercise.
	 * @param tracker
	 * @param name - will be the name of the selected exercise
	 * @param selection - will be the type of statistic that they want to see (max reps or max weight)
	 */
	public static void showStatisticsGraph(WorkoutTracker tracker, String name, int selection) {
		JFrame frame = new JFrame();
		final JFXPanel jfxPanel = new JFXPanel();
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.setTitle("Graph");
		
		JPanel jp = new JPanel();
		jp.add(jfxPanel);
		jp.setVisible(true);
		jp.setPreferredSize(new Dimension(800, 800));
		
		Scene scene = new Scene(createGraph(tracker, name, selection), 800, 800);
		jfxPanel.setScene(scene);
		jfxPanel.setVisible(true);

		frame.add(jp);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	/**
	 * This method will allow the user to see a graph of their progress through
	 * JavaFX
	 * 
	 * @param name
	 * @return
	 */
	private static LineChart<String, Number> createGraph(WorkoutTracker tracker, String name, int selection) {
		double maxYValue = Integer.MIN_VALUE;
		double minYValue = Integer.MAX_VALUE;
		double[] values = new double[2];

		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("Max Weight Per Workout");

		values = createGraphAux(tracker, name, series, selection);

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis(values[0], values[1], 5);
		xAxis.setLabel("Date");
		yAxis.setLabel("Weight (lbs");

		final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle(name);

		lineChart.getData().add(series);

		return lineChart;
	}
	
	/**
	 * Returns the min and max values to be used for the Y axis in a double[].
	 * @param tracker
	 * @param name
	 * @param series
	 * @param selection
	 * @return
	 */
	private static double[] createGraphAux(WorkoutTracker tracker, String name, XYChart.Series series, int selection) {
		double currentValue = 0;
		double maxYValue = Integer.MIN_VALUE;
		double minYValue = Integer.MAX_VALUE;
		double[] minAndMaxValues = new double[2];
		
		for (Workout Workout : tracker.getWorkoutList()) {
			for (Exercise current : Workout.getExerciseArrayList()) {
				if (current.getExerciseName().equals(name)) {
						if (current.getMaxWeight() < minYValue) {
							minYValue = current.getMaxWeight();
						}

						if (current.getMaxWeight() > maxYValue) {
							maxYValue = current.getMaxWeight();
						}
						
						currentValue = current.getMaxWeight();
				
					series.getData()
							.add(new XYChart.Data(Workout.getStartTime().toString().substring(0, 10), currentValue));

				}
			}
		}
		
		minAndMaxValues[0] = minYValue - 10;
		minAndMaxValues[1] = maxYValue + 10;
		
		return minAndMaxValues;
		
		
	}
}
