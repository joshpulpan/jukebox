package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.dataservice.UserDb;

/**
 * A small service for enabling persistence of the model. This implementation
 * uses XML to save and load the data.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class PersistenceService {
	private File stateFile;
	private List<Object> state;

	/**
	 * Create a new persistence service tied to the given file.
	 * 
	 * @param stateFile
	 *            where to find persistence information
	 */
	public PersistenceService(File stateFile) {
		this.stateFile = stateFile;
	}

	/**
	 * Save the current state to a file.
	 * 
	 * @param songCache
	 *            the cache of songs
	 * @param songQueue
	 *            the play queue
	 * @param userCache
	 *            the cache of users
	 * @throws PersistenceServiceException
	 *             thrown when persistence data cannot be written
	 */
	public void saveState(SongCache songCache, SongQueue songQueue, UserCache userCache)
			throws PersistenceServiceException {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(this.stateFile))) {
			List<Object> fileState = new ArrayList<>();
			fileState.add(LocalDate.now());
			fileState.add(songCache);
			fileState.add(userCache);
			List<UUID> queue = new ArrayList<>();
			for (int i = 0; i < songQueue.getSize(); i++) {
				queue.add(songQueue.getElementAt(i).getId());
			}
			fileState.add(queue);
			output.writeObject(fileState);
		} catch (IOException e) {
			throw new PersistenceServiceException("couldn't save persistence data");
		}
	}

	/**
	 * Read the date from this state file.
	 * 
	 * @return the date the file was save
	 * @throws PersistenceServiceException
	 *             thrown when data cannot be read
	 */
	public LocalDate readDate() throws PersistenceServiceException {
		Object date = getState().get(0);
		if (date instanceof LocalDate) {
			return (LocalDate) date;
		}
		throw new PersistenceServiceException("could not read date");
	}

	/**
	 * Read the songs from this state file.
	 * 
	 * @return a SongCache with the saved state
	 * @throws PersistenceServiceException
	 *             thrown when date cannot be read
	 */
	public SongCache readSongs() throws PersistenceServiceException {
		Object songCache = getState().get(1);
		if (songCache instanceof SongCache) {
			return (SongCache) songCache;
		}
		throw new PersistenceServiceException("could not read song cache");
	}

	/**
	 * Read the users from this state file.
	 * 
	 * @param userDb
	 *            a user database to tie to the users
	 * @return a UserCache object with the saved state
	 * @throws PersistenceServiceException
	 *             thrown when data cannot be read
	 */
	public UserCache readUsers(UserDb userDb) throws PersistenceServiceException {
		Object userCache = getState().get(2);
		if (userCache instanceof UserCache) {
			((UserCache) userCache).bind(userDb);
			return (UserCache) userCache;
		}
		throw new PersistenceServiceException("could not read user cache");
	}

	/**
	 * Read the play list from this state file. This is technically unchecked
	 * but if you look closely it really is. Unfortunately the compiler doesn't
	 * really like what we do here. Too bad for it.
	 * 
	 * @return a list of songs for adding to the queue
	 * @throws PersistenceServiceException
	 *             thrown when data cannot be read
	 */
	@SuppressWarnings("unchecked")
	public List<UUID> readSongList() throws PersistenceServiceException {
		Object songList = getState().get(3);
		if (songList instanceof List<?>) {
			if (!(((List<?>) songList).isEmpty()) && ((List<?>) songList).get(0) instanceof UUID) {
				return (List<UUID>) songList;
			} else {
				return new ArrayList<UUID>(0);
			}
		}
		throw new PersistenceServiceException("could not read song queue");
	}

	@SuppressWarnings("unchecked")
	private List<Object> getState() throws PersistenceServiceException {
		if (this.state == null) {
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(this.stateFile))) {
				Object fileState = input.readObject();
				if (fileState instanceof List && ((List<?>) fileState).get(0) != null) {
					this.state = (List<Object>) fileState;
				}
				if (this.state.size() != 4) {
					throw new ClassNotFoundException();
				}
			} catch (ClassNotFoundException | IOException e) {
				throw new PersistenceServiceException("couldn't read persistence data");
			}
		}
		return this.state;
	}
}
