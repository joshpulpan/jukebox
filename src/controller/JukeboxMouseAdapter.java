package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.JTable;

import model.JukeboxModel;
import view.SongTable;

/**
 * A mouse adapter for the Jukebox. This handles clicks in the table of songs.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class JukeboxMouseAdapter extends MouseAdapter {
	private JukeboxModel model;
	
	/**
	 * Set up a new mouse adapter connected to the model and view.
	 * 
	 * @param model
	 *            the model to bind
	 */
	public JukeboxMouseAdapter(final JukeboxModel model) {
		this.model = model;
	}

	/**
	 * Mouse click handler.
	 */
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() == 2) {
			JTable songs = (JTable) mouseEvent.getSource();
			int row = songs.convertRowIndexToModel(songs.rowAtPoint(mouseEvent.getPoint()));
			UUID song = ((SongTable) songs.getModel()).getIdAt(row);
			JukeboxController.queueSong(this.model, song);
		}
	}
}
