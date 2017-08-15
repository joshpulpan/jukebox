package model.dataservice.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.dataservice.DataService;
import model.dataservice.DataServiceException;
import model.dataservice.SongDb;
import model.dataservice.UserDb;

/**
 * An implementation of a DataService that reads from XML files. No pagination
 * of any kind is implemented here so if you need more than ~100 items I
 * recommend you build and use a SqlDataService. This class enforces the
 * singleton property on generated databases to prevent problems. Callers don't
 * need to worry about it.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class XmlDataService implements DataService {

	private File songDbFile;
	private File userDbFile;

	private SongDb songDbInstance;
	private UserDb userDbInstance;

	/**
	 * Create a new XmlDataService connected to the specified song and user
	 * database files.
	 * 
	 * @param songDbFile
	 *            where to find the song db
	 * @param userDbFile
	 *            where to find the user db
	 */
	public XmlDataService(File songDbFile, File userDbFile) {
		this.songDbFile = songDbFile;
		this.userDbFile = userDbFile;
	}

	@Override
	public SongDb readSongDb() throws DataServiceException {
		if (this.songDbInstance == null) {
			this.songDbInstance = readSongDb(this.songDbFile);
		}
		return this.songDbInstance;
	}

	@Override
	public UserDb readUserDb() throws DataServiceException {
		if (this.userDbInstance == null) {
			this.userDbInstance = readUserDb(this.userDbFile);
		}
		return this.userDbInstance;
	}

	private static SongDb readSongDb(File dbFile) throws DataServiceException {
		XmlSongDb songDb = null;
		try (Reader reader = new InputStreamReader(new FileInputStream(dbFile), StandardCharsets.UTF_8)) {
			JAXBContext jaxbContext = JAXBContext.newInstance(XmlSongDb.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			songDb = (XmlSongDb) jaxbUnmarshaller.unmarshal(reader);
		} catch (IOException | JAXBException e) {
			throw new DataServiceException("couldn't parse song db");
		}
		return songDb;
	}

	private static UserDb readUserDb(File userDbFile) throws DataServiceException {
		XmlUserDb userDb = null;
		try (Reader reader = new InputStreamReader(new FileInputStream(userDbFile), StandardCharsets.UTF_8)) {
			JAXBContext jaxbContext = JAXBContext.newInstance(XmlUserDb.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			userDb = (XmlUserDb) jaxbUnmarshaller.unmarshal(reader);
		} catch (IOException | JAXBException e) {
			throw new DataServiceException("couldn't parse user db");
		}
		return userDb;
	}
}
