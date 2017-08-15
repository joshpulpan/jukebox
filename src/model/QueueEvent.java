package model;

/**
 * This class gives possible return values for queuing songs. The controller can
 * then respond appropriately.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public enum QueueEvent {
	/**
	 * Song added to queue.
	 */
	SONG_ADDED,
	/**
	 * Song played too many times today.
	 */
	SONG_EXCEEDED_PLAY_LIMIT,
	/**
	 * Song had an incorrect ID. This indicates a serious problem with the code.
	 */
	SONG_NOT_FOUND,
	/**
	 * User has played too many songs today.
	 */
	USER_EXCEEDED_PLAY_LIMIT,
	/**
	 * User has exceeded lifetime allotment of time.
	 */
	USER_EXCEEDED_TIME_LIMIT,
	/**
	 * Nobody is logged in.
	 */
	NO_USER_LOGGED_IN;
}
