package model;

import java.util.HashMap;
import java.util.UUID;

/**
 * A class that keeps track of each played song and the number of times it has
 * been played today.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class SongCache extends Cache {
	private static final long serialVersionUID = -752398757918774743L;

	/**
	 * Create a new cache for song play counts.
	 */
	public SongCache() {
		this.plays = new HashMap<>();
	}

	@Override
	protected void cache(UUID id) {
		if (!this.plays.containsKey(id)) {
			this.plays.put(id, Integer.valueOf(0));
		}
	}
}
