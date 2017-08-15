package model;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import model.dataservice.DataService;
import model.dataservice.DataServiceException;
import model.dataservice.Song;
import model.dataservice.SongDb;
import model.dataservice.User;
import model.dataservice.UserDb;
import songplayer.EndOfSongEvent;
import songplayer.EndOfSongListener;
import songplayer.SongPlayer;

/**
 * This is the model for the Jukebox. It allows adding songs to the queue,
 * authorizing users, and playing the song at the top of the queue.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class JukeboxModel extends Observable {
	private SongDb songDb;
	private UserDb userDb;
	private SongCache songCache;
	private UserCache userCache;
	private SongQueue songQueue;
	private UUID currentUser;
	private QueuePlayer queuePlayer;

	/**
	 * Construct a new model with empty play counts for all users and songs and
	 * initialize an empty song queue.
	 * 
	 * @param dateService
	 *            a DateService for getting midnight
	 */
	public JukeboxModel(DateService dateService) {
		this.songCache = new SongCache();
		this.userCache = new UserCache();
		new ResetTimer(dateService).start();
	}

	/**
	 * Bind this model to a specific DataService for getting user and song
	 * information. Also starts the thread that plays songs.
	 * 
	 * @param dataService
	 *            a DataService for reading user and song information
	 * @throws DataServiceException
	 *             thrown when the databases can't be read
	 */
	public void bind(DataService dataService) throws DataServiceException {
		this.songDb = dataService.readSongDb();
		this.userDb = dataService.readUserDb();
		this.songQueue = new SongQueue(this.songDb);
		this.queuePlayer = new QueuePlayer(this.songQueue);
		this.songQueue.addListDataListener(this.queuePlayer);
		this.queuePlayer.start();

		this.userCache.bind(this.userDb);
	}

	/**
	 * Queue a song. Returns a status representing either that the song was
	 * added to the queue or a reason why it was not.
	 * 
	 * @param songId
	 *            the id of a song to queue
	 * @return an event representing the result of adding the song
	 */
	public QueueEvent queueSong(UUID songId) {
		Song song = this.songDb.getSongById(songId);
		CachedUser user;
		if (this.currentUser != null) {
			user = this.userCache.getUserData(this.currentUser);
		} else {
			return QueueEvent.NO_USER_LOGGED_IN;
		}
		if (song == null) {
			return QueueEvent.SONG_NOT_FOUND;
		}
		if (this.songCache.getPlays(songId) >= 3) {
			return QueueEvent.SONG_EXCEEDED_PLAY_LIMIT;
		}
		if (user.getPlays() >= 3) {
			return QueueEvent.USER_EXCEEDED_PLAY_LIMIT;
		}
		if (user.getAllottedTime().compareTo(song.getLength()) < 0) {
			return QueueEvent.USER_EXCEEDED_TIME_LIMIT;
		}
		this.songCache.incPlays(songId);
		this.userCache.playSong(this.currentUser, song);
		setChanged();
		notifyObservers(this.userCache.getUserData(this.currentUser));
		this.songQueue.add(songId);
		return QueueEvent.SONG_ADDED;
	}

	/**
	 * Tries to log a user in. Returns true if successful and notifies any
	 * observers. Otherwise, returns false.
	 * 
	 * @param name
	 *            the user's account name
	 * @param password
	 *            the user's password
	 * @return true if logged in, false otherwise
	 */
	public boolean authUser(String name, String password) {
		User user = this.userDb.getUserByName(name);
		if (user != null && user.getPassword().equals(password)) {
			this.currentUser = user.getId();
			updateUser();
			return true;
		}
		return false;
	}

	/**
	 * Logs the current user out (sort of). The way this works is that notifying
	 * observers usually includes some user data as an argument. This is the
	 * only time that is not true, so if the observer doesn't get an argument it
	 * knows the user has logged out.
	 */
	public void signOut() {
		this.currentUser = null;
		updateUser();
	}

	/**
	 * Get the SongQueue.
	 * 
	 * @return a SongQueue object for this model
	 */
	public SongQueue getSongQueue() {
		return this.songQueue;
	}

	/**
	 * Get the song database.
	 * 
	 * @return the song database for this model
	 */
	public SongDb getSongDb() {
		return this.songDb;
	}

	/**
	 * Read the state in from a file.
	 * 
	 * @param state
	 *            where to read the state
	 * @throws PersistenceServiceException
	 *             thrown when the state cannot be read
	 */
	public void readState(File state) throws PersistenceServiceException {
		PersistenceService p = new PersistenceService(state);
		this.userCache = p.readUsers(this.userDb);
		if (LocalDate.now().equals(p.readDate())) {
			this.songCache = p.readSongs();
		} else {
			this.userCache.resetCache();
		}
		List<UUID> songList = p.readSongList();
		for (UUID song : songList) {
			this.songQueue.add(song);
		}
	}

	/**
	 * Writes the current state to the given file.
	 * 
	 * @param state
	 *            where to write the state
	 * @throws PersistenceServiceException
	 *             throw when the state cannot be written
	 */
	public void writeState(File state) throws PersistenceServiceException {
		PersistenceService p = new PersistenceService(state);
		p.saveState(this.songCache, this.songQueue, this.userCache);
	}

	protected SongCache getSongCache() {
		return this.songCache;
	}

	protected UserCache getUserCache() {
		return this.userCache;
	}

	protected void updateUser() {
		setChanged();
		if (this.currentUser != null) {
			notifyObservers(this.userCache.getUserData(this.currentUser));
		} else {
			notifyObservers();
		}
	}

	private class QueuePlayer extends Thread implements EndOfSongListener, ListDataListener {
		private SongQueue queue;
		private boolean playing;

		public QueuePlayer(SongQueue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while (true) {
				// we just listen for events so loop infinitely
			}
		}

		@Override
		public void songFinishedPlaying(EndOfSongEvent e) {
			this.queue.remove();
			this.playing = playNextSong();
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			if (!this.playing) {
				this.playing = playNextSong();
			}
		}

		@Override
		public void intervalAdded(ListDataEvent e) {
			// unused method stub
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			// unused method stub
		}

		private boolean playNextSong() {
			if (!this.queue.isEmpty()) {
				UUID song = this.queue.peek();
				String file = "songfiles/" + JukeboxModel.this.getSongDb().getSongById(song).getFilename();
				SongPlayer.playFile(this, file);
				return true;
			}
			return false;
		}
	}

	private class ResetTimer extends Thread {
		private DateService dateService;

		public ResetTimer(DateService dateService) {
			this.dateService = dateService;
		}

		@Override
		public void run() {
			while (true) {
				LocalDateTime midnight = this.dateService.getMidnight();
				long delta = LocalDateTime.now().until(midnight, ChronoUnit.MILLIS);
				try {
					sleep(delta);
					JukeboxModel.this.getSongCache().resetCache();
					JukeboxModel.this.getUserCache().resetCache();
					JukeboxModel.this.updateUser();
				} catch (InterruptedException e) {
					// don't worry about being interrupted
				}
			}
		}
	}
}
