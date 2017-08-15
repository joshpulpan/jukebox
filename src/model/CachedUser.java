package model;

import java.time.Duration;

/**
 * A cached object that keeps track of a user's plays and time remaining.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class CachedUser {
	private String name;
	private int plays;
	private Duration allottedTime;

	/**
	 * Builds an object that can store all information related to a user for
	 * display in the view.
	 * 
	 * @param name
	 *            the name of the user
	 * @param plays
	 *            the number of plays for the user
	 * @param allottedTime
	 *            the allotted time left to the user
	 */
	public CachedUser(String name, int plays, Duration allottedTime) {
		this.name = name;
		this.plays = plays;
		this.allottedTime = allottedTime;
	}

	/**
	 * Get name.
	 * 
	 * @return the name of the user
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get plays.
	 * 
	 * @return the number of plays for the user
	 */
	public int getPlays() {
		return this.plays;
	}

	/**
	 * Get allotted time.
	 * 
	 * @return the allotted time left to the user
	 */
	public Duration getAllottedTime() {
		return this.allottedTime;
	}
}
