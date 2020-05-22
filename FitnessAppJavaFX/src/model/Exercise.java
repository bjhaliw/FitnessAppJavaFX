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

	String exerciseName;
	String exerciseType;
	ArrayList<Set> setList;
	double maxWeight;

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
	}

	/**
	 * Adds a Set object for the exercise
	 * 
	 * @param set
	 */
	public void addSet(Set set) {
		this.setList.add(set);
	}

	/**
	 * Removes the indicated set.
	 * 
	 * @param set
	 */
	public void removeSet(Set set) {
		this.setList.remove(set);
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
	 * Returns the max amount of weight done in the sets for the exercise
	 * @return amount
	 */
	public double getMaxWeight() {
		double amount = 0;
		
		for(Set set : this.setList) {
			if (set.getWeight() > amount) {
				amount = set.getWeight();
			}
		}
		
		return amount;
	}

	public String toString() {
		return "Exercise name: " + this.exerciseName + " Exercise Type: " + this.exerciseType;
	}

	@Override
	public int compareTo(Exercise exercise) {
		return this.exerciseName.compareToIgnoreCase(exercise.exerciseName);
	}

}
