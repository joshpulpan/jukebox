package model.dataservice;

import java.time.Duration;
import java.util.UUID;

/**
 * An interface for a song object. This should be created somehow by the
 * DataService.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public interface Song {
	/**
	 * Get song ID.
	 * 
	 * @return the id of the song
	 */
	public UUID getId();

	/**
	 * Get song artist.
	 * 
	 * @return the artist of the song
	 */
	public String getArtist();

	/**
	 * Get song title.
	 * 
	 * @return the title of the song
	 */
	public String getTitle();

	/**
	 * Get song file name.
	 * 
	 * @return the file name of the song
	 */
	public String getFilename();

	/**
	 * Get song length.
	 * 
	 * @return the length of the song as a Duration
	 */
	public Duration getLength();
}
