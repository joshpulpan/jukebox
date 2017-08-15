package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.UUID;

import model.JukeboxModel;
import model.QueueEvent;
import model.SongQueue;
import view.JukeboxView;
import view.LoginView;

/**
 * Controller for the main Jukebox view. Handles song interactions and sets up
 * the login panel when necessary.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class JukeboxController implements ActionListener {
	private static LoginView loginView = new LoginView();
	private JukeboxMouseAdapter mouseAdapter;
	private JukeboxWindowAdapter windowAdapter;
	private JukeboxModel model;
	private final File state;

	/**
	 * Attempt to queue a song in the model. Tells the view to display a
	 * message on failure.
	 * 
	 * @param model the model to queue the song in
	 * @param song the id of the song
	 */
	public static void queueSong(JukeboxModel model, UUID song) {
		QueueEvent queueEvent = model.queueSong(song);
		switch (queueEvent) {
		case NO_USER_LOGGED_IN:
			JukeboxView.showMessage("You must be logged in to play a song.");
			break;
		case SONG_NOT_FOUND:
			JukeboxView.showMessage("There was a problem adding the song.");
			break;
		case SONG_EXCEEDED_PLAY_LIMIT:
			JukeboxView.showMessage("That song has already been played 3 times today.");
			break;
		case USER_EXCEEDED_PLAY_LIMIT:
			JukeboxView.showMessage("You have already played 3 songs today.");
			break;
		case USER_EXCEEDED_TIME_LIMIT:
			JukeboxView.showMessage("You have used all your allotted time for playing songs.");
			break;
		default:
			break;
		}
	}

	/**
	 * Sets up a new JukeboxController
	 * 
	 * @param state
	 *            a file for saving and reading state
	 */
	public JukeboxController(File state) {
		this.state = state;
	}

	/**
	 * Binds this controller to the given model. Also binds the subordinate
	 * LoginView to the same model.
	 * 
	 * @param model
	 *            a valid JukeboxModel for manipulating
	 */
	public void bind(final JukeboxModel model) {
		this.model = model;
		loginView.bind(model);
		this.mouseAdapter = new JukeboxMouseAdapter(this.model);
		this.windowAdapter = new JukeboxWindowAdapter(this.model, this.state);
	}

	/**
	 * Adds the passed in view as an observer of the model and sets up list data
	 * listening.
	 * 
	 * @param view
	 *            a valid JukeboxView for binding.
	 * @return the model's SongQueue to allow view initialization
	 */
	public SongQueue addModelListener(JukeboxView view) {
		this.model.addObserver(view);
		return this.model.getSongQueue();
	}

	/**
	 * Returns the MouseAdapter.
	 * 
	 * @return the mouse adapter
	 */
	public JukeboxMouseAdapter getMouseAdapter() {
		return this.mouseAdapter;
	}

	/**
	 * Returns the WindowAdapter.
	 * 
	 * @return the window adapter
	 */
	public JukeboxWindowAdapter getWindowAdapter() {
		return this.windowAdapter;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		switch (actionEvent.getActionCommand()) {
		case "Login":
			loginView.clear();
			loginView.setVisible(true);
			break;
		case "Sign out":
			this.model.signOut();
			break;
		default:
			UUID song = UUID.fromString(actionEvent.getActionCommand());
			JukeboxController.queueSong(this.model, song);
			break;
		}
	}
}
