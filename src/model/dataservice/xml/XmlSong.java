package model.dataservice.xml;

import java.time.Duration;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import model.dataservice.Song;

/**
 * An implementation of the Song interface that reads song information from an
 * XML file. Elements look like:
 * <song id="...">
 * 	<artist>...</artist>
 * 	<title>...</title>
 * 	<filename>...</filename>
 * 	<length>...</length>
 * </song>
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
@XmlRootElement(name = "song")
public class XmlSong implements Song {

	@XmlAttribute
	private UUID id;
	@XmlElement
	private String artist;
	@XmlElement
	private String title;
	@XmlElement
	private String filename;
	@XmlElement
	private long length;
	
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
		return Duration.ofSeconds(this.length);
	}

	@Override
	public String toString() {
		return formatLength() + " " + getTitle() + " by " + getArtist();
	}
	
	private String formatLength() {
		Long min = Long.valueOf(this.length % 3600 / 60);
		Long sec = Long.valueOf(this.length % 60);
		return String.format("%02d:%02d", min, sec);
	}
}
