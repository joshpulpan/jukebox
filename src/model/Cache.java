package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This file implements a simple cache for counting plays (by song or user).
 * Mainly a small wrapper for the Map.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public abstract class Cache implements Serializable {
	private static final long serialVersionUID = -814096284360934367L;
	protected Map<UUID, Integer> plays;

	/**
	 * Gets the number of plays associated with the cache object.
	 * 
	 * @param id
	 *            the ID of the needed cache object
	 * @return the number of plays associated with that id
	 */
	public int getPlays(UUID id) {
		cache(id);
		return this.plays.get(id).intValue();
	}

	/**
	 * Increments the plays associated with an ID (user or song).
	 * 
	 * @param id
	 *            the id to increment plays
	 */
	public void incPlays(UUID id) {
		int oldValue = getPlays(id);
		this.plays.put(id, Integer.valueOf(++oldValue));
	}

	/**
	 * Resets the number of plays associated with all IDs.
	 */
	public void resetCache() {
		this.plays = new HashMap<>();
	}

	protected abstract void cache(UUID id);
}
