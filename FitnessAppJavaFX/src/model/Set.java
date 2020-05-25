package model;

/**
 * This class creates a Set object to be used by an Exercise object. Sets
 * contain two attributes: reps and weight. This allows the user to perform an
 * exercise a certain number of times at a specified weight.
 * 
 * @author Brenton Haliw
 *
 */
public class Set {

	double reps;
	double weight;

	/**
	 * Default constructor for the Set class
	 */
	public Set() {
		reps = 0;
		weight = 0;
	}

	/**
	 * Used for loading already created sets
	 * 
	 * @param reps
	 * @param weight
	 */
	public Set(double reps, double weight) {
		this.reps = reps;
		this.weight = weight;
	}

	/**
	 * Returns the amount of reps completed for this Set
	 * 
	 * @return reps
	 */
	public double getReps() {
		return this.reps;
	}

	/**
	 * Returns the current weight used during this Set.
	 * 
	 * @return weight
	 */
	public double getWeight() {
		return this.weight;
	}

	/**
	 * Sets the reps in this Set to a specified number
	 * 
	 * @param reps
	 */
	public void setReps(int reps) {
		this.reps = reps;
	}

	/**
	 * Sets the weight in this Set to a specified number
	 * 
	 * @param weight
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * Will increase the amount of reps by 1 (used by button push?)
	 * 
	 * @return
	 */
	public double increaseReps() {
		this.reps += 1;
		return this.reps;
	}

	/**
	 * Will decrease the amount of reps by 1 (used by button push?)
	 * 
	 * @return reps
	 */
	public double decreaseReps() {
		this.reps -= 1;
		return this.reps;
	}

	/**
	 * Will increase the weight by 5 lbs (used by button push?)
	 * 
	 * @return weight
	 */
	public double increaseWeight() {
		this.weight += 5;
		return this.weight;
	}

	/**
	 * Will decrease the weight by 5 lbs (used by button push?)
	 * 
	 * @return weight
	 */
	public double decreaseWeight() {
		this.weight -= 5;
		return this.weight;
	}

	public String toString() {
		return "Reps: " + this.reps + " Weight: " + this.weight;
	}

}
