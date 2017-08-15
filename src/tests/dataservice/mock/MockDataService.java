package tests.dataservice.mock;

import model.dataservice.DataService;
import model.dataservice.DataServiceException;
import model.dataservice.SongDb;
import model.dataservice.UserDb;

/**
 * A mocked DataService with known values.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class MockDataService implements DataService {
	private boolean songPass;
	private boolean userPass;

	/**
	 * Create a new MockDataService. The caller can specify if they want
	 * exceptions or not.
	 * 
	 * @param songPass
	 *            whether to successfully create the song database
	 * @param userPass
	 *            whether to successfully create the user database
	 */
	public MockDataService(boolean songPass, boolean userPass) {
		this.songPass = songPass;
		this.userPass = userPass;
	}

	/**
	 * "Read" the SongDb.
	 */
	@Override
	public SongDb readSongDb() throws DataServiceException {
		if (!this.songPass) {
			throw new DataServiceException("test asked for a mock failure in the song db");
		}
		return MockSongDb.getInstance();
	}

	/**
	 * "Read" the UserDb.
	 */
	@Override
	public UserDb readUserDb() throws DataServiceException {
		if (!this.userPass) {
			throw new DataServiceException("test asked for a mock failure in the user db");
		}
		return MockUserDb.getInstance();
	}
}
