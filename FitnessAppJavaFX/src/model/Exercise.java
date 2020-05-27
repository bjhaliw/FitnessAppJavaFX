package model;

import java.util.ArrayList;

/**
 * This class creates the individual exercise for the user to track. Each
 * exercise will have an amount of weight and reps attached to it, along with
 * the name of the exercise and what type of exercise that it will be (such as
 * chest, back, triceps, etc.)
 * 
 * @author Brenton Haliw
 *
 */

public class Exercise implements Comparable<Exercise> {

	private String exerciseName;
	private String exerciseType;
	private ArrayList<Set> setList;
	private double maxWeight, oneRepMax, totalVolume;
	private double maxReps;

	/**
	 * Constructor that creates an Exercise object to be used.
	 * 
	 * @param exerciseName
	 * @param exerciseType
	 */
	public Exercise(String exerciseName, String exerciseType) {
		this.exerciseName = exerciseName;
		this.exerciseType = exerciseType;
		this.setList = new ArrayList<>();
		maxWeight = 0;
		oneRepMax = 0;
		totalVolume = 0;
		maxReps = 0;
	}

	/**
	 * Adds the indicated set and updates the max values
	 * 
	 * @param set
	 */
	public void addSet(Set set) {
		this.setList.add(set);
		setMaxValues();
	}

	/**
	 * Removes the indicated set and updates the max values
	 * 
	 * @param set
	 */
	public void removeSet(Set set) {
		this.setList.remove(set);
		setMaxValues();
	}

	/**
	 * Returns the set list
	 * 
	 * @return setList
	 */
	public ArrayList<Set> getSetList() {
		return this.setList;
	}

	/**
	 * Allows the user to rename the exercise
	 * 
	 * @param exerciseName
	 */
	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}

	/**
	 * Allows the user to change the exercise type
	 * 
	 * @param exerciseType
	 */
	public void setExerciseType(String exerciseType) {
		this.exerciseType = exerciseType;
	}

	/**
	 * Returns the exercise's name
	 * 
	 * @return exerciseName
	 */
	public String getExerciseName() {
		return this.exerciseName;
	}

	/**
	 * Returns the exercise's type
	 * 
	 * @return exerciseType
	 */
	public String getExerciseType() {
		return this.exerciseType;
	}

	/**
	 * Returns the max weight for the exercise
	 * 
	 * @return maxWeight
	 */
	public double getMaxWeight() {
		return maxWeight;
	}

	/**
	 * Sets the max weight for the exercise
	 */
	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}

	/**
	 * Returns the one rep max weight for the exercise
	 * 
	 * @return oneRepMax
	 */
	public double getOneRepMax() {
		return oneRepMax;
	}

	/**
	 * Sets the one rep max weight for the exercise
	 */
	public void setOneRepMax(double oneRepMax) {
		this.oneRepMax = oneRepMax;
	}

	/**
	 * Returns the total volume max for the exercise
	 * 
	 * @return totalVolume
	 */
	public double getTotalVolume() {
		return totalVolume;
	}

	/**
	 * Sets the total volume max for the exercise
	 */
	public void setTotalVolume(double totalVolume) {
		this.totalVolume = totalVolume;
	}

	/**
	 * Returns the max reps for the exercise
	 * 
	 * @return maxReps
	 */
	public double getMaxReps() {
		return maxReps;
	}

	/**
	 * Sets the max reps for the exercise
	 */
	public void setMaxReps(int maxReps) {
		this.maxReps = maxReps;
	}

	/**
	 * Prints out the current Set ArrayList
	 * 
	 * @return output
	 */
	public String printSetList() {
		String output = "";
		int i = 1;

		for (Set set : this.setList) {
			output += i + ": " + set + "\n";
			i++;
		}

		return output;
	}

	/**
	 * Sets the current max weight, reps, volume, and estimates the user's one rep
	 * max for the current exercise
	 */
	public void setMaxValues() {
		///// Setting Max Weight /////
		double amount = 0;
		double max = 0;
		for (Set set : this.setList) {
			if (set.getWeight() > amount) {
				amount = set.getWeight();
			}
		}
		this.maxWeight = amount;

		///// Setting One Rep Max /////
		amount = 0;
		max = 0;
		for (Set set : this.setList) {
			amount = set.getWeight() * (1 + (set.getReps() / 30));
			if (amount > max) {
				max = amount;
			}
		}
		this.oneRepMax = max;

		///// Setting Max Reps /////
		amount = 0;
		for (Set set : this.setList) {
			if (set.getReps() > amount) {
				amount = set.getReps();
			}
		}
		this.maxReps = amount;

		///// Setting Max Volume /////
		amount = 0;
		max = 0;
		for (Set set : this.setList) {
			amount = set.getWeight() * set.getReps();

			if (amount > max) {
				max = amount;
			}
		}
		this.totalVolume = max;
	}

	public String toString() {
		return "Exercise name: " + this.exerciseName + " Exercise Type: " + this.exerciseType;
	}

	@Override
	public int compareTo(Exercise exercise) {
		return this.exerciseName.compareToIgnoreCase(exercise.exerciseName);
	}

}
