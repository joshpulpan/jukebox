package model.dataservice;

/**
 * An interface representing some type of data service. The data service must be
 * able to create a SongDb and a UserDb.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public interface DataService {
	/**
	 * Read and return a valid SongDb.
	 * 
	 * @return a new SongDb with the requested songs
	 * @throws DataServiceException
	 *             thrown when there was a problem reading the data
	 */
	public SongDb readSongDb() throws DataServiceException;

	/**
	 * Read and return a valid UserDb.
	 * 
	 * @return a new UserDb with the requested users
	 * @throws DataServiceException
	 *             thrown when there was a problem reading the data
	 */
	public UserDb readUserDb() throws DataServiceException;
}
