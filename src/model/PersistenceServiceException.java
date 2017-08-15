package model;

/**
 * An exception for reporting problems with the persistence service.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class PersistenceServiceException extends Exception {

	private static final long serialVersionUID = 5248468570538400628L;

	/**
	 * Create a new persistence exception.
	 * 
	 * @param message
	 *            the message to pass to the caller
	 */
	public PersistenceServiceException(String message) {
		super(message);
	}
}
