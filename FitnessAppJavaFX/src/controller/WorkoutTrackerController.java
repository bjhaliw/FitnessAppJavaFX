package controller;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.Driver;
import model.Exercise;
import model.ExerciseList;
import model.ReadAndWrite;
import model.Set;
import model.Statistics;
import model.Workout;
import model.WorkoutTracker;

/**
 * This is the controller class for the WorkoutTracker object. Allows
 * the user to access different menus to manipulate the WorkoutTracker
 * to perform activities such as adding and deleting workouts
 * @author bjhal
 *
 */
public class WorkoutTrackerController {

	public static final int MAX_WEIGHT_STATS = 1;
	public static final int MAX_REPS_STATS = 2;
	public static final int ONE_REP_MAX = 3;
	public static final int TOTAL_VOLUME = 4;

	/**
	 * This is the main menu of the WorkoutTrackerController. From here, the user will
	 * be able to access a variety of different menus to manipulate the WorkoutTracker
	 * @param tracker
	 * @param list
	 * @param scanner
	 */
	public static void workoutTrackerOptions(WorkoutTracker tracker, ExerciseList list, Scanner scanner) {
		int selection = Integer.MAX_VALUE;

		while (selection != Driver.RETURN_TO_MENU) {
			System.out.println();
			System.out.println("**** Workout Tracker Menu ****");
			System.out.println(
					"1. View past workouts\n2. Edit a past workout\n" + "3. View statistics\n"
							+ "4. Add past workout\n5. Save all information\n0. Return to main menu");
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
					addPastWorkout(tracker, list, scanner);
					break;
				case 5:
					try {
						ReadAndWrite.saveAllInformation(tracker, list);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case Driver.RETURN_TO_MENU:
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
	 * Adds a workout to the WorkoutTracker object. Allows the user to create past workouts
	 * and then orders them in the tracker's ArrayList of workouts by date.
	 * @param tracker
	 * @param list
	 * @param scanner
	 */
	public static void addPastWorkout(WorkoutTracker tracker, ExerciseList list, Scanner scanner) {
		int selection = Integer.MAX_VALUE;
		Workout workout;
		String date, time;
		String[] dateArray, timeArray;
		

		while (selection != Driver.RETURN_TO_MENU) {
			System.out.println();
			System.out.println("**** Add Workout Menu ****");
			System.out.println(
					"1. Create a new workout\n0. Return to main menu");
			System.out.print("Enter your selection: ");
			
			selection = scanner.nextInt();
			scanner.nextLine();
			
			switch(selection) {
			case Driver.RETURN_TO_MENU:
				System.out.println("Now returning to Workout Tracker Menu");
				return;
			case 1:
				workout = new Workout();
				System.out.println("What date did this workout occur?");
				System.out.print("Enter date (YYYY-MM-DD): ");
				date = scanner.nextLine();
				System.out.println("What time did this workout occur?");
				System.out.print("Enter time (HH:MM in 24 hour time): ");
				time = scanner.nextLine();
				dateArray = date.split("-");
				timeArray = time.split(":");
				workout.setStartTime(dateArray, timeArray);
				tracker.addWorkout(workout);
				Collections.sort(tracker.getWorkoutList());
				System.out.println("Workout with start time: " + workout.getStartTime() 
									+ " has been added to the tracker");
				break;
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
		for (Workout current : tracker.getWorkoutList()) {
			System.out.println(i + ": " + current.getStartTime());
			i++;
		}
		i = 1;

		System.out.print("Please select the workout or 0 to return to previous menu: ");

		try {
			selection = scanner.nextInt();
			scanner.nextLine();

			if (selection == Driver.RETURN_TO_MENU) {
				return null;
			}
			workout = tracker.getWorkoutList().get(selection - 1);

			System.out.println();
			System.out.println("Workout Details from: " + workout.getStartTime().toString());
			for (Exercise exercise : workout.getExerciseArrayList()) {
				System.out.println(i + ": " + exercise);

				for (Set set : exercise.getSetList()) {
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

			if (selection == Driver.RETURN_TO_MENU) {
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

	/**
	 * This is the menu for the user to view Statistics about exercises that have been performed
	 * and are loaded in the current WorkoutTracker.
	 * @param tracker
	 * @param list
	 * @param scanner
	 */
	public static void viewStatisticsOptions(WorkoutTracker tracker, ExerciseList list, Scanner scanner) {
		int selection = Integer.MAX_VALUE;
		

		while (selection != Driver.RETURN_TO_MENU) {
			System.out.println();
			System.out.println("**** View Workout Statistics Menu ****");
			System.out.println("1. View Max Weight\n2. View Max Reps\n"
					+ "3. View One Rep Max\n4. View Max Total Volume\n"
					+ "5. Save Statistics to Excel Document\n0. Return to tracker menu");
			System.out.print("Enter your selection: ");

			try {
				selection = scanner.nextInt();

				if (selection == Driver.RETURN_TO_MENU) {
					System.out.println("Returning to tracker menu");
					return;
				} else if (selection >= 1 && selection <= 4) {
					viewStatisticsAux(tracker, list, scanner, selection);
				} else if (selection == 5){
					try {
						Statistics.saveGraphExcel(tracker);
					} catch (IOException e) {
						System.out.println("Writing to Excel Document Failed");
						e.printStackTrace();
					}
				} else {
					System.out.println("Please enter a valid selection");
				}
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid input (NUMBERS ONLY)");
				scanner.nextLine();
				selection = Integer.MAX_VALUE;
			}
		}
	}

	/** 
	 * This is a helper method designed to act as a 
	 * @param tracker
	 * @param list
	 * @param scanner
	 * @param numSelection
	 */
	private static void viewStatisticsAux(WorkoutTracker tracker, ExerciseList list, Scanner scanner,
			int numSelection) {
		int selection = 0, exerciseSelection = 0;
		double max = 0;
		String name = "";

		while (selection != 5) {
			System.out.println();
			System.out.println("**** View Specific Workout Statistics ****");
			System.out.println("1. Abs Exercises\n2. Back exercises\n3. Biceps exercises\n"
					+ "4. Cardio exercises\n5. Chest exercises\n6. Legs exercises\n7. Shoulders Exercises"
					+ "\n8. Triceps exercises\n0. Back to statistics menu");

			System.out.print("Please make your selection: ");
			
			// See about trying to cut down on code

			try {
				selection = scanner.nextInt();

				switch (selection) {
				case 1:
					System.out.println(list.printExerciseList(ExerciseList.ABS));
					System.out.print("Select exercise: ");
					exerciseSelection = scanner.nextInt();
					name = list.getAbsExercises().get(exerciseSelection - 1).getExerciseName();
					break;
				case 2:
					System.out.println(list.printExerciseList(ExerciseList.BACK));
					System.out.print("Select exercise: ");
					exerciseSelection = scanner.nextInt();
					name = list.getBackExercises().get(exerciseSelection - 1).getExerciseName();
					break;					
				case 3:
					System.out.println(list.printExerciseList(ExerciseList.BICEPS));
					System.out.print("Select exercise: ");
					exerciseSelection = scanner.nextInt();
					name = list.getBicepsExercises().get(exerciseSelection - 1).getExerciseName();
					break;			
				case 4:
					System.out.println(list.printExerciseList(ExerciseList.CARDIO));
					System.out.print("Select exercise: ");
					exerciseSelection = scanner.nextInt();
					name = list.getCardioExercises().get(exerciseSelection - 1).getExerciseName();
					break;
				case 5:
					System.out.println(list.printExerciseList(ExerciseList.CHEST));
					System.out.print("Select exercise: ");
					exerciseSelection = scanner.nextInt();
					name = list.getChestExercises().get(exerciseSelection - 1).getExerciseName();
					break;
				case 6:
					System.out.println(list.printExerciseList(ExerciseList.LEGS));
					System.out.print("Select exercise: ");
					exerciseSelection = scanner.nextInt();
					name = list.getLegsExercises().get(exerciseSelection - 1).getExerciseName();
					break;
				case 7:
					System.out.println(list.printExerciseList(ExerciseList.SHOULDERS));
					System.out.print("Select exercise: ");
					exerciseSelection = scanner.nextInt();
					name = list.getShouldersExercises().get(exerciseSelection - 1).getExerciseName();
					break;				
				case 8:
					System.out.println(list.printExerciseList(ExerciseList.TRICEPS));
					System.out.print("Select exercise: ");
					exerciseSelection = scanner.nextInt();
					name = list.getTricepsExercises().get(exerciseSelection - 1).getExerciseName();
					break;
				case Driver.RETURN_TO_MENU:
					System.out.println("Returning to statistics menu.");
					return;
				default:
					System.out.println("Please enter a valid selection.");
					continue;
				}
				
				max = getMaxValues(tracker, name, numSelection);
				
				if (max == Integer.MIN_VALUE) {
					System.out.println(name + " does not have any statistical data.");
					return;
				}

				System.out.println("The requested value for " + name + " is: " + max);
				System.out.println("Would you like to a view a graph with numerical data for this exercise?");
				System.out.print("Type 1 to see a graph or 2 to return to menu: ");

				selection = scanner.nextInt();

				if (selection == 1) {
					showStatisticsGraph(tracker, name, numSelection);
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
	 * Provides the overall max for all instances of the selected Exercise that
	 * appears inside of the WorkoutTracker object
	 * 
	 * @param tracker
	 * @param name
	 * @param selection
	 * @return
	 */
	private static double getMaxValues(WorkoutTracker tracker, String name, int selection) {
		double max = Integer.MIN_VALUE;

		for (Workout workout : tracker.getWorkoutList()) { // Looking through the workouts
			for (Exercise current : workout.getExerciseArrayList()) { // Looking through the exercises inside of the
																		// workout
				if (current.getExerciseName().equals(name)) { // Looking to see if the workout has that exercise

					switch (selection) {
					case WorkoutTrackerController.MAX_REPS_STATS:
						if (current.getMaxReps() > max) {
							max = current.getMaxReps();
						}
						break;
					case WorkoutTrackerController.MAX_WEIGHT_STATS:
						if (current.getMaxWeight() > max) {
							max = current.getMaxWeight();
						}
						break;
					case WorkoutTrackerController.ONE_REP_MAX:
						if (current.getOneRepMax() > max) {
							max = current.getOneRepMax();
						}
						break;
					case WorkoutTrackerController.TOTAL_VOLUME:
						if (current.getTotalVolume() > max) {
							max = current.getTotalVolume();
						}

					}
				}
			}
		}

		return max;
	}

	/**
	 * This method allows the user to view a graph of their current progress with a
	 * specified exercise. Utilizes a JFrame to display a JFXPanel to view the
	 * LineChart.
	 * 
	 * @param tracker
	 * @param name      - will be the name of the selected exercise
	 * @param selection - will be the type of statistic that they want to see (max
	 *                  reps or max weight)
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
	 * JavaFX. At this step, it is assumed that there is at least one entry to add
	 * to the graph.
	 * 
	 * @param name
	 * @return
	 */
	private static LineChart<String, Number> createGraph(WorkoutTracker tracker, String name, int selection) {
		double[] values = new double[2];
		String statType = "";

		// defining a series
		XYChart.Series series = new XYChart.Series();

		values = createGraphAux(tracker, name, series, selection);

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis(values[0], values[1], 5);
		xAxis.setLabel("Date");
		
		if (selection == WorkoutTrackerController.MAX_REPS_STATS) {
			yAxis.setLabel("Reps");

		} else {
			yAxis.setLabel("Weight (lbs)");
		}

		final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle(name);

		switch(selection) {
		case WorkoutTrackerController.MAX_WEIGHT_STATS:
			statType = "Max Weight Per Workout";
			series.setName("Max Weight Per Workout");
			break;
		case WorkoutTrackerController.MAX_REPS_STATS:
			statType = "Max Reps Per Workout";
			series.setName("Max Reps Per Workout");
			break;
		case WorkoutTrackerController.ONE_REP_MAX:
			statType = "One Rep Max Estimate";
			series.setName("One Rep Max Estimate Per Workout");
			break;
		case WorkoutTrackerController.TOTAL_VOLUME:
			statType = "Max Volume Per Workout";
			series.setName("Max Volume Per Workout");

			break;
		default:
			System.out.println("An invalid input was processed for the chart");
			return null;
		}
		
		lineChart.setTitle(name + " - " + statType);
		lineChart.getData().add(series);

		return lineChart;
	}

	/**
	 * Returns the min and max values to be used for the Y axis in a double[].
	 * Directly manipulates the XYChart parameter and adds values to it.
	 * 
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
					
					switch(selection) {
					case WorkoutTrackerController.MAX_WEIGHT_STATS:
						if (current.getMaxWeight() < minYValue) {
							minYValue = current.getMaxWeight();
						}

						if (current.getMaxWeight() > maxYValue) {
							maxYValue = current.getMaxWeight();
						}
						
						currentValue = current.getMaxWeight();
						break;
					
					case WorkoutTrackerController.MAX_REPS_STATS:
						if (current.getMaxReps() < minYValue) {
							minYValue = current.getMaxReps();
						}

						if (current.getMaxReps() > maxYValue) {
							maxYValue = current.getMaxReps();
						}
						
						currentValue = current.getMaxReps();
						break;
						
					case WorkoutTrackerController.ONE_REP_MAX:
						if (current.getOneRepMax() < minYValue) {
							minYValue = current.getOneRepMax();
						}

						if (current.getOneRepMax() > maxYValue) {
							maxYValue = current.getOneRepMax();
						}
						
						currentValue = current.getOneRepMax();
						break;
					
					case WorkoutTrackerController.TOTAL_VOLUME:
						if (current.getTotalVolume() < minYValue) {
							minYValue = current.getTotalVolume();
						}

						if (current.getTotalVolume() > maxYValue) {
							maxYValue = current.getTotalVolume();
						}
						
						currentValue = current.getTotalVolume();
						break;
					}


					series.getData()
							.add(new XYChart.Data(Workout.getStartTime().toString().substring(0, 10), currentValue));

				}
			}
		}

		minAndMaxValues[0] = minYValue - 10;
		minAndMaxValues[1] = maxYValue + 10;

		if (minAndMaxValues[0] < 0) {
			minAndMaxValues[0] = 0;
		}

		return minAndMaxValues;

	}
}
