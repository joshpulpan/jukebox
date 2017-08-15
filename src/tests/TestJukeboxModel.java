package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import model.JukeboxModel;
import model.PersistenceServiceException;
import model.QueueEvent;
import model.RealDateService;
import model.SongQueue;
import model.dataservice.DataService;
import model.dataservice.DataServiceException;
import model.dataservice.Song;
import tests.dataservice.mock.MockDataService;

/**
 * Tests for the Jukebox model. Uses a mocked DataService to provide data and a
 * mocked DateService to reset the data every 3 seconds.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class TestJukeboxModel {

	private DataService dataService;
	private JukeboxModel model;

	/**
	 * Create a new JukeboxModel for testing.
	 * 
	 * @throws DataServiceException
	 *             never thrown
	 */
	@Before
	public void setUp() throws DataServiceException {
		this.dataService = new MockDataService(true, true);
		this.model = new JukeboxModel(new MockDateService());

		this.model.bind(this.dataService);
	}

	/**
	 * Test that songs are queued properly and all QueueEvents fire.
	 */
	@Test
	public void testQueueSong() {
		List<Song> songs = this.model.getSongDb().getSongs();

		UUID song1 = songs.get(0).getId();
		UUID song2 = songs.get(1).getId();
		UUID song3 = songs.get(2).getId();
		UUID song4 = songs.get(3).getId();

		UUID noSong = UUID.fromString("00000000-0000-0000-0000-000000000006");

		assertEquals(QueueEvent.NO_USER_LOGGED_IN, this.model.queueSong(song1));
		assertTrue(this.model.authUser("MockUser1", "MockPassword1"));
		assertEquals(QueueEvent.SONG_NOT_FOUND, this.model.queueSong(noSong));
		assertEquals(QueueEvent.SONG_ADDED, this.model.queueSong(song1));
		assertEquals(QueueEvent.SONG_ADDED, this.model.queueSong(song1));
		assertEquals(QueueEvent.SONG_ADDED, this.model.queueSong(song1));
		assertEquals(QueueEvent.USER_EXCEEDED_PLAY_LIMIT, this.model.queueSong(song2));
		this.model.signOut();
		assertEquals(QueueEvent.NO_USER_LOGGED_IN, this.model.queueSong(song2));
		assertTrue(this.model.authUser("MockUser2", "MockPassword2"));
		assertEquals(QueueEvent.SONG_ADDED, this.model.queueSong(song2));
		assertEquals(QueueEvent.SONG_EXCEEDED_PLAY_LIMIT, this.model.queueSong(song1));
		assertEquals(QueueEvent.SONG_ADDED, this.model.queueSong(song2));
		assertTrue(this.model.authUser("MockUser3", "MockPassword3"));
		assertEquals(QueueEvent.SONG_ADDED, this.model.queueSong(song4));
		assertEquals(QueueEvent.SONG_ADDED, this.model.queueSong(song3));
		assertEquals(QueueEvent.USER_EXCEEDED_TIME_LIMIT, this.model.queueSong(song3));
		assertEquals(QueueEvent.SONG_ADDED, this.model.queueSong(song2));
		assertFalse(this.model.authUser("MockUser1", "FakePassword"));
		assertFalse(this.model.authUser("FakeUser", "FakePassword"));
	}

	/**
	 * Test that the queue works, and that its ListModel works.
	 */
	@Test
	public void testSongQueue() {
		SongQueue queue = this.model.getSongQueue();
		ListDataListener listDataListener = new ListDataListener() {
			@Override
			public void intervalAdded(ListDataEvent listDataEvent) {
				// unused method stub
			}

			@Override
			public void intervalRemoved(ListDataEvent listDataEvent) {
				// unused method stub
			}

			@Override
			public void contentsChanged(ListDataEvent listDataEvent) {
				// unused method stub
			}
		};
		queue.addListDataListener(listDataListener);

		List<Song> songs = this.model.getSongDb().getSongs();
		UUID song = songs.get(0).getId();
		this.model.authUser("MockUser1", "MockPassword1");
		this.model.queueSong(song);
		queue.remove();
		queue.removeListDataListener(listDataListener);
	}

	/**
	 * Tests saving and reading state.
	 * 
	 * @throws IOException
	 *             thrown on file errors
	 * @throws InterruptedException
	 *             thrown if halted while sleeping
	 * @throws PersistenceServiceException
	 *             thrown if there was a problem with persistence
	 */
	@Test
	public void testState() throws IOException, InterruptedException, PersistenceServiceException {
		List<Song> songs = this.model.getSongDb().getSongs();
		UUID song = songs.get(0).getId();

		this.model.authUser("MockUser1", "MockPassword1");
		this.model.queueSong(song);
		this.model.queueSong(song);
		this.model.queueSong(song);

		TemporaryFolder tmp = new TemporaryFolder();
		tmp.create();
		File file = tmp.newFile("TestState.dat");

		this.model.writeState(file);

		// our mock date service resets every 3 seconds, so we wait 3.5
		Thread.sleep(3500);

		assertEquals(QueueEvent.SONG_ADDED, this.model.queueSong(song));
		this.model.readState(file);
		assertEquals(QueueEvent.SONG_EXCEEDED_PLAY_LIMIT, this.model.queueSong(song));

		Thread.sleep(3500);

		assertEquals(QueueEvent.SONG_ADDED, this.model.queueSong(song));
		this.model.readState(file);
		assertEquals(QueueEvent.SONG_EXCEEDED_PLAY_LIMIT, this.model.queueSong(song));
	}

	/**
	 * Make sure the date is properly reported by the DateService.
	 */
	@Test
	public void testRealDateService() {
		assertEquals(new RealDateService().getMidnight(),
				LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT));
	}

	/**
	 * Test the QueueEvent class.
	 */
	@Test
	public void testQueueEvent() {
		assertEquals(QueueEvent.SONG_ADDED, QueueEvent.valueOf("SONG_ADDED"));
	}

	/**
	 * Test that binding a broken DataService fails.
	 *
	 * @throws DataServiceException
	 *             thrown intentionally
	 */
	@Test(expected = DataServiceException.class)
	public void testDataServiceFailureSong() throws DataServiceException {
		this.model.bind(new MockDataService(false, true));
	}

	/**
	 * Test that binding a broken DataService fails.
	 * 
	 * @throws DataServiceException
	 *             thrown intentionally
	 */
	@Test(expected = DataServiceException.class)
	public void testDataServiceFailureUser() throws DataServiceException {
		this.model.bind(new MockDataService(true, false));
	}

	/**
	 * Test that read failures are thrown.
	 * 
	 * @throws IOException
	 *             thrown if there was a problem creating the file
	 * @throws PersistenceServiceException
	 *             thrown intentionally
	 */
	@Test(expected = PersistenceServiceException.class)
	public void testPersistenceServiceFailureRead() throws IOException, PersistenceServiceException {
		TemporaryFolder tmp = new TemporaryFolder();
		tmp.create();
		File file = tmp.newFile("TestState.xml");
		file.setReadable(false);
		this.model.readState(file);
	}

	/**
	 * Test that write failures are thrown.
	 * 
	 * @throws IOException
	 *             thrown if there was a problem creating the file
	 * @throws PersistenceServiceException
	 *             thrown intentionally
	 */
	@Test(expected = PersistenceServiceException.class)
	public void testPersistenceServiceFailureWrite() throws IOException, PersistenceServiceException {
		TemporaryFolder tmp = new TemporaryFolder();
		tmp.create();
		File file = tmp.newFile("TestState.xml");
		file.setWritable(false);
		this.model.writeState(file);
	}
}
