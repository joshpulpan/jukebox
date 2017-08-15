package tests.dataservice.mock;

import java.time.Duration;
import java.util.UUID;

import model.dataservice.Song;

/**
 * A mock song class for testing.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class MockSong implements Song {
	private UUID id;
	private String artist;
	private String title;
	private String filename;
	private Duration length;

	/**
	 * Creates a new MockSong for testing.
	 * 
	 * @param id the id of the song
	 * @param artist the artist of the song
	 * @param title the title of the song
	 * @param filename the filename of the song
	 * @param length the length of the song
	 */
	public MockSong(UUID id, String artist, String title, String filename, Duration length) {
		this.id = id;
		this.artist = artist;
		this.title = title;
		this.filename = filename;
		this.length = length;
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public String getArtist() {
		return this.artist;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getFilename() {
		return this.filename;
	}

	@Override
	public Duration getLength() {
		return this.length;
	}
}
