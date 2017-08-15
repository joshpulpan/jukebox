package controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.JukeboxModel;
import model.PersistenceServiceException;
import view.JukeboxView;

/**
 * A window adapter for the Jukebox. Handles window open and close to prompt for
 * data saves and restores.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class JukeboxWindowAdapter extends WindowAdapter {
	private final JukeboxModel model;
	private final File state;

	private static final Logger LOGGER = Logger.getLogger(JukeboxWindowAdapter.class.getName());

	/**
	 * Create a new window adapter tied to the model and view with the state
	 * file for reading/writing.
	 * 
	 * @param model
	 *            the model to use for state events
	 * @param state
	 *            the file to save and restore state to/from
	 */
	public JukeboxWindowAdapter(JukeboxModel model, File state) {
		this.model = model;
		this.state = state;
	}

	@Override
	public void windowClosing(WindowEvent windowEvent) {
		if (JukeboxView.promptWrite()) {
			try {
				this.model.writeState(this.state);
			} catch (PersistenceServiceException e) {
				if (LOGGER.isLoggable(Level.SEVERE)) {
					LOGGER.severe("fatal persistence service error: " + e.getMessage());
				}
			}
		}
	}

	@Override
	public void windowOpened(WindowEvent windowEvent) {
		if (this.state.exists()) {
			if (JukeboxView.promptRead()) {
				try {
					this.model.readState(this.state);
				} catch (PersistenceServiceException e) {
					if (LOGGER.isLoggable(Level.SEVERE)) {
						LOGGER.severe("fatal persistence service error: " + e.getMessage());
					}
				}
			}
		}
	}
}
