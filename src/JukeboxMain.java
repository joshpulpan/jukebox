import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.JukeboxController;
import model.JukeboxModel;
import model.RealDateService;
import model.dataservice.DataServiceException;
import model.dataservice.xml.XmlDataService;
import view.JukeboxView;

/**
 * Entry point for Jukebox. Sets up the model, view, and controller and binds
 * them together.
 * 	
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class JukeboxMain {
	private static final File STATE = new File("JukeboxState.dat");
	private static final File SONG_DB = new File("resources/SongDb.xml");
	private static final File USER_DB = new File("resources/UserDb.xml");

	private static final Logger LOGGER = Logger.getLogger(JukeboxMain.class.getName());

	/**
	 * The main method for the Jukebox. Sets up all necessary parts and binds
	 * them all together.
	 * 
	 * @param args
	 *            standard field, ignored
	 */
	public static void main(String[] args) {
		JukeboxController controller = new JukeboxController(STATE);
		JukeboxModel model = new JukeboxModel(new RealDateService());
		try {
			model.bind(new XmlDataService(SONG_DB, USER_DB));
		} catch (DataServiceException e) {
			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.severe("fatal data service error: " + e.getMessage());
			}
			System.exit(1);
		}
		controller.bind(model);

		JukeboxView view = new JukeboxView(model.getSongDb());
		view.bind(controller);
		view.setVisible(true);
	}
}