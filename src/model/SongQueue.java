package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import model.dataservice.Song;
import model.dataservice.SongDb;

/**
 * A queue with a list of songs to play. The naming originally reflected the
 * actual implementation but since this must be a ListModel for UI display a
 * more flexible data structure was necessary. The name stays because this is,
 * at an abstract level, still a queue.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class SongQueue implements ListModel<Song> {
	private SongDb songDb;
	private List<ListDataListener> listeners;
	private List<UUID> queue;

	/**
	 * Create a new empty queue for songs.
	 * 
	 * @param songDb
	 *            the database of all songs
	 */
	public SongQueue(SongDb songDb) {
		this.listeners = new ArrayList<>();
		this.queue = new ArrayList<>();
		this.songDb = songDb;
	}
	
	/**
	 * Add a song to the queue.
	 * 
	 * @param id
	 *            the is of the song to add
	 */
	public void add(UUID id) {
		this.queue.add(id);
		notifyListeners();
	}

	/**
	 * Is the queue empty?
	 * 
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

	/**
	 * Pop the top item off the queue.
	 */
	public void remove() {
		this.queue.remove(0);
		notifyListeners();
	}

	/**
	 * Peek at the top queue item.
	 * 
	 * @return the id of the top item
	 */
	public UUID peek() {
		return this.queue.get(0);
	}

	@Override
	public int getSize() {
		return this.queue.size();
	}

	@Override
	public Song getElementAt(int i) {
		return this.songDb.getSongById(this.queue.get(i));
	}

	@Override
	public void addListDataListener(ListDataListener listDataListener) {
		this.listeners.add(listDataListener);
	}

	@Override
	public void removeListDataListener(ListDataListener listDataListener) {
		this.listeners.remove(listDataListener);
	}

	private void notifyListeners() {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
		for (ListDataListener l : this.listeners) {
			l.contentsChanged(e);
		}
	}
}
