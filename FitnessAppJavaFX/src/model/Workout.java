package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * This class creates an array of exercises that will make up the workout for
 * the day. The workout will keep track of the date and time that the 
 * exercises were performed.
 * 
 * @author Brenton Haliw
 *
 */
public class Workout implements Comparable<Workout> {

	private ArrayList<Exercise> exerciseArrayList;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String startTimeString;

	/**
	 * Default Constructor for the workout object. Initializes the exercise
	 * ArrayList and notes the start time of the workout.
	 */
	public Workout() {
		this.exerciseArrayList = new ArrayList<>();
		this.startTime = LocalDateTime.now();
		this.startTimeString = startTime.toString();
	}

	public void addExercise(Exercise exercise) {
		this.exerciseArrayList.add(exercise);
	}

	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	public LocalDateTime getEndTime() {
		return this.endTime;
	}

	public void removeExercise(Exercise exercise) {
		this.exerciseArrayList.remove(exercise);
	}

	public void endWorkout(LocalDateTime time) {
		this.endTime = time;
	}

	public Exercise getExercise(int index) {
		return exerciseArrayList.get(index);
	}

	public ArrayList<Exercise> getExerciseArrayList() {
		return this.exerciseArrayList;
	}
	
	public void setStartTime(String[] dateArray, String[] timeArray) {
		this.startTime = LocalDate.of(Integer.parseInt(dateArray[0]),Integer.parseInt(dateArray[1]), 
				Integer.parseInt(dateArray[2])).atTime(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]),1);
		
	}
	
	public void setStartTime(String input) {
		this.startTime = LocalDateTime.parse(input);
	}

	/**
	 * Prints out the items in the Exercise ArrayList
	 * 
	 * @return
	 */
	public String printExerciseArrayList() {
		String output = "";
		int i = 1;

		for (Exercise exercise : this.exerciseArrayList) {
			output += i + ": " + exercise.getExerciseName() + "\n";
			i++;
		}

		return output;
	}

	@Override
	public int compareTo(Workout otherWorkout) {
		return this.startTime.compareTo(otherWorkout.startTime);
	}

}
