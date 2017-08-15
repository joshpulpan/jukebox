package model.dataservice.xml;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import model.dataservice.Song;
import model.dataservice.SongDb;

/**
 * An implementation of a SongDb that reads information from an XML file. A file
 * should look something like:
 * <songs>
 * 	<song ...> ... </song>
 * 	<song ...> ... </song>
 * 	...
 * </songs>
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
@XmlRootElement(name = "songs")
public class XmlSongDb implements SongDb {
	@XmlElement(name = "song", type = XmlSong.class)
	private List<Song> songs;

	private XmlSongDb() {
		// disallow object creation outside of JAXB
	}
	
	@Override
	public List<Song> getSongs() {
		return this.songs;
	}

	@Override
	public Song getSongById(UUID id) {
		Optional<Song> song = this.songs.stream().filter(s -> s.getId().equals(id)).findFirst();
		return song.isPresent() ? song.get() : null;
	}
}
