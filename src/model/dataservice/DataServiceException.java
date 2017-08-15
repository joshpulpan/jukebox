package model.dataservice;

/**
 * An exception representing a failure in the DataService. Requires a message to
 * pass to the caller.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class DataServiceException extends Exception {

	private static final long serialVersionUID = 4669518759180853403L;

	/**
	 * Create a new DataServiceExcpetion with the specified message.
	 * 
	 * @param message
	 *            the message to pass to the caller
	 */
	public DataServiceException(String message) {
		super(message);
	}
}
