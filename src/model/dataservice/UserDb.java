package model.dataservice;

import java.util.UUID;

/**
 * An interface for the UserDb. This interface is not as limited as the SongDb
 * and allows this structure to be built lazily if necessary.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public interface UserDb {
	/**
	 * Get a user by ID.
	 * 
	 * @param id
	 *            the id of the user
	 * @return a User object represented by that id
	 */
	public User getUserById(UUID id);

	/**
	 * Get a user by name.
	 * 
	 * @param name
	 *            the name of the user
	 * @return a User object represented by that name
	 */
	public User getUserByName(String name);
}
