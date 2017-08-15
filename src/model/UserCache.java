package model;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import model.dataservice.Song;
import model.dataservice.UserDb;

/**
 * An extended cache that keeps track of both the number of plays that a user
 * has for the day as well as their total time allotted. Only resets plays when
 * asked. Allotted time is tracked as long as the program is running.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class UserCache extends Cache {
	private static final long serialVersionUID = -8085520546494929915L;
	private static final Duration MAX_TIME = Duration.ofMinutes(1500);
	private transient UserDb userDb;
	private Map<UUID, Duration> allottedTime;

	/**
	 * Create a new cache for user plays and time allotted.
	 */
	public UserCache() {
		this.plays = new HashMap<>();
		this.allottedTime = new HashMap<>();
	}

	/**
	 * Bind this user cache to a user database. This is used to retrieve user
	 * names on demand.
	 * 
	 * @param userDb
	 *            the database to bind to this cache
	 */
	public void bind(UserDb userDb) {
		this.userDb = userDb;
	}

	/**
	 * Return a new object with information about the specified user. These
	 * objects are transient and will be invalid after a single transaction. The
	 * intent is that their data is extracted by the caller immediately and then
	 * they are destroyed. DO NOT RELY ON THESE FOR TRACKING USERS!!
	 * 
	 * @param id
	 *            the id of a user
	 * @return a CachedUser that represents that id
	 */
	public CachedUser getUserData(UUID id) {
		cache(id);
		return new CachedUser(this.userDb.getUserById(id).getName(), this.getPlays(id), this.allottedTime.get(id));
	}

	/**
	 * Remove the length of a song from the users allotted time and increment
	 * the number of plays they have for the day.
	 * 
	 * @param id
	 *            the id of the user playing the song
	 * @param song
	 *            the id of the played song
	 */
	public void playSong(UUID id, Song song) {
		cache(id);
		long oldTime = this.allottedTime.get(id).toNanos();
		Duration newTime = Duration.ofNanos(oldTime - song.getLength().toNanos());
		this.allottedTime.put(id, newTime);
		incPlays(id);
	}

	@Override
	protected void cache(UUID id) {
		if (!this.allottedTime.containsKey(id)) {
			this.allottedTime.put(id, MAX_TIME);
		}
		if (!this.plays.containsKey(id)) {
			this.plays.put(id, Integer.valueOf(0));
		}
	}
}
