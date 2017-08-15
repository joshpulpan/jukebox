package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import model.dataservice.DataService;
import model.dataservice.DataServiceException;
import model.dataservice.Song;
import model.dataservice.SongDb;
import model.dataservice.User;
import model.dataservice.UserDb;
import model.dataservice.xml.XmlDataService;

/**
 * Test for the XML DataService. Uses the XML files used in the actual
 * application so any changes there must be reflected here.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class TestXmlDataService {
	private static final File SONG_DB_FILE = new File("resources/SongDb.xml");
	private static final File USER_DB_FILE = new File("resources/UserDb.xml");

	private DataService xmlDataService;

	/**
	 * Set up a new XmlDataService for tests.
	 */
	@Before
	public void setUp() {
		this.xmlDataService = new XmlDataService(SONG_DB_FILE, USER_DB_FILE);
	}

	/**
	 * Test that reading the song database works.
	 * 
	 * @throws DataServiceException
	 *             thrown when failing to read the database
	 */
	@Test
	public void testReadSongDb() throws DataServiceException {
		SongDb songDb = this.xmlDataService.readSongDb();

		List<Song> songs = songDb.getSongs();
		assertEquals(9, songs.size());

		UUID songId = UUID.fromString("4158af70-2792-4214-b449-2d966b5f85b5");
		Song song = songDb.getSongById(songId);
		assertTrue(song != null);

		assertEquals("Kevin Macleod", song.getArtist());
		assertEquals("Danse Macabre", song.getTitle());
		assertEquals("DanseMacabreViolinHook.mp3", song.getFilename());
		assertEquals(Duration.ofSeconds(34), song.getLength());
		assertEquals("00:34 Danse Macabre by Kevin Macleod", song.toString());
	}

	/**
	 * Test that reading the user database works.
	 * 
	 * @throws DataServiceException
	 *             thrown when failing to read the database
	 */
	@Test
	public void testReadUserDb() throws DataServiceException {
		UserDb userDb = this.xmlDataService.readUserDb();

		UUID userId = UUID.fromString("c50d3ee4-6e8b-4b8b-ba8a-21ca7dd5f534");
		User user = userDb.getUserById(userId);
		assertTrue(user != null);

		assertEquals("Chris", user.getName());
		assertEquals("1", user.getPassword());

		user = userDb.getUserByName("Devon");
		assertTrue(user != null);

		assertEquals("Devon", user.getName());
		assertEquals("22", user.getPassword());
	}

	/**
	 * Test that incorrect song database fails.
	 * 
	 * @throws DataServiceException
	 *             thrown intentionally
	 */
	@Test(expected = DataServiceException.class)
	public void testReadSongDbError() throws DataServiceException {
		this.xmlDataService = new XmlDataService(new File("SongDx.xml"), USER_DB_FILE);
		this.xmlDataService.readSongDb();
	}

	/**
	 * Test that incorrect user database fails.
	 * 
	 * @throws DataServiceException
	 *             thrown intentionally.
	 */
	@Test(expected = DataServiceException.class)
	public void testReadUserDbError() throws DataServiceException {
		this.xmlDataService = new XmlDataService(SONG_DB_FILE, new File("UserDx.xml"));
		this.xmlDataService.readUserDb();
	}
}
