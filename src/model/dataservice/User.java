package model.dataservice;

import java.util.UUID;

/**
 * An interface for a user object. Must be created by the DataService.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public interface User {
	/**
	 * Get user ID.
	 * 
	 * @return the id of the user
	 */
	public UUID getId();

	/**
	 * Get user name.
	 * 
	 * @return the name of the user
	 */
	public String getName();

	/**
	 * Get user password.
	 * 
	 * @return the password of the user
	 */
	public String getPassword();
}
