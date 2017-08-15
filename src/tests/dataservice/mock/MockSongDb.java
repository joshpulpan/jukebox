package tests.dataservice.mock;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import model.dataservice.Song;
import model.dataservice.SongDb;

/**
 * A mock song database for testing.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public final class MockSongDb implements SongDb {
	private static final MockSongDb INSTANCE = new MockSongDb();

	private MockSongDb() {
	}

	/**
	 * Get the MockSongDb. Enforces singleton property.
	 * 
	 * @return the instance of this class.
	 */
	public static SongDb getInstance() {
		return INSTANCE;
	}

	@Override
	public List<Song> getSongs() {
		List<Song> songs = new ArrayList<>(3);
		songs.add(new MockSong(UUID.fromString("00000000-0000-0000-0000-000000000001"), "MockArtist1", "MockTitle1",
				"MockFilename1", Duration.ofSeconds(1)));
		songs.add(new MockSong(UUID.fromString("00000000-0000-0000-0000-000000000002"), "MockArtist2", "MockTitle2",
				"MockFilename2", Duration.ofSeconds(1)));
		songs.add(new MockSong(UUID.fromString("00000000-0000-0000-0000-000000000003"), "MockArtist3", "MockTitle3",
				"MockFilename3", Duration.ofSeconds(59)));
		songs.add(new MockSong(UUID.fromString("00000000-0000-0000-0000-000000000004"), "MockArtist4", "MockTitle4",
				"MockFilename4", Duration.ofMinutes(1499)));
		return songs;
	}

	@Override
	public Song getSongById(UUID id) {
		Optional<Song> song = getSongs().stream().filter(s -> s.getId().equals(id)).findFirst();
		return song.isPresent() ? song.get() : null;
	}
}
