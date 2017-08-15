package model;

import java.time.LocalDateTime;

/**
 * An interface for getting dates. This is used for mocking a date with tests.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public interface DateService {
	/**
	 * Get whatever passes for midnight according to this DateService.
	 * 
	 * @return a LocalDateTime object representing midnight
	 */
	public LocalDateTime getMidnight();
}
