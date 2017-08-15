package model.dataservice;

import java.util.List;
import java.util.UUID;

/**
 * An interface for the SongDb. This interface is unfortunately limited. Callers
 * should expect a data structure containing all songs in the entire database so
 * if there are many this is probably going to push up against JVM memory
 * limits. C'est la vie. :(
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public interface SongDb {
	/**
	 * Get a list of all songs in the database.
	 * 
	 * @return a list with all songs
	 */
	public List<Song> getSongs();

	/**
	 * Get a specific song by its ID.
	 * 
	 * @param id
	 *            the id of the song
	 * @return a Song object represented by that ID
	 */
	public Song getSongById(UUID id);
}
