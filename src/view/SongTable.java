package view;

import java.util.List;
import java.util.UUID;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.dataservice.Song;
import model.dataservice.SongDb;

/**
 * An adapter for the SongDb so that it can be used as a table in the view.
 * Since the model doesn't have much to do with tables it doesn't make sense to
 * handle this there.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class SongTable implements TableModel {

	private List<Song> songs;

	/**
	 * Create a new table from the given song database.
	 * 
	 * @param songDb
	 *            the song database to model
	 */
	public SongTable(SongDb songDb) {
		this.songs = songDb.getSongs();
	}

	@Override
	public int getRowCount() {
		return this.songs.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Artist";
		case 1:
			return "Title";
		case 2:
			return "Length";
		default:
			return null;
		}
	}

	/**
	 * Get the UUID of the song on a specific row.
	 * 
	 * @param row the row to retrieve
	 * @return the UUID of the song at that row
	 */
	public UUID getIdAt(int row) {
		return this.songs.get(row).getId();
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public Object getValueAt(int row, int column) {
		Song song = this.songs.get(row);
		switch (column) {
		case 0:
			return song.getArtist();
		case 1:
			return song.getTitle();
		case 2:
			long length = song.getLength().getSeconds();
			Long mins = Long.valueOf((length % 3600) / 60);
			Long secs = Long.valueOf(length % 60);
			return String.format("%02d:%02d", mins, secs);
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object o, int row, int column) {
		// unused method stub
	}

	@Override
	public void addTableModelListener(TableModelListener tableModelListener) {
		// unused method stub
	}

	@Override
	public void removeTableModelListener(TableModelListener tableModelListener) {
		// unused method stub
	}
}
